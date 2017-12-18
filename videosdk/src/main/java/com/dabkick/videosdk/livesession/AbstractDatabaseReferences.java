package com.dabkick.videosdk.livesession;


import com.dabkick.videosdk.Prefs;

public abstract class AbstractDatabaseReferences {

    private static final String BASE = "DabKickVideoLiveSessionSDK";
    private static final String LIVE_SESSIONS = "liveSessions";
    protected static final String SEPARATOR = "/";

    protected static String getBase() {
        return BASE + SEPARATOR +
                LIVE_SESSIONS + SEPARATOR +
                Prefs.getDeveloperKey() + SEPARATOR;
    }

    public static String getSessionId() {
        return "DabKick Lobby";
    }

}
