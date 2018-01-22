package com.mystrimz.android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mystrimz.android.R;
import com.mystrimz.android.bean.TrackList;
import com.mystrimz.android.ui.activity.AddNewPlaylistActivity;

import java.util.List;

/**
 * Created by manishjoshi on 10/1/18.
 */

public class AddNewPlaylistAdapter extends RecyclerView.Adapter<AddNewPlaylistAdapter.MyviewHolde>{
    private Context mContext;
    private LayoutInflater inflater;
    private List<TrackList> mList;
    public AddNewPlaylistAdapter(Context context, List<TrackList> youTubeList) {
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.mList = youTubeList;
    }

    @Override
    public MyviewHolde onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.add_playlist_view, parent, false);
        return new MyviewHolde(view);
    }

    @Override
    public void onBindViewHolder(final MyviewHolde holder, final int position) {

        holder.songTitle.setText(mList.get(position).getTitle());
        if (mList.get(position).getHoster().equals("youtube")){
            holder.songType.setImageResource(R.drawable.utube);
        } else {
            holder.songType.setImageResource(R.drawable.cloud);
        }
        if (mList.get(position).getSelected()) {
            holder.addList.setImageResource(R.drawable.tick_circle);
        }else {
            holder.addList.setImageResource(R.drawable.add_btn);
        }
        holder.addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mList.get(position).getSelected()) {
                    mList.get(position).setSelected(true);
                    holder.addList.setImageResource(R.drawable.tick_circle);
                }else {
                    mList.get(position).setSelected(false);
                    holder.addList.setImageResource(R.drawable.add_btn);
                }

                if (mContext instanceof AddNewPlaylistActivity) {
                    ((AddNewPlaylistActivity) mContext).saveList(position, mList.get(position).getSelected());
                }
            }
        });


        holder.songType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContext instanceof AddNewPlaylistActivity) {
                    ((AddNewPlaylistActivity) mContext).playYoutube(mList.get(position).getHosterTrackId(), mList.get(position).getTitle(),
                            position, mList.get(position).getHoster());
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void swapeItem(List<TrackList> youtube) {
        mList = youtube;
        notifyDataSetChanged();
    }

    public class MyviewHolde extends RecyclerView.ViewHolder {

        private ImageView addList;
        private TextView songTitle;
        private ImageView songType;
        public MyviewHolde(View itemView) {
            super(itemView);
            addList = (ImageView)itemView.findViewById(R.id.add_list);
            songTitle = (TextView)itemView.findViewById(R.id.songs_name);
            songType = (ImageView)itemView.findViewById(R.id.type_song);
        }

    }
}
