package com.dabkick.videosdk;

import com.dabkick.videosdk.livesession.stage.StageModel;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LivesessionModule.class})
public interface LivesessionComponent {
    void inject(StageModel stageModel);

}
