package com.dabkick.videosdk.livesession;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dabkick.videosdk.R;
import com.dabkick.videosdk.Util;
import com.dabkick.videosdk.livesession.chat.ChatAdapter;
import com.dabkick.videosdk.livesession.chat.ChatMessage;
import com.dabkick.videosdk.livesession.chat.ChatPresenter;
import com.dabkick.videosdk.livesession.chat.ChatView;
import com.dabkick.videosdk.livesession.chat.ClearFocusBackPressedEditText;
import com.dabkick.videosdk.livesession.livestream.LivestreamPresenter;
import com.dabkick.videosdk.livesession.livestream.LivestreamPresenterImpl;
import com.dabkick.videosdk.livesession.livestream.LivestreamView;
import com.dabkick.videosdk.livesession.livestream.SessionParticipantsAdapter;
import com.twilio.video.VideoView;

import java.util.ArrayList;

import timber.log.Timber;

public class LiveSessionActivity extends AppCompatActivity implements ChatView, LivestreamView {

    // Chat MVP
    private ChatAdapter chatAdapter;
    private ChatPresenter chatPresenter;

    // Session Participant MVP
    private SessionParticipantsAdapter sessionParticipantsAdapter;
    private LivestreamPresenter livestreamPresenter;

    private VideoView myVideoView;

    // constants
    private final int PERMISSION_REQUEST_CODE = 3928;
    private final int DEFAULT_CHAT_MSG_LENGTH_LIMIT = 256;

    // Views
    private ImageView chatToggleButton;
    private ListView chatListView;
    private ClearFocusBackPressedEditText chatEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_session);

        if (savedInstanceState == null) {
            Util.register();
        }

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
                toggleChatUi();
            } else {
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false);
        livestreamRecyclerView.setLayoutManager(layoutManager);
        sessionParticipantsAdapter = new SessionParticipantsAdapter(this, this);
        livestreamRecyclerView.setAdapter(sessionParticipantsAdapter);
        livestreamPresenter = new LivestreamPresenterImpl(this);

    }

    // toggle visibility of chat UI and swap button drawable
    private void toggleChatUi() {
        // toggle chat list and icon
        int visibility = chatListView.getVisibility();
        if (visibility == View.INVISIBLE) {
            chatListView.setVisibility(View.VISIBLE);
            Drawable drawable = ContextCompat.getDrawable(
                    this, R.drawable.ic_accessible_white_36dp);
            chatToggleButton.setImageDrawable(drawable);
        } else {
            chatListView.setVisibility(View.INVISIBLE);
            Drawable drawable = ContextCompat.getDrawable(
                    this, R.drawable.ic_directions_boat_white_36dp);
            chatToggleButton.setImageDrawable(drawable);
        }
    }

    @Override
    public void clickSendButton(String message) {
        chatPresenter.sendMessage(message);
    }

    @Override
    public void addChatMessage(ChatMessage chatMessage) {
        chatAdapter.add(chatMessage);
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
    public void myVideoViewCreated(VideoView videoView) {
        livestreamPresenter.bindMyVideoView(videoView);
    }

    @Override
    public void otherUserStreamClicked(int index) {

    }

    @Override
    public void otherUserVideoViewCreated(VideoView videoView, int index) {

    }

    @Override
    public void addFriendClicked() {
        Timber.d("addFriendClicked");
        // waiting on Ronnie to finish server auth work before this can work fully
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        String appName = Util.getAppName(this);
        String text = "Let's watch videos together on " + appName + " by clicking link <link-from-server>";
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        Intent chooserIntent = Intent.createChooser(shareIntent, "Share Room With");
        startActivity(chooserIntent);
    }

}