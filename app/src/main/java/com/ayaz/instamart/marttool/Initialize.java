package com.ayaz.instamart.marttool;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ayaz.instamart.activities.LoginActivity;
import com.ayaz.instamart.marttool.handler.ConnectHandler;
import com.ayaz.instamart.sdk.AsyncHttpClient;
import com.ayaz.instamart.sdk.AsyncTaskListener;
import com.ayaz.instamart.sdk.ClientMethod;
import com.ayaz.instamart.sdk.ConfigurationManager;
import com.ayaz.instamart.sdk.DeviceInfo;
import com.ayaz.instamart.sdk.InitializeHandler;
import com.ayaz.instamart.sdk.Preferences;
import com.ayaz.instamart.sdk.RequestParams;
import com.ayaz.instamart.sdk.TaskParams;
import com.ayaz.instamart.sdk.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ayaz on 19.11.2016.
 */

public class Initialize {

    public Initialize(final Context context){
        InitializeHandler initializeHandler = new InitializeHandler() {
            @Override
            public void onFirstConnection(String android_id) {
                Preferences.saveIsFirstConnection(context, false);
            }

            @Override
            public void onFirstExe() {
                Preferences.saveIsFirstExe(context, false);
            }

            @Override
            public void onLoggedIn() {

            }
        };
        ConfigurationManager.initialize(context, initializeHandler);
    }
}
