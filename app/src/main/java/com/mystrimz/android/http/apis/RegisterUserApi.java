package com.mystrimz.android.http.apis;

import android.database.Observable;

import com.mystrimz.android.bean.Model;
import com.mystrimz.android.bean.TrackList;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by tarun on 27/12/16.
 */

public interface RegisterUserApi {


    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("login")
    Call<ResponseBody> loginUser(
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_type") String device_type,
            @Field("device_token") String token);

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("registration")
    Call<ResponseBody> registerUser(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("email") String email,
            @Field("password") String password,
            @Field("device_type") String devicetype,
            @Field("device_token") String devicetoken,
            @Field("registration_type") String access_token);

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("forgot_password")
    Call<ResponseBody> forgotPass(
            @Field("email") String email);

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("socialLogin")
    Call<ResponseBody> socialRegister(
            @Field("first_name") String first_name,
            @Field("last_name") String last_name,
            @Field("device_type") String device_type,
            @Field("device_token") String device_token,
            @Field("registration_type") String registration_type,
            @Field("social_id") String social_id,
            @Field("social_token") String social_token,
            @Field("email") String email,
            @Field("image") String image);

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("search_youtube")
    Call<ResponseBody> searchYouTube(
            @Field("title") String title,
            @Field("pagination") String last_name,
            @Field("play_list") String play_list);

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("search_soundcloud")
    Call<ResponseBody> searchSoundClould(
            @Field("title") String title,
            @Field("pagination") String last_name,
            @Field("play_list") String play_list);

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("my_play_list")
    Call<ResponseBody> myPlaylist(
            @Field("access_token") String access_token);


    @Headers("Content-Type: application/json")
    @POST("add_play_list")
    Call<ResponseBody> addPlayList(@Body Model request);


   /* @POST("add_play_list")
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    Observable<ResponseBody> addPlayList(@Body Model request);*/

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("delete_album")
    Call<ResponseBody> deleteList(
            @Field("access_token") String access_token,
            @Field("album_id") String album_id);

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("like_dislike")
    Call<ResponseBody> callLike(
            @Field("access_token") String access_token,
            @Field("album_id") String album_id);

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("report")
    Call<ResponseBody> callContent(
            @Field("access_token") String access_token,
            @Field("album_id") String album_id,
            @Field("message") String message);

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("recently_played")
    Call<ResponseBody> recentlyPlaylist(
            @Field("access_token") String access_token);

    @FormUrlEncoded
    @Headers("Cache-Control: no-cache")
    @POST("liked_playlist")
    Call<ResponseBody> likedPlaylist(
            @Field("access_token") String access_token);
}
