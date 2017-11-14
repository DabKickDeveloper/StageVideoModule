package com.dabkick.videosdk.livesession.chat;


public class ChatModel {

    private String message, senderUserId;

    public ChatModel() {
        // Firebase req'd constructor
    }

    ChatModel(String message, String senderUserId) {
        this.message = message;
        this.senderUserId = senderUserId;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderUserId() {
        return senderUserId;
    }


}
