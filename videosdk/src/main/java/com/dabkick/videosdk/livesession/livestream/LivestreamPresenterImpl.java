package com.dabkick.videosdk.livesession.livestream;


import com.dabkick.videosdk.Prefs;
import com.dabkick.videosdk.livesession.Presenter;
import com.twilio.video.RoomState;
import com.twilio.video.VideoView;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class LivestreamPresenterImpl implements LivestreamPresenter, ParticipantDatabase.ParticipantModelCallback, Presenter {

    private LivestreamView view;
//    private StreamingManager streamingManager;
    private VideoView myVideoView;
    private ParticipantDatabase participantDatabase;
    private List<Participant> participantList;

    VideoActivity va;

    public LivestreamPresenterImpl(LivestreamView view) {
        this.view = view;
//        streamingManager = new StreamingManager(this);
        participantDatabase = new ParticipantDatabase(this);
        participantList = new ArrayList<>();
    }

    @Override
    public void toggleMyStream() {


        //gopal
//        if (streamingManager.isStreaming()) {
//            streamingManager.stopStreaming(myVideoView);
//        } else {
//            streamingManager.startStreaming(myVideoView);
//        }

        if (va.getInstance().isStreaming)
        {

        }

        else
        {

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
        if (participant.getDabname().equals(Prefs.getDabname())) {
            // do not add participant with same dabname
            return;
        }
        participantList.add(participant);
        view.notifyDataSetChanged();
    }

    public void setAudioEnabled(boolean b) {
        participantDatabase.setIsAudioEnabled(b);
    }

    public void setVideoEnabled(boolean b) {
        participantDatabase.setisVideoEnabled(b);
    }

    @Override
    public void onParticipantRemoved(Participant participant) {
        participantList.remove(participant);
        view.notifyDataSetChanged();
    }

    @Override
    public void onParticipantAudioVideoEnabled() {
        Timber.i("onParticipantAudioVideoEnabled");
        // TODO
        // streamingManager.joinRoom()
        //enter room and the rest happens via listner

        if (va.room == null || va.room.getState() == RoomState.DISCONNECTED)

        {
            view.enterRoomTwilio(); //this will also secure new token if expired
        }

    }

    @Override
    public List<Participant> getLivestreamParticipants() {
        return participantList;
    }

    @Override
    public void onStart() {
        participantDatabase.addChildEventListener();
    }

    @Override
    public void onStop() {
        participantDatabase.removeChildEventListener();
    }
}