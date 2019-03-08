package com.ayaz.instamart.sdk;

/**
 * Created by Ayaz on 18.11.2016.
 */

public class TaskParams{
    String path;
    ClientMethod method;
    RequestParams requestParams;
    HttpResponseHandler httpResponseHandler;
    int timeoutInMs;

    public TaskParams(String path, RequestParams requestParams, HttpResponseHandler httpResponseHandler, int timeout){
        this.path = path;
        this.requestParams = requestParams;
        this.httpResponseHandler = httpResponseHandler;
        this.timeoutInMs = timeout;
    }

    public TaskParams(String path, RequestParams requestParams, HttpResponseHandler httpResponseHandler){
        this.path = path;
        this.requestParams = requestParams;
        this.httpResponseHandler = httpResponseHandler;
    }

    public TaskParams(String path, ClientMethod method, RequestParams requestParams, HttpResponseHandler httpResponseHandler){
        this.path = path;
        this.method = method;
        this.requestParams = requestParams;
        this.httpResponseHandler = httpResponseHandler;
    }

    public TaskParams(String path, ClientMethod method, RequestParams requestParams){
        this.path = path;
        this.method = method;
        this.requestParams = requestParams;
    }
}
