package com.dabkick.videosdk.livesession.overviews;


public class OverviewModel {

    private int stagedVideoPosition;

    public OverviewModel() {
        // required Firebase empty constructor
    }

    public OverviewModel(int stagedVideoPosition) {
        this.stagedVideoPosition = stagedVideoPosition;
    }

    public int getStagedVideoPosition() {
        return stagedVideoPosition;
    }


}