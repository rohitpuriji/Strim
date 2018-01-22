package com.mystrimz.android.http.listeners;

import okhttp3.ResponseBody;

/**
 * Created by tarun on 23/12/16.
 */

public interface OnWebServiceCompleteListener {

    void onWebServiceComplete(ResponseBody baseObject);
    // this method will fired when service status is false
    void onWebStatusFalse(String message);


}
