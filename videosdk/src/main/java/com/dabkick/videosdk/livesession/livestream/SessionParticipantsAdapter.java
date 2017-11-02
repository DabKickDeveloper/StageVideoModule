package com.dabkick.videosdk.livesession.livestream;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabkick.videosdk.R;
import com.twilio.video.VideoView;

import java.util.HashSet;

import timber.log.Timber;

public class SessionParticipantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ParticipantModelCallback {

    private Context context;
    private LivestreamView livestreamView;
    private ParticipantModel participantModel;
    private HashSet<Participant> participants;

    public SessionParticipantsAdapter(Context context, LivestreamView livestreamView) {
        this.context = context;
        this.livestreamView = livestreamView;
        participants = new HashSet<>();
        participantModel = new ParticipantModel(this);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        switch (viewType) {
            case MyViewHolder.TYPE: {
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.livestream_my_viewholder, parent, false);
                vh = new MyViewHolder(view);
                break;
            }
            case ParticipantViewHolder.TYPE: {
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.livestream_my_viewholder, parent, false);
                vh = new MyViewHolder(view);
                break;
            }
            case AddFriendViewHolder.TYPE: {
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.livestream_add_friend_viewholder, parent, false);
                vh = new AddFriendViewHolder(view);
                break;
            }
            default: {
                Timber.e("unable to create viewholder for %s", viewType);
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.livestream_add_friend_viewholder, parent, false);
                vh = new AddFriendViewHolder(view);
            }

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
                holder.itemView.setOnClickListener(v-> livestreamView.addFriendClicked());
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MyViewHolder.TYPE;
        } else if (position == getItemCount() - 1){
            return AddFriendViewHolder.TYPE;
        } else {
            return ParticipantViewHolder.TYPE;
        }
    }

    @Override
    public int getItemCount() {
        int count = 2 + // my view + add friend view
                participants.size();
        Timber.d("item count: %s", count);
        return count;
    }

    @Override
    public void onAdded(Participant participant) {
        participants.add(participant);
        notifyDataSetChanged();
    }

    @Override
    public void onRemoved(Participant participant) {
        participants.remove(participant);
        notifyDataSetChanged();
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

    // any participant in the session besides this one
    class ParticipantViewHolder extends RecyclerView.ViewHolder {

        static final int TYPE = 1;

        ParticipantViewHolder(View itemView) {
            super(itemView);
        }
    }

    // the last ViewHolder, always exists at last spot
    class AddFriendViewHolder extends RecyclerView.ViewHolder {

        static final int TYPE = 2;

        AddFriendViewHolder(View itemView) {
            super(itemView);
        }
    }
}