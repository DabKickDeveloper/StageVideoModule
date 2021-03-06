package com.dabkick.videosdk.livesession.livestream;


public class Participant {

    private String userId;

    public String getUserId() {
        return userId;
    }

    public String getDabname() {
        return dabname;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public boolean getIsVideoEnabled() {
        return isVideoEnabled;
    }

    public boolean getIsAudioEnabled() {
        return isAudioEnabled;
    }

    private String dabname, profilePicUrl;
    private boolean isVideoEnabled, isAudioEnabled;

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

    @Override
    public boolean equals(Object obj) {

        if (obj == null) return false;

        if (!Participant.class.isAssignableFrom(obj.getClass())) return false;

        final Participant other = (Participant) obj;

        return this.userId.equals(other.userId);
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + Integer.parseInt(this.userId);
        return result;
    }
}
