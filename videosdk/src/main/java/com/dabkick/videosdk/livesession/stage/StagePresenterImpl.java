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
    private NotifyStageListener stageViewListener;

    // models
    @Inject StageDatabase stageDatabase;
    @Inject OverviewDatabase overviewDatabase;

    public StagePresenterImpl(StageView view, NotifyStageListener stageViewListener) {
        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
        this.stageViewListener = stageViewListener;
        this.view = view;
        stageDatabase.setCallback(this);
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
        if (stageDatabase.getStageModelList().get(overviewDatabase.getStagedVideoPosition()).isPlaying() &&
                newState.equals("paused")) {
            Timber.i("newState: %s, pausing video...", newState);
            view.onStageVideoStateChanged(i, true);
        // if video is paused and server state changes to playing
        } else if (!stageDatabase.getStageModelList().get(overviewDatabase.getStagedVideoPosition()).isPlaying() &&
                newState.equals("playing")) {
            Timber.i("newState: %s, playing video...", newState);
            view.onStageVideoStateChanged(i, false);
        }
    }

    @Override
    public List<StageModel> getStageItems() {
        return stageDatabase.getStageModelList();
    }

    @Override
    public StageRecyclerViewAdapter.VideoControlListener getVideoControlsListener() {
        return new StageRecyclerViewAdapter.VideoControlListener() {
            @Override
            public void onPause(long milliseconds) {
                stageDatabase.pauseVideo(milliseconds);
            }

            @Override
            public void onResume(long milliseconds) {
                stageDatabase.resumeVideo(milliseconds);
            }

            @Override
            public void onSeekBarChanged(long currentTime) {
                stageDatabase.seekTo(currentTime);
            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MediaItemClickEvent event) {
        stageDatabase.addVideo(event.url);
    }

    @Override
    public void onStart() {
        stageDatabase.addChildEventListener();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        stageDatabase.removeChildEventListener();
        EventBus.getDefault().unregister(this);
    }

    public interface NotifyStageListener {
        void notifyStageRecyclerView();
    }

}