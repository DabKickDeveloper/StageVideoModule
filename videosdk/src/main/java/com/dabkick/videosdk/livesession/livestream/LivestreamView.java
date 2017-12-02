package com.dabkick.videosdk.livesession.livestream;


import com.twilio.video.VideoView;

public interface LivestreamView {

    void clickVideo();
    void otherUserStreamClicked(int index);
    void myVideoViewCreated(VideoView videoView);
    void otherUserVideoViewCreated(VideoView videoView, int index);
    void notifyDataSetChanged();
    void onEnterTwilio();
    void onStartStreaming(VideoView videoView);
    void onStopStreaming();


    void enterRoomTwilio();
    void startStreaming(VideoView videoView);
    void stopStreaming();

    void clickVoice();

    void clickSwap();
}
