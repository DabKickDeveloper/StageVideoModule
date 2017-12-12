package com.dabkick.videosdk.livesession.stage;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.dabkick.videosdk.R;
import com.dabkick.videosdk.SdkApp;
import com.dabkick.videosdk.livesession.livestream.NotifyStageItemAddedEvent;
import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.listener.OnSeekCompletionListener;
import com.devbrackets.android.exomedia.listener.VideoControlsButtonListener;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import timber.log.Timber;

// Manages all VideoViews for the stage
public class VideoManager {

    private ArrayList<VideoItem> items;
    private Context appCtx;
    private Handler handler = new Handler(Looper.getMainLooper());
    private StageDatabase.StageDatabaseCallback databaseCallback;

    public VideoManager() {
        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
        appCtx = SdkApp.getAppContext();
        items = new ArrayList<>();
    }

    public void add(StageModel stageModel) {
        VideoItem newItem = new VideoItem(stageModel);
        items.add(newItem);
        EventBus.getDefault().post(new NotifyStageItemAddedEvent(items.size() - 1));
    }

    public int getSize() {
        return items.size();
    }

    VideoView getVideoViewAtIndex(int index) {
        return items.get(index).videoView;
    }

    // release all VideoViews
    void onAdapterDetached() {
        Timber.i("adapter detached");
        Stream.of(items).forEach(item -> item.videoView.release());
    }
    
    void clear() {
        items.clear();
    }

    // return a video's index in items from given key
    int getIndexFromKey(String key) {
        for (int i = 0; i < items.size(); i++) {
            StageModel sm = items.get(i).stageModel;
            if (sm.getKey().equals(key)) {
                return i;
            }
        }
        Timber.w("unable to find index for %s", key);
        return 0;
    }

    // return a video's key in items from given index
    String getKeyFromIndex(int index) {
        try {
            return items.get(index).stageModel.getKey();
        } catch (IndexOutOfBoundsException e) {
            Timber.e("unable to find key for index %s", index);
            return "";
        }

    }

    void updateStageModel(StageModel newModel) {
        for (VideoItem i : items) {
            if (i.stageModel.equals(newModel)) {

                // update seek time
                if (i.stageModel.getPlayedMillis() != newModel.getPlayedMillis()) {
                    i.videoView.seekTo(newModel.getPlayedMillis());
                    i.stageModel.setPlayedMillis(newModel.getPlayedMillis());
                }

                // update state
                if (!i.stageModel.getState().equals(newModel.getState())) {
                    // TODO
                }

                break;
            }
        }

    }

    void setDatabaseListener(StageDatabase.StageDatabaseCallback databaseCallback) {
        this.databaseCallback = databaseCallback;
    }

    public class VideoItem {

        VideoView videoView;
        StageModel stageModel;

        VideoItem(StageModel stageModel) {
            this.stageModel = stageModel;

            videoView = (VideoView) LayoutInflater.from(appCtx).inflate(R.layout.item_videoview, null);
            videoView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            videoView.setScaleType(ScaleType.CENTER_CROP);
            videoView.setReleaseOnDetachFromWindow(false);

            videoView.setOnErrorListener(e -> {
                Timber.e("Failed to load video %s retrying...", stageModel.getKey());
                loadVideoWithUrl(handler);
                return true;
            });

            videoView.setOnSeekCompletionListener(onSeekCompletionListener);

            videoView.setOnPreparedListener(() -> {
                Timber.i("Prepared video: %s", stageModel.getKey());
                // need "+ 1" to fix issue of player UI in "loading" state when actually prepared
                seekLocal(stageModel.getPlayedMillis() + 1);
                if (stageModel.isPlaying()) videoView.start();
            });


            if (videoView.getVideoControls() != null) {
                videoView.getVideoControls().setButtonListener(new VideoControlsButtonListener() {
                    @Override
                    public boolean onPlayPauseClicked() {
                        if (videoView.isPlaying()) {
                            //videoControlListener.onPause(vh.videoView.getCurrentPosition());
                        } else {
                            //videoControlListener.onResume(vh.videoView.getCurrentPosition());
                        }
                        return false;
                    }
                    @Override public boolean onPreviousClicked() {return false;}
                    @Override public boolean onNextClicked() {return false;}
                    @Override public boolean onRewindClicked() {return false;}
                    @Override public boolean onFastForwardClicked() {return false;}
                });
            }

            loadVideoWithUrl(handler);
        }
        
        @SuppressLint("StaticFieldLeak")
        private void loadVideoWithUrl(Handler handler) {
            new YouTubeExtractor(appCtx) {
                @Override
                public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                    if (ytFiles != null) {
                        for (int i = 0; i < ytFiles.size(); i++) {
                            if (ytFiles.valueAt(i) != null) {
                                int finalI = i;
                                handler.post(() -> {
                                    String downloadUrl = ytFiles.valueAt(finalI).getUrl();
                                    videoView.setVideoPath(downloadUrl);
                                });
                                break;
                            }
                        }

                    }
                }
            }.extract(stageModel.getUrl(), false, false);

        }

        void seekLocal(long millis) {
            videoView.setOnSeekCompletionListener(null);
            videoView.seekTo(millis);
            videoView.setOnSeekCompletionListener(onSeekCompletionListener);
        }


        private OnSeekCompletionListener onSeekCompletionListener = () -> {
            stageModel.setPlayedMillis(videoView.getCurrentPosition());
            databaseCallback.onStageVideoTimeChanged(stageModel.getKey(), stageModel.getPlayedMillis());
        };

    }

}