package com.dabkick.videosdk.livesession.livestream;


import com.twilio.video.VideoView;

public interface LivestreamView {

    void myStreamClicked(VideoView videoView);
    void otherUserStreamClicked(int index);

}
