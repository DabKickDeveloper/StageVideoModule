package com.dabkick.videosdk.livesession.livestream;


import android.support.annotation.NonNull;

import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;

class ParticipantDatabaseReferences extends AbstractDatabaseReferences {

    private static String PARTICIPANTS = "participants";
    static String IS_AUDIO_ENABLED = "isAudioEnabled", IS_VIDEO_ENABLED = "isVideoEnabled";

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
