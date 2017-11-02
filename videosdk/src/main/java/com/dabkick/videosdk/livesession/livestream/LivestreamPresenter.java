package com.dabkick.videosdk.livesession.livestream;


import com.twilio.video.VideoView;

public interface LivestreamPresenter {


    void toggleMyStream();
    void bindMyVideoView(VideoView videoView);


}
