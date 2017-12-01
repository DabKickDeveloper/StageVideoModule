package com.dabkick.videosdk.livesession.emoji;


public class EmojiModel {

    private String emojiType, key, userId;

    public EmojiModel(){
        // required empty constructor for Firebase
    }

    public EmojiModel(String emojiType, String key, String userId) {
        this.emojiType = emojiType;
        this.key = key;
        this.userId = userId;
    }

    public String getEmojiType() {
        return emojiType;
    }

    public String getKey() {
        return key;
    }

    public String getUserId() {
        return userId;
    }

}
