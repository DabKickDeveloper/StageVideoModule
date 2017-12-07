package com.dabkick.videosdk.livesession.stage;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.ViewGroup;

import com.annimon.stream.Stream;
import com.dabkick.videosdk.SdkApp;
import com.devbrackets.android.exomedia.core.video.scale.ScaleType;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import java.util.ArrayList;

import timber.log.Timber;

// Manages all VideoViews for the stage
public class VideoManager {

    private ArrayList<VideoItem> items;
    private Context appCtx;

    public VideoManager() {
        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
        appCtx = SdkApp.getAppContext();
        items = new ArrayList<>();
    }

    public void addVideo(StageModel stageModel) {
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
        Stream.of(items).forEach(item -> item.videoView.release());
    }


    public class VideoItem {

        VideoView videoView;
        StageModel stageModel;

        VideoItem(StageModel stageModel) {
            this.stageModel = stageModel;

            videoView = new VideoView(appCtx);
            videoView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            videoView.setScaleType(ScaleType.CENTER_CROP);

            videoView.setOnErrorListener(e -> {
                Timber.e("Failed to load video %s retrying...", stageModel.getKey());
                loadVideoWithUrl();
                return true;
            });

            videoView.setOnPreparedListener(() -> {
                Timber.i("Prepared video: %s", stageModel.getKey());
                videoView.setOnSeekCompletionListener(null);
                videoView.seekTo(stageModel.getPlayedMillis());
                if (stageModel.isPlaying()) videoView.start();

                Runnable r = () -> videoView.setOnSeekCompletionListener(() -> {
                    //videoControlListener.onSeekBarChanged(videoView.getCurrentPosition());
                });
                new Handler().postDelayed(r, 1000);

            });

            
            loadVideoWithUrl();
        }
        
        @SuppressLint("StaticFieldLeak")
        private void loadVideoWithUrl() {
            videoView.setVideoPath("http://dabkick.com/Assets/Promo%20Video.mp4");
//            new YouTubeExtractor(appCtx) {
//                @Override
//                public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
//                    if (ytFiles != null) {
//                        for (int i = 0; i < ytFiles.size(); i++) {
//                            if (ytFiles.valueAt(i) != null) {
//                                String downloadUrl = ytFiles.valueAt(i).getUrl();
//                                videoView.setVideoPath(downloadUrl);
//                                break;
//                            }
//                        }
//
//                    }
//                }
//            }.extract(stageModel.getUrl(), false, false);

        }

    }

}
