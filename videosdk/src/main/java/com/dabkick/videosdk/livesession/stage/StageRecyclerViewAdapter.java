package com.dabkick.videosdk.livesession.stage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.dabkick.videosdk.R;


public class StageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String[] urls;

    public StageRecyclerViewAdapter() {
        urls = new String[]{};
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stage, parent, false);
        return new StageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StageViewHolder vh = (StageViewHolder) holder;
        vh.videoView.setVideoPath(urls[position]);
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return urls.length;
    }

    private class StageViewHolder extends RecyclerView.ViewHolder {

        VideoView videoView;

        StageViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.item_stage_videoview);
        }
    }

}