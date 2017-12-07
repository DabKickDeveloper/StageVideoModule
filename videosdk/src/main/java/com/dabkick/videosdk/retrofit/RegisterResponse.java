package com.dabkick.videosdk.retrofit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RegisterResponse {

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("expires_in")
    @Expose
    private int expiresIn;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("scope")
    @Expose
    private Object scope;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("user_details")
    @Expose
    private UserDetails userDetails;
    @SerializedName("firebase_token")
    @Expose
    private String firebaseToken;
    @SerializedName("twilio_access_token")
    @Expose
    private String twilioaccessToken;

    /**
     * No args constructor for use in serialization
     *
     */
    public RegisterResponse() {
    }

    /**
     *
     * @param scope
     * @param tokenType
     * @param accessToken
     * @param expiresIn
     * @param refreshToken
     * @param userDetails
     * @param twilioaccessToken
     */
    public RegisterResponse(String accessToken, int expiresIn, String tokenType, Object scope, String refreshToken, UserDetails userDetails, String firebaseToken, String twilioaccessToken) {
        super();
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.scope = scope;
        this.refreshToken = refreshToken;
        this.userDetails = userDetails;
        this.firebaseToken = firebaseToken;
        this.twilioaccessToken = twilioaccessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Object getScope() {
        return scope;
    }

    public void setScope(Object scope) {
        this.scope = scope;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public String getTwilioaccessToken() {
        return twilioaccessToken;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
