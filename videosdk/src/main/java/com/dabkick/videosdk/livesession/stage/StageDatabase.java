package com.dabkick.videosdk.livesession.stage;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.SdkApp;
import com.dabkick.videosdk.livesession.livestream.ParticipantDatabaseReferences;
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

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private List<StageModel> stageModelList;

    @Inject OverviewDatabase overviewDatabase;

    StageDatabase(@NonNull StageDatabaseCallback callback) {

        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);

        stageModelList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();

        String stagePath = StageDatabaseReferences.getStageReference(ParticipantDatabaseReferences.getSessionId());
        databaseReference = firebaseDatabase.getReference(stagePath);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StageModel sv = dataSnapshot.getValue(StageModel.class);
                sv.setKey(dataSnapshot.getKey());
                Timber.i("onChildAdded: %s", dataSnapshot.getKey());
                stageModelList.add(sv);
                callback.onStageVideoAdded();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                StageModel changedStageModel = dataSnapshot.getValue(StageModel.class);
                Timber.i("onChildChanged: %s", dataSnapshot.getKey());
                for (int i = 0; i < stageModelList.size(); i++) {
                    if (changedStageModel.equals(stageModelList.get(i))) {
                        // update stage time
                        if (changedStageModel.getPlayedMillis() != stageModelList.get(0).getPlayedMillis()) {
                            Timber.i("changed time: %s", changedStageModel.getPlayedMillis());
                            callback.onStageVideoTimeChanged(i, changedStageModel.getPlayedMillis());
                        }
                        // do not update play/pause
                        if (!changedStageModel.getState().equals(stageModelList.get(0).getState())) {
                            Timber.i("changed state: %s", changedStageModel.getState());
                            callback.onStageVideoStateChanged(i, changedStageModel.getState());
                        }


                        stageModelList.set(i, changedStageModel);
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addChildEventListener(childEventListener);

    }

    void pauseVideo(long milliseconds) {
        Timber.i("pauseVideo: %s", milliseconds);
        stageModelList.get(0).setPlayedMillis(milliseconds);
        stageModelList.get(0).setState(StageModel.PAUSED);
        databaseReference.child(stageModelList.get(0).getKey()).setValue(stageModelList.get(0));
    }

    void resumeVideo(long milliseconds) {
        Timber.i("resumeVideo: %s", milliseconds);
        stageModelList.get(0).setPlayedMillis(milliseconds);
        stageModelList.get(0).setState(StageModel.PLAYING);
        databaseReference.child(stageModelList.get(0).getKey()).setValue(stageModelList.get(0));
    }

    void seekTo(long secs) {
        Timber.i("seekTo: %s", secs);
        //stageModelList.get(0).setPlayedMillis(secs);
        databaseReference.child(stageModelList.get(0).getKey()).child("playedMillis").setValue(secs);
    }

    List<StageModel> getStageModelList() {
        return stageModelList;
    }

    void addVideo(String url) {
        Timber.i("added video: %s", url);
        StageModel stageModel = new StageModel(url);
        databaseReference.push().setValue(stageModel);
    }


}