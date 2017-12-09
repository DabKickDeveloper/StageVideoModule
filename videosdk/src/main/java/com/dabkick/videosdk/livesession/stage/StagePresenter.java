package com.dabkick.videosdk.livesession.stage;


import com.dabkick.videosdk.livesession.Presenter;

public interface StagePresenter extends Presenter {

    StageRecyclerViewAdapter.VideoControlListener getVideoControlsListener();
    void onUserSwipedStage(int newPosition);

}