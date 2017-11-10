package com.dabkick.videosdk.livesession.stage;


import java.util.List;

public interface StagePresenter {

    List<StageVideo> getStageItems();

    StageRecyclerViewAdapter.VideoControlListener getVideoControlsListener();
}