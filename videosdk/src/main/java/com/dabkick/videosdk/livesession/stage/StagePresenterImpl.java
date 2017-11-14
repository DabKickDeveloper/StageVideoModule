package com.dabkick.videosdk.livesession.stage;

import java.util.List;

import timber.log.Timber;

public class StagePresenterImpl implements StagePresenter, StageDatabase.StageDatabaseCallback {

    private StageView view;
    private StageDatabase model;

    public StagePresenterImpl(StageView view) {
        this.view = view;
        model = new StageDatabase(this);
    }

    @Override
    public void onStageVideoAdded() {
        view.onStageDataUpdated();
    }

    @Override
    public void onStageVideoTimeChanged(int position, long playedMillis) {
        view.onStageVideoTimeChanged(position, playedMillis);
    }

    @Override
    public void onStageVideoStateChanged(int i, String newState) {
        // if video is playing and server state changes to paused
        if (model.getStageModelList().get(0).isPlaying() &&
                newState.equals("paused")) {
            Timber.i("newState: %s, pausing video...", newState);
            view.onStageVideoStateChanged(i, true);
        // if video is paused and server state changes to playing
        } else if (!model.getStageModelList().get(0).isPlaying() &&
                newState.equals("playing")) {
            Timber.i("newState: %s, playing video...", newState);
            view.onStageVideoStateChanged(i, false);
        }
    }

    @Override
    public List<StageModel> getStageItems() {
        return model.getStageModelList();
    }

    @Override
    public StageRecyclerViewAdapter.VideoControlListener getVideoControlsListener() {
        return new StageRecyclerViewAdapter.VideoControlListener() {
            @Override
            public void onPause(long milliseconds) {
                model.pauseVideo(milliseconds);
            }

            @Override
            public void onResume(long milliseconds) {
                model.resumeVideo(milliseconds);
            }

            @Override
            public void onSeekBarChanged(long currentTime) {
                model.seekTo(currentTime);
            }
        };
    }
}