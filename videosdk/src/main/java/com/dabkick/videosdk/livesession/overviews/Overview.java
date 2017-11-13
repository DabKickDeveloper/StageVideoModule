package com.dabkick.videosdk.livesession.overviews;


public class Overview {

    private int stagedVideoPosition;

    public Overview() {
        // required Firebase empty constructor
    }

    public Overview(int stagedVideoPosition) {
        this.stagedVideoPosition = stagedVideoPosition;
    }

    public int getStagedVideoPosition() {
        return stagedVideoPosition;
    }


}