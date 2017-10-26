package com.dabkick.videosdk.livesession;


import android.support.annotation.NonNull;

// TODO builder pattern. consider stage, etc.
public class ChatDatabaseReferences {

    private static final String BASE = "DabKickVideoLiveSessionSDK";
    private static final String LIVE_SESSIONS = "liveSessions";
    private static final String LIVE_SESSION_CHATS = "liveSessionChats";
    private static final String SEPARATOR = "/";

    public static String getChatReference(@NonNull final String developerId, @NonNull final String roomId) {
        String roomRef = getRoomReference(developerId);
        return roomRef + SEPARATOR +
                roomId;
    }

    public static String getRoomReference(@NonNull final String developerId) {
        return BASE + SEPARATOR +
                LIVE_SESSIONS + SEPARATOR +
                developerId + SEPARATOR +
                LIVE_SESSION_CHATS;
    }

}