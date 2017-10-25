package com.dabkick.videosdk.retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class RegisterRequestBody {

    @SerializedName("isAnonymous")
    @Expose
    private boolean isAnonymous;
    @SerializedName("versionNumber")
    @Expose
    private String versionNumber;
    @SerializedName("deviceType")
    @Expose
    private String deviceType;
    @SerializedName("osVersion")
    @Expose
    private String osVersion;
    @SerializedName("bundle")
    @Expose
    private String bundle;
    @SerializedName("devId")
    @Expose
    private String devId;
    @SerializedName("devToken")
    @Expose
    private String devToken;

    /**
     * No args constructor for use in serialization
     *
     */
    public RegisterRequestBody() {
    }

    /**
     *
     * @param osVersion
     * @param devToken
     * @param deviceType
     * @param bundle
     * @param devId
     * @param isAnonymous
     * @param versionNumber
     */
    public RegisterRequestBody(boolean isAnonymous, String versionNumber, String deviceType, String osVersion, String bundle, String devId, String devToken) {
        super();
        this.isAnonymous = isAnonymous;
        this.versionNumber = versionNumber;
        this.deviceType = deviceType;
        this.osVersion = osVersion;
        this.bundle = bundle;
        this.devId = devId;
        this.devToken = devToken;
    }

    public boolean isIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(boolean isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getBundle() {
        return bundle;
    }

    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getDevToken() {
        return devToken;
    }

    public void setDevToken(String devToken) {
        this.devToken = devToken;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("isAnonymous", isAnonymous).append("versionNumber", versionNumber).append("deviceType", deviceType).append("osVersion", osVersion).append("bundle", bundle).append("devId", devId).append("devToken", devToken).toString();
    }

}
