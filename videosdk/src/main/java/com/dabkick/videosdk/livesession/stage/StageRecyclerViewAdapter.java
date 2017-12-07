package com.dabkick.videosdk.livesession.stage;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dabkick.videosdk.R;
import com.dabkick.videosdk.SdkApp;
import com.dabkick.videosdk.livesession.LiveSessionActivity;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import java.util.List;

import javax.inject.Inject;


public class StageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StageView {

    private LiveSessionActivity context;
    private VideoControlListener videoControlListener;

    @Inject VideoManager videoManager;

    public interface VideoControlListener {
        void onPause(long milliseconds);
        void onResume(long milliseconds);
        void onSeekBarChanged(long currentTime);
    }

    public StageRecyclerViewAdapter(LiveSessionActivity activity) {
        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
        this.context = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stage, parent, false);
        return new StageViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        StageViewHolder vh = (StageViewHolder) holder;

        if (vh.layout.getChildCount() == 1) {
            vh.layout.removeViewAt(0);
        }

        VideoView toAddView = videoManager.getVideoViewAtIndex(position);
        vh.layout.addView(toAddView);


//        vh.layout.setOnTouchListener((v, event) -> {
//            if (!context.isVideoInMainStage()) {
//                EventBus.getDefault().post(new SwapStageEvent());
//                return true;
//            }
//            return false;
//        });
//
//        if (vh.videoView.getVideoControls() != null) {
//            vh.videoView.getVideoControls().setButtonListener(new VideoControlsButtonListener() {
//                @Override
//                public boolean onPlayPauseClicked() {
//                    if (vh.videoView.isPlaying()) {
//                        videoControlListener.onPause(vh.videoView.getCurrentPosition());
//                    } else {
//                        videoControlListener.onResume(vh.videoView.getCurrentPosition());
//                    }
//                    return false;
//                }
//                @Override public boolean onPreviousClicked() {
//                    return false;
//                }
//                @Override public boolean onNextClicked() {
//                    return false;
//                }
//                @Override public boolean onRewindClicked() {
//                    return false;
//                }
//                @Override public boolean onFastForwardClicked() {
//                    return false;
//                }
//            });
//        }


    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        videoManager.onAdapterDetached();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        /* if (!payloads.isEmpty()) {

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
        }*/
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
//        test only - to stop spinning forever if video loading is slow return zero
//        return 0;
        return videoManager.getSize();
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

    public void setVideoControlListener(VideoControlListener videoControlListener) {
        this.videoControlListener = videoControlListener;
    }

    private class StageViewHolder extends RecyclerView.ViewHolder {

        FrameLayout layout;

        StageViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.item_stage_videoview);
        }
    }

}