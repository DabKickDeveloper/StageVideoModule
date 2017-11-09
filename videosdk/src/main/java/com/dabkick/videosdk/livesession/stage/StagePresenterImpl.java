package com.dabkick.videosdk.livesession.stage;

import java.util.List;

import timber.log.Timber;

public class StagePresenterImpl implements StagePresenter, StageModel.StageModelCallback {

    private StageView view;
    private StageModel model;

    public StagePresenterImpl(StageView view) {
        this.view = view;
        model = new StageModel(this);
    }

    @Override
    public void onStageVideoAdded() {
        view.onStageDataUpdated();
    }

    @Override
    public void onStageVideoTimeChanged(int position, int playedMillis) {
        view.onStageVideoTimeChanged(position, playedMillis);
    }

    @Override
    public void onStageVideoStateChanged(int i, String newState) {
        // if video is playing and server state changes to paused
        if (model.getStageVideoList().get(0).isPlaying() &&
                newState.equals("paused")) {
            Timber.d("newState: %s, pausing video...", newState);
            view.onStageVideoStateChanged(i, true);
        // if video is paused and server state changes to playing
        } else if (!model.getStageVideoList().get(0).isPlaying() &&
                newState.equals("playing")) {
            Timber.d("newState: %s, playing video...", newState);
            view.onStageVideoStateChanged(i, false);
        }
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