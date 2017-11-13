package com.dabkick.videosdk.livesession.overviews;


import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OverviewModel {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Overview overview;

    public OverviewModel() {
        firebaseDatabase = FirebaseDatabase.getInstance();

        String chatPath = OverviewDatabaseReferences.getOverviewRoomReference(AbstractDatabaseReferences.getSessionId());
        databaseReference = firebaseDatabase.getReference(chatPath);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Overview overview = dataSnapshot.getValue(Overview.class);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Overview overview = dataSnapshot.getValue(Overview.class);
            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addChildEventListener(childEventListener);



    }

}
