package com.dabkick.videosdk.livesession.livestream;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.Prefs;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

class ParticipantDatabase {

    interface ParticipantModelCallback {
        void onParticipantAdded(Participant participant);
        void onParticipantRemoved(Participant participant);
    }

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private String myParticipantKey;

    ParticipantDatabase(@NonNull ParticipantModelCallback callback) {
        firebaseDatabase = FirebaseDatabase.getInstance();

        String participantPath = ParticipantDatabaseReferences.getParticipantReference(ParticipantDatabaseReferences.getSessionId());
        databaseReference = firebaseDatabase.getReference(participantPath);

        addSelfToDatabase();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Participant participant = dataSnapshot.getValue(Participant.class);
                Timber.i("onChildAdded: %s", participant.dabname);
                callback.onParticipantAdded(participant);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // TODO
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Participant participant = dataSnapshot.getValue(Participant.class);
                Timber.i("onChildRemoved: %s", participant.dabname);
                callback.onParticipantRemoved(participant);
            }
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addChildEventListener(childEventListener);

    }

    private void addSelfToDatabase() {
        Participant myParticipant = new Participant(
                Prefs.getUserId(),
                Prefs.getDabname(),
                Prefs.getProfilePicUrl(),
                false,
                false
        );
        myParticipantKey = databaseReference.push().getKey();
        databaseReference.child(myParticipantKey).setValue(myParticipant);
        databaseReference.child(myParticipantKey).onDisconnect().removeValue();
    }

    void removeSelfFromDatabase() {
        databaseReference.child(myParticipantKey).removeValue();
    }

}