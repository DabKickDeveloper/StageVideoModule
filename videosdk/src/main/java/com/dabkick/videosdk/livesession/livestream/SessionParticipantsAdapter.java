package com.dabkick.videosdk.livesession.livestream;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dabkick.videosdk.R;
import com.twilio.video.VideoView;

import java.util.List;

import timber.log.Timber;

public class SessionParticipantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LivestreamView livestreamView;
    private List<Participant> participantList;

    public SessionParticipantsAdapter(Context context, LivestreamView livestreamView, List<Participant> participantList) {
        this.context = context;
        this.livestreamView = livestreamView;
        this.participantList = participantList;
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
                        .inflate(R.layout.livestream_participant_viewholder, parent, false);
                vh = new ParticipantViewHolder(view);
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
            case MyViewHolder.TYPE: {
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
            }
            case ParticipantViewHolder.TYPE: {
                ParticipantViewHolder participantViewHolder = (ParticipantViewHolder) holder;
                participantViewHolder.name.setText(participantList.get(position - 1).dabname);
            }
            case AddFriendViewHolder.TYPE: {
                holder.itemView.setOnClickListener(v -> livestreamView.addFriendClicked());
                break;
            }
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
                participantList.size();
        return count;
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

        TextView name;

        ParticipantViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.livestream_participant_name);
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