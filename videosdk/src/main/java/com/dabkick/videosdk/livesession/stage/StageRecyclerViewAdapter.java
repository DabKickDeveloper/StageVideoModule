package com.dabkick.videosdk.livesession.stage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.dabkick.videosdk.R;

import java.util.List;


public class StageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StageVideo> items;

    public StageRecyclerViewAdapter(List<StageVideo> items) {
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stage, parent, false);
        return new StageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StageViewHolder vh = (StageViewHolder) holder;
        vh.videoView.setVideoPath(items.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private class StageViewHolder extends RecyclerView.ViewHolder {

        VideoView videoView;

        StageViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.item_stage_videoview);
        }
    }

}