package com.dabkick.videosdk;

import com.dabkick.videosdk.livesession.stage.StageDatabase;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LivesessionModule.class})
public interface LivesessionComponent {
    void inject(StageDatabase stageDatabase);

}
