package com.mystrimz.android.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amitrai on 29/3/17.
 * see more at www.github.com/amitrai98
 */

public class RegisterBean extends BaseBean {

    @SerializedName("id")
    @Expose
    private String userId;

    @SerializedName("first_name")
    @Expose
    private String firstname;

    @SerializedName("last_name")
    @Expose
    private String lastname;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("device_type")
    @Expose
    private String devicetoken;

    @SerializedName("device_token")
    @Expose
    private String devicetype;

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @SerializedName("registration_type")
    @Expose
    private String usertype;

    @SerializedName("image")
    @Expose
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    public String getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(String devicetype) {
        this.devicetype = devicetype;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
