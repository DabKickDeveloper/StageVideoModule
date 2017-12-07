package com.dabkick.videosdk.livesession.stage;


import com.dabkick.videosdk.SdkApp;
import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;
import com.dabkick.videosdk.livesession.overviews.OverviewDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class StageDatabase {

    interface StageDatabaseCallback {
        void onStageVideoAdded();
        void onStageVideoTimeChanged(int position, long playedMillis);
        void onStageVideoStateChanged(int i, String newState);
    }

    private StageDatabaseCallback callback;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ChildEventListener childEventListener;
    private List<StageModel> stageModelList;

    @Inject OverviewDatabase overviewDatabase;
    @Inject VideoManager videoManager;

    public StageDatabase() {

        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);

        stageModelList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();

        String stagePath = StageDatabaseReferences.getStageReference(AbstractDatabaseReferences.getSessionId());
        databaseReference = firebaseDatabase.getReference(stagePath);

    }

    void addChildEventListener() {
        Timber.d("addChildEventListener");
        childEventListener = createChildEventListener();
        databaseReference.addChildEventListener(childEventListener);
    }

    void removeChildEventListener() {
        Timber.d("removeChildEventListener");
        databaseReference.removeEventListener(childEventListener);
        stageModelList.clear();
        childEventListener = null;
    }

    void pauseVideo(long milliseconds) {
        Timber.i("pauseVideo: %s", milliseconds);
        int index = getIndexFromKey(overviewDatabase.getStagedVideoKey());
        stageModelList.get(index).setPlayedMillis(milliseconds);
        stageModelList.get(index).setState(StageModel.PAUSED);
        databaseReference.child(stageModelList.get(
                index).getKey()).setValue(
                        stageModelList.get(index));
    }

    void resumeVideo(long milliseconds) {
        Timber.i("resumeVideo: %s", milliseconds);
        int index = getIndexFromKey(overviewDatabase.getStagedVideoKey());
        stageModelList.get(index).setPlayedMillis(milliseconds);
        stageModelList.get(index).setState(StageModel.PLAYING);
        databaseReference.child(stageModelList.get(index)
                .getKey()).setValue(stageModelList.get(index));
    }

    void seekTo(long secs) {
        Timber.i("seekTo: %s", secs);
        int index = getIndexFromKey(overviewDatabase.getStagedVideoKey());
        databaseReference.child(stageModelList.get(index)
                .getKey()).child(StageDatabaseReferences.PLAYED_MILLIS).setValue(secs);
    }

    List<StageModel> getStageModelList() {
        return stageModelList;
    }

    void addVideo(String url) {
        Timber.i("added video: %s", url);
        String key = databaseReference.push().getKey();
        StageModel stageModel = new StageModel(url, key);
        databaseReference.child(key).setValue(stageModel);
        overviewDatabase.setStageKey(key);
    }

    private ChildEventListener createChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StageModel sv = dataSnapshot.getValue(StageModel.class);
                sv.setKey(dataSnapshot.getKey());
                Timber.d("onChildAdded: %s", dataSnapshot.getKey());
                stageModelList.add(sv);
                videoManager.addVideo(sv);
                if (callback != null) callback.onStageVideoAdded();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                StageModel changedStageModel = dataSnapshot.getValue(StageModel.class);
                Timber.d("onChildChanged: %s", dataSnapshot.getKey());
                for (int i = 0; i < stageModelList.size(); i++) {
                    if (changedStageModel.equals(stageModelList.get(i))) {

                        // update stage time
                        int index = getIndexFromKey(changedStageModel.getKey());
                        if (changedStageModel.getPlayedMillis() != stageModelList.get(index).getPlayedMillis()) {
                            Timber.i("changed time: %s", changedStageModel.getPlayedMillis());
                            stageModelList.get(index).setPlayedMillis(changedStageModel.getPlayedMillis());
                            if (callback != null) callback.onStageVideoAdded();
                            //if (callback != null) callback.onStageVideoTimeChanged(i, changedStageModel.getPlayedMillis());
                        }

                        // do not update play/pause
                        if (!changedStageModel.getState().equals(stageModelList.get(index).getState())) {
                            Timber.i("changed state: %s", changedStageModel.getState());
                            stageModelList.get(index).setState(changedStageModel.getState());
                            if (callback != null) callback.onStageVideoAdded();
                            //if (callback != null) callback.onStageVideoStateChanged(i, changedStageModel.getState());
                        }

                        stageModelList.set(i, changedStageModel);
                        break;
                    }
                }
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        };
    }

    // return a video's index in stageModelList from given key
    public int getIndexFromKey(String key) {
        for (int i = 0; i < stageModelList.size(); i++) {
            StageModel sm = stageModelList.get(i);
            if (sm.getKey().equals(key)) {
                return i;
            }
        }
        Timber.w("unable to find index for %s", key);
        return 0;
    }

    // return a video's key in stageModelList from given index
    public String getKeyFromIndex(int index) {
        try {
            return stageModelList.get(index).getKey();
        } catch (IndexOutOfBoundsException e) {
            Timber.e("unable to find key for index %s", index);
            return "";
        }

    }

    void setCallback(StageDatabaseCallback callback) {
        this.callback = callback;
    }

}