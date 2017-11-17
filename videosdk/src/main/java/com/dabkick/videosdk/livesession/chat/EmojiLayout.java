package com.dabkick.videosdk.livesession.chat;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dabkick.videosdk.R;

public class EmojiLayout extends LinearLayout {

    EmojiClickCallback emojiClickCallbackListener;
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
    }

    public RelativeLayout getInnerContainer() {
        return innerContainer;
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

    public EmojiClickCallback getEmojiClickCallbackListener() {
        return emojiClickCallbackListener;
    }

    public void setEmojiClickCallbackListener(EmojiClickCallback emojiClickCallbackListener) {
        this.emojiClickCallbackListener = emojiClickCallbackListener;
    }

    public void initOnClickListeners(){

        findViewById(R.id.emoji_icon1).setOnClickListener(view -> {
            AnimationUtils.SlideToAbove(getResources().getDrawable(R.drawable.reactions_default), innerContainer, container,
                    mContext);
            if(emojiClickCallbackListener != null)
                emojiClickCallbackListener.emojiClicked(Emoji.SMILE);
        });

        findViewById(R.id.emoji_icon2).setOnClickListener(view -> {

            AnimationUtils.SlideToAbove(getResources().getDrawable(R.drawable.cool), innerContainer, container,
                    mContext);
            if(emojiClickCallbackListener != null)
                emojiClickCallbackListener.emojiClicked(Emoji.COOL);
        });

        findViewById(R.id.emoji_icon3).setOnClickListener(view -> {

            AnimationUtils.SlideToAbove(getResources().getDrawable(R.drawable.winky), innerContainer, container,
                    mContext);
            if(emojiClickCallbackListener != null)
                emojiClickCallbackListener.emojiClicked(Emoji.WINK);
        });

        findViewById(R.id.emoji_icon4).setOnClickListener(view -> {

            AnimationUtils.SlideToAbove(getResources().getDrawable(R.drawable.love), innerContainer, container,
                    mContext);
            if(emojiClickCallbackListener != null)
                emojiClickCallbackListener.emojiClicked(Emoji.LOVE);
        });

        findViewById(R.id.emoji_icon5).setOnClickListener(view -> {

            AnimationUtils.SlideToAbove(getResources().getDrawable(R.drawable.tongue), innerContainer, container,
                    mContext);
            if(emojiClickCallbackListener != null)
                emojiClickCallbackListener.emojiClicked(Emoji.TONGUE);

        });

        findViewById(R.id.emoji_icon6).setOnClickListener(view -> {

            AnimationUtils.SlideToAbove(getResources().getDrawable(R.drawable.rofl), innerContainer, container,
                    mContext);
            if(emojiClickCallbackListener != null)
                emojiClickCallbackListener.emojiClicked(Emoji.ROFL);

        });

        findViewById(R.id.emoji_icon7).setOnClickListener(view -> {

            AnimationUtils.SlideToAbove(getResources().getDrawable(R.drawable.crying), innerContainer, container,
                    mContext);
            if(emojiClickCallbackListener != null)
                emojiClickCallbackListener.emojiClicked(Emoji.CRY);

        });

        findViewById(R.id.emoji_icon8).setOnClickListener(view -> {

            AnimationUtils.SlideToAbove(getResources().getDrawable(R.drawable.angry), innerContainer, container,
                    mContext);
            if(emojiClickCallbackListener != null)
                emojiClickCallbackListener.emojiClicked(Emoji.ANGRY);

        });

        findViewById(R.id.emoji_icon9).setOnClickListener(view -> {

            AnimationUtils.SlideToAbove(getResources().getDrawable(R.drawable.x_eyes), innerContainer, container,
                    mContext);
            if(emojiClickCallbackListener != null)
                emojiClickCallbackListener.emojiClicked(Emoji.XEYES);

        });

        findViewById(R.id.emoji_icon10).setOnClickListener(view -> {

            AnimationUtils.SlideToAbove(getResources().getDrawable(R.drawable.shocked), innerContainer, container,
                    mContext);
            if(emojiClickCallbackListener != null)
                emojiClickCallbackListener.emojiClicked(Emoji.SHOCKED);

        });

    }

    public enum Emoji{
        SMILE,
        COOL,
        WINK,
        LOVE,
        TONGUE,
        ROFL,
        CRY,
        ANGRY,
        XEYES,
        SHOCKED
    }

    public interface EmojiClickCallback{

        void emojiClicked(Emoji emoji);

    }
}
