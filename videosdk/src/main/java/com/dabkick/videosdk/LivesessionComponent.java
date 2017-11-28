package com.dabkick.videosdk;

import com.dabkick.videosdk.livesession.LiveSessionActivity;
import com.dabkick.videosdk.livesession.emoji.EmojiPresenter;
import com.dabkick.videosdk.livesession.mediadrawer.MediaDrawerDialogFragment;
import com.dabkick.videosdk.livesession.mediadrawer.MediaFragment;
import com.dabkick.videosdk.livesession.stage.StageDatabase;
import com.dabkick.videosdk.livesession.stage.StagePresenterImpl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LivesessionModule.class})
public interface LivesessionComponent {

    void inject(StageDatabase stageDatabase);
    void inject(MediaFragment mediaFragment);
    void inject(MediaDrawerDialogFragment mediaDrawerDialogFragment);
    void inject(StagePresenterImpl stagePresenterImpl);
    void inject(LiveSessionActivity liveSessionActivity);
    void inject(EmojiPresenter emojiPresenter);

}