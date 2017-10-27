package com.dabkick.videosdk.livesession.chat;


import android.support.annotation.NonNull;

// TODO builder pattern. consider stage, etc.
class ChatDatabaseReferences {

    private static final String BASE = "DabKickVideoLiveSessionSDK";
    private static final String LIVE_SESSIONS = "liveSessions";
    private static final String LIVE_SESSION_CHATS = "liveSessionChats";
    private static final String SEPARATOR = "/";

    static String getChatReference(@NonNull final String developerId, @NonNull final String roomId) {
        String roomRef = getRoomReference(developerId);
        return roomRef + SEPARATOR +
                roomId;
    }

    static String getRoomReference(@NonNull final String developerId) {
        return BASE + SEPARATOR +
                LIVE_SESSIONS + SEPARATOR +
                developerId + SEPARATOR +
                LIVE_SESSION_CHATS;
    }

}