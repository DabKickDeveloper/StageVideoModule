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
        void onStageVideoChanged();
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
                callback.onStageVideoChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                StageVideo stageVideo = dataSnapshot.getValue(StageVideo.class);
                Timber.d("onChildChanged: %s", dataSnapshot.getKey());
                for (int i = 0; i < stageVideoList.size(); i++) {
                    if (stageVideo.equals(stageVideoList.get(i))) {
                        Timber.d("replaced video: %s", stageVideo.getKey());
                        stageVideoList.set(i, stageVideo);
                        break;
                    }
                }
                callback.onStageVideoChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                StageVideo sv = dataSnapshot.getValue(StageVideo.class);
                stageVideoList.remove(sv);
                callback.onStageVideoChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addChildEventListener(childEventListener);

    }

    void pauseVideo(int secs) {
        Timber.d("pauseVideo: %s", secs);
        stageVideoList.get(0).setPlayedSeconds(secs);
        stageVideoList.get(0).setState(StageVideo.PAUSED);
        databaseReference.child(stageVideoList.get(0).getKey()).setValue(stageVideoList.get(0));
    }

    void resumeVideo() {
        Timber.d("resumeVideo");
        stageVideoList.get(0).setState(StageVideo.PLAYING);
        databaseReference.child(stageVideoList.get(0).getKey()).setValue(stageVideoList.get(0));
    }

    void seekTo(int secs) {
        Timber.d("seekTo: %s", secs);
        //stageVideoList.get(0).setPlayedSeconds(secs);
        databaseReference.child(stageVideoList.get(0).getKey()).child("playedSeconds").setValue(secs);
    }

    List<StageVideo> getStageVideoList() {
        return stageVideoList;
    }

    public void addVideo(String url) {
        StageVideo stageVideo = new StageVideo(url);
        databaseReference.push().setValue(stageVideo);
    }


}