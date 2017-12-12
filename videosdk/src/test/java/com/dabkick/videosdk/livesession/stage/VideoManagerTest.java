package com.dabkick.videosdk.livesession.stage;

import com.devbrackets.android.exomedia.ui.widget.VideoView;

import org.junit.Before;
import org.junit.Test;

// does not work as VideoManager is not entirely mocked
public class VideoManagerTest {

    private VideoManager videoManager;

    @Before
    public void setup() {
        videoManager = new VideoManager();
    }


    @Test
    public void add() throws Exception {
        StageModel sm = new StageModel();
        videoManager.add(sm);
        assert(videoManager.getSize() == 1);
    }

    @Test
    public void getVideoViewAtIndex() throws Exception {
        StageModel sm = new StageModel();
        videoManager.add(sm);
        VideoView view = videoManager.getVideoViewAtIndex(0);
        assert(view != null);
    }

    @Test
    public void getIndexFromKey() throws Exception {

    }

    @Test
    public void getKeyFromIndex() throws Exception {
    }

    @Test
    public void updateStageModel() throws Exception {
    }

}