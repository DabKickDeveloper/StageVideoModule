package com.dabkick.videosdk;


import com.dabkick.videosdk.livesession.overviews.OverviewDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LivesessionModule {

    public LivesessionModule() {}

    @Provides
    @Singleton
    OverviewDatabase providesOverviewModel() {
        return new OverviewDatabase();
    }

}
