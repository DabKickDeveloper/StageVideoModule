package com.dabkick.videosdk.livesession.emoji;


import com.dabkick.videosdk.Prefs;
import com.dabkick.videosdk.Util;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

public class EmojiDatabase {

    private DatabaseReference databaseReference;
    private EmojiListener listener;

    public EmojiDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        String path = EmojiDatabaseReferences.getEmojiReference();
        databaseReference = firebaseDatabase.getReference(path);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                EmojiModel emoji = dataSnapshot.getValue(EmojiModel.class);
                Timber.i("emoji added: %s", emoji.getKey());
                listener.onDatabaseEvent(emoji.getEmojiType());

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                EmojiModel emoji = dataSnapshot.getValue(EmojiModel.class);
                Timber.i("emoji changed: %s", emoji.getKey());
                listener.onDatabaseEvent(emoji.getEmojiType());
            }
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addChildEventListener(childEventListener);
    }

    void setListener(EmojiListener listener) {
        this.listener = listener;
    }

    void sendEvent(String emojiType) {
        String key = Util.getSaltString();
        String participantName = Prefs.getDabname();
        EmojiModel model = new EmojiModel(emojiType, key, participantName);
        databaseReference.setValue(model);
    }

    interface EmojiListener {
        void onDatabaseEvent(String emojiType);
    }


}