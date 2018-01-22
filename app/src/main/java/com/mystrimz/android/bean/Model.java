package com.mystrimz.android.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by manishjoshi on 17/1/18.
 */

public class Model {
    @SerializedName("album_title")
    @Expose
    private String albumTitle;

    @SerializedName("access_token")
    @Expose
    private String accessToken;

    @SerializedName("tracks")
    @Expose
    private List<TrackList> tracks = null;

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public List<TrackList> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackList> tracks) {
        this.tracks = tracks;
    }
}
