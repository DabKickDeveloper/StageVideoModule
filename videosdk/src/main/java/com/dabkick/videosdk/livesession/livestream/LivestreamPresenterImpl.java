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

    VideoActivity va = VideoActivity.getInstance();

    public LivestreamPresenterImpl(LivestreamView view) {
        this.view = view;
//        streamingManager = new StreamingManager(this);
        participantDatabase = new ParticipantDatabase(this);
        participantList = new ArrayList<>();
        participantDatabase.addChildEventListener();
    }

    @Override
    public void toggleMyStream() {


        //gopal
//        if (streamingManager.isStreaming()) {
//            streamingManager.stopStreaming(myVideoView);
//        } else {
//            streamingManager.startStreaming(myVideoView);
//        }



        if (va.isStreaming)
        {
            view.stopStreaming();
            va.isStreaming = false;
        }

        else
        {

            //if not in room: enable audio, video booleans in firebase and in local tracks, update UI and enter room

            view.startStreaming(myVideoView);
            va.isStreaming = true;

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
        if ((participant.getDabname() != null)  &&  (participant.getDabname().equals(Prefs.getDabname()))) {
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
    public void onParticipantAudioVideoEnabled(Participant participant) {
        Timber.i("onParticipantAudioVideoEnabled");
        //enter room and the rest happens via listner

        //check who triggered it?
        //it is me: then -
        //if already in room: I must have toggled the camera icon - already handled via startstreaming
        //if not already in the room: it's me and so I must have clicked on camera for first time, so ignore it as it is already handled


        //not me: then -
        //if already in the room: do nothing, as the TW listners will do everything
        //if not already in the room: enter the room - that's it.

        if ((participant.getDabname() != null)  &&  (!participant.getDabname().equalsIgnoreCase(Prefs.getDabname()))) {
            if (va.room == null || va.room.getState() == RoomState.DISCONNECTED)

            {
                view.enterRoomTwilio(); //this will also secure new token if expired
            }

        }

    }

    @Override
    public void onParticipantAudioEnabled(Participant participant) {
        Timber.i("onParticipantAudioEnabled");


        //its me:
        // not in room: Must have clicked on microphone; handled in clickvoice(). Ignore
        //in room: also handled via clickvoice()

        //not me:
        //not in room: enter room, since some one wants a phone call
        //in room: twilio handles it, so ignore it here.

        if ((participant.getDabname() != null)  &&  (!participant.getDabname().equalsIgnoreCase(Prefs.getDabname())))
        {
            if (va.room == null || va.room.getState() == RoomState.DISCONNECTED)

            {
                view.enterRoomTwilio(); //this will also secure new token if expired
            }
        }

    }



    @Override
    public List<Participant> getLivestreamParticipants() {
        return participantList;
    }

    @Override
    public void onStart() {}

    @Override
    public void onStop() {}

    @Override
    public void onDestroy() {
        participantDatabase.removeChildEventListener();
    }
}