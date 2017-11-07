package com.dabkick.videosdk.livesession.stage;

import java.util.List;

public class StagePresenterImpl implements StagePresenter, StageModel.StageModelCallback {

    private StageView view;
    private StageModel model;

    public StagePresenterImpl(StageView view) {
        this.view = view;
        model = new StageModel(this);
    }

    @Override
    public void onStageVideoChanged() {
        view.onStageDataUpdated();
    }

    @Override
    public List<StageVideo> getStageItems() {
        return model.getStageVideoList();
    }

    @Override
    public ObservableVideoView.VideoControlListener getVideoControlsListener() {
        return new ObservableVideoView.VideoControlListener() {
            @Override
            public void onPause(int secs) {
                model.pauseVideo(secs);
            }

            @Override
            public void onResume() {
                model.resumeVideo();
            }

            @Override
            public void onSeekBarChanged(int currentTime) {
                model.seekTo(currentTime);
            }
        };
    }
}