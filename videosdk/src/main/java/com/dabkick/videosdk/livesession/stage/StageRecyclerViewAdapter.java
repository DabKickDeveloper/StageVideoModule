package com.dabkick.videosdk.livesession.stage;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabkick.videosdk.R;
import com.devbrackets.android.exomedia.listener.VideoControlsButtonListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import java.util.List;

import timber.log.Timber;


public class StageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StageView {

    private List<StageVideo> items;
    private Context context;
    private VideoControlListener videoControlListener;

    public interface VideoControlListener {
        void onPause(long milliseconds);
        void onResume(long milliseconds);
        void onSeekBarChanged(long currentTime);
    }

    public StageRecyclerViewAdapter(Activity activity) {
        this.context = activity;
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
        StageVideo stageVideo = items.get(position);

        vh.videoView.setOnPreparedListener(() -> {
            if (stageVideo.isPlaying()) vh.videoView.start();
        });

        vh.videoView.setOnSeekCompletionListener(() -> {
            videoControlListener.onSeekBarChanged(vh.videoView.getCurrentPosition());
        });

        vh.videoView.getVideoControls().setButtonListener(new VideoControlsButtonListener() {
            @Override
            public boolean onPlayPauseClicked() {
                if (vh.videoView.isPlaying()) {
                    videoControlListener.onPause(vh.videoView.getCurrentPosition());
                } else {
                    videoControlListener.onResume(vh.videoView.getCurrentPosition());
                }
                return false;
            }

            @Override
            public boolean onPreviousClicked() {
                return false;
            }

            @Override
            public boolean onNextClicked() {
                return false;
            }

            @Override
            public boolean onRewindClicked() {
                return false;
            }

            @Override
            public boolean onFastForwardClicked() {
                return false;
            }
        });

        vh.videoView.setOnCompletionListener(() -> {
            vh.videoView.restart();
            vh.videoView.seekTo(items.get(0).getPlayedMillis());
            Runnable r = () ->  {
                if (vh.videoView.isPlaying()) vh.videoView.pause();
            };
            new Handler().postDelayed(r, 1000);
        });

        vh.videoView.seekTo(stageVideo.getPlayedMillis());

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (!payloads.isEmpty()) {

            StageViewHolder vh = (StageViewHolder) holder;
            Long seekTime = getSeekTimeFromPayloads(payloads);
            Boolean shouldPause = getStateFromPayloads(payloads);

            // updated seekTime
            if (seekTime != null) {
                Timber.i("update video to time: %s", seekTime);
                vh.videoView.seekTo(seekTime);
            }

            // updated play/pause state
            if (shouldPause != null) {
                Timber.i("update video to pause: %s", shouldPause);
                if (shouldPause) vh.videoView.pause();
                else vh.videoView.start();
            }
        }
        super.onBindViewHolder(holder, position, payloads);
    }

    private Long getSeekTimeFromPayloads(List<Object> payloads) {
        for (Object o : payloads) {
            if (o instanceof Long) return (Long) o;
        }
        return null;
    }

    private Boolean getStateFromPayloads(List<Object> payloads) {
        for (Object o : payloads) {
            if (o instanceof Boolean) return (Boolean) o;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onStageDataUpdated() {
        notifyDataSetChanged();
    }

    @Override
    public void onStageVideoTimeChanged(int position, long playedMillis) {
        notifyItemChanged(position, playedMillis);
    }

    @Override
    public void onStageVideoStateChanged(int position, boolean shouldPause) {
        notifyItemChanged(position, shouldPause);
    }

    public void setItems(List<StageVideo> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setVideoControlListener(VideoControlListener videoControlListener) {
        this.videoControlListener = videoControlListener;
    }


    private class StageViewHolder extends RecyclerView.ViewHolder {

        VideoView videoView;

        StageViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.item_stage_videoview);
        }
    }

}