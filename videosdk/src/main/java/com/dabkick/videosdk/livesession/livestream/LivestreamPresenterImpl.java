package com.dabkick.videosdk.livesession.livestream;


import com.twilio.video.VideoView;

public class LivestreamPresenterImpl implements LivestreamPresenter {

    private LivestreamView view;
    private StreamingManager manager;
    private VideoView myVideoView;

    public LivestreamPresenterImpl(LivestreamView view) {
        this.view = view;
        manager = new StreamingManager(this);
    }

    @Override
    public void toggleMyStream() {
        if (manager.isStreaming()) {
            manager.stopStreaming(myVideoView);
        } else {
            manager.startStreaming(myVideoView);
        }
    }

    @Override
    public void bindMyVideoView(VideoView myVideoView) {
        this.myVideoView = myVideoView;
    }

}