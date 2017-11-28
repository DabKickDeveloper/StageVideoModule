package com.dabkick.videosdk.livesession.overviews;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

public class OverviewDatabase {

    private OverviewListener listener;
    private DatabaseReference databaseReference;
    private OverviewModel overviewModel;

    public OverviewDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        String overviewPath = OverviewDatabaseReferences.getOverviewReference();
        databaseReference = firebaseDatabase.getReference(overviewPath);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                databaseReference = databaseReference.child(dataSnapshot.getKey());
                overviewModel = dataSnapshot.getValue(OverviewModel.class);
                Timber.i("added stage key: %s", overviewModel.getStagedVideoKey());
                listener.onStageKeyFromDatabaseChanged(overviewModel.getStagedVideoKey());

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                overviewModel = dataSnapshot.getValue(OverviewModel.class);
                Timber.i("changed stage key: %s", overviewModel.getStagedVideoKey());
                listener.onStageKeyFromDatabaseChanged(overviewModel.getStagedVideoKey());
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

    void setStageKey(String newKey) {
        databaseReference.child(OverviewDatabaseReferences.STAGED_VIDEO_KEY).setValue(newKey);
    }

    public String getStagedVideoKey() {
        return (overviewModel == null) ? "" : overviewModel.getStagedVideoKey();
    }

    interface OverviewListener {
        void onStageKeyFromDatabaseChanged(String newIndex);
    }

    public void setListener(OverviewListener listener) {
        this.listener = listener;
    }

}
