package com.dabkick.videosdk.livesession.stage;


public interface StageView {

    void onStageDataUpdated();

    void onStageVideoTimeChanged(int position, int playedMillis);

    void onStageVideoStateChanged(int position, boolean shouldPause);

}