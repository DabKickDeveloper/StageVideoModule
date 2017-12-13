package com.dabkick.videosdk.livesession.stage;


import com.dabkick.videosdk.SdkApp;
import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;
import com.dabkick.videosdk.livesession.overviews.OverviewDatabase;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import timber.log.Timber;

public class StageDatabase {

    interface StageDatabaseCallback {
        void onStageVideoTimeChanged(String key, long playedMillis);
        void onStageVideoStateChanged(String key, String newState, long playedMillis);
        void onVideoComplete(String key);
    }

    private StageDatabaseCallback callback;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private ChildEventListener childEventListener;
    private StagePresenter presenter;

    @Inject OverviewDatabase overviewDatabase;
    @Inject VideoManager videoManager;

    public StageDatabase() {

        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
        firebaseDatabase = FirebaseDatabase.getInstance();

        String stagePath = StageDatabaseReferences.getStageReference(AbstractDatabaseReferences.getSessionId());
        databaseReference = firebaseDatabase.getReference(stagePath);

        videoManager.setDatabaseListener(new StageDatabaseCallback() {

            @Override
            public void onStageVideoTimeChanged(String key, long playedMillis) {
                updateSeekTime(key, playedMillis);
            }

            @Override
            public void onStageVideoStateChanged(String key, String newState, long playedMillis) {
                updateState(key, newState, playedMillis);
            }

            @Override
            public void onVideoComplete(String key) {
                updateState(key, "paused", 1);
            }

        });

    }

    void addChildEventListener() {
        Timber.d("addChildEventListener");
        childEventListener = createChildEventListener();
        databaseReference.addChildEventListener(childEventListener);
    }

    void removeChildEventListener() {
        Timber.d("removeChildEventListener");
        databaseReference.removeEventListener(childEventListener);
        videoManager.clear();
        childEventListener = null;
    }

    private void updateState(String key, String newState, long playedMillis) {
        Timber.i("update video state. key: %s, newState: %s, playedMillis: %s", key, newState, playedMillis);
        Map<String, Object> updateMap = new HashMap<>();
        updateMap.put("state", newState);
        updateMap.put("playedMillis", playedMillis);
        databaseReference.child(key).updateChildren(updateMap);

    }

    public void updateSeekTime(String key, long millis) {
        Timber.d("update seek time to: %s, key: %s", millis, key);
        databaseReference.child(key).child(StageDatabaseReferences.PLAYED_MILLIS).setValue(millis);
    }

    void addVideo(String url) {
        Timber.i("added video: %s", url);
        String key = databaseReference.push().getKey();
        StageModel stageModel = new StageModel(url, key);
        databaseReference.child(key).setValue(stageModel);
        overviewDatabase.setStageKey(key);
    }

    void setPresenter(StagePresenterImpl presenter) {
        this.presenter = presenter;
    }

    private ChildEventListener createChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StageModel sv = dataSnapshot.getValue(StageModel.class);
                sv.setKey(dataSnapshot.getKey());
                Timber.d("onChildAdded: %s", dataSnapshot.getKey());
                videoManager.add(sv);
                presenter.updatePositionOnVideoAdded();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                StageModel changedStageModel = dataSnapshot.getValue(StageModel.class);
                Timber.d("updateStageModel: %s", dataSnapshot.getKey());
                videoManager.updateStageModel(changedStageModel);
            }

            @Override public void onChildRemoved(DataSnapshot dataSnapshot) {}
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        };
    }

}