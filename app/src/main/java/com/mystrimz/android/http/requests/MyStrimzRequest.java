package com.mystrimz.android.http.requests;

import com.google.gson.Gson;
import com.mystrimz.android.bean.MyPlaylistBean;
import com.mystrimz.android.http.ApiClient;
import com.mystrimz.android.http.ConnectionManager;
import com.mystrimz.android.http.apicallback.MyPlaylistCallBack;
import com.mystrimz.android.http.apis.RegisterUserApi;
import com.mystrimz.android.http.listeners.OnWebServiceCompleteListener;
import com.mystrimz.android.util.Constants;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by manishjoshi on 22/1/18.
 */

public class MyStrimzRequest {
    private static final MyStrimzRequest ourInstance = new MyStrimzRequest();

    public static MyStrimzRequest getInstance() {
        return ourInstance;
    }

    private MyStrimzRequest() {
    }
    public void callMyPlaylist(String title, final MyPlaylistCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        try{
            Call<ResponseBody> responseBodyCall =
                    registerUserApi.myPlaylist(title);
            ConnectionManager connectionManager = ConnectionManager.getInstance();
            connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
                @Override
                public void onWebServiceComplete(ResponseBody baseObject) {

                    try {
                        JSONObject jsonObject = new JSONObject(baseObject.string());

                        if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                            Gson gson = new Gson();
                            MyPlaylistBean listBean = gson.fromJson(jsonObject.toString(), MyPlaylistBean.class);
                            listBean.setMessage(jsonObject.getString(Constants.KEY_MESSAGE));
                            mainActivityCallback.onSuccess(listBean.getData());
                        } else
                            mainActivityCallback.onError(jsonObject.getString(Constants.KEY_MESSAGE));
                    } catch (Exception e) {
                        e.printStackTrace();
                        mainActivityCallback.onError(e.getMessage());
                    }
                }

                @Override
                public void onWebStatusFalse(String message) {
                    mainActivityCallback.onError(message);
                }
            });
        }catch (Exception exp){
            exp.printStackTrace();
        }

    }

    public void callRecentlyPlayed(String accessToken, final MyPlaylistCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        try{
            Call<ResponseBody> responseBodyCall =
                    registerUserApi.recentlyPlaylist(accessToken);
            ConnectionManager connectionManager = ConnectionManager.getInstance();
            connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
                @Override
                public void onWebServiceComplete(ResponseBody baseObject) {

                    try {
                        JSONObject jsonObject = new JSONObject(baseObject.string());

                        if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                            Gson gson = new Gson();
                            MyPlaylistBean listBean = gson.fromJson(jsonObject.toString(), MyPlaylistBean.class);
                            listBean.setMessage(jsonObject.getString(Constants.KEY_MESSAGE));
                            mainActivityCallback.onSuccess(listBean.getData());
                        } else
                            mainActivityCallback.onError(jsonObject.getString(Constants.KEY_MESSAGE));
                    } catch (Exception e) {
                        e.printStackTrace();
                        mainActivityCallback.onError(e.getMessage());
                    }
                }

                @Override
                public void onWebStatusFalse(String message) {
                    mainActivityCallback.onError(message);
                }
            });
        }catch (Exception exp){
            exp.printStackTrace();
        }

    }

    public void callLikedPlayed(String accessToken, final MyPlaylistCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        try{
            Call<ResponseBody> responseBodyCall =
                    registerUserApi.likedPlaylist(accessToken);
            ConnectionManager connectionManager = ConnectionManager.getInstance();
            connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
                @Override
                public void onWebServiceComplete(ResponseBody baseObject) {

                    try {
                        JSONObject jsonObject = new JSONObject(baseObject.string());

                        if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                            Gson gson = new Gson();
                            MyPlaylistBean listBean = gson.fromJson(jsonObject.toString(), MyPlaylistBean.class);
                            listBean.setMessage(jsonObject.getString(Constants.KEY_MESSAGE));
                            mainActivityCallback.onSuccess(listBean.getData());
                        } else
                            mainActivityCallback.onError(jsonObject.getString(Constants.KEY_MESSAGE));
                    } catch (Exception e) {
                        e.printStackTrace();
                        mainActivityCallback.onError(e.getMessage());
                    }
                }

                @Override
                public void onWebStatusFalse(String message) {
                    mainActivityCallback.onError(message);
                }
            });
        }catch (Exception exp){
            exp.printStackTrace();
        }

    }
}
