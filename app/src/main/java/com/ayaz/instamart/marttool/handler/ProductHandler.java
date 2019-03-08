package com.ayaz.instamart.marttool.handler;

import com.ayaz.instamart.marttool.model.Product;

import java.util.ArrayList;

/**
 * Created by Ayaz on 1.3.2017
 */

public class ProductHandler {
    private static ArrayList<Product> products;

    public static synchronized ArrayList<Product> getProducts(){
        return products;
    }

    public static synchronized void setProducts(ArrayList<Product> products){
        ProductHandler.products = products;
    }
}