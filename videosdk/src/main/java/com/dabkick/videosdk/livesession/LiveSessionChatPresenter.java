package com.dabkick.videosdk.livesession;

/**
 * Essentially a LiveSession Controller
 */
public class LiveSessionChatPresenter implements Presenter {

    private LiveSessionChatView view;
    private ChatModel model;

    public LiveSessionChatPresenter(LiveSessionChatView view) {
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
