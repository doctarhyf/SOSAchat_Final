package com.example.rhyfdocta.sosachat;

import android.graphics.drawable.Drawable;
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

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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

        String uniqueName = itemBundle.getString(Product.KEY_PD_IMG).split("_")[0] + "_" +
                itemBundle.getString(Product.KEY_PD_IMG).split("_")[1];
        final Uri picUri = Uri.parse(SOS_API.DIR_PATH_CATEGORIES + "products/" + uniqueName + "_main.jpg");
        picassoLoadInto(picUri, ivMain, tw, th);

        tw = 300;//ivp1.getWidth();
        th = 300;//ivp1.getHeight();
        final Uri pic1Uri = Uri.parse(SOS_API.DIR_PATH_CATEGORIES + "products/" + uniqueName + "_pic1.jpg");
        picassoLoadInto(pic1Uri, ivp1, tw, th);

        /*tw = ivp2.getWidth();
        th = ivp2.getHeight();*/
        final Uri pic2Uri = Uri.parse(SOS_API.DIR_PATH_CATEGORIES + "products/" + uniqueName + "_pic2.jpg");
        picassoLoadInto(pic2Uri, ivp2, tw, th);

        /*tw = ivp3.getWidth();
        th = ivp3.getHeight();*/
        final Uri pic3Uri = Uri.parse(SOS_API.DIR_PATH_CATEGORIES + "products/" + uniqueName + "_pic3.jpg");
        picassoLoadInto(pic3Uri, ivp3, tw, th);








    }

    private void picassoLoadInto(final Uri picUri, final ImageView iv, int tw, int th) {


        //Drawable placeHolder = getResources().getDrawable(R.drawable.placeholder_item_pics);
        Picasso.with(this).load(picUri).error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).centerInside().resize(tw, th).into(iv, new Callback() {
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
        });

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


        getMenuInflater().inflate(R.menu.ctx_menu_product_image, menu);
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
