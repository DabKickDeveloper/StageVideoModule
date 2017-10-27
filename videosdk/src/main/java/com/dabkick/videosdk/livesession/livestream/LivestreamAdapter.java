package com.dabkick.videosdk.livesession.livestream;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabkick.videosdk.R;

public class LivestreamAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == MyViewHolder.TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.livestream_myviewholder, parent, false);
            vh = new MyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.livestream_add_friend_viewholder, parent, false);
            vh = new AddFriendViewHolder(view);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()) {
            case MyViewHolder.TYPE:

                break;
            case AddFriendViewHolder.TYPE:

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

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    // the last ViewHolder, always exists at last spot
    class AddFriendViewHolder extends RecyclerView.ViewHolder {

        static final int TYPE = 1;

        public AddFriendViewHolder(View itemView) {
            super(itemView);
        }
    }
}
