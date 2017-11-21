package com.dabkick.videosdk.livesession.livestream;


import com.dabkick.videosdk.Prefs;
import com.twilio.video.VideoView;

import java.util.ArrayList;
import java.util.List;

public class LivestreamPresenterImpl implements LivestreamPresenter, ParticipantDatabase.ParticipantModelCallback {

    private LivestreamView view;
    private StreamingManager streamingManager;
    private VideoView myVideoView;
    private ParticipantDatabase participantDatabase;
    private List<Participant> participantList;

    public LivestreamPresenterImpl(LivestreamView view) {
        this.view = view;
        streamingManager = new StreamingManager(this);
        participantDatabase = new ParticipantDatabase(this);
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
    public void onFinishing() {
        participantDatabase.removeSelfFromDatabase();
    }

    @Override
    public void onParticipantAdded(Participant participant) {
        if (participant.dabname.equals(Prefs.getDabname())) {
            // do not add participant with same dabname
            return;
        }
        participantList.add(participant);
        view.notifyDataSetChanged();
    }

    @Override
    public void onParticipantRemoved(Participant participant) {
        participantList.remove(participant);
        view.notifyDataSetChanged();
    }

    @Override
    public List<Participant> getLivestreamParticipants() {
        return participantList;
    }

}