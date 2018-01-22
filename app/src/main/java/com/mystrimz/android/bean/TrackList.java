package com.mystrimz.android.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import bolts.Bolts;

/**
 * Created by manishjoshi on 16/1/18.
 */

public class TrackList implements Serializable {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("hoster")
    @Expose
    private String hoster;

    @SerializedName("hoster_track_id")
    @Expose
    private String hosterTrackId;

    @SerializedName("position")
    @Expose
    private String position;

    @SerializedName("duration")
    @Expose
    private String duration;
    @SerializedName("tags")
    @Expose
    private List<String> tags = null;
    @SerializedName("is_playlist")
    @Expose
    private String isPlaylist;
    @SerializedName("is_active")
    @Expose
    private String isActive;

    private Boolean isSelected=false;

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHoster() {
        return hoster;
    }

    public void setHoster(String hoster) {
        this.hoster = hoster;
    }

    public String getHosterTrackId() {
        return hosterTrackId;
    }

    public void setHosterTrackId(String hosterTrackId) {
        this.hosterTrackId = hosterTrackId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getIsPlaylist() {
        return isPlaylist;
    }

    public void setIsPlaylist(String isPlaylist) {
        this.isPlaylist = isPlaylist;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String isActive) {
        this.isActive = isActive;
    }
}
