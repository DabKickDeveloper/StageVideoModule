package com.dabkick.videosdk.livesession.livestream;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabkick.videosdk.R;
import com.twilio.video.VideoView;

public class SessionParticipantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LivestreamView livestreamView;

    public SessionParticipantsAdapter(Context context, LivestreamView livestreamView) {
        this.context = context;
        this.livestreamView = livestreamView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == MyViewHolder.TYPE) {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.livestream_my_viewholder, parent, false);
            vh = new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(context)
                    .inflate(R.layout.livestream_add_friend_viewholder, parent, false);
            vh = new AddFriendViewHolder(view);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case MyViewHolder.TYPE:
                MyViewHolder myViewHolder = (MyViewHolder) holder;
                livestreamView.myVideoViewCreated(myViewHolder.videoView);

                holder.itemView.setOnClickListener(v -> {
                    livestreamView.myStreamClicked();
                    /*
                    int isVisible = myViewHolder.videoView.getVisibility();
                    if (isVisible == View.VISIBLE) {
                        myViewHolder.videoView.setVisibility(View.INVISIBLE);
                    } else {
                        myViewHolder.videoView.setVisibility(View.VISIBLE);
                    } */
                });
                break;
            case AddFriendViewHolder.TYPE:
                holder.itemView.setOnClickListener(v-> livestreamView.otherUserStreamClicked(position));
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MyViewHolder.TYPE;
        } else {
            return AddFriendViewHolder.TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    // this device's Viewholder
    class MyViewHolder extends RecyclerView.ViewHolder {

        static final int TYPE = 0;
        VideoView videoView;

        MyViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.livestream_my_viewholder_videoview);
        }
    }

    // the last ViewHolder, always exists at last spot
    class AddFriendViewHolder extends RecyclerView.ViewHolder {

        static final int TYPE = 1;

        AddFriendViewHolder(View itemView) {
            super(itemView);
        }
    }
}