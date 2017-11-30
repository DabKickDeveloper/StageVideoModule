package com.dabkick.videosdk.livesession.livestream;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
                String name = context.getString(R.string.me);
                myViewHolder.nameView.setText(name);
                break;
            }
            case ParticipantViewHolder.TYPE: {
                ParticipantViewHolder participantViewHolder = (ParticipantViewHolder) holder;
                String userId = participantList.get(position-1).getUserId();
                if ((videoTrackList != null) && !videoTrackList.isEmpty() && videoTrackList.containsKey(userId))
                {
                    videoTrackList.get(userId).addRenderer(participantViewHolder.videoView);
                }
                holder.itemView.setOnClickListener(v -> livestreamView.otherUserStreamClicked(position - 1));
                String name = participantList.get(position - 1).getDabname();
                participantViewHolder.nameView.setText(name);
                break;
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

    static abstract class AbstractViewHolder extends RecyclerView.ViewHolder {

        VideoView videoView;
        TextView nameView;

        AbstractViewHolder(View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.livestream_my_viewholder_videoview);
            nameView = itemView.findViewById(R.id.livestream_viewholder_name);
        }
    }

    // this device's Viewholder
    static class MyViewHolder extends AbstractViewHolder {

        static final int TYPE = 0;

        MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    // any participant who is not the current user
    static class ParticipantViewHolder extends AbstractViewHolder {

        static final int TYPE = 1;

        ParticipantViewHolder(View itemView) {
            super(itemView);
        }
    }
}