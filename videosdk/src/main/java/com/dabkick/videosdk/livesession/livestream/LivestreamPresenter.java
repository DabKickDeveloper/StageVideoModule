package com.dabkick.videosdk.livesession.livestream;


import com.twilio.video.VideoView;

import java.util.List;

public interface LivestreamPresenter {

    List<Participant> getLivestreamParticipants();
    void toggleMyStream();
    void bindMyVideoView(VideoView videoView);


}
