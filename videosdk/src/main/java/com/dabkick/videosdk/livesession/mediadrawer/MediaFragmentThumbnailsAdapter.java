package com.dabkick.videosdk.livesession.mediadrawer;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dabkick.videosdk.DabKickVideoInfo;
import com.dabkick.videosdk.R;

import java.util.List;

/**
 * Created by iFocus on 22-11-2017.
 */

public class MediaFragmentThumbnailsAdapter extends BaseAdapter {

    List<DabKickVideoInfo> data;
    Context mCurrentActivity;

    public MediaFragmentThumbnailsAdapter(List<DabKickVideoInfo> data, Context mCurrentActivity) {
        this.data = data;
        this.mCurrentActivity = mCurrentActivity;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        if (row == null) {

            row = LayoutInflater.from(mCurrentActivity).inflate(R.layout.video_thumbnails, null);

        } else {

            row = view;
        }

        ImageView thumbnail = row.findViewById(R.id.first_img);
        TextView title = row.findViewById(R.id.title);
        TextView length = row.findViewById(R.id.length);
        TextView author = row.findViewById(R.id.author);

        String videoThumbnailUrl = data.get(i).getThumbnailUrl();
        title.setText(data.get(i).getTitle());
        length.setText(data.get(i).getDuration());
        author.setText(data.get(i).getAuthorName());

        Glide.with(mCurrentActivity)
                .load(videoThumbnailUrl) // or URI/path
                .into(thumbnail);

        return row;

    }
}
