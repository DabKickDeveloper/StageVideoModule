package com.dabkick.videosdk.developerbutton;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.dabkick.videosdk.DabKickSession;
import com.dabkick.videosdk.R;
import com.dabkick.videosdk.livesession.LiveSessionActivity;

/**
 * The external view for usage in partner applications
 */
public class DabKickVideoButton extends LinearLayout {

    private DabKickSession dabKickSession;

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
        setOnClickListener(view -> startLiveSessionActivity());
    }

    public void setDabKickSession(final DabKickSession dabKickSession) {
        this.dabKickSession = dabKickSession;
    }

    private void startLiveSessionActivity() {
        Intent intent = new Intent(getContext(), LiveSessionActivity.class);
        getContext().startActivity(intent);
    }


}