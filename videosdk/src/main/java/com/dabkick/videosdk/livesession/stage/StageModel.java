package com.dabkick.videosdk.livesession.stage;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.livesession.livestream.ParticipantDatabaseReferences;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

class StageModel {

    interface StageModelCallback {
        void onStageVideoAdded();

        void onStageVideoTimeChanged(int position, int playedMillis);

        void onStageVideoStateChanged(int i, String newState);
    }

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;

    private List<StageVideo> stageVideoList;

    StageModel(@NonNull StageModelCallback callback) {

        stageVideoList = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();

        String stagePath = StageDatabaseReferences.getStageReference(ParticipantDatabaseReferences.getSessionId());
        databaseReference = firebaseDatabase.getReference(stagePath);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                StageVideo sv = dataSnapshot.getValue(StageVideo.class);
                sv.setKey(dataSnapshot.getKey());
                Timber.d("onChildAdded: %s", dataSnapshot.getKey());
                stageVideoList.add(sv);
                callback.onStageVideoAdded();
            }

            // FIXME move if-statement logic into controller
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                StageVideo changedStageVideo = dataSnapshot.getValue(StageVideo.class);
                Timber.d("onChildChanged: %s", dataSnapshot.getKey());
                for (int i = 0; i < stageVideoList.size(); i++) {
                    if (changedStageVideo.equals(stageVideoList.get(i))) {
                        // update stage time
                        if (changedStageVideo.getPlayedMillis() != stageVideoList.get(0).getPlayedMillis()) {
                            callback.onStageVideoTimeChanged(i, changedStageVideo.getPlayedMillis());
                        }
                        // do not update play/pause
                        if (!changedStageVideo.getState().equals(stageVideoList.get(0).getState())) {
                            callback.onStageVideoStateChanged(i, changedStageVideo.getState());
                        }


                        Timber.d("replaced video: %s", changedStageVideo.getKey());
                        stageVideoList.set(i, changedStageVideo);
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

    void pauseVideo(int milliseconds) {
        Timber.d("pauseVideo: %s", milliseconds);
        stageVideoList.get(0).setPlayedMillis(milliseconds);
        stageVideoList.get(0).setState(StageVideo.PAUSED);
        databaseReference.child(stageVideoList.get(0).getKey()).setValue(stageVideoList.get(0));
    }

    void resumeVideo(int milliseconds) {
        Timber.d("resumeVideo: %s", milliseconds);
        stageVideoList.get(0).setPlayedMillis(milliseconds);
        stageVideoList.get(0).setState(StageVideo.PLAYING);
        databaseReference.child(stageVideoList.get(0).getKey()).setValue(stageVideoList.get(0));
    }

    void seekTo(int secs) {
        Timber.d("seekTo: %s", secs);
        //stageVideoList.get(0).setPlayedMillis(secs);
        databaseReference.child(stageVideoList.get(0).getKey()).child("playedMillis").setValue(secs);
    }

    List<StageVideo> getStageVideoList() {
        return stageVideoList;
    }

    public void addVideo(String url) {
        StageVideo stageVideo = new StageVideo(url);
        databaseReference.push().setValue(stageVideo);
    }


}