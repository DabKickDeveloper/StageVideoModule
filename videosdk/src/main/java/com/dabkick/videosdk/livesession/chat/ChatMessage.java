package com.dabkick.videosdk.livesession.chat;


public class ChatMessage {

    String message, senderUserId;

    public ChatMessage() {
        // Firebase req'd constructor
    }

    ChatMessage(String message, String senderUserId) {
        this.message = message;
        this.senderUserId = senderUserId;
    }


}
