package com.mystrimz.android.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sachin on 23/12/16.
 */

public class BaseBean {
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("message")
    @Expose
    private String message;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getmessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
