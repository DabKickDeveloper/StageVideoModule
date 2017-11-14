package com.dabkick.videosdk.livesession.overviews;


import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OverviewDatabase {

    private OverviewModel overviewModel;
    private OverviewListener overviewListener;
    private DatabaseReference databaseReference;

    public OverviewDatabase(OverviewListener overviewListener) {
        this.overviewListener = overviewListener;
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        String chatPath = OverviewDatabaseReferences.getOverviewRoomReference(AbstractDatabaseReferences.getSessionId());
        databaseReference = firebaseDatabase.getReference(chatPath);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                OverviewDatabase.this.overviewModel = dataSnapshot.getValue(OverviewModel.class);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                OverviewDatabase.this.overviewModel = dataSnapshot.getValue(OverviewModel.class);
                overviewListener.onOverviewChanged();
            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addChildEventListener(childEventListener);

    }

    private void createInitialObject() {
        overviewModel = new OverviewModel(0);
        databaseReference.push().setValue(overviewModel);
    }

    public interface OverviewListener {
        void onOverviewChanged();
    }

}
