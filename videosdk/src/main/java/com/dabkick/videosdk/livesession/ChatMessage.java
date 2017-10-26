package com.dabkick.videosdk.livesession;


class ChatMessage {

    String message, senderUserId;

    ChatMessage() {
        // Firebase req'd constructor
    }

    ChatMessage(String message, String senderUserId) {
        this.message = message;
        this.senderUserId = senderUserId;
    }


}
