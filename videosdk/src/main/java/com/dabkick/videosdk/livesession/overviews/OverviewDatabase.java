package com.dabkick.videosdk.livesession.overviews;


import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OverviewDatabase {

    private OverviewModel overviewModel;

    public OverviewDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        String chatPath = OverviewDatabaseReferences.getOverviewRoomReference(AbstractDatabaseReferences.getSessionId());
        DatabaseReference databaseReference = firebaseDatabase.getReference(chatPath);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                OverviewDatabase.this.overviewModel = dataSnapshot.getValue(OverviewModel.class);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                OverviewDatabase.this.overviewModel = dataSnapshot.getValue(OverviewModel.class);
            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addChildEventListener(childEventListener);

    }

    interface OverviewListener {
        void onOverviewChanged();
    }

}
