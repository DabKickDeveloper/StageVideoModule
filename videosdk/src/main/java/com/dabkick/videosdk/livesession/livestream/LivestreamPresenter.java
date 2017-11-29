package com.dabkick.videosdk.livesession.livestream;


import com.dabkick.videosdk.livesession.Presenter;
import com.twilio.video.VideoView;

import java.util.List;

public interface LivestreamPresenter extends Presenter {

    List<Participant> getLivestreamParticipants();
    void toggleMyStream();
    void bindMyVideoView(VideoView videoView);
    void setAudioEnabled(boolean b);
    void setVideoEnabled(boolean b);

    void onFinishing();


}
