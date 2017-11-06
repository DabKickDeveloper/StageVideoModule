package com.dabkick.videosdk.livesession.stage;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;

public class StageDatabaseReferences extends AbstractDatabaseReferences {

    private static String LIVE_SESSION_VIDEOS = "liveSessionVideos";

    static String getStageReference(@NonNull final String roomId) {
        String roomRef = getRoomReference();
        return roomRef + SEPARATOR +
                roomId;
    }

    static String getRoomReference() {
        return getBase() +
                LIVE_SESSION_VIDEOS;
    }

}

