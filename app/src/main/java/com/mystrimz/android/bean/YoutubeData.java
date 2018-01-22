package com.mystrimz.android.bean;

import java.util.List;

/**
 * Created by manishjoshi on 16/1/18.
 */

public class YoutubeData {

    private List<TrackList> tracks;
    private List<TrackList> playlists;
    private String hoster;
    private String pagination_key;

    public List<TrackList> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackList> tracks) {
        this.tracks = tracks;
    }


    public String getHoster() {
        return hoster;
    }

    public void setHoster(String hoster) {
        this.hoster = hoster;
    }

    public String getPagination_key() {
        return pagination_key;
    }

    public void setPagination_key(String pagination_key) {
        this.pagination_key = pagination_key;
    }

    public List<TrackList> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<TrackList> playlists) {
        this.playlists = playlists;
    }
}
