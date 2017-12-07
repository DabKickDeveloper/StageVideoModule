package com.dabkick.videosdk.livesession.livestream;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.Prefs;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

import static com.dabkick.videosdk.livesession.livestream.ParticipantDatabaseReferences.*;

public class ParticipantDatabase {

    interface ParticipantModelCallback {
        void onParticipantAdded(Participant participant);
        void onParticipantRemoved(Participant participant);
        void onParticipantAudioVideoEnabled(Participant participant);
        void onParticipantAudioEnabled(Participant participant);
    }

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private String myParticipantKey;
    private ChildEventListener childEventListener;
    private ParticipantModelCallback callback;

    ParticipantDatabase(@NonNull ParticipantModelCallback callback) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        this.callback = callback;

        String participantPath = getParticipantReference(getSessionId());
        databaseReference = firebaseDatabase.getReference(participantPath);

        addSelfToDatabase();
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

    void setIsAudioEnabled(boolean isEnabled){
        databaseReference.child(myParticipantKey).child(IS_AUDIO_ENABLED).setValue(isEnabled);
    }

    void setisVideoEnabled(boolean isEnabled){
        databaseReference.child(myParticipantKey).child(IS_VIDEO_ENABLED).setValue(isEnabled);
    }

    void addChildEventListener() {
        Timber.d("addChildEventListener");
        if (childEventListener != null) {
            Timber.d("ignore participant child listener already created");
            return;
        }
        childEventListener = createChildEventListener();
        databaseReference.addChildEventListener(childEventListener);
    }

    void removeChildEventListener() {
        Timber.i("removing child event listener");
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }
    }

    private ChildEventListener createChildEventListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Participant participant = dataSnapshot.getValue(Participant.class);
                Timber.i("onChildAdded: %s", participant.getDabname());
                callback.onParticipantAdded(participant);
                if (participant.getIsAudioEnabled() && participant.getIsVideoEnabled())
                    handleOnAudioVideoEnabled(participant);
                else if (participant.getIsAudioEnabled() && !participant.getIsVideoEnabled())
                    handleOnAudioEnabled(participant);
                else if (participant.getIsVideoEnabled() && !participant.getIsAudioEnabled())
                    Timber.i("Error- onChildAdded - only video enabled!: %s", participant.getDabname()); //error!
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Participant participant = dataSnapshot.getValue(Participant.class);
                Timber.i("onChildChanged: %s", participant.getDabname());
                if (participant.getIsAudioEnabled() && participant.getIsVideoEnabled())
                    handleOnAudioVideoEnabled(participant);
                else if (participant.getIsAudioEnabled() && !participant.getIsVideoEnabled())
                    handleOnAudioEnabled(participant);
                else if (participant.getIsVideoEnabled() && !participant.getIsAudioEnabled())
                    Timber.i("Error - onChildChanged - only video enabled!: %s", participant.getDabname()); //error!
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Participant participant = dataSnapshot.getValue(Participant.class);
                Timber.i("onChildRemoved: %s", participant.getDabname());
                callback.onParticipantRemoved(participant);
            }
            @Override public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            @Override public void onCancelled(DatabaseError databaseError) {}
        };
    }

    private void handleOnAudioVideoEnabled(Participant participant) {
//        if (participant.getIsVideoEnabled() && participant.getIsAudioEnabled()) {
            callback.onParticipantAudioVideoEnabled(participant);
//        }
    }

    private void handleOnAudioEnabled(Participant participant) {
//        if (participant.getIsAudioEnabled()) {
            callback.onParticipantAudioEnabled(participant);
//        }
    }

    void removeSelfFromDatabase() {
        databaseReference.child(myParticipantKey).removeValue();
    }

}