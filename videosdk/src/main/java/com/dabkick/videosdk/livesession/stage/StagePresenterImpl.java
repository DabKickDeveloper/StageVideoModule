package com.dabkick.videosdk.livesession.stage;

import com.dabkick.videosdk.SdkApp;
import com.dabkick.videosdk.livesession.mediadrawer.MediaItemClickEvent;
import com.dabkick.videosdk.livesession.overviews.OverviewDatabase;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class StagePresenterImpl implements StagePresenter, StageDatabase.StageDatabaseCallback {

    private StageView view;
    private StageDatabase model;
    @Inject OverviewDatabase overviewDatabase;
    private NotifyStageListener stageViewListener;

    public StagePresenterImpl(StageView view, NotifyStageListener stageViewListener) {

        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
        this.stageViewListener = stageViewListener;
        this.view = view;
        model = new StageDatabase(this);
    }

    @Override
    public void onStageVideoAdded() {
        view.onStageDataUpdated();
        stageViewListener.notifyStageRecyclerView();
    }

    @Override
    public void onStageVideoTimeChanged(int position, long playedMillis) {
        view.onStageVideoTimeChanged(position, playedMillis);
    }

    @Override
    public void onStageVideoStateChanged(int i, String newState) {
        // if video is playing and server state changes to paused
        if (model.getStageModelList().get(overviewDatabase.getStagedVideoPosition()).isPlaying() &&
                newState.equals("paused")) {
            Timber.i("newState: %s, pausing video...", newState);
            view.onStageVideoStateChanged(i, true);
        // if video is paused and server state changes to playing
        } else if (!model.getStageModelList().get(overviewDatabase.getStagedVideoPosition()).isPlaying() &&
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MediaItemClickEvent event) {
        model.addVideo(event.url);
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }


    public interface NotifyStageListener {
        void notifyStageRecyclerView();
    }

}