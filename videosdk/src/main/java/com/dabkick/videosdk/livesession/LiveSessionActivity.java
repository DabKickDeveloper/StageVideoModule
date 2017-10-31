package com.dabkick.videosdk.livesession;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.dabkick.videosdk.R;
import com.dabkick.videosdk.Util;
import com.dabkick.videosdk.livesession.chat.ChatAdapter;
import com.dabkick.videosdk.livesession.chat.ChatMessage;
import com.dabkick.videosdk.livesession.chat.ChatPresenter;
import com.dabkick.videosdk.livesession.chat.ChatView;
import com.dabkick.videosdk.livesession.livestream.LivestreamPresenter;
import com.dabkick.videosdk.livesession.livestream.LivestreamPresenterImpl;
import com.dabkick.videosdk.livesession.livestream.LivestreamView;
import com.dabkick.videosdk.livesession.livestream.SessionParticipantsAdapter;
import com.twilio.video.VideoView;

import java.util.ArrayList;

public class LiveSessionActivity extends AppCompatActivity implements ChatView, LivestreamView {

    private ChatAdapter chatAdapter;
    private ChatPresenter chatPresenter;

    private SessionParticipantsAdapter sessionParticipantsAdapter;
    private LivestreamPresenter livestreamPresenter;
    private VideoView myVideoView;
    private final String[] TWILIO_PERMISSIONS = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private final int PERMISSION_REQUEST_CODE = 3928;
    private final int DEFAULT_CHAT_MSG_LENGTH_LIMIT = 256;
    private VideoView  mainVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_session);

        if (savedInstanceState == null) {
            Util.register();
        }

        mainVideoView = findViewById(R.id.videoview_main);

        ListView chatListView = findViewById(R.id.listview_livesession_chat);
        chatAdapter = new ChatAdapter(this, new ArrayList<>());
        chatListView.setAdapter(chatAdapter);

        chatPresenter = new ChatPresenter(this);

        Button sendButton = findViewById(R.id.send_button);

        EditText chatEditText = findViewById(R.id.message_edit_text);
        chatEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0) {
                    sendButton.setEnabled(true);
                } else {
                    sendButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        chatEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(DEFAULT_CHAT_MSG_LENGTH_LIMIT)});


        sendButton.setOnClickListener(view -> {
            clickSendButton(chatEditText.getText().toString());
            chatEditText.setText("");
        });

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

        sessionParticipantsAdapter = new SessionParticipantsAdapter(this, this);
        livestreamPresenter = new LivestreamPresenterImpl(this);

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
    public void myStreamClicked(VideoView videoView) {
        if (checkPermissionForCameraAndMicrophone()) {
            livestreamPresenter.toggleStream(mainVideoView); // FIXME pass reference from adapter
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
                    livestreamPresenter.toggleStream(mainVideoView); // FIXME use adapter video view
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
    public void otherUserStreamClicked(int index) {

    }

}