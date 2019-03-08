package com.ayaz.instamart.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayaz.instamart.marttool.R;
import com.ayaz.instamart.marttool.View.ImageViewSquare;
import com.ayaz.instamart.marttool.handler.ConnectHandler;
import com.ayaz.instamart.marttool.model.Product;
import com.ayaz.instamart.marttool.task.UploadProductTask;
import com.ayaz.instamart.sdk.Preferences;
import com.ayaz.instamart.sdk.Util;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PublicationActivity extends AppCompatActivity {

    @BindView(R.id.publication_button)
    Button publicationButton;

    @BindView(R.id.price_text)
    TextView priceView;

    @BindView(R.id.explanation_text)
    TextView explanationView;

    @BindView(R.id.uploading_progress_bar)
    ProgressBar progressView;

    private Context context = this;
    private ImageViewSquare preview;
    private Bitmap bitmap = null;
    private Product product = new Product();
    private Double price = 0.0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication);
        ButterKnife.bind(this);
        preview = (ImageViewSquare) findViewById(R.id.productImagePreview);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        try {
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Instamart/instamartPic.png";
            File file = new File(path);
            bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, options);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
        preview.setImageBitmap(bitmap);

        publicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!Util.isNullOrEmpty(priceView.getText().toString())){
                    price = Double.parseDouble(priceView.getText().toString());
                }
                setProduct(price, explanationView.getText().toString());
                uploadProduct();
            }
        });
    }

    void uploadProduct(){
        progressView.setVisibility(View.VISIBLE);
        ConnectHandler connectHandler = new ConnectHandler() {
            @Override
            public void onSucceed(JSONObject result) {
                progressView.setVisibility(View.GONE);
                Toast.makeText(context, "Ürün Gönderildi.", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailed(JSONObject result) {
                progressView.setVisibility(View.GONE);
                Toast.makeText(context, "Ürün Gönderilemedi.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProblem() {
                progressView.setVisibility(View.GONE);
                Toast.makeText(context, "Gönderimde Problem Var.", Toast.LENGTH_SHORT).show();
            }
        };
        UploadProductTask task = new UploadProductTask(context, product);
        task.setHandler(connectHandler);
        task.start();
    }

    private void setProduct(Double price, String explanation){
        Date date = new Date();
        DateFormat df = SimpleDateFormat.getDateInstance();

        product.setOwnerName(Preferences.getSavedName(context));
        product.setDate(df.format(date));
        product.setExplanation(explanation);
        product.setLongitude(Double.parseDouble(Preferences.getSavedLongitude(context)));
        product.setLatitude(Double.parseDouble(Preferences.getSavedLatitude(context)));
        product.setPrice(price);
        product.setImageView(preview);
    }
}
