package com.dabkick.videosdk.livesession.stage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dabkick.videosdk.R;
import com.dabkick.videosdk.SdkApp;
import com.dabkick.videosdk.livesession.livestream.NotifyStageItemAddedEvent;
import com.devbrackets.android.exomedia.ui.widget.VideoView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;


public class StageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements StageView {

    @Inject VideoManager videoManager;

    public StageRecyclerViewAdapter() {
        ((SdkApp) SdkApp.getAppContext()).getLivesessionComponent().inject(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stage, parent, false);
        return new StageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        StageViewHolder vh = (StageViewHolder) holder;

        if (vh.layout.getChildCount() == 1) {
            // what's this do?
            vh.layout.removeViewAt(0);
        }

        VideoView toAddView = videoManager.getVideoViewAtIndex(position);
        vh.layout.addView(toAddView);

    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        videoManager.onAdapterDetached();
    }

    @Override
    public int getItemCount() {
        return videoManager.getSize();
    }

    @Override
    public void onStageDataUpdated() {
        notifyDataSetChanged();
    }

    @Override
    public void onStageVideoTimeChanged(int position, long playedMillis) {
        notifyItemChanged(position, playedMillis);
    }

    @Override
    public void onStageVideoStateChanged(int position, boolean shouldPause) {
        notifyItemChanged(position, shouldPause);
    }

    private class StageViewHolder extends RecyclerView.ViewHolder {

        FrameLayout layout;

        StageViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.item_stage_videoview);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NotifyStageItemAddedEvent event) {
        notifyItemInserted(event.index);
    }

}