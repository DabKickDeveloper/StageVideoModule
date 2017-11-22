package com.dabkick.videosdk.livesession;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.text.InputFilter;
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
import com.dabkick.videosdk.livesession.livestream.SessionParticipantsAdapter;
import com.dabkick.videosdk.livesession.mediadrawer.MediaDrawerDialogFragment;
import com.dabkick.videosdk.livesession.overviews.OverviewDatabase;
import com.dabkick.videosdk.livesession.overviews.OverviewPresenter;
import com.dabkick.videosdk.livesession.overviews.OverviewPresenterImpl;
import com.dabkick.videosdk.livesession.overviews.OverviewView;
import com.dabkick.videosdk.livesession.stage.StagePresenter;
import com.dabkick.videosdk.livesession.stage.StagePresenterImpl;
import com.dabkick.videosdk.livesession.stage.StageRecyclerViewAdapter;
import com.dabkick.videosdk.livesession.usersetup.GetUserDetailsFragment;
import com.twilio.video.VideoView;

import java.util.ArrayList;

import javax.inject.Inject;

public class LiveSessionActivity extends AppCompatActivity implements
        ChatView, LivestreamView, OverviewView, StagePresenterImpl.NotifyStageListener {

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

    // Overview
    private OverviewPresenter overviewPresenter;
    @Inject OverviewDatabase overviewDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_session);

        if (savedInstanceState == null) {
            Util.register();
        }

        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);

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
        chatAdapter = new ChatAdapter(this, new ArrayList<>());
        chatListView.setAdapter(chatAdapter);

        chatPresenter = new ChatPresenter(this);

        chatToggleButton = findViewById(R.id.chat_toggle);
        chatToggleButton.setOnClickListener(v -> toggleChatUi());

        chatEditText = findViewById(R.id.message_edit_text);
        chatEditText.setOnEditorActionListener((v, actionId, event) -> {
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
        });

        chatEditText.setOnFocusChangeListener((v, hasFocus) -> {
            // show keyboard if widget has focus
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
                param.setMargins(4,4,4,12);
                chatEditText.setLayoutParams(param);
                emojiLayout.setVisibility(View.GONE);

                toggleChatUi();
            } else {

                int px = Math.round(TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP, 180,getResources().getDisplayMetrics()));
                LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                        px,LinearLayout.LayoutParams.MATCH_PARENT);
                param.setMargins(4,4,4,4);
                chatEditText.setLayoutParams(param);
                emojiLayout.setVisibility(View.VISIBLE);

                toggleChatUi();
            }
        });

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

        // Overview
        overviewPresenter = new OverviewPresenterImpl(this);


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
                    overviewPresenter.onUserSwipedStage(position);
                }
            }
        });

    }

    @Override
    public void notifyStageRecyclerView() {
        stageRecyclerView.scrollToPosition(overviewDatabase.getStagedVideoPosition());
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
            frameLayout.setVisibility(View.VISIBLE);
            AnimationUtils.leftToRight(findViewById(R.id.iv_media_drawer), LiveSessionActivity.this);
            AnimationUtils.leftToRight(frameLayout, LiveSessionActivity.this);
        }else{

            //AnimationUtils.rightToLeft(findViewById(R.id.iv_media_drawer), LiveSessionActivity.this);
            AnimationUtils.rightToLeft(frameLayout, LiveSessionActivity.this);
            frameLayout.setVisibility(View.GONE);
        }
    }

    // toggle visibility of chat UI and swap button drawable
    private void toggleChatUi() {
        // toggle chat list and icon
        int visibility = chatListView.getVisibility();
        if (visibility == View.INVISIBLE) {
            chatListView.setVisibility(View.VISIBLE);
            Drawable drawable = ContextCompat.getDrawable(
                    this, R.drawable.ic_show_chat);
            chatToggleButton.setImageDrawable(drawable);
        } else {
            chatListView.setVisibility(View.INVISIBLE);
            Drawable drawable = ContextCompat.getDrawable(
                    this, R.drawable.ic_hide_chat);
            chatToggleButton.setImageDrawable(drawable);
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

    private void requestPermissionForCameraAndMicrophone(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.RECORD_AUDIO)) {
            Toast.makeText(this,
                    getString(R.string.permissions_needed),
                    Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO},
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String PERMISSIONS[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {

                boolean cameraAndMicPermissionGranted = true;

                for (int grantResult : grantResults) {
                    cameraAndMicPermissionGranted &= grantResult == PackageManager.PERMISSION_GRANTED;
                }

                if (cameraAndMicPermissionGranted) {
                    // TODO enable my stream livestreamPresenter.toggleMyStream();
                } else {
                    Toast.makeText(this,
                            R.string.permissions_needed,
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isFinishing()) {
            livestreamPresenter.onFinishing();
        }
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frag_media_drawer);
//        if(fragment != null)
//            ft.remove(fragment);
//        ft.commit();
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

    @Override
    public void notifyDataSetChanged() {
        sessionParticipantsAdapter.notifyDataSetChanged();
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
        stagePresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stagePresenter.onStop();
    }

    @Override
    public void setStageIndex(int newPosition) {
        stageRecyclerView.smoothScrollToPosition(newPosition);
    }

}