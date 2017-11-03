package com.dabkick.videosdk.livesession.chat;


public class ChatMessage {

    private String message, senderUserId;

    public ChatMessage() {
        // Firebase req'd constructor
    }

    ChatMessage(String message, String senderUserId) {
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
