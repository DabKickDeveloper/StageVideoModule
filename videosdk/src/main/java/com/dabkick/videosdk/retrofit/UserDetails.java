package com.dabkick.videosdk.retrofit;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class UserDetails {

    @SerializedName("userid")
    @Expose
    private int userid;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("userphone")
    @Expose
    private String userphone;
    @SerializedName("userjid")
    @Expose
    private String userjid;
    @SerializedName("usergender")
    @Expose
    private String usergender;
    @SerializedName("userphoto")
    @Expose
    private String userphoto;
    @SerializedName("useremail")
    @Expose
    private String useremail;
    @SerializedName("userjidpassword")
    @Expose
    private String userjidpassword;
    @SerializedName("userBirthday")
    @Expose
    private String userBirthday;
    @SerializedName("userCountryCode")
    @Expose
    private String userCountryCode;
    @SerializedName("userValidatedEmail")
    @Expose
    private String userValidatedEmail;
    @SerializedName("userFullPhoneNumber")
    @Expose
    private String userFullPhoneNumber;
    @SerializedName("userDabName")
    @Expose
    private String userDabName;
    @SerializedName("userProfilePicVersionNumber")
    @Expose
    private String userProfilePicVersionNumber;
    @SerializedName("userCityState")
    @Expose
    private String userCityState;
    @SerializedName("userCountryName")
    @Expose
    private String userCountryName;

    /**
     * No args constructor for use in serialization
     *
     */
    public UserDetails() {
    }

    /**
     *
     * @param userProfilePicVersionNumber
     * @param usergender
     * @param userid
     * @param userjidpassword
     * @param userphoto
     * @param userCountryCode
     * @param userDabName
     * @param userCountryName
     * @param userBirthday
     * @param userValidatedEmail
     * @param userCityState
     * @param username
     * @param userphone
     * @param userjid
     * @param useremail
     * @param userFullPhoneNumber
     */
    public UserDetails(int userid, String username, String userphone, String userjid, String usergender, String userphoto, String useremail, String userjidpassword, String userBirthday, String userCountryCode, String userValidatedEmail, String userFullPhoneNumber, String userDabName, String userProfilePicVersionNumber, String userCityState, String userCountryName) {
        super();
        this.userid = userid;
        this.username = username;
        this.userphone = userphone;
        this.userjid = userjid;
        this.usergender = usergender;
        this.userphoto = userphoto;
        this.useremail = useremail;
        this.userjidpassword = userjidpassword;
        this.userBirthday = userBirthday;
        this.userCountryCode = userCountryCode;
        this.userValidatedEmail = userValidatedEmail;
        this.userFullPhoneNumber = userFullPhoneNumber;
        this.userDabName = userDabName;
        this.userProfilePicVersionNumber = userProfilePicVersionNumber;
        this.userCityState = userCityState;
        this.userCountryName = userCountryName;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUserjid() {
        return userjid;
    }

    public void setUserjid(String userjid) {
        this.userjid = userjid;
    }

    public String getUsergender() {
        return usergender;
    }

    public void setUsergender(String usergender) {
        this.usergender = usergender;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserjidpassword() {
        return userjidpassword;
    }

    public void setUserjidpassword(String userjidpassword) {
        this.userjidpassword = userjidpassword;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserCountryCode() {
        return userCountryCode;
    }

    public void setUserCountryCode(String userCountryCode) {
        this.userCountryCode = userCountryCode;
    }

    public String getUserValidatedEmail() {
        return userValidatedEmail;
    }

    public void setUserValidatedEmail(String userValidatedEmail) {
        this.userValidatedEmail = userValidatedEmail;
    }

    public String getUserFullPhoneNumber() {
        return userFullPhoneNumber;
    }

    public void setUserFullPhoneNumber(String userFullPhoneNumber) {
        this.userFullPhoneNumber = userFullPhoneNumber;
    }

    public String getUserDabName() {
        return userDabName;
    }

    public void setUserDabName(String userDabName) {
        this.userDabName = userDabName;
    }

    public String getUserProfilePicVersionNumber() {
        return userProfilePicVersionNumber;
    }

    public void setUserProfilePicVersionNumber(String userProfilePicVersionNumber) {
        this.userProfilePicVersionNumber = userProfilePicVersionNumber;
    }

    public String getUserCityState() {
        return userCityState;
    }

    public void setUserCityState(String userCityState) {
        this.userCityState = userCityState;
    }

    public String getUserCountryName() {
        return userCountryName;
    }

    public void setUserCountryName(String userCountryName) {
        this.userCountryName = userCountryName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
