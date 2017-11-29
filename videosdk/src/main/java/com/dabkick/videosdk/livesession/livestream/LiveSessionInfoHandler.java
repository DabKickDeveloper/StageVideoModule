package com.dabkick.videosdk.livesession.livestream;

/**
 * Created by developer on 11/27/17.
 */

public class LiveSessionInfoHandler {

    public enum LoadProgress {SESSIONS_LOADED, SESSION_DETAILS_LOADED, SESSIONS_AND_USERS_LOADED, TOKEN_ACQUIRED}

    public interface InfoCallback {
        public void callback(LoadProgress progress);
    }

}
