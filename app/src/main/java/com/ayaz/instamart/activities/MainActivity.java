package com.ayaz.instamart.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ayaz.instamart.marttool.Initialize;
import com.ayaz.instamart.marttool.InitializeIntent;

public class MainActivity extends Activity {
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Initialize(this);
        InitializeIntent initializeIntent = new InitializeIntent(this);
        intent = initializeIntent.determineIntent();
        startActivity(intent);
        finish();
    }
}