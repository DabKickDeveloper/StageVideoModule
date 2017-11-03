package com.dabkick.videosdk.livesession.livestream;


import com.twilio.video.VideoView;

import java.util.ArrayList;
import java.util.List;

public class LivestreamPresenterImpl implements LivestreamPresenter, ParticipantModel.ParticipantModelCallback {

    private LivestreamView view;
    private StreamingManager streamingManager;
    private VideoView myVideoView;
    private ParticipantModel participantModel;
    private List<Participant> participantList;

    public LivestreamPresenterImpl(LivestreamView view) {
        this.view = view;
        streamingManager = new StreamingManager(this);
        participantModel = new ParticipantModel(this);
        participantList = new ArrayList<>();
    }

    @Override
    public void toggleMyStream() {
        if (streamingManager.isStreaming()) {
            streamingManager.stopStreaming(myVideoView);
        } else {
            streamingManager.startStreaming(myVideoView);
        }
    }

    @Override
    public void bindMyVideoView(VideoView myVideoView) {
        this.myVideoView = myVideoView;
    }

    @Override
    public void onParticipantAdded(Participant participant) {
        participantList.add(participant);
    }

    @Override
    public void onParticipantRemoved(Participant participant) {
        participantList.remove(participant);
    }

    @Override
    public List<Participant> getLivestreamParticipants() {
        return participantList;
    }

}