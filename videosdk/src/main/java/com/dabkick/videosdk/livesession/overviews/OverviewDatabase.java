package com.dabkick.videosdk.livesession.overviews;


import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OverviewDatabase {

    private OverviewListener listener;
    private DatabaseReference databaseReference;

    public OverviewDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        String overviewPath = OverviewDatabaseReferences.getOverviewRoomReference(AbstractDatabaseReferences.getSessionId());
        databaseReference = firebaseDatabase.getReference(overviewPath);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                databaseReference = databaseReference.child(dataSnapshot.getKey());
                OverviewModel item = dataSnapshot.getValue(OverviewModel.class);
                listener.onStageIndexFromDatabaseChanged(item.getStagedVideoPosition());

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                OverviewModel overviewModel = dataSnapshot.getValue(OverviewModel.class);
                listener.onOverviewChanged();
            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addChildEventListener(childEventListener);
        //createInitialObject();
    }

    private void createInitialObject() {
        //overviewModel = new OverviewModel(0);
        //databaseReference.push().setValue(overviewModel);
    }

    public void setStageIndex(int stageIndex) {
        databaseReference.child(OverviewDatabaseReferences.STAGED_VIDEO_POSITION).setValue(stageIndex);
    }

    public interface OverviewListener {
        void onOverviewChanged();
        void onStageIndexFromDatabaseChanged(int newIndex);
    }

    public void setListener(OverviewListener listener) {
        this.listener = listener;
    }

}
