package com.dabkick.videosdk.livesession.chat;

import com.dabkick.videosdk.livesession.Presenter;

/**
 * Essentially a LiveSession Controller
 */
public class ChatPresenter implements Presenter {

    private ChatView view;
    private ChatModel model;

    public ChatPresenter(ChatView view) {
        this.view = view;
        model = new ChatModel(this);
    }

    public void messageAdded(ChatMessage chatMessage) {
        view.addChatMessage(chatMessage);
    }


    public void sendMessage(String message) {
        model.sendMessage(message);
    }

}
