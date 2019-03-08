package com.ayaz.instamart.marttool.model;

import android.content.Context;

import com.ayaz.instamart.sdk.Preferences;

/**
 * Created by Ayaz on 25.2.2017.
 */

public class Profile {
    private CachedInfo cachedInfo;
    private Context context;

    private class CachedInfo{
        private String name;
        private String email;
        private String location;
        private String photo;

        public CachedInfo(){
            this.name = getName();
            this.email = getEmail();
            this.location = getLocation();
            this.photo = getLocation();
        }

        private String getName(){
            return Preferences.getSavedName(context);
        }

        private String getEmail(){
            return Preferences.getSavedEmail(context);
        }

        private String getLocation(){
            return Preferences.getSavedLocation(context);
        }

        private String getPhoto(){
            return Preferences.getSavedPhoto(context);
        }
    }

    public Profile(Context context){
        this.context = context;
    }

    private CachedInfo getCachedInfo() {
        if (this.cachedInfo == null) {
            this.cachedInfo = new CachedInfo();
        }
        return this.cachedInfo;
    }

    public String getName(){
        return cachedInfo.getName();
    }

    public void setName(String s){
        Preferences.saveName(context, s);
    }

    public String getEmail(){
        return cachedInfo.getEmail();
    }

    public void setEmail(String s){
        Preferences.saveEmail(context, s);
    }

    public String getLocation(){
        return cachedInfo.getLocation();
    }

    public void setLocation(String s){
        Preferences.saveLocation(context, s);
    }

    public String getPhoto(){
        return cachedInfo.getPhoto();
    }

    public void setPhoto(String s){
        Preferences.savePhoto(context, s);
    }
}