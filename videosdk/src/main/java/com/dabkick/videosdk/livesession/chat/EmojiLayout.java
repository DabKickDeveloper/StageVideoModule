package com.dabkick.videosdk.livesession.chat;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.dabkick.videosdk.R;

public class EmojiLayout extends LinearLayout {

    static EmojiClickCallback emojiClickCallbackListener;

    public EmojiLayout(Context context) {
        super(context);
        init();
    }

    public EmojiLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EmojiLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.emoji_layout, this);

        findViewById(R.id.emoji_icon1).setOnClickListener(view -> emojiClickCallbackListener.emojiClicked(Emoji.SMILE));
        findViewById(R.id.emoji_icon2).setOnClickListener(view -> emojiClickCallbackListener.emojiClicked(Emoji.COOL));
        findViewById(R.id.emoji_icon3).setOnClickListener(view -> emojiClickCallbackListener.emojiClicked(Emoji.WINK));
        findViewById(R.id.emoji_icon4).setOnClickListener(view -> emojiClickCallbackListener.emojiClicked(Emoji.LOVE));
        findViewById(R.id.emoji_icon5).setOnClickListener(view -> emojiClickCallbackListener.emojiClicked(Emoji.TONGUE));
        findViewById(R.id.emoji_icon6).setOnClickListener(view -> emojiClickCallbackListener.emojiClicked(Emoji.ROFL));
        findViewById(R.id.emoji_icon7).setOnClickListener(view -> emojiClickCallbackListener.emojiClicked(Emoji.CRY));
        findViewById(R.id.emoji_icon8).setOnClickListener(view -> emojiClickCallbackListener.emojiClicked(Emoji.ANGRY));
        findViewById(R.id.emoji_icon9).setOnClickListener(view -> emojiClickCallbackListener.emojiClicked(Emoji.XEYES));
        findViewById(R.id.emoji_icon10).setOnClickListener(view -> emojiClickCallbackListener.emojiClicked(Emoji.SHOCKED));
    }

    public static void setEmojiClickCallbackListener(EmojiClickCallback listener){

        emojiClickCallbackListener = listener;
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
