package com.dabkick.videosdk.livesession.livestream;


import com.dabkick.videosdk.Prefs;
import com.dabkick.videosdk.SdkApp;
import com.dabkick.videosdk.livesession.AbstractDatabaseReferences;
import com.dabkick.videosdk.retrofit.RetrofitCreator;
import com.dabkick.videosdk.retrofit.TwilioAccessToken;
import com.twilio.video.CameraCapturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.RemoteAudioTrack;
import com.twilio.video.RemoteAudioTrackPublication;
import com.twilio.video.RemoteDataTrack;
import com.twilio.video.RemoteDataTrackPublication;
import com.twilio.video.RemoteParticipant;
import com.twilio.video.RemoteVideoTrack;
import com.twilio.video.RemoteVideoTrackPublication;
import com.twilio.video.Room;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
import com.twilio.video.VideoRenderer;
import com.twilio.video.VideoView;

import java.util.Collections;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Model to handle Twilio-related components
 * We are billed at room creation and per data usage, so
 * only enter a room when a user wants to livestream or
 * someone already is in the room (check Firebase)
 */
class StreamingManager implements StreamingManagerInterface {

    private LivestreamPresenterImpl liveStreamPresenter;
    private StreamingManagerInterface callback; // TODO send events to presenter

    // a room is communication between the local participant and one or more participants
    private Room room;
    private LocalParticipant localParticipant;

    private LocalAudioTrack localAudioTrack;
    private LocalVideoTrack localVideoTrack;
    private VideoRenderer localVideoView;
    private CameraCapturerCompat cameraCapturerCompat;

    /*
     * Audio and video tracks can be created with names. This feature is useful for categorizing
     * tracks of participants. For example, if one participant publishes a video track with
     * ScreenCapturer and CameraCapturer with the names "screen" and "camera" respectively then
     * other participants can use RemoteVideoTrack#getName to determine which video track is
     * produced from the other participant's screen or camera.
    */
    private static final String LOCAL_AUDIO_TRACK_NAME = "mic";
    private static final String LOCAL_VIDEO_TRACK_NAME = "camera";

    private String accessToken = null;
    private final String ROOM_NAME_TODO_DYNAMICALLY_OBTAIN = AbstractDatabaseReferences.getSessionId();

    StreamingManager(LivestreamPresenterImpl liveStreamPresenter) {
        this.liveStreamPresenter = liveStreamPresenter;
    }

    private void createAudioVideoTracks() {
        localAudioTrack = LocalAudioTrack.create(SdkApp.getAppContext(), true, LOCAL_AUDIO_TRACK_NAME);
        cameraCapturerCompat = new CameraCapturerCompat(SdkApp.getAppContext(), getAvailableCameraSource());
        localVideoTrack = LocalVideoTrack.create(SdkApp.getAppContext(),
                true,
                cameraCapturerCompat.getVideoCapturer(),
                LOCAL_VIDEO_TRACK_NAME);

    }

    @Override
    public void startStreaming(VideoView videoView) {
        createAudioVideoTracks();
        localVideoView = videoView;
        localVideoTrack.addRenderer(localVideoView);
        if (accessToken == null) {
            initAccessToken();
        } else {
            connectToRoom(ROOM_NAME_TODO_DYNAMICALLY_OBTAIN);
        }
    }

    // do not directly disconnect from room - save $ from creating too many Twilio rooms
    @Override
    public void stopStreaming(VideoView myVideoView) {
        // free native memory resources
        localVideoTrack.removeRenderer(myVideoView);
        localAudioTrack.release();
        localAudioTrack = null;
        localVideoTrack.release();
        localVideoTrack = null;
    }

    private void initAccessToken() {
        RetrofitCreator.getAuthenticatedApiInterface()
                .getLivestreamAccessToken()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<TwilioAccessToken>() {
                    @Override public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(TwilioAccessToken twilioAccessToken) {
                        Timber.d("retrieved Twilio access token");
                        accessToken = twilioAccessToken.getAccessToken();
                        connectToRoom(ROOM_NAME_TODO_DYNAMICALLY_OBTAIN);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("unable to get Twilio access token");
                        Timber.e(e);
                    }
                });
    }

    private void connectToRoom(String roomName) {
        ConnectOptions connectOptions = new ConnectOptions.Builder(Prefs.getUserId())
                .roomName(roomName)
                .audioTracks(Collections.singletonList(localAudioTrack))
                .videoTracks(Collections.singletonList(localVideoTrack))
                .build();
        room = Video.connect(SdkApp.getAppContext(), connectOptions, roomListener());
    }

    private Room.Listener roomListener() {
        return new Room.Listener() {
            @Override
            public void onConnected(Room room) {
                Timber.d("Connected to %s", room.getName());
                localParticipant = room.getLocalParticipant();
                localParticipant.publishTrack(localAudioTrack);
                localParticipant.publishTrack(localVideoTrack);

                for (RemoteParticipant participant : room.getRemoteParticipants()) {
                    addRemoteParticipant(participant);
                    break;
                }
            }

            @Override
            public void onConnectFailure(Room room, TwilioException e) {
                // TODO pass up error to show in Activity
                Timber.e("Twilio room connection failure");
                Timber.e(e);
            }

            @Override
            public void onDisconnected(Room room, TwilioException e) {
                Timber.d("Disconnected from %s", room.getName());
                localParticipant = null;
                room = null;
            }

            @Override
            public void onParticipantConnected(Room room, RemoteParticipant participant) {
                Timber.d("participant %s has connected to %s",
                        participant.getIdentity(), room.getName());
                addRemoteParticipant(participant);
            }

            @Override
            public void onParticipantDisconnected(Room room, RemoteParticipant participant) {
                Timber.d("participant %s has disconnected from %s",
                        participant.getIdentity(), room.getName());
            }

            @Override public void onRecordingStarted(Room room) {}
            @Override public void onRecordingStopped(Room room) {}
        };


    }

    /*
     * Called when participant joins the room
     */
    private void addRemoteParticipant(RemoteParticipant remoteParticipant) {

        /*
         * Add remote participant renderer
         */
        if (remoteParticipant.getRemoteVideoTracks().size() > 0) {
            RemoteVideoTrackPublication remoteVideoTrackPublication =
                    remoteParticipant.getRemoteVideoTracks().get(0);

            /*
             * Only render video tracks that are subscribed to
             */
            if (remoteVideoTrackPublication.isTrackSubscribed()) {
                // TODO invoke VideoTrack.addRenderer(...) on correct VideoView
                addRemoteParticipantVideo(remoteVideoTrackPublication.getRemoteVideoTrack());
            }
        }

        /*
         * Start listening for participant events
         */
        remoteParticipant.setListener(remoteParticipantListener());
    }

    private void addRemoteParticipantVideo(RemoteVideoTrack remoteVideoTrack) {

    }

    private void removeParticipantVideo(RemoteVideoTrack remoteVideoTrack) {

    }

    private RemoteParticipant.Listener remoteParticipantListener() {
        return new RemoteParticipant.Listener() {
            @Override
            public void onAudioTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteAudioTrackPublication remoteAudioTrackPublication) {}

            @Override
            public void onAudioTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteAudioTrackPublication remoteAudioTrackPublication) {}

            @Override
            public void onDataTrackPublished(RemoteParticipant remoteParticipant,
                                             RemoteDataTrackPublication remoteDataTrackPublication) {}

            @Override
            public void onDataTrackUnpublished(RemoteParticipant remoteParticipant,
                                               RemoteDataTrackPublication remoteDataTrackPublication) {}

            @Override
            public void onVideoTrackPublished(RemoteParticipant remoteParticipant,
                                              RemoteVideoTrackPublication remoteVideoTrackPublication) {}

            @Override
            public void onVideoTrackUnpublished(RemoteParticipant remoteParticipant,
                                                RemoteVideoTrackPublication remoteVideoTrackPublication) {}

            @Override
            public void onAudioTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteAudioTrackPublication remoteAudioTrackPublication,
                                               RemoteAudioTrack remoteAudioTrack) {}

            @Override
            public void onAudioTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteAudioTrackPublication remoteAudioTrackPublication,
                                                 RemoteAudioTrack remoteAudioTrack) {}

            @Override
            public void onDataTrackSubscribed(RemoteParticipant remoteParticipant,
                                              RemoteDataTrackPublication remoteDataTrackPublication,
                                              RemoteDataTrack remoteDataTrack) {}

            @Override
            public void onDataTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                RemoteDataTrackPublication remoteDataTrackPublication,
                                                RemoteDataTrack remoteDataTrack) {}

            @Override
            public void onVideoTrackSubscribed(RemoteParticipant remoteParticipant,
                                               RemoteVideoTrackPublication remoteVideoTrackPublication,
                                               RemoteVideoTrack remoteVideoTrack) {
                addRemoteParticipantVideo(remoteVideoTrack);
            }

            @Override
            public void onVideoTrackUnsubscribed(RemoteParticipant remoteParticipant,
                                                 RemoteVideoTrackPublication remoteVideoTrackPublication,
                                                 RemoteVideoTrack remoteVideoTrack) {
                removeParticipantVideo(remoteVideoTrack);
            }

            @Override
            public void onAudioTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteAudioTrackPublication remoteAudioTrackPublication) {}

            @Override
            public void onAudioTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteAudioTrackPublication remoteAudioTrackPublication) {}

            @Override
            public void onVideoTrackEnabled(RemoteParticipant remoteParticipant,
                                            RemoteVideoTrackPublication remoteVideoTrackPublication) {}

            @Override
            public void onVideoTrackDisabled(RemoteParticipant remoteParticipant,
                                             RemoteVideoTrackPublication remoteVideoTrackPublication) {}
        };
    }


    public boolean isStreaming() {
        return localAudioTrack != null && localVideoTrack != null;
    }

    private CameraCapturer.CameraSource getAvailableCameraSource() {
        return (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA)) ?
                (CameraCapturer.CameraSource.FRONT_CAMERA) :
                (CameraCapturer.CameraSource.BACK_CAMERA);
    }

}