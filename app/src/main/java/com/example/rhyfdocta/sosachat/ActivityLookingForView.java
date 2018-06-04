package com.example.rhyfdocta.sosachat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.Helpers.HM;

import com.example.rhyfdocta.sosachat.ObjectsModels.LookingFor;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;

public class ActivityLookingForView extends AppCompatActivity {

    private LookingFor lookingFor;
    private TextView tvPosterName, tvPhone, tvEmail, tvTitle, tvDesc, tvDatePosted, tvInqPriority;
    private Button btnContact;
    private ImageView ivPp;
    private String title;
    private View customView;
    private Bundle data = null;
    private AlertDialog alertContactChoice;
    private SOS_API sosApi;
    private String TAG = "SOSAchat";
    private MyGlideBitmapLoaderCallbacks glideBitmapLoaderCallbacks;
    private String phoneNumber;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_view);

        sosApi = new SOS_API(this);
        glideBitmapLoaderCallbacks = new MyGlideBitmapLoaderCallbacks(this);
        initGUI();

        Intent intent = getIntent();
        title = "No LookingFor Data";

        if(intent.getExtras() != null){

            data = intent.getExtras();
            lookingFor = new LookingFor(data);

            phoneNumber = data.getString(SOS_API.KEY_ACC_DATA_MOBILE);
            email = data.getString(SOS_API.KEY_ACC_DATA_EMAIL);

            updateGUI();
            initDialogContact();

        }


        getSupportActionBar().setTitle("Looking for : " + title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        
    }

    private void initDialogContact(){

        customView = getLayoutInflater().inflate(R.layout.alert_dialog_choose_contact_by, null);

        View rowContactByPhone = customView.findViewById(R.id.llDialogContactByPhone);
        View rowContactBySMS = customView.findViewById(R.id.llDialogContactBySMS);
        View rowContactByEmail = customView.findViewById(R.id.llDialogContactByEmail);
        // TODO: 12/28/2017 SOS DM NEXT FEATURE
        //View rowContactBySOSDM = customView.findViewById(R.id.llDialogContactBySOSDM);

        TextView tvMobile = customView.findViewById(R.id.tvContactVendorMobile);
        TextView tvSMS = customView.findViewById(R.id.tvContactVendorSMS);
        TextView tvEmail = customView.findViewById(R.id.tvContactVendorEmail);

        tvMobile.setText(data.getString(SOS_API.KEY_ACC_DATA_MOBILE));
        tvSMS.setText(data.getString(SOS_API.KEY_ACC_DATA_MOBILE));
        tvEmail.setText(data.getString(SOS_API.KEY_ACC_DATA_EMAIL));


        rowContactByPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConctactChoiceMade(SOS_API.KEY_CONTACT_BY_PHONE);
            }
        });

        rowContactBySMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConctactChoiceMade(SOS_API.KEY_CONTACT_BY_SMS);
            }
        });

        rowContactByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConctactChoiceMade(SOS_API.KEY_CONTACT_BY_EMAIL);
            }
        });

        // TODO: 12/28/2017 SOS DM NEXT FEATURE
        /*rowContactBySOSDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConctactChoiceMade(KEY_CONTACT_BY_SOSDM);
            }
        });*/


        String vendor = data.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME);
        String contactBy = HM.RGS(this, R.string.dgTitleContactBy);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(String.format(contactBy, vendor))
                .setView(customView);



        alertContactChoice = builder.create();

    }


    protected void onConctactChoiceMade(int choice) {
        Log.e(TAG, "onConctactChoiceMade: " + choice );
        Intent intent;

        switch (choice){

            case SOS_API.KEY_CONTACT_BY_PHONE:

                String uri = "tel:" + phoneNumber ;
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));

                startActivity(intent);

                break;

            case SOS_API.KEY_CONTACT_BY_SMS:

                String message = "Hello I just read that you are interrested in \"" + title + "\", let's get in touch, I can hook you up";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                intent.putExtra("sms_body", message);
                startActivity(intent);
                //alertContactChoice.dismiss();
                break;

            case SOS_API.KEY_CONTACT_BY_EMAIL:
                intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email, null));
                intent.putExtra(Intent.EXTRA_SUBJECT, "Re : " + title);
                intent.putExtra(Intent.EXTRA_TEXT, "Hello I just read that you are interrested in \"" + title + "\", let's get in touch, I can hook you up");
                startActivity(Intent.createChooser(intent, "Send email..."));
                //alertContactChoice.dismiss();
                break;

            case SOS_API.KEY_CONTACT_BY_SOSDM:
                Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show();
                //alertContactChoice.dismiss();
                break;

        }

        alertContactChoice.dismiss();
        //alertContactChoice = null;


    }

    private void updateGUI() {
        title  = lookingFor.getTitle();
        tvPosterName.setText((String) lookingFor.getProperty(LookingFor.KEY_POSTERNAME));
        tvPhone.setText((String) lookingFor.getProperty(SOS_API.KEY_ACC_DATA_MOBILE));
        tvEmail.setText((String) lookingFor.getProperty(SOS_API.KEY_ACC_DATA_EMAIL));
        tvInqPriority.setText((String) lookingFor.getProperty(LookingFor.KEY_INQUIRY_RATING));
        // TODO: 5/16/2018 ADD STR TO RESOURCE
        tvTitle.setText(title);
        tvDatePosted.setText((String) lookingFor.getProperty(LookingFor.KEY_DATETIME));
        tvDesc.setText(lookingFor.getMessage());

        String ppName = (String) lookingFor.getProperty(SOS_API.KEY_ACC_DATA_MOBILE_HASH) + ".jpg";

        String path = SOS_API.DIR_PATH_PP + ppName ;//+ "?ts=" + HM.getTimeStamp();//accDataBundle.get(SOS_API.KEY_ACC_DATA_ACC_PIC_NAME);


        String cachePath = BitmapCacheManager.GetImageCachePath(BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_PROFILE_PIC, ppName);
        final Uri picUri = BitmapCacheManager.loadImageFromCacheOrNetwork(Uri.parse(path), cachePath);

        //final Uri picUri = Uri.parse(path);
        ivPp.setImageResource(R.drawable.progress_animation);

        Log.e("SOSAchat", "onAccountDataLoaded: picUri -> " + picUri.toString() );


        Glide.with(this)
                .load(picUri)

                .asBitmap()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_error)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>(300,300) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {




                        glideBitmapLoaderCallbacks.saveBitmapToLocalCache(resource, picUri.toString(), SOS_API.DIR_NAME_PIX_CACHE_PROFILCE_PIC);
                        ivPp.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        ivPp.setImageResource(R.drawable.ic_error);
                        sosApi.TADRWM(true, HM.RGS(ActivityLookingForView.this, R.string.msgFailedToLoadPP));
                    }
                });

    }

    private class MyGlideBitmapLoaderCallbacks implements BitmapCacheManager.CallbacksBitmapLoading {

        private final Context context;

        public MyGlideBitmapLoaderCallbacks(Context context){
            this.context = context;
        }

        @Override
        public void onItemClicked(Product pd) {

        }

        @Override
        public void saveBitmapToLocalCache(Bitmap bitmap, String picUrl, String dirName) {

            Log.e(TAG, "FILE EX : -> " +  sosApi.getBitmapCacheManager().saveBitmapToCache(bitmap,  picUrl, dirName));

        }
    }

    private void initGUI() {
        tvInqPriority = findViewById(R.id.tvInqPriority);
        tvPosterName = findViewById(R.id.tvInqPostername);
        tvPhone = findViewById(R.id.tvInqPosterPhone);
        tvEmail = findViewById(R.id.tvInqPosterEmail);
        tvDatePosted = findViewById(R.id.tvInqPostedTime);
        tvTitle = findViewById(R.id.tvInqTitle);
        ivPp = findViewById(R.id.ivInqPp);
        btnContact = findViewById(R.id.btnInqContact);
        tvDesc = findViewById(R.id.tvInqDesc);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    public void contactClient(View view) {
        alertContactChoice.show();
    }
}
