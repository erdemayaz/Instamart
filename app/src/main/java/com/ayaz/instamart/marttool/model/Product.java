package com.ayaz.instamart.marttool.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.ayaz.instamart.marttool.View.ImageViewSquare;

/**
 * Created by Ayaz on 27.2.2017
 */

public class Product {
    private String ownerName;
    private String photo;
    private String thumbnail;
    private String date;
    private String explanation;
    private ImageViewSquare imageViewSquare;
    private double price;
    private double latitude;
    private double longitude;

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public ImageViewSquare getImageView() {
        return imageViewSquare;
    }

    public void setImageView(ImageViewSquare imageViewSquare) {
        this.imageViewSquare = imageViewSquare;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
