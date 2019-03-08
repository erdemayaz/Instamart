package com.ayaz.instamart.marttool.handler;

import org.json.JSONObject;

/**
 * Created by Ayaz on 27.2.2017.
 */

public interface ConnectHandler {

    void onSucceed(JSONObject result);

    void onFailed(JSONObject result);

    void onProblem();

}
