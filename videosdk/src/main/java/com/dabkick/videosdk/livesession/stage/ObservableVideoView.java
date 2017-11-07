package com.dabkick.videosdk.livesession.stage;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

import java.util.concurrent.TimeUnit;

/**
 * VideoView with callbacks when pause, resume, and seek bar are changed
 */
public class ObservableVideoView extends VideoView {

    private VideoControlListener videoControlListener;
    private boolean mIsOnPauseMode = false;

    public interface VideoControlListener {
        void onPause(int currentTime);
        void onResume();
        void onSeekBarChanged(int currentTime);
    }

    public void setVideoControlsListener(VideoControlListener listener) {
        videoControlListener = listener;
    }

    @Override
    public void pause() {
        super.pause();

        if (videoControlListener != null) {
            int secs = (int) TimeUnit.SECONDS.convert(getCurrentPosition(), TimeUnit.MILLISECONDS);
            videoControlListener.onPause(secs);
        }

        mIsOnPauseMode = true;
    }

    @Override
    public void start() {
        super.start();

        if (mIsOnPauseMode) {
            if (videoControlListener != null) {
                videoControlListener.onResume();
            }

            mIsOnPauseMode = false;
        }
    }

    @Override
    public void seekTo(int msec) {
        super.seekTo(msec);

        if (videoControlListener != null) {
            videoControlListener.onSeekBarChanged((int) TimeUnit.SECONDS.convert(msec, TimeUnit.MILLISECONDS));
        }
    }

    public ObservableVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableVideoView(Context context) {
        super(context);
    }

    public ObservableVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}