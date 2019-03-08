package com.ayaz.instamart.sdk;

import android.os.AsyncTask;

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
 * Created by Ayaz on 18.11.2016.
 */

public class AsyncHttpClient extends AsyncTask<TaskParams, Void, String> {
    AsyncTaskListener asyncTaskListener;
    int timeoutInMs = Constant.TIMEOUT_IN_MS;


    public AsyncHttpClient(){
        asyncTaskListener = new AsyncTaskListener() {
            @Override
            public void onCompleted(String result) {

            }
        };
    }

    public AsyncHttpClient(AsyncTaskListener asyncTaskListener){
        this.asyncTaskListener = asyncTaskListener;
    }

    @Override
    protected String doInBackground(TaskParams... params) {
        if(params[0].method == ClientMethod.POST){
            URL url = null;
            JSONObject jsonObject = new JSONObject();
            try {
                url = new URL(Constant.DOMAIN_NAME + params[0].path);
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(timeoutInMs);
                    connection.setConnectTimeout(timeoutInMs);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Content-Type", "application/json");

                    for(int i = 0; i < params[0].requestParams.size(); i++){
                        try {
                            jsonObject.put(params[0].requestParams.get(i).getKey(), params[0].requestParams.get(i).getValue());
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
                        connection.disconnect();
                        return output;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        else if(params[0].method == ClientMethod.GET){
            String charset = "UTF-8";
            String input = createParametersString(params[0].requestParams);
            URL url = null;
            try {
                url = new URL(Constant.DOMAIN_NAME + params[0].path + Constant.PARAMETER_QUEUE_DELIMITER + input);
                HttpURLConnection connection = null;
                try {
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setReadTimeout(timeoutInMs);
                    connection.setConnectTimeout(timeoutInMs);
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("GET");
                    connection.setRequestProperty("Accept-Charset", charset);
                    int responseCode = connection.getResponseCode();

                    if(responseCode == 200){
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();

                        while (bufferedReader.readLine() != null) {
                            stringBuilder.append(bufferedReader.readLine());
                        }

                        String output = stringBuilder.toString();
                        connection.disconnect();
                        return output;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        else {
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        asyncTaskListener.onCompleted(s);
    }

    private static String createParametersString(RequestParams requestParams){
        StringBuilder stringBuilder = new StringBuilder();
        if(requestParams != null){
            for(int i = 0; i < requestParams.size(); i++){
                stringBuilder.append(requestParams.get(i).getKey());
                stringBuilder.append(Constant.PARAMETER_EQUALS);
                stringBuilder.append(requestParams.get(i).getValue());
                if(i + 1 != requestParams.size()){
                    stringBuilder.append(Constant.PARAMETER_DELIMITER);
                }
            }
            return stringBuilder.toString();
        }
        else {
            return null;
        }

    }
}