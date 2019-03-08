package com.ayaz.instamart.marttool;


import android.content.Context;
import android.content.Intent;

import com.ayaz.instamart.activities.LoginActivity;
import com.ayaz.instamart.activities.ProductListActivity;
import com.ayaz.instamart.activities.SignActivity;
import com.ayaz.instamart.sdk.ConfigurationManager;
import com.ayaz.instamart.sdk.IntentHandler;
import com.ayaz.instamart.sdk.Preferences;

/**
 * Created by Ayaz on 20.11.2016.
 */

public class InitializeIntent {
    Context context;
    public InitializeIntent(Context context){
        this.context = context;
    }

    public Intent determineIntent(){
        IntentHandler intentHandler = new IntentHandler() {
            @Override
            public Intent onDetermine() {
                if(!Preferences.getSavedLoggedIn(context)){
                    if(Preferences.getIsFirstConnection(context)){
                        return new Intent(context, SignActivity.class);
                    }
                    else{
                        return new Intent(context, LoginActivity.class);
                    }
                }
                else {
                    return new Intent(context, ProductListActivity.class);
                }
            }
        };
        return ConfigurationManager.determineIntent(context, intentHandler);
    }
}