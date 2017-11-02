package com.dabkick.videosdk.livesession.livestream;


public class Participant {

    String userId;

    public String getUserId() {
        return userId;
    }

    public String getDabname() {
        return dabname;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public boolean isVideoEnabled() {
        return isVideoEnabled;
    }

    public boolean isAudioEnabled() {
        return isAudioEnabled;
    }

    String dabname, profilePicUrl;
    boolean isVideoEnabled, isAudioEnabled;

    public Participant() {
        // required Firebase empty constructor
    }

    public Participant(String userId, String dabname, String profilePicUrl, boolean isVideoEnabled,
                       boolean isAudioEnabled) {
        this.userId = userId;
        this.dabname = dabname;
        this.profilePicUrl = profilePicUrl;
        this.isVideoEnabled = isVideoEnabled;
        this.isAudioEnabled = isAudioEnabled;
    }

}
