package com.dabkick.videosdk.livesession.emoji;


import com.dabkick.videosdk.SdkApp;

import javax.inject.Inject;

import static com.dabkick.videosdk.livesession.emoji.Constants.ANGRY;
import static com.dabkick.videosdk.livesession.emoji.Constants.COOL;
import static com.dabkick.videosdk.livesession.emoji.Constants.CRY;
import static com.dabkick.videosdk.livesession.emoji.Constants.LOVE;
import static com.dabkick.videosdk.livesession.emoji.Constants.ROFL;
import static com.dabkick.videosdk.livesession.emoji.Constants.SHOCKED;
import static com.dabkick.videosdk.livesession.emoji.Constants.SMILE;
import static com.dabkick.videosdk.livesession.emoji.Constants.TONGUE;
import static com.dabkick.videosdk.livesession.emoji.Constants.WINK;
import static com.dabkick.videosdk.livesession.emoji.Constants.XEYES;

public class EmojiPresenter implements EmojiDatabase.EmojiListener, EmojiLayout.EmojiClickCallback {

    @Inject EmojiDatabase database;
    private EmojiView view;

    public EmojiPresenter(EmojiView view) {
        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
        database.setListener(this);
        this.view = view;
    }

    @Override
    public void onDatabaseEvent(String emojiType) {
        view.showEmoji(emojiType);
    }

    @Override
    public void onSmile() {
        database.sendEvent(SMILE);
    }

    @Override
    public void onCool() {
        database.sendEvent(COOL);
    }

    @Override
    public void onWink() {
        database.sendEvent(WINK);
    }

    @Override
    public void onLove() {
        database.sendEvent(LOVE);
    }

    @Override
    public void onTongue() {
        database.sendEvent(TONGUE);
    }

    @Override
    public void onRofl() {
        database.sendEvent(ROFL);
    }

    @Override
    public void onCry() {
        database.sendEvent(CRY);
    }

    @Override
    public void onAngry() {
        database.sendEvent(ANGRY);
    }

    @Override
    public void onXeyes() {
        database.sendEvent(XEYES);
    }

    @Override
    public void onShocked() {
        database.sendEvent(SHOCKED);
    }
}