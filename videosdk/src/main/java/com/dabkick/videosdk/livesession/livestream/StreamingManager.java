package com.dabkick.videosdk.livesession.livestream;


import com.dabkick.videosdk.SdkApp;
import com.dabkick.videosdk.retrofit.RetrofitCreator;
import com.dabkick.videosdk.retrofit.TwilioAccessToken;
import com.twilio.video.CameraCapturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.Participant;
import com.twilio.video.Room;
import com.twilio.video.TwilioException;
import com.twilio.video.Video;
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
    private Room room;
    private LocalAudioTrack localAudioTrack;
    private LocalVideoTrack localVideoTrack;
    private String accessToken = null;
    private final String ROOM_NAME_TODO_DYNAMICALLY_OBTAIN = "DabKick Lobby"; // FIXME

    StreamingManager(LivestreamPresenterImpl liveStreamPresenter) {
        this.liveStreamPresenter = liveStreamPresenter;
    }

    private void setupAudioVideoTracks() {
        localAudioTrack = LocalAudioTrack.create(SdkApp.getAppContext(), true);
        CameraCapturer cameraCapturer = new CameraCapturer(SdkApp.getAppContext(),
                CameraCapturer.CameraSource.FRONT_CAMERA);
        localVideoTrack = LocalVideoTrack.create(SdkApp.getAppContext(),
                true, cameraCapturer);
    }

    public void startStreaming(VideoView videoView) {
        setupAudioVideoTracks();
        localVideoTrack.addRenderer(videoView);
        if (accessToken == null) {
            initAccessToken();
        } else {
            connectToRoom(ROOM_NAME_TODO_DYNAMICALLY_OBTAIN);
        }
    }

    // do not directly disconnect from room - save $ from creating too many Twilio rooms
    public void stopStreaming() {
        // free native memory resources
        localAudioTrack.release();
        localAudioTrack = null;
        localVideoTrack.release();
        localVideoTrack = null;
    }

    private void initAccessToken() {
        RetrofitCreator.getUnauthenticatedApiInterface()
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
        // re-init if streaming was previously stopped
        if (localAudioTrack == null || localVideoTrack == null) {
            setupAudioVideoTracks();
        }

        ConnectOptions connectOptions = new ConnectOptions.Builder(accessToken)
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
            }

            @Override
            public void onParticipantConnected(Room room, Participant participant) {
                Timber.d("participant %s has connected to %s", participant.getIdentity(), room.getName());
            }
            @Override public void onParticipantDisconnected(Room room, Participant participant) {
                Timber.d("participant %s has disconnected from %s", participant.getIdentity(), room.getName());
            }
            @Override public void onRecordingStarted(Room room) {}
            @Override public void onRecordingStopped(Room room) {}
        };


    }

    public boolean isStreaming() {
        return localAudioTrack != null && localVideoTrack != null;
    }

}