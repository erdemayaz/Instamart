package com.ayaz.instamart.sdk;

import com.ayaz.instamart.marttool.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Ayaz on 16.11.2016.
 */

public class HttpClient {

    int timeoutInMs = Constant.TIMEOUT_IN_MS;

    public void setTimeout(int timeout){
        this.timeoutInMs = timeout;
    }

    public void post(String path, RequestParams requestParams, HttpResponseHandler httpResponseHandler){
        URL url = null;
        JSONObject jsonObject = new JSONObject();
        try {
            url = new URL(Constant.DOMAIN_NAME + path);
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(timeoutInMs);
                connection.setConnectTimeout(timeoutInMs);
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");

                for(int i = 0; i < requestParams.size(); i++){
                    try {
                        jsonObject.put(requestParams.get(i).getKey(), requestParams.get(i).getValue());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                String input = jsonObject.toString();
                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(input.getBytes());
                outputStream.flush();
                int responseCode = connection.getResponseCode();

                if(responseCode == 200){
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();

                    while (bufferedReader.readLine() != null) {
                        stringBuilder.append(bufferedReader.readLine());
                    }

                    String output = stringBuilder.toString();
                    httpResponseHandler.onSuccess(output);
                    connection.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
