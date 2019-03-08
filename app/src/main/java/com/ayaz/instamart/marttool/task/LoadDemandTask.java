package com.ayaz.instamart.marttool.task;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.ayaz.instamart.marttool.Constant;
import com.ayaz.instamart.marttool.handler.DemandPresentationHandler;
import com.ayaz.instamart.marttool.model.Demand;
import com.ayaz.instamart.marttool.model.DemandList;
import com.ayaz.instamart.marttool.model.ProductList;
import com.ayaz.instamart.sdk.AsyncHttpClient;
import com.ayaz.instamart.sdk.AsyncTaskListener;
import com.ayaz.instamart.sdk.ClientMethod;
import com.ayaz.instamart.sdk.Preferences;
import com.ayaz.instamart.sdk.RequestParams;
import com.ayaz.instamart.sdk.TaskParams;
import com.ayaz.instamart.sdk.Util;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by Ayaz on 23.5.2017
 */

public class LoadDemandTask {
    private Context context;
    private int pageNumber;
    private DemandPresentationHandler handler;
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
                    JSONArray jsonArray = new JSONArray(json);
                    for(int i = 0; i < jsonArray.length(); i++){
                        Demand demand = new Demand();
                        demand.setOwnerName(jsonArray.getJSONObject(i).getString("owner_name"));
                        demand.setDate(jsonArray.getJSONObject(i).getString("date"));
                        demand.setExplanation(jsonArray.getJSONObject(i).getString("explanation"));
                        demand.setLatitude(jsonArray.getJSONObject(i).getDouble("latitude"));
                        demand.setLongitude(jsonArray.getJSONObject(i).getDouble("longitude"));
                        DemandList.demands.add(demand);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("beforeWorkListener", String.valueOf(ProductList.products.size()));
                handler.onCompleted(json, DemandList.demands);
            }
        }
    };

    public LoadDemandTask(Context context, int pageNumber){
        this.context = context;
        this.pageNumber = pageNumber;

    }

    public void setHandler(DemandPresentationHandler handler){
        this.handler = handler;
    }

    public void start(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("constantkey", "njDP9ynPD3RGBzrrqUHdfRS7");
        requestParams.add("api_key", Preferences.getSavedapiKey(context));
        requestParams.add("page_number", String.valueOf(pageNumber));
        requestParams.add("latitude", Preferences.getSavedLatitude(context));
        requestParams.add("longitude", Preferences.getSavedLongitude(context));
        TaskParams taskParams = new TaskParams(Constant.API_PATH + Constant.DEMAND_PRESENTATION,
                ClientMethod.POST, requestParams);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(asyncTaskListener);
        asyncHttpClient.execute(taskParams);
    }
}
