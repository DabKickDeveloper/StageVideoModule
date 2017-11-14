package com.dabkick.videosdk.livesession.chat;


public interface ChatView {

    void clickSendButton(String message);


    void addChatMessage(ChatModel chatModel);
}
