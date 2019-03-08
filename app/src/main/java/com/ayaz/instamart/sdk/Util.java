package com.ayaz.instamart.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by Ayaz on 22.11.2016.
 */

public class Util {
    public static boolean isNullOrEmpty(String s){
        if(s == null || s.isEmpty()){
            return true;
        }
        else {
            return false;
        }
    }

    public static void setError(EditText editText, String message, Drawable icon){
        int errorColor = Color.WHITE;
        ForegroundColorSpan fgcspan = new ForegroundColorSpan(errorColor);
        SpannableStringBuilder ssbuilder = new SpannableStringBuilder(message);
        ssbuilder.setSpan(fgcspan, 0, message.length(), 0);
        editText.setError(ssbuilder, icon);
    }

    public static void setError(EditText editText){
        editText.setError(null);
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isJSONValid(String test){
        try{
            new JSONObject(test);
        }catch (JSONException e){
            try{
                new JSONArray(test);
            } catch (JSONException e1){
                return false;
            }
        }
        return true;
    }

    public static class Image{
        public static void stringToImageView(ImageView imageView, String stringImage){
            byte[] decodedString = Base64.decode(stringImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }

        public static Bitmap rotateBitmap(Bitmap source, float angle)
        {
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        }


    }
    
    public static File getDir(){
        File dir = Environment.getExternalStorageDirectory();
        return new File(dir, "Instamart");
    }



}