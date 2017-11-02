package com.dabkick.videosdk.livesession.chat;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;

class ChatDatabaseReferences extends AbstractDatabaseReferences {

    private static String LIVE_SESSION_CHATS = "liveSessionChats";


    static String getChatReference(@NonNull final String roomId) {
        String roomRef = getRoomReference();
        return roomRef + SEPARATOR +
                roomId;
    }

    static String getRoomReference() {
        return getBase() +
                LIVE_SESSION_CHATS;
    }

}