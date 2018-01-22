package com.mystrimz.android.http.listeners;

/**
 * Created by manishjoshi on 15/1/18.
 */

public interface MethordCallBack {
    public void saveList(int position, Boolean selected);
    public void openPlayer();
    public void playYoutube(String youTubeId, String title, int position, String hoster);
}
