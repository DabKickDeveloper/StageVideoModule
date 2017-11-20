package com.dabkick.videosdk.livesession.chat;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dabkick.videosdk.R;

public class EmojiLayout extends LinearLayout {

    EmojiClickCallback listener;
    RelativeLayout innerContainer;
    ConstraintLayout container;
    Context mContext;

    public EmojiLayout(Context context) {
        super(context);
        this.mContext = context;
        init();
    }

    public EmojiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    public EmojiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.emoji_layout, this);
        initOnClickListeners();
    }

    public void setInnerContainer(RelativeLayout innerContainer) {
        this.innerContainer = innerContainer;
    }

    public ConstraintLayout getContainer() {
        return container;
    }

    public void setContainer(ConstraintLayout container) {
        this.container = container;
    }

    public void setListener(EmojiClickCallback listener) {
        this.listener = listener;
    }

    private void initOnClickListeners(){

        findViewById(R.id.emoji_icon1).setOnClickListener(view -> {
            AnimationUtils.slideToAbove(getResources().getDrawable(R.drawable.reactions_default), innerContainer, container,
                    mContext);
            if(listener != null) listener.onSmile();
        });

        findViewById(R.id.emoji_icon2).setOnClickListener(view -> {

            AnimationUtils.slideToAbove(getResources().getDrawable(R.drawable.cool), innerContainer, container,
                    mContext);
            if(listener != null) listener.onCool();
        });

        findViewById(R.id.emoji_icon3).setOnClickListener(view -> {

            AnimationUtils.slideToAbove(getResources().getDrawable(R.drawable.winky), innerContainer, container,
                    mContext);
            if(listener != null) listener.onWink();
        });

        findViewById(R.id.emoji_icon4).setOnClickListener(view -> {

            AnimationUtils.slideToAbove(getResources().getDrawable(R.drawable.love), innerContainer, container,
                    mContext);
            if(listener != null) listener.onLove();
        });

        findViewById(R.id.emoji_icon5).setOnClickListener(view -> {

            AnimationUtils.slideToAbove(getResources().getDrawable(R.drawable.tongue), innerContainer, container,
                    mContext);
            if(listener != null) listener.onTongue();

        });

        findViewById(R.id.emoji_icon6).setOnClickListener(view -> {

            AnimationUtils.slideToAbove(getResources().getDrawable(R.drawable.rofl), innerContainer, container,
                    mContext);
            if(listener != null)
                listener.onRofl();

        });

        findViewById(R.id.emoji_icon7).setOnClickListener(view -> {

            AnimationUtils.slideToAbove(getResources().getDrawable(R.drawable.crying), innerContainer, container,
                    mContext);
            if(listener != null) listener.onCrying();

        });

        findViewById(R.id.emoji_icon8).setOnClickListener(view -> {

            AnimationUtils.slideToAbove(getResources().getDrawable(R.drawable.angry), innerContainer, container,
                    mContext);
            if(listener != null) listener.onAngry();

        });

        findViewById(R.id.emoji_icon9).setOnClickListener(view -> {

            AnimationUtils.slideToAbove(getResources().getDrawable(R.drawable.x_eyes), innerContainer, container,
                    mContext);
            if(listener != null) listener.onXeyes();

        });

        findViewById(R.id.emoji_icon10).setOnClickListener(view -> {

            AnimationUtils.slideToAbove(getResources().getDrawable(R.drawable.shocked), innerContainer, container,
                    mContext);
            if(listener != null) listener.onShocked();

        });

    }

    public interface EmojiClickCallback{
        void onSmile();
        void onCool();
        void onWink();
        void onLove();
        void onTongue();
        void onRofl();
        void onCrying();
        void onAngry();
        void onXeyes();
        void onShocked();
    }
}
