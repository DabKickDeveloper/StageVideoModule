package com.dabkick.videosdk.livesession;


import android.content.Context;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChatModel {

    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private Context context;

    public ChatModel(Context context) {
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        //databaseReference = firebaseDatabase.getReference(PathBuilder.PathToChat);
    }



}
