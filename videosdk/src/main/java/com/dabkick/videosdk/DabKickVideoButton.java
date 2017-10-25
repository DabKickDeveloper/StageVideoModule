package com.dabkick.videosdk;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import timber.log.Timber;

/**
 * The external view for usage in partner applications
 */
public class DabKickVideoButton extends LinearLayout {

    public DabKickVideoButton(Context context) {
        super(context);
        init();
    }

    public DabKickVideoButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DabKickVideoButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        LayoutInflater.from(getContext()).inflate(R.layout.layout_video_button, this, true);
        Log.d("Trevor", "clicked");
        setOnClickListener(view -> {
            Timber.d("clicked");
            Util.register();
            // TODO flow for login - pass off this functionality to another class using MVC
        });
    }


}