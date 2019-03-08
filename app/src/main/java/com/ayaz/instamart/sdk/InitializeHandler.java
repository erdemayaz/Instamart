package com.ayaz.instamart.sdk;

/**
 * Created by Ayaz on 19.11.2016.
 */

public interface InitializeHandler {
    void onFirstConnection(String android_id);

    void onFirstExe();

    void onLoggedIn();
}
