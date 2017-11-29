package com.dabkick.videosdk.livesession.livestream;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dabkick.videosdk.R;
import com.twilio.video.VideoTrack;
import com.twilio.video.VideoView;

import java.util.List;
import java.util.Map;

import timber.log.Timber;

public class SessionParticipantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    Map<String, VideoTrack> videoTrackList = VideoActivity.getInstance().videoTrackList;
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
            default: {
                Timber.e("unable to create viewholder for %s", viewType);
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.livestream_add_friend_viewholder, parent, false);
                vh = new ParticipantViewHolder(view);
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
                holder.itemView.setOnClickListener(v -> livestreamView.myStreamClicked());
                break;
            }
            case ParticipantViewHolder.TYPE: {
                ParticipantViewHolder participantViewHolder = (ParticipantViewHolder) holder;
                String userId = participantList.get(position-1).getUserId();
                videoTrackList.get(userId).addRenderer(participantViewHolder.videoView);
                holder.itemView.setOnClickListener(v -> livestreamView.otherUserStreamClicked(position - 1));
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return MyViewHolder.TYPE;
        } else {
            return ParticipantViewHolder.TYPE;
        }
    }

    @Override
    public int getItemCount() {
        // my view + participants
        return 1 + participantList.size();
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

        VideoView videoView;

        ParticipantViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.livestream_my_viewholder_videoview);
        }
    }
}