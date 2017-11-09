package com.dabkick.videosdkapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dabkick.videosdk.DabKickVideoInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    ArrayList<DabKickVideoInfo> data;
    Context mCtx;

    public ListAdapter(ArrayList<DabKickVideoInfo> data, Context ctx) {
        this.data = data;
        mCtx = ctx;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DabKickVideoInfo getItem(int position) {

        return data.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if (row == null) {

            row = LayoutInflater.from(mCtx).inflate(R.layout.list_adapter, null);

        }

        ImageView img = row.findViewById(R.id.thumbnail);
        TextView url = row.findViewById(R.id.url);
        TextView title = row.findViewById(R.id.title);
        TextView author = row.findViewById(R.id.author);
        TextView duration = row.findViewById(R.id.duration);

        url.setText(getItem(position).getVideoUrl());

        title.setText(getItem(position).getTitle());

        author.setText(getItem(position).getAuthorName());

        duration.setText(getItem(position).getDuration());

        Picasso.with(mCtx)
                .load(getItem(position).getThumbnailUrl())
                .into(img);

        return row;
    }
}
