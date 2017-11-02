package com.dabkick.videosdk.livesession.livestream;



interface ParticipantModelCallback {

    void onAdded(Participant participant);
    void onRemoved(Participant participant);

}