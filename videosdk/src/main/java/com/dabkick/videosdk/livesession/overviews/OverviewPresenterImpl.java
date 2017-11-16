package com.dabkick.videosdk.livesession.overviews;


import com.dabkick.videosdk.SdkApp;

import javax.inject.Inject;

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
        overviewDatabase.setStageIndex(newPosition);
    }

    @Override
    public void onDatabaseStageIndexChanged(int newPosition) {
        view.setStageIndex(newPosition);
    }

    @Override
    public void onOverviewChanged() {

    }

    @Override
    public void onStageIndexFromDatabaseChanged(int newIndex) {
        view.setStageIndex(newIndex);
    }
}