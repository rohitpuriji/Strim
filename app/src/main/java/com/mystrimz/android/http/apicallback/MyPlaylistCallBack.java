package com.mystrimz.android.http.apicallback;

import com.mystrimz.android.bean.Datum;
import com.mystrimz.android.bean.YoutubeData;

import java.util.List;

/**
 * Created by manishjoshi on 18/1/18.
 */

public interface MyPlaylistCallBack {
    void onSuccess(List<Datum> data);
    void onError(String error);
}
