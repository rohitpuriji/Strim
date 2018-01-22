package com.mystrimz.android.adapter;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mystrimz.android.R;
import com.mystrimz.android.bean.TrackList;
import com.mystrimz.android.ui.activity.NewPlayListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by manishjoshi on 9/1/18.
 */

public class NewPlayListAdapter extends RecyclerView.Adapter<NewPlayListAdapter.MyViewholder> {
    private Context mContext;
    private LayoutInflater inflater;
    private List<TrackList> youTubeList;
    public NewPlayListAdapter(Context context, List<TrackList> listItems) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.youTubeList = listItems;
    }

    @Override
    public MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.new_playlist_view, parent, false);
        return new MyViewholder(view);
    }

    @Override
    public void onBindViewHolder(MyViewholder holder, int position) {

        holder.songTitle.setText(youTubeList.get(position).getTitle());
        if (youTubeList.get(position).getHoster().equals("youtube")){
            holder.song_type.setImageResource(R.drawable.utube);
        } else {
            holder.song_type.setImageResource(R.drawable.cloud);
        }
    }

    @Override
    public int getItemCount() {
        return youTubeList.size();
    }

    public void swapeItem(List<TrackList> youtube) {
        youTubeList = youtube;
        notifyDataSetChanged();
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        private TextView songTitle;
        private ImageView song_menu;
        private ImageView song_type;
        public MyViewholder(View itemView) {
            super(itemView);
            songTitle = (TextView)itemView.findViewById(R.id.songs_name);
            song_menu = (ImageView)itemView.findViewById(R.id.sideMenu);
            song_type = (ImageView)itemView.findViewById(R.id.song_type);
        }
    }
}
