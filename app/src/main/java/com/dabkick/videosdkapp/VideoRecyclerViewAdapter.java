package com.dabkick.videosdkapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;


class VideoRecyclerViewAdapter extends RecyclerView.Adapter<VideoRecyclerViewAdapter.VideoHolder> {


    @Override
    public VideoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(VideoHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class VideoHolder extends RecyclerView.ViewHolder {

        public VideoHolder(View itemView) {
            super(itemView);
        }
    }
}
