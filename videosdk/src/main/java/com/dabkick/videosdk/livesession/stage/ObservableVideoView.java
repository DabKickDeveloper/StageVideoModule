package com.dabkick.videosdk.livesession.stage;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.VideoView;

import timber.log.Timber;

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
            videoControlListener.onPause(getCurrentPosition());
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

    // seeks the app, where overridden seek() only notifies the listener
    public void actualSeekTo(int msec) {
        Timber.d("actualSeekTo: %s", msec);
        setOnPreparedListener(mp -> {
            mp.seekTo(msec);
        });
    }

    @Override
    public void seekTo(int msec) {
        Timber.d("seekTo: %s", msec);
        if (videoControlListener != null) {
            videoControlListener.onSeekBarChanged(msec);
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