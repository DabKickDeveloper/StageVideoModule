package com.dabkick.videosdk.livesession.stage;


import com.dabkick.videosdk.livesession.Presenter;

import java.util.List;

public interface StagePresenter extends Presenter {

    List<StageModel> getStageItems();
    StageRecyclerViewAdapter.VideoControlListener getVideoControlsListener();
    void onUserSwipedStage(int newPosition);

}