package com.dabkick.videosdk.livesession.overviews;


public class OverviewModel {

    private String stagedVideoKey;

    public OverviewModel() {
        // required Firebase empty constructor
    }

    public OverviewModel(String stagedVideoKey) {
        this.stagedVideoKey = stagedVideoKey;
    }

    public String getStagedVideoKey() {
        return stagedVideoKey;
    }


}