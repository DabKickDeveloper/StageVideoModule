package com.dabkick.videosdk.livesession.chat;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;

class ChatDatabaseReferences extends AbstractDatabaseReferences {

    private static String CHATS = "chats";


    static String getChatReference(@NonNull final String roomId) {
        String roomRef = getRoomReference();
        return roomRef + SEPARATOR +
                roomId;
    }

    static String getRoomReference() {
        return getBase() +
                CHATS;
    }

}