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
import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

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

    public VideoManager() {
        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
        appCtx = SdkApp.getAppContext();
        items = new ArrayList<>();
    }

    public void add(StageModel stageModel) {
        VideoItem newItem = new VideoItem(stageModel);
        items.add(newItem);
    }

    public int getSize() {
        return items.size();
    }

    public VideoView getVideoViewAtIndex(int index) {
        return items.get(index).videoView;
    }

    // release all VideoViews
    public void onAdapterDetached() {
        Timber.i("adapter detached");
        Stream.of(items).forEach(item -> item.videoView.release());
    }
    
    public void clear() {
        items.clear();
    }

    // return a video's index in items from given key
    public int getIndexFromKey(String key) {
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
    public String getKeyFromIndex(int index) {
        try {
            return items.get(index).stageModel.getKey();
        } catch (IndexOutOfBoundsException e) {
            Timber.e("unable to find key for index %s", index);
            return "";
        }

    }

    public void updateStageModel(StageModel newModel) {

        for (VideoItem i : items) {
            if (i.stageModel.equals(newModel)) {
                i.stageModel = newModel;
                // TODO notify UI
                break;
            }
        }

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

            videoView.setOnPreparedListener(() -> {
                Timber.i("Prepared video: %s", stageModel.getKey());
                videoView.setOnSeekCompletionListener(null);
                // need "+ 1" to fix issue of player UI in "loading" state when actually prepared
                videoView.seekTo(stageModel.getPlayedMillis() + 1);
                if (stageModel.isPlaying()) videoView.start();

                Runnable r = () -> videoView.setOnSeekCompletionListener(() -> {
                    //videoControlListener.onSeekBarChanged(videoView.getCurrentPosition());
                });
                //new Handler().postDelayed(r, 1000);

            });

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

    }

}