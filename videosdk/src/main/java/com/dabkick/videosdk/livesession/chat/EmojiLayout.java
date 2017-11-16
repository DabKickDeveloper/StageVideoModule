package com.dabkick.videosdk.livesession.chat;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.dabkick.videosdk.R;

public class EmojiLayout extends LinearLayout {

    private static EmojiClickCallback emojiClickCallbackListener;

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
    }

    public static  EmojiClickCallback getEmojiClickCallbackListener(){

        return emojiClickCallbackListener;
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
