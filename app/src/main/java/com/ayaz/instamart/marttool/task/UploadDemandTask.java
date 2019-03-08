package com.ayaz.instamart.marttool.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Base64;
import android.util.Log;

import com.ayaz.instamart.marttool.Constant;
import com.ayaz.instamart.marttool.Feedback;
import com.ayaz.instamart.marttool.handler.ConnectHandler;
import com.ayaz.instamart.marttool.model.Demand;
import com.ayaz.instamart.marttool.model.Product;
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
 * Created by Ayaz on 23.5.2017
 */

public class UploadDemandTask {
    private Context context;
    private Demand demand;
    private ConnectHandler handler;
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
                    Log.e("feeddemand", feedback);
                    if(feedback.equals(Feedback.SUCCESSFULLY_UPLOAD_PRODUCT)){
                        handler.onSucceed(jsonObject);
                    } else if(feedback.equals(Feedback.UNSUCCESSFULLY_UPLOAD_PRODUCT)){
                        handler.onFailed(jsonObject);
                    } else {
                        handler.onProblem();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public UploadDemandTask(Context context, Demand demand){
        this.context = context;
        this.demand = demand;

    }

    public void setHandler(ConnectHandler handler){
        this.handler = handler;
    }

    public void start(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("constantkey", "njDP9ynPD3RGBzrrqUHdfRS7");
        requestParams.add("api_key", Preferences.getSavedapiKey(context));
        requestParams.add("owner_name", demand.getOwnerName());
        requestParams.add("date", demand.getDate());
        requestParams.add("explanation", demand.getExplanation());
        requestParams.add("longitude", Double.toString(demand.getLongitude()));
        requestParams.add("latitude", Double.toString(demand.getLatitude()));
        TaskParams taskParams = new TaskParams(Constant.API_PATH + Constant.DEMAND_PUBLICATION,
                ClientMethod.POST, requestParams);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(asyncTaskListener);
        asyncHttpClient.execute(taskParams);
    }
}
