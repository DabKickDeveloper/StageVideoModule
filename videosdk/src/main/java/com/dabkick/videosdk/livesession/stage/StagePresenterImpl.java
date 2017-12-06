package com.dabkick.videosdk.livesession.stage;

import com.annimon.stream.Stream;
import com.dabkick.videosdk.DabKickSession;
import com.dabkick.videosdk.DabKickVideoInfo;
import com.dabkick.videosdk.SdkApp;
import com.dabkick.videosdk.livesession.mediadrawer.MediaItemClickEvent;
import com.dabkick.videosdk.livesession.overviews.OverviewDatabase;
import com.dabkick.videosdk.livesession.overviews.OverviewView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class StagePresenterImpl implements StagePresenter, StageDatabase.StageDatabaseCallback,
        OverviewDatabase.OverviewListener{

    private StageView stageView;
    private OverviewView overviewView;

    // models
    @Inject StageDatabase stageDatabase;
    @Inject OverviewDatabase overviewDatabase;

    public StagePresenterImpl(StageView stageView, OverviewView overviewView) {
        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
        this.stageView = stageView;
        this.overviewView = overviewView;
        stageDatabase.setCallback(this);
        overviewDatabase.setListener(this);

        // add any provided videos to stage
        DabKickSession.DabKickVideoProvider provider = ((SdkApp) SdkApp.getAppContext()).getDabKickSession().getDabKickVideoProvider();
        ArrayList<DabKickVideoInfo> videoList = provider.startDabKickWithVideos();
        Stream.of(videoList).forEach(v -> stageDatabase.addVideo(v.getVideoUrl()));
    }

    @Override
    public void onUserSwipedStage(int newPosition) {
        Timber.i("on user swiped stage", newPosition);
        String newKey = stageDatabase.getKeyFromIndex(newPosition);
        overviewDatabase.setStageKey(newKey);
    }

    @Override
    public void onStageKeyFromDatabaseChanged(String newKey) {
        Timber.i("database changed stage index :%s", newKey);
        int newIndex = stageDatabase.getIndexFromKey(newKey);

        if (newIndex == -1) Timber.w("key is not present in stage video list: %s", newKey);
        else overviewView.setStageIndexByKey(newIndex);

    }


    @Override
    public void onStageVideoAdded() {
        stageView.onStageDataUpdated();
    }

    @Override
    public void onStageVideoTimeChanged(int position, long playedMillis) {
        stageView.onStageVideoTimeChanged(position, playedMillis);
    }

    @Override
    public void onStageVideoStateChanged(int i, String newState) {
        // if video is playing and server state changes to paused
        int index = stageDatabase.getIndexFromKey(overviewDatabase.getStagedVideoKey());

        if (stageDatabase.getStageModelList().get(index).isPlaying() &&
                newState.equals("paused")) {
            Timber.i("newState: %s, pausing video...", newState);
            stageView.onStageVideoStateChanged(i, true);
        // if video is paused and server state changes to playing
        } else if (!stageDatabase.getStageModelList().get(index).isPlaying() &&
                newState.equals("playing")) {
            Timber.i("newState: %s, playing video...", newState);
            stageView.onStageVideoStateChanged(i, false);
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

    @Override
    public void onDestroy() {}

}