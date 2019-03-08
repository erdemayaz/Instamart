package com.ayaz.instamart.marttool.handler;

import com.ayaz.instamart.marttool.model.Product;

import java.util.ArrayList;

/**
 * Created by Ayaz on 27.2.2017.
 */

public interface ProductPresentationHandler {

    void onCompleted(String result, ArrayList<Product> products);

}
