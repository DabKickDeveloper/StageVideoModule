package com.dabkick.videosdk.livesession.livestream;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.Prefs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

public class ParticipantModel {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private ParticipantModelCallback callback;

    ParticipantModel(@NonNull ParticipantModelCallback callback) {
        this.callback = callback;
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        String participantPath = ParticipantDatabaseReferences.getParticipantReference(ParticipantDatabaseReferences.getSessionId());
        databaseReference = firebaseDatabase.getReference(participantPath);
        Participant myParticipant = new Participant(
                Prefs.getUserId(),
                Prefs.getDabname(),
                Prefs.getProfilePicUrl(),
                false,
                false
        );
        databaseReference.push().setValue(myParticipant);
        databaseReference.onDisconnect().removeValue();

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Participant participant = dataSnapshot.getValue(Participant.class);
                Timber.d("onChildAdded: %s", participant.dabname);
                callback.onAdded(participant);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Participant participant = dataSnapshot.getValue(Participant.class);
                Timber.d("onChildRemoved: %s", participant.dabname);
                callback.onRemoved(participant);

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);


    }


}