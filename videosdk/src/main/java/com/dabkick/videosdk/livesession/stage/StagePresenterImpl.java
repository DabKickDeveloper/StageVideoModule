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

import javax.inject.Inject;

import timber.log.Timber;

public class StagePresenterImpl implements StagePresenter,
        OverviewDatabase.OverviewListener{

    private StageView stageView;
    private OverviewView overviewView;

    // models
    @Inject StageDatabase stageDatabase;
    @Inject OverviewDatabase overviewDatabase;
    @Inject VideoManager videoManager;

    public StagePresenterImpl(StageView stageView, OverviewView overviewView) {
        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
        this.stageView = stageView;
        this.overviewView = overviewView;
        overviewDatabase.setListener(this);

        // add any provided videos to stage
        DabKickSession.DabKickVideoProvider provider = ((SdkApp) SdkApp.getAppContext()).getDabKickSession().getDabKickVideoProvider();
        ArrayList<DabKickVideoInfo> videoList = provider.startDabKickWithVideos();
        Stream.of(videoList).forEach(v -> stageDatabase.addVideo(v.getVideoUrl()));
    }

    @Override
    public void onUserSwipedStage(int newPosition) {
        String newKey = videoManager.getKeyFromIndex(newPosition);
        overviewDatabase.setStageKey(newKey);
    }

    @Override
    public void onStageKeyFromDatabaseChanged(String newKey) {
        int newIndex = videoManager.getIndexFromKey(newKey);

        if (newIndex == -1) Timber.w("key is not present in stage video list: %s", newKey);
        else overviewView.setStageIndexByKey(newIndex);

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