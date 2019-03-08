package com.ayaz.instamart.marttool.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Base64;
import android.util.Log;

import com.ayaz.instamart.marttool.Constant;
import com.ayaz.instamart.marttool.Feedback;
import com.ayaz.instamart.marttool.handler.ConnectHandler;
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

import java.io.ByteArrayOutputStream;

/**
 * Created by Ayaz on 08.05.2017.
 */

public class UploadProductTask {
    private Context context;
    private Product product;
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

    public UploadProductTask(Context context, Product product){
        this.context = context;
        this.product = product;

    }

    public void setHandler(ConnectHandler handler){
        this.handler = handler;
    }

    public void start(){
        product.getImageView().buildDrawingCache();
        Bitmap bitmap = product.getImageView().getDrawingCache();
        Bitmap bitmapThumb = product.getImageView().getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        bitmapThumb.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream2);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byte[] byteArray2 = byteArrayOutputStream2.toByteArray();
        String image = Base64.encodeToString(byteArray, Base64.DEFAULT);
        String image2 = Base64.encodeToString(byteArray2, Base64.DEFAULT);
        RequestParams requestParams = new RequestParams();
        requestParams.add("constantkey", "njDP9ynPD3RGBzrrqUHdfRS7");
        requestParams.add("api_key", Preferences.getSavedapiKey(context));
        requestParams.add("owner_name", product.getOwnerName());
        requestParams.add("date", product.getDate());
        requestParams.add("explanation", product.getExplanation());
        requestParams.add("longitude", Double.toString(product.getLongitude()));
        requestParams.add("latitude", Double.toString(product.getLatitude()));
        requestParams.add("price", Double.toString(product.getPrice()));
        requestParams.add("photo", image);
        requestParams.add("thumb", image2);
        TaskParams taskParams = new TaskParams(Constant.API_PATH + Constant.PUBLICATION,
                ClientMethod.POST, requestParams);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient(asyncTaskListener);
        asyncHttpClient.execute(taskParams);
    }
}
