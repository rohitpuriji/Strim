package com.mystrimz.android.http.apicallback;

import com.mystrimz.android.bean.TrackList;
import com.mystrimz.android.bean.YoutubeData;

import java.util.List;

/**
 * Created by manishjoshi on 16/1/18.
 */

public interface SongsListCallBack {
    void onSuccess(YoutubeData youtubeData);
    void onError(String error);
}
