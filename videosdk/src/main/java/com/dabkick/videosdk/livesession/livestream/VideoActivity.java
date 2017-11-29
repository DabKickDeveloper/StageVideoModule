package com.dabkick.videosdk.livesession.livestream;

import android.media.AudioManager;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.twilio.video.AudioTrack;
import com.twilio.video.CameraCapturer;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalParticipant;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.Room;
import com.twilio.video.RoomState;
import com.twilio.video.TwilioException;
import com.twilio.video.VideoRenderer;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoView;

import org.greenrobot.eventbus.EventBus;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by developer on 11/27/17.
 */

 public class VideoActivity implements AudioManager.OnAudioFocusChangeListener {

    static VideoActivity instance;


    //    public static final int CAMERA_MIC_PERMISSION_REQUEST_CODE = 1;
    private static final String TAG = "VideoActivity";

    /*
     * You must provide a Twilio Access Token to connect to the Video service
     */
//    private static final String TWILIO_ACCESS_TOKEN = "TWILIO_ACCESS_TOKEN";

    /*
     * The Video Client allows a client to connect to a room
     */
//    public VideoClient videoClient;

    /*
     * A Room represents communication between the client and one or more participants.
     */
    public Room room;

    public String accessToken = null;
//    public String CURRENT_TOKEN = null;
    public String VIDEO_CLIENT_ID = null;


    public static Toast toast = null;
    public static AlertDialog alert11;
    boolean cameraGranted = true, isLiveStreamAudioGrant = false, isLiveStreamCameraGrant = true, isPermissionForLiveStreaming = false;


    //gopal
    public int previousAudioMode;
    public boolean previousMicrophoneMute;
    private final boolean mUsePlatformAEC = true;
    public AudioManager mAudioManager;


    //audio recording feature
    public static final int RECORD_AUDIO_PERMISSION = 1;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 2;
    public static final int READ_EXTERNAL_STORAGE_PERMISSION = 3;
    public static final int WRITE_SETTING = 4;
    public static final int REQUEST_MICROPHONE_CAMERA_FOR_LIVESTREAM = 5;
    public static final int REQUEST_MICROPHONE_CAMERA_FOR_TWILIO = 6;




    /*
     * A VideoView receives frames from a local or remote video track and renders them
     * to an associated view.
     */

    //    public VideoView[] primaryVideoView;
    public Map<String, VideoView> PrimaryVideoView = new LinkedHashMap<String, VideoView>();
    public Map<String, VideoView> FullscreenVideoView = new LinkedHashMap<String, VideoView>();

    /*
     * Android application UI elements
     */
    public CameraCapturer cameraCapturer = null;
    public LocalAudioTrack localAudioTrack = null;
    public LocalVideoTrack localVideoTrack = null;
    public LocalParticipant localParticipant;
    public Map<String, com.twilio.video.Participant> participantIdentity = new ConcurrentHashMap<>();
    public Map<String, VideoTrack> videoTrackList = new LinkedHashMap<String, VideoTrack>();
    public Map<String, AudioTrack> audioTrackList = new LinkedHashMap<>();
    public VideoRenderer localVideoView;
    public boolean isFullScreen = false;
    public boolean streamMuted = false;
    public boolean isStreaming = false;
    public boolean meStreaming = false;
    public boolean meFullScreen = false;
    public boolean frFullScreen = false;


    public static synchronized VideoActivity getInstance() {
        if (instance == null)
            instance = new VideoActivity();
        return instance;
    }

    public void clear() {

        new Thread(new Runnable() {
            @Override
            public void run() {


                //if local video streaming, stop sharing
                removeLocalVideoTrackOnPause();

                //stop local video, audio
                if (cameraCapturer != null) {
                    cameraCapturer.stopCapture();
                    cameraCapturer = null;
                }

                //gopal
                setAudioFocus(false);

                //replace all streaming videos of others w their respective avatar pic
                //restore UI w/o video streaming here

                if (participantIdentity != null && !participantIdentity.isEmpty()) {
                    Iterator<Map.Entry<String, com.twilio.video.Participant>> entry = participantIdentity.entrySet().iterator();
                    while (entry.hasNext()) {
                        Map.Entry pairs = (Map.Entry) entry.next();
                        com.twilio.video.Participant participant = (com.twilio.video.Participant) pairs.getValue();
                        removeParticipant(participant);
                    }
                    participantIdentity.clear();
                }


                if (PrimaryVideoView != null && !PrimaryVideoView.isEmpty()) {

                    PrimaryVideoView.clear();
                }

                if (videoTrackList != null && !videoTrackList.isEmpty()) {
                    videoTrackList.clear();
                }

                if (participantIdentity != null && !participantIdentity.isEmpty()) {
                    participantIdentity.clear();
                }

                if (FullscreenVideoView != null && !FullscreenVideoView.isEmpty()) {
                    FullscreenVideoView.clear();
                }

                //disconnect room
                disconnectRoomonDestroy();


                isFullScreen = false;
                streamMuted = false;
                isStreaming = false;
                meStreaming = false;
                meFullScreen = false;
                frFullScreen = false;

                instance = null;
            }
        });
    }


    public void reset() {
        instance = null;
    }

    public boolean isMuted() {
        return streamMuted;
    }


    public void removeLocalTrack() {
        removeLocalVideoTrackOnPause();
        isFullScreen = false;
        meStreaming = false;
    }


    //gopal
//    public void addLocalVideoTrackOnResumeFromBG()
//
//    {
//        if (LiveSessionManager.getInstance().isLiveStreamingStarted()) {
//
//            if (cameraCapturer == null)
//                return; //in case called without start of livestreaming! such as when reconnecting
//
//        /*
//         * If the local video track was released when the app was put in the background, recreate.
//         */
//            if (localVideoTrack == null && LivesessionActivity.getLiveChatActivity().checkPermissionForCameraAndMicrophone()) {
//                localVideoTrack = LocalVideoTrack.create(BaseActivity.mCurrentActivity, true, cameraCapturer);
//                if (meStreaming) localVideoTrack.addRenderer(localVideoView);
//
//            /*
//             * If connected to a Room then share the local video track.
//             */
//                if (localParticipant != null) {
//                    localParticipant.addVideoTrack(localVideoTrack);
//                    videoTrackList.put(PreferenceHandler.getXMPPUserName(BaseActivity.mCurrentActivity), localVideoTrack);
//                }
//
//
//            }
//        }
//
//    }


    public void removeLocalVideoTrackOnPause() {

        /*
         * Release the local video track before going in the background. This ensures that the
         * camera can be used by other applications while this app is in the background.
         */
        if (localVideoTrack != null) {
            /*
             * If this local video track is being shared in a Room, remove from local
             * participant before releasing the video track. Participants will be notified that
             * the track has been removed.
             */
            if (localParticipant != null) {
                localParticipant.removeVideoTrack(localVideoTrack);
            }

            localVideoTrack.release();
            localVideoTrack = null;
        }

        if (localAudioTrack != null) {
            /*
             * If this local video track is being shared in a Room, remove from local
             * participant before releasing the video track. Participants will be notified that
             * the track has been removed.
             */
            if (localParticipant != null) {
                localParticipant.removeAudioTrack(localAudioTrack);
            }

            localAudioTrack.release();
            localAudioTrack = null;
        }

//gopal
//        if (videoTrackList.containsKey(PreferenceHandler.getXMPPUserName(BaseActivity.mCurrentActivity)))
//            videoTrackList.remove(PreferenceHandler.getXMPPUserName(BaseActivity.mCurrentActivity));
//
//        if (PrimaryVideoView.containsKey(PreferenceHandler.getXMPPUserName(BaseActivity.mCurrentActivity)))
//            PrimaryVideoView.remove(PreferenceHandler.getXMPPUserName(BaseActivity.mCurrentActivity));


    }

    public void disconnectRoomonDestroy() {

        /*
         * Always disconnect from the room before leaving the Activity to
         * ensure any memory allocated to the Room resource is freed.
         */
        if (room != null && room.getState() != RoomState.DISCONNECTED) {
            room.disconnect();
//            disconnectedFromOnDestroy = true;
        }

        isStreaming = false;
        meStreaming = false;

        /*
         * Release the local audio and video tracks ensuring any memory allocated to audio
         * or video is freed.
         */
        if (localAudioTrack != null) {
            localAudioTrack.release();
            localAudioTrack = null;
        }
        if (localVideoTrack != null) {
            localVideoTrack.release();
            localVideoTrack = null;
        }

    }


    /*
     * Called when participant joins the room
     */
    public void addParticipant(com.twilio.video.Participant participant) {
        /*
         * This app only displays video for one additional participant per Room
         */
        String jid = participant.getIdentity();


        participantIdentity.put(jid, participant);

//        PrimaryVideoView.put(jid, vv);

        /*
         * Add participant renderer
         */
        if (participant.getVideoTracks().size() > 0) {
            //gopal
            addParticipantVideo(participant.getVideoTracks().get(0), jid, false);
        }

        /*
         * Start listening for participant events
         */
        participant.setListener(participantListener());


//        participant.getMedia().setListener(mediaListener());
    }

    /*
     * Set primary view as renderer for participant video track
     */
    public void addParticipantVideo(VideoTrack videoTrack, String jid, boolean statusChanged) {

        if ((videoTrack == null) || !videoTrack.isEnabled())
            return;

//        if (videoTrack != null)

        videoTrackList.put(jid, videoTrack);
        EventBus.getDefault().post(new NotifyLivestreamAdapterEvent());


//        if (BaseActivity.mCurrentActivity.getClass() == LiveChat.class)
//            LivesessionActivity.getLiveChatActivity().startStreamingTwilio(jid, statusChanged);


        //show incoming video




    }

    /*
     * Called when participant leaves the room
     */
    public void removeParticipant(com.twilio.video.Participant participant) {
//        if (!participant.getIdentity().equals(participantIdentity)) {
//            return;
//        }


        //gopal
//        if (!LiveSessionManager.getInstance().isLiveSessionStarted())
//            return;

        String jid = participant.getIdentity();

        /*
         * Remove participant renderer
         */
        if (participant.getVideoTracks().size() > 0) {
            //gopal
            removeParticipantVideo(participant.getVideoTracks().get(0), jid);
        }

        //commenting the following line as this is not allowed in new twilio sdk
        //participant.setListener(null);


        if (PrimaryVideoView.containsKey(jid))
            PrimaryVideoView.remove(jid);

        if (participantIdentity.containsKey(jid))
            participantIdentity.remove(jid);

    }

    public void removeUserTwilio(String jid) {


    }


    //gopal
    public void removeParticipantVideo(VideoTrack videoTrack, String jid) {

//        if (!LiveSessionManager.getInstance().isLiveSessionStarted())
//            return;
//
//        if (BaseActivity.mCurrentActivity.getClass() == LiveChat.class) {
//            if (LivesessionActivity.getLiveChatActivity().profilePicRecyclerViewAdapter.rendererMap.containsKey(jid) && videoTrackList.get(jid) != null){
//                videoTrackList.get(jid).removeRenderer(LivesessionActivity.getLiveChatActivity().profilePicRecyclerViewAdapter.rendererMap.get(jid));
//                LivesessionActivity.getLiveChatActivity().profilePicRecyclerViewAdapter.rendererMap.remove(jid);
//            }
//
//        }







//        if (BaseActivity.mCurrentActivity.getClass() == LiveChat.class)
//            LivesessionActivity.getLiveChatActivity().stopStreamingTwilio(jid);

        if (videoTrackList.containsKey(jid))
            videoTrackList.remove(jid);

        //stop streaming
        EventBus.getDefault().post(new NotifyLivestreamAdapterEvent());


        if (videoTrackList.isEmpty())
            isStreaming = false;

        if (PrimaryVideoView.containsKey(jid))
            PrimaryVideoView.remove(jid);

        if (participantIdentity.containsKey(jid))
            participantIdentity.remove(jid);


    }


    /*
     * Room events listener
     */
    public Room.Listener roomListener() {
        return new Room.Listener() {


            public void onConnected(Room room) {

                VideoActivity.this.room = room;

                localParticipant = room.getLocalParticipant();
//                videoStatusTextView.setText("Connected to " + room.getName());
//                setTitle(room.getName());

                for (com.twilio.video.Participant participant : room.getParticipants()) {
                    addParticipant(participant);
                    participantIdentity.put(participant.getIdentity(), participant);
//                    break;
                }
            }

            @Override
            public void onConnectFailure(Room room, TwilioException e) {
//                videoStatusTextView.setText("Failed to connect");
                Log.d("gopal", "room connect failure for Twilio");

                setAudioFocus(false);

                //gopal
//                if (BaseActivity.mCurrentActivity.getClass() == LiveChat.class)
//                    LivesessionActivity.getLiveChatActivity().setAudioFocus(false);
            }

            @Override
            public void onDisconnected(Room room, TwilioException e) {
//                videoStatusTextView.setText("Disconnected from " + room.getName());
                Log.d("Disconnected from ", room.getName());
                localParticipant = null;


                //gopal
//                if (BaseActivity.mCurrentActivity.getClass() == LiveChat.class)
                    setAudioFocus(false);

                //restore UI w/o video streaming here

                if (participantIdentity != null && !participantIdentity.isEmpty()) {
                    Iterator<Map.Entry<String, com.twilio.video.Participant>> entry = participantIdentity.entrySet().iterator();
                    while (entry.hasNext()) {
                        Map.Entry pairs = (Map.Entry) entry.next();
                        com.twilio.video.Participant participant = (com.twilio.video.Participant) pairs.getValue();
                        removeParticipant(participant);
                    }
                    participantIdentity.clear();
                }

                VideoActivity.this.room = null;
                isStreaming = false;
                meStreaming = false;

                if (participantIdentity != null && !participantIdentity.isEmpty())
                    participantIdentity.clear();

                if (videoTrackList != null && !videoTrackList.isEmpty())
                    videoTrackList.clear();

                if (PrimaryVideoView != null && !PrimaryVideoView.isEmpty())
                    PrimaryVideoView.clear();

                Log.d("RoomState", " " + room.getState().toString());


            }

            @Override
            public void onParticipantConnected(Room room, com.twilio.video.Participant participant) {

                //gopal
//                if (!LiveSessionManager.getInstance().isLiveSessionStarted())
//                    return;
                addParticipant(participant);

//                //add participant to local structure
                if (participantIdentity.containsKey(participant.getIdentity())) {
                    participantIdentity.remove(participant.getIdentity());
                    participantIdentity.put(participant.getIdentity(), participant);
                } else
                    participantIdentity.put(participant.getIdentity(), participant);

            }

            @Override
            public void onParticipantDisconnected(Room room, com.twilio.video.Participant participant) {
                removeParticipant(participant);
            }

            @Override
            public void onRecordingStarted(Room room) {
                /*
                 * Indicates when media shared to a Room is being recorded. Note that
                 * recording is only available in our Group Rooms developer preview.
                 */
                Log.d(TAG, "onRecordingStarted");
            }

            @Override
            public void onRecordingStopped(Room room) {
                /*
                 * Indicates when media shared to a Room is no longer being recorded. Note that
                 * recording is only available in our Group Rooms developer preview.
                 */
                Log.d(TAG, "onRecordingStopped");
            }
        };
    }

    private com.twilio.video.Participant.Listener participantListener() {
        return new com.twilio.video.Participant.Listener() {

            @Override
            public void onAudioTrackAdded(com.twilio.video.Participant participant, AudioTrack audioTrack) {
//

                //gopal
//                if (!LiveSessionManager.getInstance().isLiveSessionStarted())
//                    return;

                Log.e("KESHAVA ", "is Audio NOT Muted? "+audioTrack.isEnabled());

                String jid = participant.getIdentity();

                // get audio track from list and enable it

                //gopal
//                if (jid != null)
//                    addParticipantVideo(videoTrack, jid, true);



                //gopal
//                if (BaseActivity.mCurrentActivity.getClass() == LiveChat.class) {
                    setAudioFocus(true);
//                    ((LiveChat) BaseActivity.mCurrentActivity).mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, AudioManager.FLAG_PLAY_SOUND);
//                    ((LiveChat) BaseActivity.mCurrentActivity).mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
//                            ((LiveChat) BaseActivity.mCurrentActivity).mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FLAG_PLAY_SOUND);
//                }
            }

            @Override
            public void onAudioTrackRemoved(com.twilio.video.Participant participant, AudioTrack audioTrack) {
//

                //gopal
//                if (!LiveSessionManager.getInstance().isLiveSessionStarted())
//                    return;

                String jid = participant.getIdentity();

                //gopal
                //get audio track from list and disable it


                //gopal
//                if (BaseActivity.mCurrentActivity.getClass() == LiveChat.class) {
                    setAudioFocus(false);
//                    ((LiveChat) BaseActivity.mCurrentActivity).setVolumeControlStream(AudioManager.STREAM_MUSIC);
//                    ((LiveChat) BaseActivity.mCurrentActivity).mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
//                            ((LiveChat) BaseActivity.mCurrentActivity).mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND);
//                }

            }

            @Override
            public void onVideoTrackAdded(com.twilio.video.Participant participant, VideoTrack videoTrack) {


                //gopal
//                if (!LiveSessionManager.getInstance().isLiveSessionStarted())
//                    return;

                String jid = participant.getIdentity();

                // get video track from list and enable it

                //gopal
                if (jid != null)
                    addParticipantVideo(videoTrack, jid, true);

            }

            @Override
            public void onVideoTrackRemoved(com.twilio.video.Participant participant, VideoTrack videoTrack) {


                //gopal
//                if (!LiveSessionManager.getInstance().isLiveSessionStarted())
//                    return;

                String jid = participant.getIdentity();

                // get video track from list and disable it


                //gopal
                if (jid != null)
                    removeParticipantVideo(videoTrack, jid);
            }

            @Override
            public void onAudioTrackEnabled(com.twilio.video.Participant participant, AudioTrack audioTrack) {

                //gopal
//                if (!LiveSessionManager.getInstance().isLiveSessionStarted())
//                    return;

                Log.e("KESHAVA ", "is Audio Muted "+audioTrack.isEnabled());

                String jid = participant.getIdentity();

                setAudioFocus(true);

                //gopal

//                if (jid != null) {


//                    if (BaseActivity.mCurrentActivity.getClass() == LiveChat.class) {
//                        ((LiveChat) BaseActivity.mCurrentActivity).mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, AudioManager.FLAG_PLAY_SOUND);
//                        ((LiveChat) BaseActivity.mCurrentActivity).mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
//                                ((LiveChat) BaseActivity.mCurrentActivity).mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FLAG_PLAY_SOUND);
//                        ((LiveChat) BaseActivity.mCurrentActivity).setAudioFocus(true);
//                        ((LiveChat) BaseActivity.mCurrentActivity).showGenericStatus("NotMuted", jid);
//                    }
//                }

            }

            @Override
            public void onAudioTrackDisabled(com.twilio.video.Participant participant, AudioTrack audioTrack) {


                //gopal
//                if (!LiveSessionManager.getInstance().isLiveSessionStarted())
//                    return;

                Log.e("KESHAVA ", "is Audio Muted "+audioTrack.isEnabled());

                String jid = participant.getIdentity();
                setAudioFocus(false);


                //gopal
//                if (jid != null)
//                    if (BaseActivity.mCurrentActivity.getClass() == LiveChat.class) {
//                        ((LiveChat) BaseActivity.mCurrentActivity).setAudioFocus(false);
//                        ((LiveChat) BaseActivity.mCurrentActivity).setVolumeControlStream(AudioManager.STREAM_MUSIC);
//                        ((LiveChat) BaseActivity.mCurrentActivity).mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
//                                ((LiveChat) BaseActivity.mCurrentActivity).mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_PLAY_SOUND);
//                        ((LiveChat) BaseActivity.mCurrentActivity).showGenericStatus("Muted", jid);
//                    }
            }

            @Override
            public void onVideoTrackEnabled(com.twilio.video.Participant participant, VideoTrack videoTrack) {


                //gopal
//                if (!LiveSessionManager.getInstance().isLiveSessionStarted())
//                    return;

                String jid = participant.getIdentity();

                //gopal
                if (jid != null)
                {
                    addParticipantVideo(videoTrack, jid, true);

                }

            }

            @Override
            public void onVideoTrackDisabled(com.twilio.video.Participant participant, VideoTrack videoTrack) {


                //gopal
//                if (!LiveSessionManager.getInstance().isLiveSessionStarted())
//                    return;

                String jid = participant.getIdentity();
                //gopal
                if (jid != null)
                    removeParticipantVideo(videoTrack, jid);


            }

        };
    }


    public void startLocalTrackTwilio() {
//                /*
//                 * Enable/disable the local video track
//                 */
        if (localVideoTrack != null) {
            boolean enable = !localVideoTrack.isEnabled();
            localVideoTrack.enable(enable);

        }
    }

    public void stopLocalTrackTwilio() {
                /*
                 * Enable/disable the local video track
                 */
        if (localVideoTrack != null) {
            boolean enable = !localVideoTrack.isEnabled();
            localVideoTrack.enable(enable);
        }
    }

    //For Live Stream Access Token Validity Check:
    public static boolean isTokenExpired = true;
    private static int hoursToGo = 0;
    private static int minutesToGo = 30;
    private static int secondsToGo = 0;
    private static int millisToGo = secondsToGo*1000+minutesToGo*1000*60+hoursToGo*1000*60*60;

    public static void twilioAccessTokenCheckTimer(){

        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                new CountDownTimer(millisToGo, 1000) {

                    @Override
                    public void onTick(long millis) {

                        isTokenExpired = false;
                    }

                    @Override
                    public void onFinish() {

                        isTokenExpired = true;
                    }
                }.start();
            }
        });

    }

    static public void runOnUIThread(Runnable runnable) {
        new Handler(Looper.getMainLooper()).post(runnable);
    }


    public static void dismissToast() {

        if (toast != null) {
            toast.cancel();
            return;
        }

    }

    @Override
    public void onAudioFocusChange(int i) {
        switch (i) {
            case AudioManager.AUDIOFOCUS_GAIN:
                mAudioManager.setMode(AudioManager.STREAM_MUSIC);
                break;

            case AudioManager.AUDIOFOCUS_GAIN_TRANSIENT:
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 1, 0);
                break;
        }
    }


    //gopal
    public void setAudioFocus(boolean focus) {

        if (focus) {
            previousAudioMode = mAudioManager.getMode();
            // Request audio focus before making any device switch.

            //Checking if Live streaming is in progress,
            //if live streaming is nit started then
            //the volume will be speaker volume for music and video
            int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, AudioManager.FLAG_PLAY_SOUND);
            }
            /*
             * Use MODE_IN_COMMUNICATION as the default audio mode. It is required
             * to be in this mode when playout and/or recording starts for the best
             * possible VoIP performance. Some devices have difficulties with
             * speaker mode if this is not set.
             */

            //Adding this mode as MODE_IN_COMMUNICATION is causing problems and
            //in few devices volume is getting set to call volume instead of speaker
            //volume for music and video.
            //mAudioManager.setMode(MediaRecorder.AudioSource.VOICE_COMMUNICATION);

            /*
             * Always disable microphone mute during a WebRTC call.
             */
            previousMicrophoneMute = mAudioManager.isMicrophoneMute();


            //gopal
//            mAudioManager.setMode(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);

//            mAudioManager.setMicrophoneMute(previousMicrophoneMute);
//            if (mAudioManager.isWiredHeadsetOn()) {
//                mAudioManager.setSpeakerphoneOn(false);
//            } else {
//                mAudioManager.setSpeakerphoneOn(true);
//                mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FLAG_PLAY_SOUND);
//            }

            mAudioManager.setMicrophoneMute(false);


            //vallabh commented
            //this is causing a major bug that when media is played it is playing in call speaker volume and later user is unable to increase it aswell!
//            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
        } else {
            if (!mAudioManager.isWiredHeadsetOn())
                mAudioManager.setSpeakerphoneOn(true);
            try {
                if (previousAudioMode != 0)
                    previousAudioMode = 0;
            } catch (Exception e) {
                //Catching any security exception obtained from setting the mode
            }
            mAudioManager.setMode(previousAudioMode);
            mAudioManager.abandonAudioFocus(null);
            mAudioManager.setMicrophoneMute(previousMicrophoneMute);
        }
    }


}
