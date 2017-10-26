package com.dabkick.videosdk.livesession;

/**
 * Essentially a LiveSession Controller
 */
public class LiveSessionChatPresenter implements Presenter {

    private LiveSessionChatView view;
  //  private MyChatModel model;

    public LiveSessionChatPresenter(LiveSessionChatView view) {
        this.view = view;
    }


    @Override
    public void onCreate() {
 //       model = new MyChatModel();
    }

    @Override
    public void onPause() {}
    @Override
    public void onResume() {}
    @Override
    public void onDestroy() {}


}
