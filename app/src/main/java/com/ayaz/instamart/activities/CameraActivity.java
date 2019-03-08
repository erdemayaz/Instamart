package com.ayaz.instamart.activities;

import android.content.Intent;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.ayaz.instamart.marttool.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CameraActivity extends AppCompatActivity implements SurfaceHolder.Callback{
    Camera camera;

    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;

    @BindView(R.id.button_take_photo)
    FloatingActionButton floatingActionButton;
    SurfaceHolder surfaceHolder;
    android.hardware.Camera.PictureCallback jpegCallback;
    android.hardware.Camera.ShutterCallback shutterCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        floatingActionButton.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraImage();
            }
        });
        jpegCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream fileOutputStream = null;
                File fileImage = getDir();
                if(!fileImage.exists() && !fileImage.mkdirs()){
                    Toast.makeText(getApplicationContext(), "Fotoğrafları kaydetmek için dosya " +
                            "oluşturulamıyor", Toast.LENGTH_SHORT).show();
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyymmddhhmmss");
                String date = simpleDateFormat.format(new Date());
                String photoFile = "instamart_" + date + ".jpg";
                String fileName = fileImage.getAbsolutePath() + "/" + photoFile;
                File picFile = new File(fileName);
                try {
                    fileOutputStream = new FileOutputStream(picFile);
                    fileOutputStream.write(data);
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                refreshCamera();
                refreshGallery(picFile);
            }
        };
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        camera = Camera.open();
        Camera.Parameters parameters;
        parameters = camera.getParameters();
        parameters.setPreviewFrameRate(25);
        parameters.setPreviewSize(352,288);
        camera.setParameters(parameters);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        refreshCamera();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void cameraImage(){
        camera.takePicture(null, null, jpegCallback);
    }

    private File getDir(){
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        return new File(dir, "Instamart");
    }

    public void refreshCamera(){
        if(surfaceHolder.getSurface() == null)
            return;
        camera.stopPreview();
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshGallery(File file){
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(file));
        sendBroadcast(intent);
    }
}
