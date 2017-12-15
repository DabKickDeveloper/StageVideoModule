package com.dabkick.videosdk.livesession.stage;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
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
        Stream.of(items).forEach(VideoItem::pauseLocal);
        items.clear();
    }

    // return a video's index in items from given key
    Integer getIndexFromKey(String key) {
        for (int i = 0; i < items.size(); i++) {
            StageModel sm = items.get(i).stageModel;
            if (sm.getKey().equals(key)) {
                return i;
            }
        }
        Timber.d("unable to find index for %s", key);
        return null;
    }

    // return a video's key in items from given index
    String getKeyFromIndex(int index) {
        try {
            return items.get(index).stageModel.getKey();
        } catch (IndexOutOfBoundsException e) {
            Timber.d("unable to find key for index %s", index);
            return "";
        }

    }

    void updateStageModel(StageModel newModel) {
        for (VideoItem i : items) {
            if (i.stageModel.equals(newModel)) {

                // update seek time
                if (i.stageModel.getPlayedMillis() != newModel.getPlayedMillis()) {
                    i.seekLocal(newModel.getPlayedMillis());
                    i.stageModel.setPlayedMillis(newModel.getPlayedMillis());
                }

                // update state
                if (!i.stageModel.getState().equals(newModel.getState())) {
                    if (newModel.getState().equals(StageModel.PLAYING)) {
                        i.videoView.start();
                    } else {
                        i.videoView.pause();
                    }
                    i.stageModel.setState(newModel.getState());
                }

                break;
            }
        }

    }

    void setDatabaseListener(StageDatabase.StageDatabaseCallback databaseCallback) {
        this.databaseCallback = databaseCallback;
    }

    // forces a video to seek so it ends in a prepared state
    // fixes issue where a scrolled to video will play for several seconds without new video frames
    public void prepare(int newIndex) {
        if (newIndex >= items.size()) {
            // ignore calls to this method before we have a video
            return;
        }
        Timber.d("prepare: %s", newIndex);
        long millis = items.get(newIndex).stageModel.getPlayedMillis();
        items.get(newIndex).seekLocal(millis);
    }

    // locally pauses all videos except newKey. newKey changed to reflect state
    public void setPlayPausedStates(String newKey) {
        Stream.of(items).forEach(item -> {
            // pauses all items except newKey
            if (!item.stageModel.getKey().equals(newKey)) {
                item.pauseLocal();
            // play newKey if in 'playing' state
            } else {
                item.seekLocal(item.stageModel.getPlayedMillis());
                if (item.stageModel.isPlaying()) item.videoView.start();
            }
        });

    }

    public class VideoItem {

        com.devbrackets.android.exomedia.ui.widget.VideoView videoView;
        StageModel stageModel;

        VideoItem(StageModel stageModel) {
            this.stageModel = stageModel;

            videoView = (com.devbrackets.android.exomedia.ui.widget.VideoView) LayoutInflater.from(appCtx).inflate(R.layout.item_videoview, null);
            videoView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            videoView.setScaleType(ScaleType.CENTER_CROP);
            videoView.setReleaseOnDetachFromWindow(false);

            // continuously retry on any video error
            videoView.setOnErrorListener(e -> {
                Timber.e("Failed to load video %s retrying...", stageModel.getKey());
                loadVideoWithUrl(handler);
                return true;
            });

            // when prepared, seek to position then play if in playing mode
            videoView.setOnPreparedListener(() -> {
                Timber.i("Prepared video: %s", stageModel.getKey());
                seekLocal(stageModel.getPlayedMillis());
                if (stageModel.isPlaying()) videoView.start();
            });

            // on video complete, reset view state and update db
            videoView.setOnCompletionListener(() -> {
                videoView.reset();
                loadVideoWithUrl(handler);
                databaseCallback.onVideoComplete(stageModel.getKey());
            });

            // updates db when View is scrolled off screen
            videoView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override public void onViewAttachedToWindow(View v) {}
                @Override public void onViewDetachedFromWindow(View v) {
                    databaseCallback.onStageVideoTimeChanged(stageModel.getKey(), videoView.getCurrentPosition());
                }
            });

            setVideoControListener();
            loadVideoWithUrl(handler);
        }

        private void setVideoControListener() {
            if (videoView.getVideoControls() != null) {
                videoView.getVideoControls().setButtonListener(new VideoControlsButtonListener() {
                    @Override
                    public boolean onPlayPauseClicked() {
                        String newState = (videoView.isPlaying()) ? StageModel.PAUSED : StageModel.PLAYING;
                        databaseCallback.onStageVideoStateChanged(
                                stageModel.getKey(), newState, videoView.getCurrentPosition());
                        return false;
                    }
                    @Override public boolean onPreviousClicked() {return false;}
                    @Override public boolean onNextClicked() {return false;}
                    @Override public boolean onRewindClicked() {return false;}
                    @Override public boolean onFastForwardClicked() {return false;
                    }
                });
            }
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

        // seeks and does not respond to seek completed callback
        void seekLocal(long millis) {
            // need "+ 1" to fix issue of player UI in "loading" state when actually prepared
            millis++; //
            videoView.setOnSeekCompletionListener(null);
            videoView.seekTo(millis);
            handler.postDelayed(() -> videoView.setOnSeekCompletionListener(onSeekCompletionListener), 1000);
        }

        // seeks and does not respond to seek completed callback
        void pauseLocal() {
            if (videoView.getVideoControls() != null) {
                videoView.getVideoControls().setButtonListener(null);
            }
            videoView.pause();
            handler.postDelayed(this::setVideoControListener, 1000);
        }

        private OnSeekCompletionListener onSeekCompletionListener = () -> {
            stageModel.setPlayedMillis(videoView.getCurrentPosition());
            databaseCallback.onStageVideoTimeChanged(stageModel.getKey(), stageModel.getPlayedMillis());
        };

    }

}