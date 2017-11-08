package com.dabkick.videosdkapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by iFocus on 07-11-2017.
 */

public class ListAdapter extends BaseAdapter {

    ArrayList<MainActivity.VideoDetails> data;
    Context mCtx;

    public ListAdapter(ArrayList<MainActivity.VideoDetails> data, Context ctx) {
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
    public MainActivity.VideoDetails getItem(int position) {

        return data.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View row = convertView;
        if (row == null) {

            row = LayoutInflater.from(mCtx).inflate(R.layout.list_adapter, null);

        }

        ImageView img = (ImageView) row.findViewById(R.id.thumbnail);
        TextView url = (TextView) row.findViewById(R.id.url);
        TextView title = (TextView) row.findViewById(R.id.title);
        TextView author = (TextView) row.findViewById(R.id.author);
        TextView duration = (TextView) row.findViewById(R.id.duration);

        url.setText(getItem(position).Url);

        title.setText(getItem(position).title);

        author.setText(getItem(position).author);

        duration.setText(getItem(position).duration);

        Picasso.with(mCtx)
                .load(getItem(position).thumbnailUrl)
                .into(img);

        return row;
    }
}
