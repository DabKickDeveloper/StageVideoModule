package com.dabkick.videosdk.livesession.chat;


import com.dabkick.videosdk.Prefs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import timber.log.Timber;

class ChatDatabase {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    ChatDatabase(ChatPresenter presenter) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        String chatPath = ChatDatabaseReferences.getChatReference(ChatDatabaseReferences.getSessionId());
        databaseReference = firebaseDatabase.getReference(chatPath);

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
                presenter.messageAdded(chatModel);
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
        String dabname = Prefs.getDabname();
        ChatModel chatModel = new ChatModel(message, senderUserId, dabname);
        databaseReference.push().setValue(chatModel);
    }

}