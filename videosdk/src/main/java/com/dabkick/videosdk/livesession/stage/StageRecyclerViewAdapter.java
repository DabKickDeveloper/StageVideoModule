package com.dabkick.videosdk.livesession.stage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabkick.videosdk.R;
import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.VideoControlsButtonListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import timber.log.Timber;


public class StageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StageView {

    private List<StageModel> items;
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

        StageModel stageModel = items.get(position);

        StageViewHolder vh = (StageViewHolder) holder;
        vh.videoView.setScaleType(ScaleType.CENTER_CROP);
        vh.videoView.setReleaseOnDetachFromWindow(false);

        vh.videoView.setOnErrorListener(e -> {
            Timber.e("Issue with VideoView at position %s, type %s", position, e.getClass());
            Timber.e(e);
            loadVideoWithUrl(stageModel.getUrl(), vh.videoView);
            return false;
        });

        loadVideoWithUrl(stageModel.getUrl(), vh.videoView);


        vh.videoView.setOnPreparedListener(() -> {
            vh.videoView.setOnSeekCompletionListener(null);
            vh.videoView.seekTo(stageModel.getPlayedMillis());
            if (stageModel.isPlaying()) vh.videoView.start();

            Runnable r = () -> vh.videoView.setOnSeekCompletionListener(() -> {
                videoControlListener.onSeekBarChanged(vh.videoView.getCurrentPosition());
            });
            new Handler().postDelayed(r, 1000);

        });

        if (vh.videoView.getVideoControls() != null) {
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
                @Override public boolean onPreviousClicked() {
                    return false;
                }
                @Override public boolean onNextClicked() {
                    return false;
                }
                @Override public boolean onRewindClicked() {
                    return false;
                }
                @Override public boolean onFastForwardClicked() {
                    return false;
                }
            });
        }

        /* vh.videoView.setOnCompletionListener(() -> {
            vh.videoView.restart();
            vh.videoView.seekTo(stageModel.getPlayedMillis());
            Runnable r = () ->  {
                if (vh.videoView.isPlaying()) vh.videoView.pause();
            };
            new Handler().postDelayed(r, 1000);
        }); */

    }

    @SuppressLint("StaticFieldLeak")
    private void loadVideoWithUrl(String url, VideoView videoView) {
        new YouTubeExtractor(context) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    for (int i = 0; i < ytFiles.size(); i++) {
                        if (ytFiles.valueAt(i) != null) {
                            String downloadUrl = ytFiles.valueAt(i).getUrl();
                            videoView.setVideoPath(downloadUrl);
                            break;
                        }
                    }

                }
            }
        }.extract(url, true, true);

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

    public void setItems(List<StageModel> items) {
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