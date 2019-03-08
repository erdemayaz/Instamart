package com.ayaz.instamart.marttool.handler;

import com.ayaz.instamart.marttool.model.Demand;
import com.ayaz.instamart.marttool.model.Product;

import java.util.ArrayList;

/**
 * Created by Ayaz on 23.5.2017
 */

public interface DemandPresentationHandler {

    void onCompleted(String result, ArrayList<Demand> demands);
}
