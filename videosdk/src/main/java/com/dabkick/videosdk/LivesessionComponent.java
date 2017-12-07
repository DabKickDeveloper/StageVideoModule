package com.dabkick.videosdk;

import com.dabkick.videosdk.livesession.LiveSessionActivity;
import com.dabkick.videosdk.livesession.emoji.EmojiPresenter;
import com.dabkick.videosdk.livesession.mediadrawer.MediaDrawerDialogFragment;
import com.dabkick.videosdk.livesession.mediadrawer.MediaFragment;
import com.dabkick.videosdk.livesession.stage.StageDatabase;
import com.dabkick.videosdk.livesession.stage.StagePresenterImpl;
import com.dabkick.videosdk.livesession.stage.StageRecyclerViewAdapter;
import com.dabkick.videosdk.livesession.stage.VideoManager;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {LivesessionModule.class})
public interface LivesessionComponent {

    void inject(StageDatabase a);
    void inject(MediaFragment a);
    void inject(MediaDrawerDialogFragment a);
    void inject(StagePresenterImpl a);
    void inject(LiveSessionActivity a);
    void inject(EmojiPresenter a);
    void inject(VideoManager a);
    void inject(StageRecyclerViewAdapter a);

}