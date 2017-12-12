package com.dabkick.videosdk.livesession.stage;


import com.dabkick.videosdk.livesession.Presenter;

public interface StagePresenter extends Presenter {

    void onUserSwipedStage(int newPosition);

    void updateStagePosition(String key);
}