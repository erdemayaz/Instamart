package com.ayaz.instamart.marttool.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Ayaz on 1.3.2017
 */

public class ImageViewSquare extends ImageView {


    public ImageViewSquare(Context context) {
        super(context);
    }

    public ImageViewSquare(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageViewSquare(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}