package com.dabkick.videosdk.livesession.livestream;


import com.twilio.video.VideoView;

public interface LivestreamView {

    void myStreamClicked();
    void otherUserStreamClicked(int index);
    void myVideoViewCreated(VideoView videoView);
    void otherUserVideoViewCreated(VideoView videoView, int index);
    void addFriendClicked();

}
