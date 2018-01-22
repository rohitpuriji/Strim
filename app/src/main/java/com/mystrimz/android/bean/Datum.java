package com.mystrimz.android.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by manishjoshi on 18/1/18.
 */

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("cover_img")
    @Expose
    private String coverImg;
    @SerializedName("listen_times")
    @Expose
    private String listenTimes;

    @SerializedName("likes")
    @Expose
    private String likes;

    @SerializedName("sharing")
    @Expose
    private String sharing;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("user_image")
    @Expose
    private String userImage;

    @SerializedName("user_name")
    @Expose
    private String userName;

    @SerializedName("user_like")
    @Expose
    private String userLike;
    @SerializedName("listen_date")
    @Expose
    private String listenDate;

    @SerializedName("follower_id")
    @Expose
    private String followerId;

    @SerializedName("tracks")
    @Expose
    private List<MyListTrack> tracks = null;

   /* @SerializedName("tags")
    @Expose
    private List<String> tags = null;*/

   private Boolean isSeleted = false;

    public Boolean getSeleted() {
        return isSeleted;
    }

    public void setSeleted(Boolean seleted) {
        isSeleted = seleted;
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

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getListenTimes() {
        return listenTimes;
    }

    public void setListenTimes(String listenTimes) {
        this.listenTimes = listenTimes;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getSharing() {
        return sharing;
    }

    public void setSharing(String sharing) {
        this.sharing = sharing;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLike() {
        return userLike;
    }

    public void setUserLike(String userLike) {
        this.userLike = userLike;
    }

    public String getListenDate() {
        return listenDate;
    }

    public void setListenDate(String listenDate) {
        this.listenDate = listenDate;
    }

    public String getFollowerId() {
        return followerId;
    }

    public void setFollowerId(String followerId) {
        this.followerId = followerId;
    }

    public List<MyListTrack> getTracks() {
        return tracks;
    }

    public void setTracks(List<MyListTrack> tracks) {
        this.tracks = tracks;
    }

    /*public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }*/
}
