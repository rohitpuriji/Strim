package com.mystrimz.android.http.apicallback;

import com.mystrimz.android.bean.RegisterBean;

/**
 * Created by manishjoshi on 11/1/18.
 */

public interface UserCallBack {
    void onSuccess(RegisterBean isSuccess);
    void onError(String error);
}
