package com.dabkick.videosdk.livesession.chat;


public interface LiveSessionChatView {

    void clickSendButton(String message);


    void addChatMessage(ChatMessage chatMessage);
}
