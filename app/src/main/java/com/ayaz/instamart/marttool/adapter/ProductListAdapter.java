package com.ayaz.instamart.marttool.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ayaz.instamart.marttool.View.ImageViewSquare;
import com.ayaz.instamart.marttool.model.Product;
import com.ayaz.instamart.marttool.model.ProductList;
import com.ayaz.instamart.sdk.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.ayaz.instamart.marttool.R.id.imageView;
import static com.ayaz.instamart.marttool.R.id.product_image_container;

/**
 * Created by Ayaz on 26.2.2017.
 */

public class ProductListAdapter extends BaseAdapter {
    private Context context;

    public ProductListAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return ProductList.products.size();
    }

    @Override
    public Object getItem(int position) {
        return ProductList.products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageViewSquare imageViewSquare;

        if(convertView == null){
            imageViewSquare = new ImageViewSquare(context);
            imageViewSquare.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViewSquare.setPadding(0, 0, 0, 0);
            ProductList.products.get(position).setImageView(imageViewSquare);

        } else{
            imageViewSquare = (ImageViewSquare) convertView;
            ProductList.products.get(position).setImageView(imageViewSquare);
        }

        Picasso.with(context).load(ProductList.products.get(position).getThumbnail()).fit()
                .into(ProductList.products.get(position).getImageView());
        //return imageViewSquare;
        return ProductList.products.get(position).getImageView();
    }
}
