package com.dabkick.videosdk.livesession.overviews;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;

class OverviewDatabaseReferences extends AbstractDatabaseReferences {

    private static final String OVERVIEWS = "overviews";
    static final String STAGED_VIDEO_POSITION = "stagedVideoPosition";

    static String getOverviewRoomReference(@NonNull final String roomId) {
        String roomRef = getOverviewReference();
        return roomRef + SEPARATOR +
                roomId;
    }

    static String getOverviewReference() {
        return getBase() +
                OVERVIEWS;
    }

}
