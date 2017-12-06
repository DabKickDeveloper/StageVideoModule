package com.dabkick.videosdk.livesession.chat;

import com.dabkick.videosdk.livesession.Presenter;

/**
 * Essentially a LiveSession Controller
 */
public class ChatPresenter implements Presenter {

    private ChatView view;
    private ChatDatabase model;

    public ChatPresenter(ChatView view) {
        this.view = view;
        model = new ChatDatabase(this);
    }

    public void messageAdded(ChatModel chatModel) {
        view.addChatMessage(chatModel);
    }


    public void sendMessage(String message) {
        model.sendMessage(message);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }
}
