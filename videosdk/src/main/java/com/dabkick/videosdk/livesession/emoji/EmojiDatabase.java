package com.dabkick.videosdk.livesession.emoji;


import com.dabkick.videosdk.Prefs;
import com.dabkick.videosdk.Util;
import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashSet;
import java.util.Set;

import timber.log.Timber;

public class EmojiDatabase {

    // receiver is for database listening, sender for posting updates
    private DatabaseReference databaseReference;
    private EmojiListener listener;
    private Set<String> ignoreUserIds;

    public EmojiDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        ignoreUserIds = new HashSet<>();

        String receiverPath = EmojiDatabaseReferences.getEmojiRoomReference(AbstractDatabaseReferences.getSessionId());
        databaseReference = firebaseDatabase.getReference(receiverPath);

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

        // this listener is to ignore the first emoji event, which falsely triggers the child listener
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot tmp : dataSnapshot.getChildren()) {
                    EmojiModel emoji = tmp.getValue(EmojiModel.class);
                    ignoreUserIds.add(emoji.getUserId());
                }

                //EmojiModel emoji = dataSnapshot.child(AbstractDatabaseReferences.getSessionId()).getValue(EmojiModel.class);
                /* if (emoji != null) {
                    ignoreKey = emoji.getKey();
                    return;
                } */

                databaseReference.addChildEventListener(childEventListener);

            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        });

    }

    private void handleListenEvents(DataSnapshot dataSnapshot) {
        EmojiModel emoji = dataSnapshot.getValue(EmojiModel.class);
        if (ignoreUserIds.contains(emoji.getUserId())) {
            ignoreUserIds.remove(emoji.getUserId());
            Timber.d("ignore key : %s", emoji.getUserId());
            return;
        }
        listener.onDatabaseEvent(emoji.getEmojiType());
    }

    void setListener(EmojiListener listener) {
        this.listener = listener;
    }

    void sendEvent(String emojiType) {
        String key = Util.getSaltString();
        String userId = Prefs.getUserId();
        EmojiModel model = new EmojiModel(emojiType, key, userId);
        databaseReference.child(userId).setValue(model);
    }

    interface EmojiListener {
        void onDatabaseEvent(String emojiType);
    }

}