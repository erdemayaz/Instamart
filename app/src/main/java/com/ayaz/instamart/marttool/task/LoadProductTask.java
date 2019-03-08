package com.ayaz.instamart.marttool.task;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import com.ayaz.instamart.marttool.Constant;
import com.ayaz.instamart.marttool.handler.ProductPresentationHandler;
import com.ayaz.instamart.marttool.model.Product;
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

import java.util.ArrayList;

/**
 * Created by Ayaz on 27.2.2017
 */

public class LoadProductTask {
    private Context context;
    private int pageNumber;
    private ProductPresentationHandler handler;
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
                        Product product = new Product();
                        product.setOwnerName(jsonArray.getJSONObject(i).getString("owner_name"));
                        product.setDate(jsonArray.getJSONObject(i).getString("date"));
                        product.setExplanation(jsonArray.getJSONObject(i).getString("explanation"));
                        product.setLatitude(jsonArray.getJSONObject(i).getDouble("latitude"));
                        product.setLongitude(jsonArray.getJSONObject(i).getDouble("longitude"));
                        product.setPrice(jsonArray.getJSONObject(i).getDouble("price"));
                        product.setPhoto(jsonArray.getJSONObject(i).getString("photo"));
                        product.setThumbnail(jsonArray.getJSONObject(i).getString("photo_thumb"));
                        ProductList.products.add(product);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("beforeWorkListener", String.valueOf(ProductList.products.size()));
                handler.onCompleted(json, ProductList.products);
            }
        }
    };

    public LoadProductTask(Context context, int pageNumber){
        this.context = context;
        this.pageNumber = pageNumber;

    }

    public void setHandler(ProductPresentationHandler handler){
        this.handler = handler;
    }

    public void start(){
        RequestParams requestParams = new RequestParams();
        requestParams.add("constantkey", "njDP9ynPD3RGBzrrqUHdfRS7");
        requestParams.add("api_key", Preferences.getSavedapiKey(context));
        requestParams.add("page_number", String.valueOf(pageNumber));
        TaskParams taskParams = new TaskParams(Constant.API_PATH + Constant.PRESENTATION,
                ClientMethod.POST, requestParams);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(asyncTaskListener);
        asyncHttpClient.execute(taskParams);
    }
}