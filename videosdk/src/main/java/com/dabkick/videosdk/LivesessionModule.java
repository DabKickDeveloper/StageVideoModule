package com.dabkick.videosdk;


import com.dabkick.videosdk.livesession.overviews.OverviewDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LivesessionModule {

    private final OverviewDatabase.OverviewListener overviewListener;

    public LivesessionModule(OverviewDatabase.OverviewListener overviewListener) {
        this.overviewListener = overviewListener;
    }

    @Provides
    @Singleton
    OverviewDatabase providesOverviewModel() {
        return new OverviewDatabase(overviewListener);
    }

}
