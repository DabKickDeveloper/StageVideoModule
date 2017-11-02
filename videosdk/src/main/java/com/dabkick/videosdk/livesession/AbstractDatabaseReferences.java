package com.dabkick.videosdk.livesession;


public abstract class AbstractDatabaseReferences {

    private static final String BASE = "DabKickVideoLiveSessionSDK";
    private static final String LIVE_SESSIONS = "liveSessions";
    protected static final String SEPARATOR = "/";

    protected static String getBase() {
        return BASE + SEPARATOR + LIVE_SESSIONS + SEPARATOR;
    }



}
