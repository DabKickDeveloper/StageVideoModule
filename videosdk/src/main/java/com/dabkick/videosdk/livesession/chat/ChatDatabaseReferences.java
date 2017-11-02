package com.dabkick.videosdk.livesession.chat;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;

class ChatDatabaseReferences extends AbstractDatabaseReferences {

    private static String LIVE_SESSION_CHATS = "liveSessionChats";


    static String getChatReference(@NonNull final String developerId, @NonNull final String roomId) {
        String roomRef = getRoomReference(developerId);
        return roomRef + SEPARATOR +
                roomId;
    }

    static String getRoomReference(@NonNull final String developerId) {
        return getBase() +
                developerId + SEPARATOR +
                LIVE_SESSION_CHATS;
    }

}