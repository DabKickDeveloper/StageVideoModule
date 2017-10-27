package com.dabkick.videosdk.livesession.chat;


import com.dabkick.videosdk.Prefs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

class ChatModel {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private final String roomKey;

    ChatModel(ChatPresenter presenter) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        String developerId = Prefs.getDeveloperId();
        String roomPath =  ChatDatabaseReferences.getRoomReference(developerId);
        roomKey = firebaseDatabase.getReference(roomPath).push().getKey();
        String chatPath = ChatDatabaseReferences.getChatReference(developerId, roomKey);
        databaseReference = firebaseDatabase.getReference(chatPath);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                presenter.messageAdded(chatMessage);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String s) {}
            public void onChildRemoved(DataSnapshot dataSnapshot) {}
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {}
            public void onCancelled(DatabaseError databaseError) {}
        };
        databaseReference.addChildEventListener(childEventListener);


    }

    void sendMessage(String message) {
        String senderUserId = "";
        if (firebaseAuth.getCurrentUser() != null) {
            senderUserId = firebaseAuth.getCurrentUser().getUid();
        } else {
            Timber.w("User is not logged in");
        }

        ChatMessage chatMessage = new ChatMessage(message, senderUserId);
        databaseReference.push().setValue(chatMessage);
    }

}