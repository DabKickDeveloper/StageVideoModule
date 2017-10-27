package com.dabkick.videosdk.livesession;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.dabkick.videosdk.R;
import com.dabkick.videosdk.livesession.chat.ChatAdapter;
import com.dabkick.videosdk.livesession.chat.ChatMessage;
import com.dabkick.videosdk.livesession.chat.ChatPresenter;
import com.dabkick.videosdk.livesession.chat.ChatView;
import com.dabkick.videosdk.livesession.livestream.LiveStreamPresenterImpl;
import com.dabkick.videosdk.livesession.livestream.LivestreamAdapter;
import com.dabkick.videosdk.livesession.livestream.LivestreamPresenter;

import java.util.ArrayList;

public class LiveSessionActivity extends AppCompatActivity implements ChatView {

    private ChatAdapter chatAdapter;
    private ChatPresenter chatPresenter;

    private LivestreamAdapter livestreamAdapter;
    private LivestreamPresenter livestreamPresenter;

    private final int DEFAULT_CHAT_MSG_LENGTH_LIMIT = 256;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_session);

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

        livestreamAdapter = new LivestreamAdapter();
        livestreamPresenter = new LiveStreamPresenterImpl();

    }

    @Override
    public void clickSendButton(String message) {
        chatPresenter.sendMessage(message);
    }

    @Override
    public void addChatMessage(ChatMessage chatMessage) {
        chatAdapter.add(chatMessage);
    }

}