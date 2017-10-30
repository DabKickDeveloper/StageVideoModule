package com.dabkick.videosdk.livesession.livestream;


import com.twilio.video.VideoView;

interface StreamingManagerInterface {

    void startStreaming(VideoView videoView);
    void stopStreaming();
    boolean isStreaming();


}
