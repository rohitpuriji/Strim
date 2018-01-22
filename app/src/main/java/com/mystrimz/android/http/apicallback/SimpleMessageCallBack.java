package com.mystrimz.android.http.apicallback;

/**
 * Created by manish on 8/5/17.
 */

public interface SimpleMessageCallBack {
    void onSuccess(String message);
    void onError(String error);
}
