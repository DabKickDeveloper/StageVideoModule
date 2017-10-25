package com.dabkick.videosdk.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

class RefreshAccessTokenResponse {

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("expires_in")
    @Expose
    private Integer expiresIn;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("scope")
    @Expose
    private Object scope;

    /**
     * No args constructor for use in serialization
     *
     */
    public RefreshAccessTokenResponse() {
    }

    /**
     *
     * @param scope
     * @param tokenType
     * @param accessToken
     * @param expiresIn
     */
    public RefreshAccessTokenResponse(String accessToken, Integer expiresIn, String tokenType, Object scope) {
        super();
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.scope = scope;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
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

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}