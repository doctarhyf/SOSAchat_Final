package com.example.rhyfdocta.sosachat;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;

import java.io.File;

public class ActivityViewItemPics extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "ACT_VIEW_IT_PIX";
    Bundle itemBundle;
    SOS_API sosApi;
    ImageView ivMain, ivp1, ivp2, ivp3;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_pictures);

        sosApi = new SOS_API(this);

        ivMain = (ImageView) findViewById(R.id.ivmpMain);
        ivp1 = (ImageView) findViewById(R.id.ivMorePic1);
        ivp2 = (ImageView) findViewById(R.id.ivMorePic2);
        ivp3 = (ImageView) findViewById(R.id.ivMorePic3);


        itemBundle = getIntent().getExtras();
        title = itemBundle.getString(Product.KEY_PD_NAME);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, 500);*/



        loadPicsFromServer();

    }

    private void loadPicsFromServer() {


        int tw = 600;//ivMain.getWidth();
        int th = 600;//ivMain.getHeight();
        String picType = "_main.jpg";

        String uniqueName = itemBundle.getString(Product.KEY_PD_IMG).split("_")[0] + "_" +
                itemBundle.getString(Product.KEY_PD_IMG).split("_")[1];
        final Uri picUri = Uri.parse(SOS_API.DIR_PATH_CATEGORIES + "products/" + uniqueName + "_main.jpg");
        loadBitmapIntoImageView(picUri, uniqueName, picType, ivMain, tw, th);

        tw = 300;//ivp1.getWidth();
        th = 300;//ivp1.getHeight();
        picType = "_pic1.jpg";
        final Uri pic1Uri = Uri.parse(SOS_API.DIR_PATH_CATEGORIES + "products/" + uniqueName + picType);

        loadBitmapIntoImageView(pic1Uri, uniqueName, picType, ivp1, tw, th);

        /*tw = ivp2.getWidth();
        th = ivp2.getHeight();*/
        picType = "_pic2.jpg";
        final Uri pic2Uri = Uri.parse(SOS_API.DIR_PATH_CATEGORIES + "products/" + uniqueName + picType);

        loadBitmapIntoImageView(pic2Uri, uniqueName, picType, ivp2, tw, th);

        /*tw = ivp3.getWidth();
        th = ivp3.getHeight();*/
        picType = "_pic3.jpg";
        final Uri pic3Uri = Uri.parse(SOS_API.DIR_PATH_CATEGORIES + "products/" + uniqueName + picType);
        loadBitmapIntoImageView(pic3Uri, uniqueName, picType, ivp3, tw, th);


    }

    private void loadBitmapIntoImageView(final Uri picUri, String uniqueName, String picType, final ImageView iv, int tw, int th) {

        String itemsPixPath = SOS_API.DIR_PATH_PRODUCTS_PIX;
        Uri uri = picUri;

        //Uri picUri = Uri.parse(itemsPixPath.concat(bundle.getString(Product.KEY_PD_UNIQUE_NAME) + "_main.jpg"));
        //final String imgName = bundle.getString(Product.KEY_PD_UNIQUE_NAME);
        final String pixPath = SOS_API.DIR_PATH_PRODUCTS_PIX + uniqueName + picType;
        String picName = uniqueName;

        String cachePath = BitmapCacheManager.getImageCachePath(BitmapCacheManager.PIC_CACHE_PATH_TYPE_RECENT_ITEMS, picName + picType);
        if(BitmapCacheManager.FILE_EXISTS(cachePath)){
            uri = Uri.fromFile(new File(cachePath));


            Log.e(TAG, "PIC_PATH : -> " + picUri.toString() );

            //Toast.makeText(this, "Loade from cache", Toast.LENGTH_SHORT).show();

        }else{
            Log.e(TAG, "NO_CACHE -> " + picUri.toString() );
            //Toast.makeText(this, "Loade from network", Toast.LENGTH_SHORT).show();
        }


        // TODO: 5/13/2018 LOAD ITEM PIC FROM CACHE

        Glide.with(this)
                .load(uri)
                .asBitmap()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_error)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>(450,450) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        sosApi.getBitmapCacheManager().saveBitmapToCache(resource, pixPath, SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS);

                        iv.setImageBitmap(resource);

                        if(iv.getId() == R.id.ivmpMain){
                            registerForContextMenu(iv);

                            iv.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View view, MotionEvent motionEvent) {

                                    ImageView v = (ImageView) view;
                                    MotionEvent event = motionEvent;

                                    if(event.getAction() == 0){
                                        v.setAlpha(0.5f);

                                        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                                        vibrator.vibrate(100);
                                    }

                                    if(event.getAction() == 1 || event.getAction() == 3) {
                                        v.setAlpha(1f);
                                    }


                                    return false;
                                }
                            });


                        }else {

                            iv.setOnTouchListener(ActivityViewItemPics.this);
                        }
                    }


                });

        /*
        Picasso.with(this)
                .load(uri)
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .centerInside()
                .resize(tw, th)
                .into(iv, new Callback() {
            @Override
            public void onSuccess() {

                if(iv.getId() == R.id.ivmpMain){
                    registerForContextMenu(iv);

                   iv.setOnTouchListener(new View.OnTouchListener() {
                       @Override
                       public boolean onTouch(View view, MotionEvent motionEvent) {

                           ImageView v = (ImageView) view;
                           MotionEvent event = motionEvent;

                           if(event.getAction() == 0){
                               v.setAlpha(0.5f);

                               Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                               vibrator.vibrate(100);
                            }

                           if(event.getAction() == 1 || event.getAction() == 3) {
                               v.setAlpha(1f);
                           }


                           return false;
                       }
                   });


                }else {

                    iv.setOnTouchListener(ActivityViewItemPics.this);
                }
            }

            @Override
            public void onError() {
                Log.e(TAG, "picasso pic error ActivityViewItemPics::loadPicsFromServer(). url -> " + picUri.toString() );
            }
        });*/

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;


        }

        return true;
    }

    public void onIvPicClicked(View view){

        //Toast.makeText(this, "ITEM PIC", Toast.LENGTH_SHORT).show();

        final ImageView iv = (ImageView) view;


    }

    private void loadPic(ImageView iv) {

        ivMain.setImageDrawable(iv.getDrawable());

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);


        getMenuInflater().inflate(R.menu.menu_ctx_product_image, menu);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.ctxMenuSaveProductImgToGallery:

                saveImageToGallery();

                break;


        }



        return true;

    }

    private void saveImageToGallery() {
        Toast.makeText(this, "Will save to gallery", Toast.LENGTH_SHORT).show();




    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        final ImageView iv = (ImageView)v;
        int id = iv.getId();

        iv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == 0) {
                    iv.setAlpha(0.75f);
                }

                if (event.getAction() == 1 || event.getAction() == 3) {
                    iv.setAlpha(1.0f);

                    if(event.getAction() == 1){



                        switch (v.getId()){
                            case R.id.ivMorePic1:

                                //Toast.makeText(getApplicationContext(), "PIC1", Toast.LENGTH_SHORT).show();
                                loadPic(ivp1);

                                break;

                            case R.id.ivMorePic2: // CARS
                                //Toast.makeText(getApplicationContext(), "PIC1", Toast.LENGTH_SHORT).show();
                                loadPic(ivp2);
                                break;

                            case R.id.ivMorePic3:
                                //Toast.makeText(getApplicationContext(), "PIC1", Toast.LENGTH_SHORT).show();
                                loadPic(ivp3);
                                break;
                        }
                    }

                }

                //Log.e(TAG, "onTouch: " + event.getAction());

                return true;

            }



        });

        return false;
    }
}
