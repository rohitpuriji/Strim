package com.mystrimz.android.http.requests;

import android.util.Log;

import com.google.gson.Gson;
import com.mystrimz.android.bean.ListBean;
import com.mystrimz.android.bean.Model;
import com.mystrimz.android.bean.MyPlaylistBean;
import com.mystrimz.android.bean.RegisterBean;
import com.mystrimz.android.bean.TrackList;
import com.mystrimz.android.http.ApiClient;
import com.mystrimz.android.http.ConnectionManager;
import com.mystrimz.android.http.apicallback.MyPlaylistCallBack;
import com.mystrimz.android.http.apicallback.SimpleMessageCallBack;
import com.mystrimz.android.http.apicallback.SongsListCallBack;
import com.mystrimz.android.http.apicallback.UserCallBack;
import com.mystrimz.android.http.apis.RegisterUserApi;
import com.mystrimz.android.http.listeners.OnWebServiceCompleteListener;
import com.mystrimz.android.util.Constants;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by manishjoshi on 11/1/18.
 */

public class UserRequest {
    private static final UserRequest ourInstance = new UserRequest();

    public static UserRequest getInstance() {
        return ourInstance;
    }

    private UserRequest() {
    }

    public void callLoginApi(String email, String password, String devicetype, String devicetoken, final UserCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        Call<ResponseBody> responseBodyCall = registerUserApi.loginUser(email, password, devicetype, devicetoken);
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody baseObject) {

                try {
                    JSONObject jsonObject = new JSONObject(baseObject.string());

                    if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                        Gson gson = new Gson();
                        RegisterBean registerBean = gson.fromJson(jsonObject.getJSONObject(Constants.KEY_DATA).toString(), RegisterBean.class);
                        registerBean.setMessage(jsonObject.getString(Constants.KEY_MESSAGE));
                        mainActivityCallback.onSuccess(registerBean);
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
    }

    public void callRegisterApi(String firstName,
                                String lastName,
                                String email,
                                String password,
                                String devicetype,
                                String devicetoken,
                                String registration_type, final UserCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        Call<ResponseBody> responseBodyCall =
                registerUserApi.registerUser(firstName, lastName, email, password, devicetype, devicetoken, registration_type);
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody baseObject) {

                try {
                    JSONObject jsonObject = new JSONObject(baseObject.string());

                    if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                        Gson gson = new Gson();
                        RegisterBean registerBean = gson.fromJson(jsonObject.getJSONObject(Constants.KEY_DATA).toString(), RegisterBean.class);
                        registerBean.setMessage(jsonObject.getString(Constants.KEY_MESSAGE));
                        mainActivityCallback.onSuccess(registerBean);
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
    }

    public void callForgotPassApi(String email, final SimpleMessageCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        Call<ResponseBody> responseBodyCall =
                registerUserApi.forgotPass(email);
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody baseObject) {

                try {
                    JSONObject jsonObject = new JSONObject(baseObject.string());

                    if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                        mainActivityCallback.onSuccess(jsonObject.getString(Constants.KEY_MESSAGE));
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

    }

    public void callSocialApi(String firstName,
                              String lastName,
                              String devicetype,
                              String devicetoken,
                              String resigtrationtype,
                              String id,
                              String token,
                              String email,
                              String image_path, final UserCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        Call<ResponseBody> responseBodyCall =
                registerUserApi.socialRegister(firstName, lastName, devicetype, devicetoken, resigtrationtype,
                        id, token, email, image_path);
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody baseObject) {

                try {
                    JSONObject jsonObject = new JSONObject(baseObject.string());

                    if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                        Gson gson = new Gson();
                        RegisterBean registerBean = gson.fromJson(jsonObject.getJSONObject(Constants.KEY_DATA).toString(), RegisterBean.class);
                        registerBean.setMessage(jsonObject.getString(Constants.KEY_MESSAGE));
                        mainActivityCallback.onSuccess(registerBean);
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
    }

    public void callYoutubesearch(String title, String pagination, String isplaylist, final SongsListCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        Call<ResponseBody> responseBodyCall =
                registerUserApi.searchYouTube(title, pagination, isplaylist);
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody baseObject) {

                try {
                    JSONObject jsonObject = new JSONObject(baseObject.string());

                    if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                        Gson gson = new Gson();
                        ListBean listBean = gson.fromJson(jsonObject.toString(), ListBean.class);
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
    }

    public void callSoundClouldsearch(String title, String pagination, String isplaylist, final SongsListCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        Call<ResponseBody> responseBodyCall =
                registerUserApi.searchSoundClould(title, pagination, isplaylist);
        ConnectionManager connectionManager = ConnectionManager.getInstance();
        connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
            @Override
            public void onWebServiceComplete(ResponseBody baseObject) {

                try {
                    JSONObject jsonObject = new JSONObject(baseObject.string());

                    if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                        Gson gson = new Gson();
                        ListBean listBean = gson.fromJson(jsonObject.toString(), ListBean.class);
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
    }

    public void callAddPlaylist(Model model, final SimpleMessageCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        try{
            Call<ResponseBody> responseBodyCall = registerUserApi.addPlayList(model);
            ConnectionManager connectionManager = ConnectionManager.getInstance();
            connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
                @Override
                public void onWebServiceComplete(ResponseBody baseObject) {

                    try {
                        JSONObject jsonObject = new JSONObject(baseObject.string());

                        if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                            mainActivityCallback.onSuccess(jsonObject.getString(Constants.KEY_MESSAGE));
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


    public void callDeleteList(String access_token, String id, final SimpleMessageCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        try{
            Call<ResponseBody> responseBodyCall = registerUserApi.deleteList(access_token, id);
            ConnectionManager connectionManager = ConnectionManager.getInstance();
            connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
                @Override
                public void onWebServiceComplete(ResponseBody baseObject) {

                    try {
                        JSONObject jsonObject = new JSONObject(baseObject.string());

                        if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                            mainActivityCallback.onSuccess(jsonObject.getString(Constants.KEY_MESSAGE));
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

    public void callLikeDisLike(String access_token, String id, final SimpleMessageCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        try{
            Call<ResponseBody> responseBodyCall = registerUserApi.callLike(access_token, id);
            ConnectionManager connectionManager = ConnectionManager.getInstance();
            connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
                @Override
                public void onWebServiceComplete(ResponseBody baseObject) {

                    try {
                        JSONObject jsonObject = new JSONObject(baseObject.string());

                        if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                            mainActivityCallback.onSuccess(jsonObject.getString(Constants.KEY_MESSAGE));
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

    public void callShareContent(String access_token, String id, String content, final SimpleMessageCallBack mainActivityCallback) {
        ApiClient apiClient = ApiClient.getInstance();

        RegisterUserApi registerUserApi = apiClient.createService(RegisterUserApi.class);

        try{
            Call<ResponseBody> responseBodyCall = registerUserApi.callContent(access_token, id, content);
            ConnectionManager connectionManager = ConnectionManager.getInstance();
            connectionManager.initiateCall(responseBodyCall, new OnWebServiceCompleteListener() {
                @Override
                public void onWebServiceComplete(ResponseBody baseObject) {

                    try {
                        JSONObject jsonObject = new JSONObject(baseObject.string());

                        if (jsonObject.getString(Constants.KEY_STATUS).equals(Constants.STATUS_SUCCESS)) {

                            mainActivityCallback.onSuccess(jsonObject.getString(Constants.KEY_MESSAGE));
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
