package com.ayaz.instamart.sdk;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings.Secure;

/**
 * Created by Ayaz on 17.11.2016.
 */

public class ConfigurationManager {
    HttpClient httpClient;
    HttpResponseHandler httpResponseHandler;
    RequestParams requestParams;

    private ConfigurationManager(){}

    private static boolean isFirstExe(Context context){
        boolean isFirstExe = Preferences.getIsFirstExe(context);
        return isFirstExe;
    }

    private static void onFirstExe(Context context, InitializeHandler initializeHandler){
        boolean isFirstExe;
        ContentResolver contentResolver = context.getContentResolver();
        String android_id = Secure.getString(contentResolver, Secure.ANDROID_ID);

        Preferences.saveDeviceId(context, android_id);
        isFirstExe = false;
        Preferences.saveIsFirstExe(context, isFirstExe);
        initializeHandler.onFirstExe();
    }

    private static boolean isFirstConnection(Context context){
        boolean isFirstConnection = Preferences.getIsFirstConnection(context);
        return isFirstConnection;
    }

    private static void onFirstConnection(Context context, InitializeHandler initializeHandler){
        String android_id = Preferences.getSavedDeviceId(context);
        initializeHandler.onFirstConnection(android_id);
    }

    private static boolean isLoggedIn(Context context){
        boolean isLoggedIn = Preferences.getSavedLoggedIn(context);
        return isLoggedIn;
    }

    private static void onLoggedIn(Context context, InitializeHandler initializeHandler){
        initializeHandler.onLoggedIn();
    }

    public static void initialize(Context context, InitializeHandler initializeHandler){
        if(isFirstExe(context)){
            onFirstExe(context, initializeHandler);
        }
        if(isFirstConnection(context)){
            onFirstConnection(context, initializeHandler);
        }
        if(isLoggedIn(context)){
            onLoggedIn(context, initializeHandler);
        }
    }

    public static Intent determineIntent(Context context, IntentHandler intentHandler){
        return intentHandler.onDetermine();
    }

}
