package com.ayaz.instamart.marttool.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ayaz.instamart.marttool.View.ImageViewSquare;
import com.ayaz.instamart.marttool.model.Product;
import com.squareup.picasso.Picasso;

/**
 * Created by Ayaz on 26.3.2017
 */

public class ProductActivityAdapter extends BaseAdapter {
    private Context context;
    private Product product;
    private ImageViewSquare imageViewSquare;

    public ProductActivityAdapter(Context context, Product product){
        this.context = context;
        this.product = product;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(position == 0){
            TextView textView = new TextView(context);
            textView.setText(product.getOwnerName().subSequence(0, product.getOwnerName().length()));
            return textView;
        }else if(position == 1){
            imageViewSquare = new ImageViewSquare(context);
            Picasso.with(context).load(product.getPhoto()).fit().into(imageViewSquare);
            return  imageViewSquare;
        }
        return null;
    }
}
