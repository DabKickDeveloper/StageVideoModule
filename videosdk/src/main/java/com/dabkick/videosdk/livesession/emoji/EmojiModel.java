package com.dabkick.videosdk.livesession.emoji;


public class EmojiModel {

    private String emojiType, key, participantName;

    public EmojiModel(){
        // required empty constructor for Firebase
    }

    public EmojiModel(String emojiType, String key, String participantName) {
        this.emojiType = emojiType;
        this.key = key;
        this.participantName = participantName;
    }

    public String getEmojiType() {
        return emojiType;
    }

    public String getKey() {
        return key;
    }

    public String getParticipantName() {
        return participantName;
    }

}
