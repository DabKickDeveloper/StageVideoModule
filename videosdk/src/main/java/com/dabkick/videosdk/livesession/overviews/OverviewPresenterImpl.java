package com.dabkick.videosdk.livesession.overviews;


import com.dabkick.videosdk.SdkApp;
import com.dabkick.videosdk.livesession.stage.StageDatabase;

import javax.inject.Inject;

import timber.log.Timber;

public class OverviewPresenterImpl implements OverviewPresenter, OverviewDatabase.OverviewListener {

    @Inject StageDatabase stageDatabase;
    @Inject OverviewDatabase overviewDatabase;
    private OverviewView view;

    public OverviewPresenterImpl(OverviewView view) {
        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
        this.view = view;
        overviewDatabase.setListener(this);
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
        else view.setStageIndexByKey(newIndex);

    }


}