package com.ayaz.instamart.activities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.ayaz.instamart.marttool.Constant;
import com.ayaz.instamart.marttool.EndlessScrollListener;
import com.ayaz.instamart.marttool.Feedback;
import com.ayaz.instamart.marttool.task.LoadProductTask;
import com.ayaz.instamart.marttool.model.ProductList;
import com.ayaz.instamart.marttool.session.Logout;
import com.ayaz.instamart.marttool.model.Product;
import com.ayaz.instamart.marttool.adapter.ProductListAdapter;
import com.ayaz.instamart.marttool.handler.ConnectHandler;
import com.ayaz.instamart.marttool.handler.ProductPresentationHandler;
import com.ayaz.instamart.marttool.R;
import com.ayaz.instamart.sdk.Preferences;
import com.ayaz.instamart.sdk.Util;
import com.ayaz.instamart.services.LocationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductListActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int TAKE_PICTURE = 1;
    private static final int GALLERY_PICTURE = 2;
    private static final int CROP_PICTURE = 3;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private Context context = this;
    public Uri fileUri;
    private ProductListAdapter productAdapter;
    private Intent productIntent;
    private ProgressBar progressBar;
    private BroadcastReceiver broadcastReceiver;
    private Intent intentLocation;
    private Intent intentCrop;
    public String fileName = "default";
    public File picFile = null;
    boolean x = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ButterKnife.bind(this);
        new ProductList();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(0xAA6BC1C1, PorterDuff.Mode.SRC_IN);

        //Log.e("before", String.valueOf(products.size()));
        GridView gridView = (GridView) findViewById(R.id.gridView);
        productAdapter = new ProductListAdapter(context);
        gridView.setAdapter(productAdapter);
        EndlessScrollListener scrollListener = new EndlessScrollListener(new EndlessScrollListener.LoadListener() {
            @Override
            public boolean onRefresh(int pageCount) {
                ProductPresentationHandler handler = new ProductPresentationHandler() {
                    @Override
                    public void onCompleted(String result, ArrayList<Product> products) {
                        productAdapter.notifyDataSetChanged();
                        Log.e("after", String.valueOf(products.size()));
                        progressBar.setVisibility(View.GONE);
                        x = true;
                    }
                };

                //Log.e("beforeWorkProductTask", String.valueOf(products.size()));
                LoadProductTask loadProductTask = new LoadProductTask(context, 1);
                loadProductTask.setHandler(handler);
                loadProductTask.start();
                return x;
            }
        });

        gridView.setOnScrollListener(scrollListener);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle extras = new Bundle();
                productIntent = new Intent(context, ProductActivity.class);
                extras.putInt("position", position);
                productIntent.putExtras(extras);
                startActivity(productIntent);
            }
        });

        if(!runtimePermissionLocation())
            enableLocation();
    }

    private void enableLocation() {
        if(Preferences.getSavedLastLocationTime(getApplicationContext()) != -1){
            if(Preferences.getSavedLastLocationTime(getApplicationContext()) < System.currentTimeMillis() - Constant.Time.DAY){
                intentLocation = new Intent(context, LocationService.class);
                startService(intentLocation);
            }
        } else{
            intentLocation = new Intent(context, LocationService.class);
            startService(intentLocation);
        }
    }

    private void enableCamera() {
        //intentLocation = new Intent(context, LocationService.class);
        //startService(intentLocation);
    }

    private boolean runtimePermissionLocation() {
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION}, Constant.PERMISSION_LOCATION_CODE);
            return true;
        }
        return false;
    }

    private boolean runtimePermissionCamera() {
        if(Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) !=
                        PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, Constant.PERMISSION_CAMERA_CODE);
            return true;
        }
        return false;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(broadcastReceiver == null){
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Toast.makeText(context, Double.toString(intent.getExtras().getDouble(Constant.LONGITUDE))
                            + "" + Double.toString(intent.getExtras().getDouble(Constant.LATITUDE)), Toast.LENGTH_SHORT).show();
                    Preferences.saveLongitude(context, intent.getExtras().getDouble(Constant.LONGITUDE));
                    Preferences.saveLatitude(context, intent.getExtras().getDouble(Constant.LATITUDE));
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter(Constant.LOCATION_UPDATED));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(broadcastReceiver != null){
            if(intentLocation != null){
                unregisterReceiver(broadcastReceiver);
                context.stopService(intentLocation);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case Constant.PERMISSION_LOCATION_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED){
                    enableLocation();
                } else{
                    runtimePermissionLocation();
                }
                break;
            case Constant.PERMISSION_CAMERA_CODE:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    enableCamera();
                } else{
                    runtimePermissionCamera();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.product_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_demand) {
            Intent demandListIntent = new Intent(this, DemandListActivity.class);
            startActivity(demandListIntent);
        }else if (id == R.id.nav_logout) {
            final Intent logoutIntent = new Intent(this, LoginActivity.class);

            ConnectHandler handler = new ConnectHandler() {
                @Override
                public void onSucceed(JSONObject result) {
                    Preferences.saveLoggedIn(getApplicationContext(), false);
                    Preferences.saveApiyKey(getApplicationContext(), null);
                    startActivity(logoutIntent);
                    finish();
                }

                @Override
                public void onFailed(JSONObject result) {
                    try {
                        String feedback = result.getString("feedback");

                        if(feedback.equals(Feedback.LOGOUT_DENIED)){
                            Snackbar.make(findViewById(R.id.productlist),
                                    getString(R.string.logout_denied), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }else if(feedback.equals(Feedback.ALREADY_LOGGED_OUT)){
                            Preferences.saveLoggedIn(getApplicationContext(), false);
                            Preferences.saveApiyKey(getApplicationContext(), null);
                            startActivity(logoutIntent);
                        }else if(feedback.equals(Feedback.UNAUTHORIZED_ACCESS)){
                            Snackbar.make(findViewById(R.id.productlist),
                                    getString(R.string.unauthorized_access), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }else {
                            Snackbar.make(findViewById(R.id.productlist),
                                    getString(R.string.unknown_problem), Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onProblem() {
                    Snackbar.make(findViewById(R.id.productlist),
                            getString(R.string.serverside_problem), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            };
            Logout logout = new Logout(getApplicationContext());
            logout.setHandler(handler);
            logout.start();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void takePhoto(){
        File fileImage = Util.getDir();
        if(!fileImage.exists() && !fileImage.mkdirs()){
            Toast.makeText(getApplicationContext(), "Fotoğrafları kaydetmek için dosya " +
                    "oluşturulamıyor", Toast.LENGTH_SHORT).show();
        }

        String photoFile = "instamartPic.png";
        fileName = fileImage.getAbsolutePath() + "/" + photoFile;
        Log.e("Ayazx", fileName);
        picFile = new File(fileName);
        fileUri = Uri.fromFile(picFile);
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        intentCamera.putExtra("return-data", false);
        startActivityForResult(intentCamera, TAKE_PICTURE);
    }

    private  void openGallery(){
        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intentGallery, "Galeriden Fotoğraf Seç"), GALLERY_PICTURE);
    }

    private void cropImageForCamera(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        File fileImage = Util.getDir();
        String photoFile = "instamartPic.png";
        fileName = fileImage.getAbsolutePath() + "/" + photoFile;
        picFile = new File(fileName);
        intentCrop = new Intent("com.android.camera.action.CROP");
        intentCrop.setDataAndType(Uri.fromFile(picFile), "image/*");
        intentCrop.putExtra("crop", "true");
        intentCrop.putExtra("outputX", 600);
        intentCrop.putExtra("outputY", 600);
        intentCrop.putExtra("aspectX", 1);
        intentCrop.putExtra("aspectY", 1);
        intentCrop.putExtra("scaleUpIfNeeded", true);
        intentCrop.putExtra("return-data", false);
        intentCrop.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
        startActivityForResult(intentCrop, CROP_PICTURE);
        Log.e("cropImageNow", "true");
    }

    private void cropImageForGallery(){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        File fileImage = Util.getDir();
        String photoFile = "instamartPic.png";
        fileName = fileImage.getAbsolutePath() + "/" + photoFile;
        picFile = new File(fileName);
        intentCrop = new Intent("com.android.camera.action.CROP");
        intentCrop.setDataAndType(fileUri, "image/*");
        intentCrop.putExtra("crop", "true");
        intentCrop.putExtra("outputX", 600);
        intentCrop.putExtra("outputY", 600);
        intentCrop.putExtra("aspectX", 1);
        intentCrop.putExtra("aspectY", 1);
        intentCrop.putExtra("scaleUpIfNeeded", true);
        intentCrop.putExtra("return-data", false);
        intentCrop.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(picFile));
        startActivityForResult(intentCrop, CROP_PICTURE);
        Log.e("cropImageNow", "true");
    }

    private void selectImage(){
        final CharSequence[] items = {"Fotoğraf Çek", "Galeriden Seç", "Vazgeç"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Fotoğraf Belirle");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (items[which].toString()){
                    case "Fotoğraf Çek":
                        takePhoto();
                        break;
                    case "Galeriden Seç":
                        openGallery();
                        break;
                    case "Vazgeç":
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case TAKE_PICTURE:
                    Log.e("cropImageBefore", "true");
                    FileOutputStream out = null;
                    try {
                        File fileImage = Util.getDir();
                        String photoFile = "instamartPic.png";
                        fileName = fileImage.getAbsolutePath() + "/" + photoFile;
                        picFile = new File(fileName);
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.fromFile(picFile));
                        bitmap = imageOreintationValidator(bitmap, picFile.getPath());
                        out = new FileOutputStream(picFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            if (out != null) {
                                out.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    cropImageForCamera();
                    Log.e("cropImageAfter", "true");
                    break;
                case CROP_PICTURE:
                    Intent publicationIntent = new Intent(context, PublicationActivity.class);
                    startActivity(publicationIntent);
                    break;
                case GALLERY_PICTURE:
                    fileUri = data.getData();
                    cropImageForGallery();
                    break;
            }
        }
    }

    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }
}
