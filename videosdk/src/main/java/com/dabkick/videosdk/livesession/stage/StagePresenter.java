package com.dabkick.videosdk.livesession.stage;


import java.util.List;

public interface StagePresenter {

    List<StageModel> getStageItems();

    StageRecyclerViewAdapter.VideoControlListener getVideoControlsListener();

    // Activity Lifecycle callbacks
    void onStart();
    void onStop();
}