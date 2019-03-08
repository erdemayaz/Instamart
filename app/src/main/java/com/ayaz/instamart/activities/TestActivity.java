package com.ayaz.instamart.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ayaz.instamart.marttool.Constant;
import com.ayaz.instamart.marttool.R;
import com.ayaz.instamart.sdk.AsyncHttpClient;
import com.ayaz.instamart.sdk.AsyncTaskListener;
import com.ayaz.instamart.sdk.ClientMethod;
import com.ayaz.instamart.sdk.DeviceInfo;
import com.ayaz.instamart.sdk.Preferences;
import com.ayaz.instamart.sdk.RequestParams;
import com.ayaz.instamart.sdk.TaskParams;
import com.ayaz.instamart.sdk.Util;

public class TestActivity extends AppCompatActivity {
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final TextView textView = (TextView) findViewById(R.id.textView5);
        AsyncTaskListener asyncTaskListener = new AsyncTaskListener() {
            @Override
            public void onCompleted(String result) {
                if(!Util.isNullOrEmpty(result)){
                    String json = result.substring(result.indexOf("{"));
                    textView.setText(json);
                }
            }
        };
        DeviceInfo deviceInfo = new DeviceInfo(context);
        RequestParams requestParams = new RequestParams();
        requestParams.add("constantkey", "njDP9ynPD3RGBzrrqUHdfRS7");
        requestParams.add("brand", deviceInfo.getBrand());
        requestParams.add("manufacturer", deviceInfo.getManufacturer());
        requestParams.add("carrier", deviceInfo.getCarrier());
        requestParams.add("language", deviceInfo.getLanguage());
        requestParams.add("model", deviceInfo.getModel());
        requestParams.add("osname", deviceInfo.getOsName());
        requestParams.add("osversion", deviceInfo.getOsVersion());
        TaskParams taskParams = new TaskParams(Constant.API_PATH + "firstconnection.php", ClientMethod.POST, requestParams);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(asyncTaskListener);
        asyncHttpClient.execute(taskParams);
        if(Preferences.getIsFirstConnection(context)){
            Log.e("isfirstconnect", "true");
        }
        else {
            Log.e("isfirstconnect", "false");
        }
    }
}
