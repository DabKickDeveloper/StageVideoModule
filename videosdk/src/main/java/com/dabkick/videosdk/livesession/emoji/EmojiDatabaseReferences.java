package com.dabkick.videosdk.livesession.emoji;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;

class EmojiDatabaseReferences extends AbstractDatabaseReferences {

    private static final String EMOJIS = "emojis";

    static String getEmojiRoomReference(@NonNull final String roomId) {
        String roomRef = getEmojiReference();
        return roomRef + SEPARATOR +
                roomId;
    }

    static String getEmojiReference() {
        return getBase() +
                EMOJIS;
    }

}