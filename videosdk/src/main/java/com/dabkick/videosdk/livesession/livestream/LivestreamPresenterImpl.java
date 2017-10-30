package com.dabkick.videosdk.livesession.livestream;


import com.twilio.video.VideoView;

public class LivestreamPresenterImpl implements LivestreamPresenter {

    private LivestreamView view;
    private StreamingManager manager;

    public LivestreamPresenterImpl(LivestreamView view) {
        this.view = view;
        manager = new StreamingManager(this);
    }

    public void toggleStream(VideoView myVideoView) {
        if (manager.isStreaming()) {

        } else {
            manager.startStreaming(myVideoView);
        }
    }

}