package com.dabkick.videosdk.livesession;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.InputFilter;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dabkick.videosdk.Prefs;
import com.dabkick.videosdk.R;
import com.dabkick.videosdk.SdkApp;
import com.dabkick.videosdk.Util;
import com.dabkick.videosdk.livesession.chat.ChatAdapter;
import com.dabkick.videosdk.livesession.chat.ChatModel;
import com.dabkick.videosdk.livesession.chat.ChatPresenter;
import com.dabkick.videosdk.livesession.chat.ChatView;
import com.dabkick.videosdk.livesession.chat.ClearFocusBackPressedEditText;
import com.dabkick.videosdk.livesession.emoji.AnimationUtils;
import com.dabkick.videosdk.livesession.emoji.Constants;
import com.dabkick.videosdk.livesession.emoji.EmojiLayout;
import com.dabkick.videosdk.livesession.emoji.EmojiPresenter;
import com.dabkick.videosdk.livesession.emoji.EmojiView;
import com.dabkick.videosdk.livesession.livestream.LivestreamPresenter;
import com.dabkick.videosdk.livesession.livestream.LivestreamPresenterImpl;
import com.dabkick.videosdk.livesession.livestream.LivestreamView;
import com.dabkick.videosdk.livesession.livestream.NotifyLivestreamAdapterEvent;
import com.dabkick.videosdk.livesession.livestream.SessionParticipantsAdapter;
import com.dabkick.videosdk.livesession.livestream.VideoActivity;
import com.dabkick.videosdk.livesession.mediadrawer.MediaDrawerDialogFragment;
import com.dabkick.videosdk.livesession.overviews.OverviewDatabase;
import com.dabkick.videosdk.livesession.overviews.OverviewView;
import com.dabkick.videosdk.livesession.stage.StagePresenter;
import com.dabkick.videosdk.livesession.stage.StagePresenterImpl;
import com.dabkick.videosdk.livesession.stage.StageRecyclerViewAdapter;
import com.dabkick.videosdk.livesession.usersetup.GetUserDetailsFragment;
import com.dabkick.videosdk.retrofit.RetrofitCreator;
import com.dabkick.videosdk.retrofit.TwilioAccessToken;
import com.twilio.video.CameraCapturer;
import com.twilio.video.ConnectOptions;
import com.twilio.video.LocalAudioTrack;
import com.twilio.video.LocalVideoTrack;
import com.twilio.video.RoomState;
import com.twilio.video.Video;
import com.twilio.video.VideoConstraints;
import com.twilio.video.VideoDimensions;
import com.twilio.video.VideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class LiveSessionActivity extends AppCompatActivity implements
        ChatView, LivestreamView, OverviewView {

    public static Toast toast = null;
    public static AlertDialog alert11;
    boolean cameraGranted = true, isLiveStreamAudioGrant = false, isLiveStreamCameraGrant = true, isPermissionForLiveStreaming = false;


    // Chat MVP
    private ChatAdapter chatAdapter;
    private ChatPresenter chatPresenter;

    // Session Participant MVP
    private SessionParticipantsAdapter sessionParticipantsAdapter;
    private LivestreamPresenter livestreamPresenter;
    private StagePresenter stagePresenter;

    // constants
    private final int PERMISSION_REQUEST_CODE = 3928;
    private final int DEFAULT_CHAT_MSG_LENGTH_LIMIT = 256;

    // Views
    private ImageView chatToggleButton;
    private ListView chatListView;
    private ClearFocusBackPressedEditText chatEditText;

    // Stage
    private StageRecyclerViewAdapter stageRecyclerViewAdapter;
    private RecyclerView stageRecyclerView;

    //EmojiModel Layout
    private View emojiLayout;
    RelativeLayout innerContainer;
    ConstraintLayout container;
    EmojiLayout emojis;

    //gopal
    VideoActivity va = VideoActivity.getInstance();

    // Overview
    @Inject OverviewDatabase overviewDatabase;

    ImageView downKarat;


    //audio recording feature
    public static final int RECORD_AUDIO_PERMISSION = 1;
    public static final int WRITE_EXTERNAL_STORAGE_PERMISSION = 2;
    public static final int READ_EXTERNAL_STORAGE_PERMISSION = 3;
    public static final int WRITE_SETTING = 4;
    public static final int REQUEST_MICROPHONE_CAMERA_FOR_LIVESTREAM = 5;
    public static final int REQUEST_MICROPHONE_CAMERA_FOR_TWILIO = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_session);

        if (savedInstanceState == null) {
            Util.register();
        }

        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);


        //init twilio
        //get set w client ID and permissions etc
        //when user clicks on icon, enter room and livestream immediately

        va.mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        initTwilio();


        // Emojis
        innerContainer = findViewById(R.id.container_layout);
        container = findViewById(R.id.container);

        emojiLayout = findViewById(R.id.layout_emoji);
        emojis = findViewById(R.id.emojis);

        emojis.setInnerContainer(innerContainer);
        emojis.setContainer(container);


        EmojiView emojiView = emojiType -> {
            Drawable drawable;
            switch (emojiType) {
                case Constants.SMILE:
                    drawable = getResources().getDrawable(R.drawable.reactions_default);
                    break;
                case Constants.COOL:
                    drawable = getResources().getDrawable(R.drawable.cool);
                    break;
                case Constants.WINK:
                    drawable = getResources().getDrawable(R.drawable.winky);
                    break;
                case Constants.LOVE:
                    drawable = getResources().getDrawable(R.drawable.love);
                    break;
                case Constants.TONGUE:
                    drawable = getResources().getDrawable(R.drawable.tongue);
                    break;
                case Constants.ROFL:
                    drawable = getResources().getDrawable(R.drawable.rofl);
                    break;
                case Constants.CRY:
                    drawable = getResources().getDrawable(R.drawable.crying);
                    break;
                case Constants.ANGRY:
                    drawable = getResources().getDrawable(R.drawable.angry);
                    break;
                case Constants.XEYES:
                    drawable = getResources().getDrawable(R.drawable.x_eyes);
                    break;
                default: // shocked
                    drawable = getResources().getDrawable(R.drawable.shocked);
                    break;
            }
            AnimationUtils.slideToAbove(drawable, innerContainer,
                    container,LiveSessionActivity.this);
        };
        EmojiPresenter emojiPresenter = new EmojiPresenter(emojiView);
        emojis.setListener(emojiPresenter);

        chatListView = findViewById(R.id.listview_livesession_chat);
        downKarat = findViewById(R.id.close_chat_list);
        chatAdapter = new ChatAdapter(this, new ArrayList<>());
        chatListView.setAdapter(chatAdapter);

        chatPresenter = new ChatPresenter(this);

        chatToggleButton = findViewById(R.id.chat_toggle);
        chatToggleButton.setOnClickListener(v -> toggleChatUi());

        chatEditText = findViewById(R.id.message_edit_text);
        chatEditText.setOnEditorActionListener(getChatEditorActionListener());
        chatEditText.setOnFocusChangeListener(getChatFocusListener());
        chatEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(DEFAULT_CHAT_MSG_LENGTH_LIMIT)});

        // back button
        ImageView backBtn = findViewById(R.id.iv_leave_session_btn);
        backBtn.setOnClickListener(view -> finish());

        // setup livestream
        RecyclerView livestreamRecyclerView = findViewById(R.id.recyclerview_livestream);
        RecyclerView.LayoutManager livestreamLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        livestreamRecyclerView.setLayoutManager(livestreamLayoutManager);
        livestreamPresenter = new LivestreamPresenterImpl(this);
        sessionParticipantsAdapter = new SessionParticipantsAdapter(this, this,
                livestreamPresenter.getLivestreamParticipants());
        livestreamRecyclerView.setAdapter(sessionParticipantsAdapter);


        // setup stage
        stageRecyclerView = findViewById(R.id.recyclerview_stage);
        RecyclerView.LayoutManager stageLayoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        stageRecyclerView.setLayoutManager(stageLayoutManager);

        SnapHelper stageSnapHelper = new PagerSnapHelper();
        stageSnapHelper.attachToRecyclerView(stageRecyclerView);

        stageRecyclerViewAdapter = new StageRecyclerViewAdapter(this);
        stagePresenter = new StagePresenterImpl(stageRecyclerViewAdapter, this);
        stageRecyclerViewAdapter.setVideoControlListener(stagePresenter.getVideoControlsListener());
        stageRecyclerViewAdapter.setItems(stagePresenter.getStageItems());

        stageRecyclerView.setAdapter(stageRecyclerViewAdapter);

        stageRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    View centerView = stageSnapHelper.findSnapView(stageLayoutManager);
                    int position = stageLayoutManager.getPosition(centerView);
                    stagePresenter.onUserSwipedStage(position);
                }
            }
        });

    }


    private TextView.OnEditorActionListener getChatEditorActionListener() {
        return (v, actionId, event) -> {
            boolean handled = false;
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                // do not handle empty strings
                if (chatEditText.getText().toString().length() == 0) {
                    return true;
                }
                handled = true;
                clickSendButton(chatEditText.getText().toString());
                chatEditText.setText("");
            }

            return handled;
        };
    }

    private View.OnFocusChangeListener getChatFocusListener() {
        return (v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                }

                chatEditText.setWidth(0);
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        1.0f
                );
                param.setMargins((int)AnimationUtils.convertDpToPixel(LiveSessionActivity.this,50),7,(int)AnimationUtils.convertDpToPixel(LiveSessionActivity.this,10),7);
                chatEditText.setLayoutParams(param);
                emojiLayout.setVisibility(View.GONE);
                chatToggleButton.setVisibility(View.GONE);
                toggleChatUi();
            } else {

                int px = Math.round(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 180,getResources().getDisplayMetrics()));
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        px,LinearLayout.LayoutParams.MATCH_PARENT);
                param.setMargins(4,4,4,4);
                chatEditText.setLayoutParams(param);
                emojiLayout.setVisibility(View.VISIBLE);
                chatToggleButton.setVisibility(View.VISIBLE);
                toggleChatUi();
            }
        };
    }

    public void showContentDialog(View view) {

        FrameLayout frameLayout = findViewById(R.id.frag_media_drawer);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        if(frameLayout.getVisibility() == View.GONE) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frag_media_drawer);
            if(fragment == null) {
                DialogFragment mediaDrawerDialogFragment = MediaDrawerDialogFragment.newInstance();
                ft.add(R.id.frag_media_drawer, mediaDrawerDialogFragment);
                ft.commit();
            }
            AnimationUtils.leftToRight(frameLayout, LiveSessionActivity.this, findViewById(R.id.iv_media_drawer));
        }else{
            AnimationUtils.rightToLeft(frameLayout, LiveSessionActivity.this,findViewById(R.id.iv_media_drawer));
        }
    }

    // toggle visibility of chat UI and swap button drawable
    private void toggleChatUi() {
        // toggle chat list and icon
        int visibility = chatListView.getVisibility();
        if (visibility == View.INVISIBLE) {

            if(chatEditText.isFocused()){

                LinearLayout.LayoutParams param = (LinearLayout.LayoutParams)chatEditText.getLayoutParams();
                param.setMargins((int)AnimationUtils.convertDpToPixel(LiveSessionActivity.this,50),7,(int)AnimationUtils.convertDpToPixel(LiveSessionActivity.this,10),7);
                chatEditText.setLayoutParams(param);
            }

            chatListView.setVisibility(View.VISIBLE);
            downKarat.setVisibility(View.VISIBLE);
            Drawable drawable = ContextCompat.getDrawable(
                    this, R.drawable.ic_show_chat);
            chatToggleButton.setImageDrawable(drawable);
            chatToggleButton.setVisibility(View.GONE);

        } else {

            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams)chatEditText.getLayoutParams();
            param.setMargins(4,7,4,7);
            chatEditText.setLayoutParams(param);

            chatListView.setVisibility(View.INVISIBLE);
            downKarat.setVisibility(View.GONE);
            Drawable drawable = ContextCompat.getDrawable(
                    this, R.drawable.ic_hide_chat);
            chatToggleButton.setImageDrawable(drawable);
            chatToggleButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void clickSendButton(String message) {
        chatPresenter.sendMessage(message);
    }

    @Override
    public void addChatMessage(ChatModel chatModel) {
        chatAdapter.add(chatModel);
    }

    @Override
    public void myStreamClicked() {
        if (checkPermissionForCameraAndMicrophone()) {
            livestreamPresenter.toggleMyStream();
        } else {
            requestPermissionForCameraAndMicrophone();
        }
    }

    private boolean checkPermissionForCameraAndMicrophone(){
        int resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return resultCamera == PackageManager.PERMISSION_GRANTED &&
                resultMic == PackageManager.PERMISSION_GRANTED;
    }

//    private void requestPermissionForCameraAndMicrophone(){
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
//                ActivityCompat.shouldShowRequestPermissionRationale(this,
//                        Manifest.permission.RECORD_AUDIO)) {
//            Toast.makeText(this,
//                    getString(R.string.permissions_needed),
//                    Toast.LENGTH_LONG).show();
//        } else {
//            ActivityCompat.requestPermissions(
//                    this,
//                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
//                    PERMISSION_REQUEST_CODE);
//        }
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           @NonNull String PERMISSIONS[], @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE: {
//
//                boolean cameraAndMicPermissionGranted = true;
//
//                for (int grantResult : grantResults) {
//                    cameraAndMicPermissionGranted &= grantResult == PackageManager.PERMISSION_GRANTED;
//                }
//
//                if (cameraAndMicPermissionGranted) {
//                    // TODO enable my stream livestreamPresenter.toggleMyStream();
//                } else {
//                    Toast.makeText(this,
//                            R.string.permissions_needed,
//                            Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//
//        }
//    }

    @Override
    protected void onDestroy() {
        va.clear();
        if (isFinishing()) {
            livestreamPresenter.onFinishing();
            va.clear();
        }
        super.onDestroy();
    }

    @Override
    public void myVideoViewCreated(VideoView videoView) {
        livestreamPresenter.bindMyVideoView(videoView);
    }

    @Override
    public void otherUserStreamClicked(int index) {

    }

    @Override
    public void otherUserVideoViewCreated(VideoView videoView, int index) {

    }

    public void showInviteFriendChooser(View view) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String appName = Util.getAppName(this);
        String serverLink = String.format(
                "http://stagingquery.dabkick.com/sdk/user/sdkInvite.php?s=%s&d=%s",
                ((SdkApp)SdkApp.getAppContext()).getDabKickSession().getDeveloperKey(),
                Prefs.getDeveloperId());
        String text = "Let's watch videos together on " + appName + " by clicking " + serverLink;
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        Intent chooserIntent = Intent.createChooser(shareIntent, "Share Room With");
        if (chooserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooserIntent);
        }
    }

    public void closeChatList(View view){
        toggleChatUi();
    }



    @Override
    public void notifyDataSetChanged() {
        sessionParticipantsAdapter.notifyDataSetChanged();
    }

    //    void onEnterTwilio();
//    void onStartStreaming(VideoView videoView);
//    void onStopStreaming();

    @Override
    public void onEnterTwilio() {
        enterRoomTwilio();
    }

    @Override
    public void onStartStreaming(VideoView videoView) {
        startStreaming(videoView);
    }

    @Override
    public void onStopStreaming() {
        stopStreaming();
    }


    private void showGetUserDetailsFragment() {

        android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();

        // remove any currently shown dialog
        android.app.Fragment prev = getFragmentManager().findFragmentByTag(GetUserDetailsFragment.class.getName());
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        // show Fragment
        GetUserDetailsFragment newGetUserDetailsFragment = new GetUserDetailsFragment();
        newGetUserDetailsFragment.show(ft, GetUserDetailsFragment.class.getName());

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        livestreamPresenter.onStart();
        stagePresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
        livestreamPresenter.onStop();
        stagePresenter.onStop();
    }

    @Override
    public void setStageIndexByKey(int newPosition) {
        stageRecyclerView.smoothScrollToPosition(newPosition);
    }


    //gopal
//    public void setAudioFocus(boolean focus) {
//
//        if (focus) {
//            previousAudioMode = mAudioManager.getMode();
//            // Request audio focus before making any device switch.
//
//            //Checking if Live streaming is in progress,
//            //if live streaming is nit started then
//            //the volume will be speaker volume for music and video
//            int result = mAudioManager.requestAudioFocus(this, AudioManager.STREAM_VOICE_CALL,
//                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
//
//            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
//                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 5, AudioManager.FLAG_PLAY_SOUND);
//            }
//            /*
//             * Use MODE_IN_COMMUNICATION as the default audio mode. It is required
//             * to be in this mode when playout and/or recording starts for the best
//             * possible VoIP performance. Some devices have difficulties with
//             * speaker mode if this is not set.
//             */
//
//            //Adding this mode as MODE_IN_COMMUNICATION is causing problems and
//            //in few devices volume is getting set to call volume instead of speaker
//            //volume for music and video.
//            //mAudioManager.setMode(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
//
//            /*
//             * Always disable microphone mute during a WebRTC call.
//             */
//            previousMicrophoneMute = mAudioManager.isMicrophoneMute();
//
//
//            //gopal
////            mAudioManager.setMode(MediaRecorder.AudioSource.VOICE_COMMUNICATION);
//            mAudioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
//
////            mAudioManager.setMicrophoneMute(previousMicrophoneMute);
////            if (mAudioManager.isWiredHeadsetOn()) {
////                mAudioManager.setSpeakerphoneOn(false);
////            } else {
////                mAudioManager.setSpeakerphoneOn(true);
////                mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FLAG_PLAY_SOUND);
////            }
//
//            mAudioManager.setMicrophoneMute(false);
//
//
//            //vallabh commented
//            //this is causing a major bug that when media is played it is playing in call speaker volume and later user is unable to increase it aswell!
////            audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
//        } else {
//            if (!mAudioManager.isWiredHeadsetOn())
//                mAudioManager.setSpeakerphoneOn(true);
//            try {
//                if (previousAudioMode != 0)
//                    previousAudioMode = 0;
//            } catch (Exception e) {
//                //Catching any security exception obtained from setting the mode
//            }
//            mAudioManager.setMode(previousAudioMode);
//            mAudioManager.abandonAudioFocus(null);
//            mAudioManager.setMicrophoneMute(previousMicrophoneMute);
//        }
//    }


    public void requestPermissionForCameraAndMicrophone() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this,
                    "Camera and Microphone permissions needed. Please allow in App Settings for additional functionality.",
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE_CAMERA_FOR_LIVESTREAM);
        }
    }


    public void startStreaming(VideoView videoView) {
//        createAudioVideoTracks();
//        localVideoTrack.addRenderer(localVideoView);
//        if (accessToken == null) {
//            initAccessToken();
//        } else {
//            connectToRoom(ROOM_NAME_TODO_DYNAMICALLY_OBTAIN);
//        }

        va.localVideoView = videoView;

        //check that permissions are in order!

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            isPermissionForLiveStreaming = true;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA},
                    REQUEST_MICROPHONE_CAMERA_FOR_LIVESTREAM);
        }


        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED))
        {

            //got permission!
            ////enable local video and audio tracks


            //is local video track already created? if not, create it
            if (va.localVideoTrack == null)
                createLocalMedia();



            //enter room if not alreasy in it

            if (va.room == null || va.room.getState() == RoomState.DISCONNECTED)

            {
                enterRoomTwilio(); //this will also secure new token if expired
            }
            // Share your camera if connected to room
            if (va.localParticipant != null) {
                va.localParticipant.addVideoTrack(va.localVideoTrack);
            }

            //set up view
            videoView.setMirror(true);
            //add renderer
            va.localVideoTrack.addRenderer(videoView);
            //make visible
            videoView.setVisibility(View.VISIBLE);

            //enable video track - now listners in other connected devices will do the right thing
            va.localAudioTrack.enable(true);
//            va.mAudioManager.setMicrophoneMute(false);

            va.localVideoTrack.enable(true);

            //you are streaming!
            va.isStreaming = true;

            livestreamPresenter.setAudioEnabled(true);
            livestreamPresenter.setVideoEnabled(true);

        }

    }


    public void stopStreaming() {
//        // free native memory resources
        //stop sharing video & audio in room
        va.localVideoTrack.enable(false);
        va.localAudioTrack.enable(false);
        va.setAudioFocus(false);
        va.localVideoTrack.removeRenderer(va.localVideoView);
        EventBus.getDefault().post(new NotifyLivestreamAdapterEvent());
        livestreamPresenter.setAudioEnabled(false);
        livestreamPresenter.setVideoEnabled(false);



//        localAudioTrack.release();
//        localAudioTrack = null;
//        localVideoTrack.release();
//        localVideoTrack = null;
    }




//    public void removeVideoRenderer(String jid) {
//        if (va.localVideoTrack != null)
//            va.localVideoTrack.removeRenderer(fullScreenStreamingView);
//        if (va.videoTrackList.containsKey(jid))
//            va.videoTrackList.remove(jid);
//    }




     public void enterRoomTwilio()

    {
        String roomName = com.dabkick.videosdk.livesession.AbstractDatabaseReferences.getSessionId();

        if (roomName != null)

        {
//            String TwilioRoomName = XMPPCenter.getInstance().newRoomName + "@privaterooms.xmpp.dabkick.com";
            String TwilioRoomName = roomName;


//            LiveSessionManager.getInstance().setLiveStreamingStarted();
            livestreamPresenter.setVideoEnabled(true);
            livestreamPresenter.setAudioEnabled(true);

            connectToRoom(TwilioRoomName);
        } else {
            Log.d("gopal", "Error: Room name is null");
            return;
        }


    }

    public void connectToRoom(String roomName) {

        va.setAudioFocus(true);

        if ((va != null) && ((va.getInstance().VIDEO_CLIENT_ID == null) || (va.isTokenExpired)))
            initTwilio();

        final List<LocalAudioTrack> audioTracks = Arrays.asList(va.localAudioTrack);
        final List<LocalVideoTrack> videoTracks = Arrays.asList(va.localVideoTrack);

        ConnectOptions.Builder connectOptionsBuilder = new ConnectOptions.Builder(va.getInstance().VIDEO_CLIENT_ID)
                .roomName(roomName);

        /*
         * Add local audio track to connect options to share with participants.
         */
        if (va.localAudioTrack != null) {
//            connectOptionsBuilder.audioTracks(Collections.singletonList(va.localAudioTrack));
            connectOptionsBuilder.audioTracks(audioTracks);
        }


        /*
         * Add local video track to connect options to share with participants.
         */
        if (va.localVideoTrack != null) {
//            connectOptionsBuilder.videoTracks(Collections.singletonList(va.localVideoTrack));
            connectOptionsBuilder.videoTracks(videoTracks);
        }


        if (va.getInstance().VIDEO_CLIENT_ID != null)
            va.room = Video.connect(this, connectOptionsBuilder.build(), va.roomListener());

    }

    public void initTwilio() {

        if (((va != null) && (va.isTokenExpired)) || (va == null))

            createVideoClient();

        if (((va != null) && (va.localVideoTrack == null)) || (va == null))

            createLocalMedia();
    }

    public void createVideoClient() {
        /*
         * Create a VideoClient allowing you to connect to a Room
         */

        // OPTION 1- Generate an access token from the getting started portal
        // https://www.twilio.com/console/video/dev-tools/testing-tools
//        videoClient = new VideoClient(VideoActivity.this, TWILIO_ACCESS_TOKEN);

        // OPTION 2- Retrieve an access token from your own web app
//        retrieveAccessTokenfromServer();

//        String uid = Prefs.getUserId();
//        String jid = PreferenceHandler.getXMPPUserName(BaseActivity.mCurrentActivity);

        retrieveAccessToken();

//        retrieveAccessToken(uid, new LiveSessionInfoHandler.InfoCallback() {
//            @Override
//            public void callback(LiveSessionInfoHandler.LoadProgress progress) {
//
//                // successful check and if needed retrieval
//
//            }
//
//        });
    }

//    public void retrieveAccessToken(String jid, Live?SessionInfoHandler.InfoCallback callback) {
    public void retrieveAccessToken() {

        //// TODO: 3/4/17 --Keshav:Done
        //check if current token is valid

        //if valid, return
        if (!va.isTokenExpired) {
//            callback.callback(LiveSessionInfoHandler.LoadProgress.TOKEN_ACQUIRED);
            return;
        }

        getAccessToken();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Webb webb = Webb.create();
//                    String result = webb.get(GlobalData.getInstance().GET_LIVESTREAM_TOKEN)
//                            .param("jid", jid)
//                            .asString().getBody();
//                    Log.d("VideoActivity", "Token Fetch Result: " + result);
//
//                    GlobalData.getInstance().CURRENT_TOKEN = result;
//
////                    GlobalData.getInstance().VIDEO_CLIENT_ID = new VideoClient(BaseActivity.mCurrentActivity, result);
//                    GlobalData.getInstance().VIDEO_CLIENT_ID = result;
//
//                    GlobalData.twilioAccessTokenCheckTimer();
//
//                } catch (WebbException e) {
//                    e.printStackTrace();
//                }
//                callback.callback(LiveSessionInfoHandler.LoadProgress.TOKEN_ACQUIRED);
//            }
//        }).start();

    }

    private void getAccessToken() {
        RetrofitCreator.getAuthenticatedApiInterface()
                .getLivestreamAccessToken()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<TwilioAccessToken>() {
                    @Override public void onSubscribe(Disposable d) {}

                    @Override
                    public void onSuccess(TwilioAccessToken twilioAccessToken) {
                        Timber.i("retrieved Twilio access token");
                        va.accessToken = twilioAccessToken.getAccessToken();
                        va.VIDEO_CLIENT_ID = twilioAccessToken.getAccessToken();
                        va.twilioAccessTokenCheckTimer();

//                        connectToRoom(ROOM_NAME_TODO_DYNAMICALLY_OBTAIN);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Timber.e("unable to get Twilio access token");
                        Timber.e(e);
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0: {
//                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                    GlobalHandler.showUserDeniedDialog("NO CAMERA ACCESS ", "Uh oh! Looks like you have denied " +
//                            "the camera permission. we can't use camera to take pictire", mCurrentActivity, Manifest.permission.CAMERA);
//                } else {
//                    // If request is cancelled, the result arrays are empty.
//                    if (isCameraVisible) {
//                        if (grantResults.length > 0
//                                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                            cameraGranted = true;
//                            mDisplayView.removeView(mCameraPreview);
//                            mDisplayView.addView(mCameraPreview);
//                        } else {
//                            cameraGranted = false;
//                            cameraCtrlLayout.setVisibility(View.INVISIBLE);
//                            showSettingsDialog(" NO CAMERA ACCESS ", "Uh oh! Looks like we can't use camera to take picture. This app camera is disabled until you give DabKick permission to use your camera");
//                        }
//                    }
//                }
                return;
            }
            case RECORD_AUDIO_PERMISSION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                } else {
//
//                    GlobalHandler.runOnUIThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            showSettingsDialog("NO MICROPHONE ACCESS", "Uh oh! Looks like we can't use mic to talk. This app microphone is disabled until you give DabKick permission to use your microphone");
//                        }
//                    });
//                }
                return;
            }

            case WRITE_EXTERNAL_STORAGE_PERMISSION: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//
//                } else {
//                    GlobalHandler.runOnUIThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            showSettingsDialog("NO STORAGE ACCESS", "Uh oh! Looks like we can't use gallery to post. This app storage is disabled until you give DabKick permission to use your gallery");
//                        }
//                    });
//                }
                return;
            }

            case READ_EXTERNAL_STORAGE_PERMISSION: {
//                // If request is cancelled, the result arrays are empty.
//                if (ActivityCompat.shouldShowRequestPermissionRationale(mCurrentActivity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    GlobalHandler.showUserDeniedDialog("NO STORAGE ACCESS", "Uh oh! Looks like you have denied " +
//                            "the storage permission. we can't use gallery to post", mCurrentActivity, Manifest.permission.READ_EXTERNAL_STORAGE);
//                } else {
//                    if (grantResults.length > 0
//                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                        albumClick(photoImageLayout);
//                    } else {
//                        showSettingsDialog("NO STORAGE ACCESS", "Uh oh! Looks like we can't use gallery to post. This app storage is disabled until you give DabKick permission to use your gallery");
//                    }
//                }
//                return;
            }

            case WRITE_SETTING: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//
//                } else {
//                    GlobalHandler.runOnUIThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            showSettingsDialog("NO STORAGE ACCESS", "Uh oh! Looks like we can't use gallery to post. This app storage is disabled until you give DabKick permission to use your gallery");
//                        }
//                    });
//                }
//                return;
            }
            case REQUEST_MICROPHONE_CAMERA_FOR_LIVESTREAM: {

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                    showUserDeniedDialog("NO MICROPHONE ACCESS", "Uh oh! Looks like you have denied " +
                            "the microphone permission. we can't record a audio", this, Manifest.permission.RECORD_AUDIO);
                } else {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        isLiveStreamAudioGrant = true;
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                            showUserDeniedDialog("NO CAMERA ACCESS", "Uh oh! Looks like you have denied " +
                                    "the camera permission. we can't use camera to Live Stream", this, Manifest.permission.CAMERA);
                        } else {
                            if (grantResults.length > 0
                                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                                isLiveStreamCameraGrant = true;
                                cameraGranted = true;
                            } else if (grantResults.length > 0
                                    && grantResults[1] == PackageManager.PERMISSION_DENIED) {
                                isLiveStreamCameraGrant = false;
                                cameraGranted = false;
                                showSettingsDialog(" NO CAMERA ACCESS ", "Uh oh! Looks like we can't use camera to take picture. This app camera is disabled until you give DabKick permission to use your camera", this);
                            }
                        }
                    } else if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        isLiveStreamAudioGrant = false;
                        showSettingsDialog("NO MICROPHONE ACCESS", "Uh oh! Looks like we can't use mic to talk. This app microphone is disabled until you give DabKick permission to use your microphone", this);
                    }
                }


                if ((isPermissionForLiveStreaming) && (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                        && isLiveStreamAudioGrant) {

                    //gopal
                    startStreaming((VideoView) va.localVideoView);

//                    if (va.localAudioTrack == null)
//                        // Share your microphone
//                        va.localAudioTrack = LocalAudioTrack.create(SdkApp.getAppContext(), true);
//                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                    audioManager.setMicrophoneMute(false);
//
//                    // Share your camera
//                    createLocalVideoTrack(true);



                } else if ((!isPermissionForLiveStreaming) && (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                        && isLiveStreamAudioGrant) {

                    if (va.localAudioTrack == null)
                        // Share your microphone
//                        va.localAudioTrack = LocalAudioTrack.create(SdkApp.getAppContext(), false);
                        va.localAudioTrack = LocalAudioTrack.create(this, false);
//                    AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                        va.mAudioManager.setMicrophoneMute(true);

                    // Share your camera
                    createLocalVideoTrack(false);
                }

                return;
            }

            case REQUEST_MICROPHONE_CAMERA_FOR_TWILIO: {
                return;
            }

        }
    }


    public void createLocalMedia() {

        if (va.cameraCapturer != null) {
            va.cameraCapturer.stopCapture();
            va.cameraCapturer = null;
        }


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {


            isPermissionForLiveStreaming = false;
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA},
                    REQUEST_MICROPHONE_CAMERA_FOR_LIVESTREAM);
        }


        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
                && (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            if (va.localAudioTrack == null)
                // Share your microphone
                va.localAudioTrack = LocalAudioTrack.create(this, false);
            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            audioManager.setMicrophoneMute(false);

            // Share your camera
            createLocalVideoTrack(false);
        }

    }

    public static boolean isFrontCameraPresent(Context context) {
        PackageManager pm = context.getPackageManager();
        return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }

    public void createLocalVideoTrack(boolean enable) {
        // Share your camera
        if (va.localVideoTrack == null && checkPermissionForCameraAndMicrophone()) {
            if (isFrontCameraPresent(this))
                va.cameraCapturer = new CameraCapturer(this, CameraCapturer.CameraSource.FRONT_CAMERA);
            else
                va.cameraCapturer = new CameraCapturer(this, CameraCapturer.CameraSource.BACK_CAMERA);

            // Setup video constraints
            VideoConstraints videoConstraints = new VideoConstraints.Builder()
                    .minVideoDimensions(VideoDimensions.CIF_VIDEO_DIMENSIONS)
                    .maxVideoDimensions(VideoDimensions.HD_720P_VIDEO_DIMENSIONS)
                    .minFps(5)
                    .maxFps(24)
                    .build();

            // Add a video track with constraints
            va.localVideoTrack = LocalVideoTrack.create(this, enable, va.cameraCapturer, videoConstraints);

            // If the constraints are not satisfied a null track will be returned
            if (va.localVideoTrack == null) {
                Timber.i("VideoConstraints NOT satisfied.");
                Log.d("gopal", "Unable to satisfy constraints");
                va.localVideoTrack = LocalVideoTrack.create(this, enable, va.cameraCapturer);
            }

        }
    }


    public void showUserDeniedDialog(String title, String message, Activity activity, String permission) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        ActivityCompat.requestPermissions((Activity) activity,
                                new String[]{permission},
                                0);
//                        dialog.cancel();

                    }
                });
        alert11 = builder.create();
        //crashes sometimes if in case the activity is stopped while performing a show
        if (!this.isFinishing())
            alert11.show();
    }

    public static void showSettingsDialog(String title, String message, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);

        builder.setPositiveButton(
                "APP SETTINGS",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        try {
                            //Open the specific App Info page:
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.parse("package:" + activity.getApplicationContext().getPackageName()));
                            activity.startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            //Open the generic Apps page:
                            Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                            activity.startActivity(intent);
                        }

                    }
                });

        builder.setNegativeButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(!activity.isFinishing()) //here make sure your activity is running
                            dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder.create();
        if(!activity.isFinishing())//here make sure your activity is running
            alert11.show();
    }

//    public static void displayToast(String message) {
//        runOnUIThread(new Runnable() {
//            @Override
//            public void run() {
//                if (toast != null) {
//                    toast.cancel();
//                    if (this != null) {
//                        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
//                        toast.show();
//                    }
//                } else {
//                    if (this != null) {
//                        toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
//                        toast.show();
//                    }
//                }
//            }
//        });
//    }


    public static void dismissToast() {

        if (toast != null) {
            toast.cancel();
            return;
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotifyLivestreamAdapterEvent event) {
        sessionParticipantsAdapter.notifyDataSetChanged();
    };


    //back button enabeled and ends the sesion.
    @Override
    public void onBackPressed() {
        va.clear();
        super.onBackPressed();
    }




}