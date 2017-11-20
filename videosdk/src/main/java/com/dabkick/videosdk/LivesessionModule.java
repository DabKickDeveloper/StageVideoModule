package com.dabkick.videosdk;


import com.dabkick.videosdk.livesession.emoji.EmojiDatabase;
import com.dabkick.videosdk.livesession.mediadrawer.MediaDatabase;
import com.dabkick.videosdk.livesession.overviews.OverviewDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class LivesessionModule {

    public LivesessionModule() {

    }

    @Provides
    @Singleton
    EmojiDatabase providesEmojiDatabase() { return new EmojiDatabase(); }

    @Provides
    @Singleton
    OverviewDatabase providesOverviewModel() {
        return new OverviewDatabase();
    }

    @Provides
    @Singleton
    MediaDatabase providesMediaDatabase() { return new MediaDatabase(); }

}
