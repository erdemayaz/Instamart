package com.ayaz.instamart.marttool.session;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.util.Log;

import com.ayaz.instamart.activities.ProductListActivity;
import com.ayaz.instamart.marttool.Constant;
import com.ayaz.instamart.marttool.Feedback;
import com.ayaz.instamart.marttool.handler.ConnectHandler;
import com.ayaz.instamart.sdk.AsyncHttpClient;
import com.ayaz.instamart.sdk.AsyncTaskListener;
import com.ayaz.instamart.sdk.ClientMethod;
import com.ayaz.instamart.sdk.Preferences;
import com.ayaz.instamart.sdk.RequestParams;
import com.ayaz.instamart.sdk.TaskParams;
import com.ayaz.instamart.sdk.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ayaz on 27.2.2017.
 */

public class Sign {
    private String name;
    private String email;
    private String password;
    private ConnectHandler handler;

    public Sign(String name, String email, String password){
        this.name = name;
        this.email = email;
        this.password = password;
    }

    private AsyncTaskListener asyncTaskListener = new AsyncTaskListener() {
        @Override
        public void onCompleted(String result) {
            String json = "";

            if(!Util.isNullOrEmpty(result)){
                json = Html.fromHtml(result).toString();
            }

            if(!Util.isNullOrEmpty(json) && Util.isJSONValid(json)){
                Log.e("json", json);
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    String feedback = jsonObject.getString("feedback");
                    if(feedback.equals(Feedback.SUCCESSFULLY_REGISTERED)){
                        handler.onSucceed(jsonObject);
                    }else {
                        handler.onFailed(jsonObject);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else{
                handler.onProblem();
            }
        }
    };

    public void setHandler(ConnectHandler handler){
        this.handler = handler;
    }

    public void start(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("constantkey", "njDP9ynPD3RGBzrrqUHdfRS7");
        requestParams.add("name", name);
        requestParams.add("email", email);
        requestParams.add("password", password);
        TaskParams taskParams = new TaskParams(Constant.API_PATH + Constant.SIGN, ClientMethod.POST, requestParams);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(asyncTaskListener);
        asyncHttpClient.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, taskParams);
    }
}
