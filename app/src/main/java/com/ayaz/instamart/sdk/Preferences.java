package com.ayaz.instamart.sdk;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.graphics.drawable.BuildConfig;

import java.util.Date;

/**
 * Created by Ayaz on 17.11.2016.
 */

public class Preferences {
    private static final String DEVICE_ID_PREF_KEY = "device_id";
    private static final String FIRST_EXECUTION_KEY = "first_execution";
    private static final String FIRST_CONNECTION_KEY = "first_connection";
    private static final String LOGGED_IN_KEY = "logged_in";
    private static final String TEMPORARY_KEY_KEY = "temporary_key";
    private static final String API_KEY_KEY = "api_key";
    private static final String NAME_KEY = "name_key";
    private static final String EMAIL_KEY = "email_key";
    private static final String LOCATION_KEY = "location_key";
    private static final String PHOTO_KEY = "photo_key";
    private static final String LONGITUDE_KEY = "longitude_key";
    private static final String LATITUDE_KEY = "latitude_key";
    private static final String LOCATION_LAST_DATE_KEY = "location_last_date_key";

    private Preferences(){}

    public static void saveDeviceId(Context context, String deviceId){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(DEVICE_ID_PREF_KEY, deviceId).apply();
    }

    public static String getSavedDeviceId(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(DEVICE_ID_PREF_KEY, BuildConfig.VERSION_NAME);
    }

    public static void saveIsFirstExe(Context context, boolean firstExe){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(FIRST_EXECUTION_KEY, firstExe).apply();
    }

    public static boolean getIsFirstExe(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(FIRST_EXECUTION_KEY, true);
    }

    public static void saveIsFirstConnection(Context context, boolean first){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(FIRST_CONNECTION_KEY, first).apply();
    }

    public static boolean getIsFirstConnection(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(FIRST_CONNECTION_KEY, true);
    }

    public static void saveLoggedIn(Context context, boolean b){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putBoolean(LOGGED_IN_KEY, b).apply();
    }

    public static boolean getSavedLoggedIn(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(LOGGED_IN_KEY, false);
    }

    public static void saveTemporaryKey(Context context, String temporaryKey){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(TEMPORARY_KEY_KEY, temporaryKey).apply();
    }

    public static String getSavedTemporaryKey(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(TEMPORARY_KEY_KEY, null);
    }

    public static void saveApiyKey(Context context, String apiKey){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(API_KEY_KEY, apiKey).apply();
    }

    public static String getSavedapiKey(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(API_KEY_KEY, null);
    }

    public static void saveName(Context context, String name){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(NAME_KEY, name).apply();
    }

    public static String getSavedName(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(NAME_KEY, "İsimsiz");
    }

    public static void saveEmail(Context context, String email){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(EMAIL_KEY, email).apply();
    }

    public static String getSavedEmail(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(EMAIL_KEY, null);
    }

    public static void saveLocation(Context context, String location){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(LOCATION_KEY, location).apply();
    }

    public static String getSavedLocation(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(LOCATION_KEY, null);
    }

    public static void savePhoto(Context context, String photo){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(PHOTO_KEY, photo).apply();
    }

    public static String getSavedPhoto(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(PHOTO_KEY, null);
    }

    public static void saveLongitude(Context context, double longitude){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(LONGITUDE_KEY, Double.toString(longitude)).apply();
    }

    public static String getSavedLongitude(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(LONGITUDE_KEY, null);
    }

    public static void saveLatitude(Context context, double latitude){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putString(LATITUDE_KEY, Double.toString(latitude)).apply();
    }

    public static String getSavedLatitude(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(LATITUDE_KEY, null);
    }

    public static void saveLastLocationTime(Context context, Date date){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        sharedPreferences.edit().putLong(LOCATION_LAST_DATE_KEY, date.getTime()).apply();
    }

    public static long getSavedLastLocationTime(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getLong(LOCATION_LAST_DATE_KEY, -1);
    }
}
