package com.dabkick.videosdk.livesession.overviews;


public interface OverviewPresenter {

    void onDatabaseStageIndexChanged(int newPosition);
    void onUserSwipedStage(int newPosition);

}
