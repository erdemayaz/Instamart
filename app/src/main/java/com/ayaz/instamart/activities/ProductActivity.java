package com.ayaz.instamart.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayaz.instamart.marttool.R;
import com.ayaz.instamart.marttool.View.ImageViewSquare;
import com.ayaz.instamart.marttool.model.ProductList;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductActivity extends AppCompatActivity {
    Context context = this;

    @BindView(R.id.product_image_container)
    ImageView view;

    @BindView(R.id.owner_name)
    TextView owner;

    @BindView(R.id.price)
    TextView priceText;

    @BindView(R.id.explanation)
    TextView explanationText;

    Bitmap bitmap;
    Bundle extras;
    int position;
    double price, roundedPrice;
    String priceString;
    String explanationString;
    ProductList list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        ButterKnife.bind(this);

        extras = getIntent().getExtras();
        position = extras.getInt("position");
        price = ProductList.products.get(position).getPrice();
        explanationString = ProductList.products.get(position).getExplanation();
        priceString = Double.toString(price);
        roundedPrice = new BigDecimal(priceString).setScale(2, RoundingMode.HALF_UP).doubleValue();
        priceString = Double.toString(roundedPrice) + " TL";

        owner.setText(ProductList.products.get(position).getOwnerName());
        if(ProductList.products.get(position).getImageView().getDrawable() != null){
            bitmap = ((BitmapDrawable) ProductList.products.get(position).getImageView().getDrawable())
                    .getBitmap();
        }
        //view.setImageBitmap(bitmap);
        Picasso.with(context).load(ProductList.products.get(position).getPhoto()).fit().placeholder(ProductList.products.get(position).getImageView().getDrawable()).into(view, new Callback() {
            @Override
            public void onSuccess() {
                Toast.makeText(context, "Picasso Çalıştı", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {
                Toast.makeText(context, "Picasso'da hata var", Toast.LENGTH_SHORT).show();
            }
        });
        priceText.setText(priceString);
        explanationText.setText(explanationString);
    }
}