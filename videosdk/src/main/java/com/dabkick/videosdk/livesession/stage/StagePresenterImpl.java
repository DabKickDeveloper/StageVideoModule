package com.dabkick.videosdk.livesession.stage;

import java.util.List;

public class StagePresenterImpl implements StagePresenter, StageModel.StageModelCallback {

    private StageView view;
    private StageModel model;

    public StagePresenterImpl(StageView view) {
        this.view = view;
        model = new StageModel(this);
    }

    @Override
    public void onStageVideoAdded() {
        view.dataUpdated();
    }

    @Override
    public void onStageVideoRemoved() {
        view.dataUpdated();
    }

    @Override
    public List<StageVideo> getStageItems() {
        return model.getStageVideoList();
    }



}