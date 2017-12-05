package com.dabkick.videosdk.livesession.chat;


public class ChatModel {

    private String message, senderUserId, dabname;

    public ChatModel() {
        // Firebase req'd constructor
    }

    ChatModel(String message, String senderUserId, String dabname) {
        this.message = message;
        this.senderUserId = senderUserId;
        this.dabname = dabname;
    }

    public String getDabname() {
        return dabname;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderUserId() {
        return senderUserId;
    }


}
