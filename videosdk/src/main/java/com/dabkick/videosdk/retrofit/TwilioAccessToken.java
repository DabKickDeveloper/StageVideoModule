package com.dabkick.videosdk.retrofit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TwilioAccessToken {

    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    /**
     * No args constructor for use in serialization
     *
     */
    public TwilioAccessToken() {
    }

    /**
     *
     * @param accessToken
     */
    public TwilioAccessToken(String accessToken) {
        super();
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("accessToken", accessToken).toString();
    }

}
