package com.dabkick.videosdk.livesession.stage;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;

import com.dabkick.videosdk.R;

import java.util.List;

import timber.log.Timber;


public class StageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<StageVideo> items;
    private Context context;
    private ObservableVideoView.VideoControlListener videoControlListener;

    public StageRecyclerViewAdapter(Activity activity, List<StageVideo> items,
                                    ObservableVideoView.VideoControlListener videoControlListener) {
        this.items = items;
        this.context = activity;
        this.videoControlListener = videoControlListener;
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
        vh.videoView.setMediaController(vh.mediaController);
        vh.mediaController.setAnchorView(vh.videoView);
        vh.videoView.setVideoControlsListener(videoControlListener);

        StageVideo stageVideo = items.get(position);
        vh.videoView.actualSeekTo(stageVideo.getPlayedMillis());
        if (stageVideo.isPlaying()) {
            vh.videoView.start();
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (!payloads.isEmpty()) {
            if (payloads.get(0) instanceof Integer) {
                StageViewHolder vh = (StageViewHolder) holder;
                int seekTime = (int) payloads.get(0);
                Timber.d("update video to time: %s", seekTime);
                vh.videoView.actualSeekTo(seekTime);
            }
        }
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    private class StageViewHolder extends RecyclerView.ViewHolder {

        ObservableVideoView videoView;
        MediaController mediaController;

        StageViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.item_stage_videoview);
            mediaController = new MediaController(context);
        }
    }

}