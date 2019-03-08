package com.ayaz.instamart.sdk;

/**
 * Created by Ayaz on 16.11.2016.
 */

public class HttpResponseHandler {
    private String healthyResponse;
    private String throwableMessage;
    private Throwable throwable;

    public void onSuccess(String r){
        this.healthyResponse = r;
    }

    public void onFailure(Throwable throwable, String throwableMessage){
        this.throwable = throwable;
        this.throwableMessage = throwableMessage;
    }

    public String getResponse(){
            return healthyResponse;
    }
}
