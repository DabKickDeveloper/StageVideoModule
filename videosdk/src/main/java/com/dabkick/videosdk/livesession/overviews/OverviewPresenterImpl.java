package com.dabkick.videosdk.livesession.overviews;


import com.dabkick.videosdk.SdkApp;

import javax.inject.Inject;

import timber.log.Timber;

public class OverviewPresenterImpl implements OverviewPresenter, OverviewDatabase.OverviewListener {

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
        overviewDatabase.setStageIndex(newPosition);
    }

    @Override
    public void onStageIndexFromDatabaseChanged(int newIndex) {
        Timber.i("database changed stage index :%s", newIndex);
        view.setStageIndex(newIndex);
    }

}