package com.dabkick.videosdk.livesession.livestream;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;

public class ParticipantDatabaseReferences extends AbstractDatabaseReferences {

    private static String PARTICIPANTS = "participants";

    static String getParticipantReference(@NonNull final String roomId) {
        String roomRef = getRoomReference();
        return roomRef + SEPARATOR +
                roomId;
    }

    static String getRoomReference() {
        return getBase() +
                PARTICIPANTS;
    }

}
