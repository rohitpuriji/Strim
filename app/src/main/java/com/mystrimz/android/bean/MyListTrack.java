package com.mystrimz.android.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by manishjoshi on 18/1/18.
 */

public class MyListTrack {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("hoster_id")
    @Expose
    private String hosterId;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("hoster_track_id")
    @Expose
    private String hosterTrackId;
    /*@SerializedName("tags")
    @Expose
    private String tags;*/
    @SerializedName("hoster")
    @Expose
    private String hoster;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHosterId() {
        return hosterId;
    }

    public void setHosterId(String hosterId) {
        this.hosterId = hosterId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getHosterTrackId() {
        return hosterTrackId;
    }

    public void setHosterTrackId(String hosterTrackId) {
        this.hosterTrackId = hosterTrackId;
    }

    /*public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }*/

    public String getHoster() {
        return hoster;
    }

    public void setHoster(String hoster) {
        this.hoster = hoster;
    }
}
