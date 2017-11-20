package com.dabkick.videosdk.livesession.emoji;


import com.dabkick.videosdk.Prefs;
import com.dabkick.videosdk.Util;
import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EmojiDatabase {

    // receiver is for database listening, sender for posting updates
    private DatabaseReference receiverDatabaseReference, senderDatabaserReference;
    private EmojiListener listener;

    public EmojiDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        String senderPath = EmojiDatabaseReferences.getEmojiRoomReference(AbstractDatabaseReferences.getSessionId());
        senderDatabaserReference = firebaseDatabase.getReference(senderPath);

        String receiverPath = EmojiDatabaseReferences.getEmojiReference();
        receiverDatabaseReference = firebaseDatabase.getReference(receiverPath);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                handleListenEvents(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                handleListenEvents(dataSnapshot);
            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        };
        receiverDatabaseReference.addChildEventListener(childEventListener);
    }

    private void handleListenEvents(DataSnapshot dataSnapshot) {
        EmojiModel emoji = dataSnapshot.getValue(EmojiModel.class);
        listener.onDatabaseEvent(emoji.getEmojiType());
    }

    void setListener(EmojiListener listener) {
        this.listener = listener;
    }

    void sendEvent(String emojiType) {
        String key = Util.getSaltString();
        String participantName = Prefs.getDabname();
        EmojiModel model = new EmojiModel(emojiType, key, participantName);
        senderDatabaserReference.setValue(model);
    }

    interface EmojiListener {
        void onDatabaseEvent(String emojiType);
    }

}