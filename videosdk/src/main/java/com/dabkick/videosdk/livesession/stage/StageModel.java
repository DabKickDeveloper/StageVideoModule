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

class StageModel {

    interface StageModelCallback {
        void onStageVideoAdded();
        void onStageVideoRemoved();
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
                stageVideoList.add(sv);
                callback.onStageVideoAdded();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                StageVideo sv = dataSnapshot.getValue(StageVideo.class);
                stageVideoList.remove(sv);
                callback.onStageVideoRemoved();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addChildEventListener(childEventListener);

    }

    void pauseVideo(int secs) {
        stageVideoList.get(0).setPlayedSeconds(secs);
        stageVideoList.get(0).setState(StageVideo.PAUSED);
        databaseReference.child(stageVideoList.get(0).getKey()).setValue(stageVideoList.get(0));
    }

    void resumeVideo() {
        stageVideoList.get(0).setState(StageVideo.PLAYING);
        databaseReference.child(stageVideoList.get(0).getKey()).setValue(stageVideoList.get(0));
    }

    void seekTo(int secs) {
        stageVideoList.get(0).setPlayedSeconds(secs);
        databaseReference.child(stageVideoList.get(0).getKey()).setValue(stageVideoList.get(0));
    }

    List<StageVideo> getStageVideoList() {
        return stageVideoList;
    }

    public void addVideo(String url) {
        StageVideo stageVideo = new StageVideo(url);
        databaseReference.push().setValue(stageVideo);
    }


}