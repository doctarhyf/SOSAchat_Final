package com.koziengineering.rhyfdocta.sosachat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.koziengineering.rhyfdocta.sosachat.API.SOS_API;
import com.koziengineering.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.koziengineering.rhyfdocta.sosachat.Helpers.HM;

import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.LookingFor;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.Product;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.User;
import com.example.rhyfdocta.sosachat.R;

public class ActivityLookingForView extends AppCompatActivity {

    private LookingFor lookingFor;
    private TextView tvPosterName, tvTitle, tvDesc, tvDatePosted, tvInqPriority;
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
    private boolean mine = false;
    private Button btnDelL4;
    private Button btnEditL4;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookingfor_view);

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
            mine = data.getBoolean(LookingFor.KEY_IS_MINE, false);



            updateGUI();
            initDialogContact();

        }


        getSupportActionBar().setTitle("Looking for : " + title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lookingfor_view, menu);

        if(!mine){

            for(int i = 0; i < menu.size(); i++){
                MenuItem item = menu.getItem(i);
                if(item.getItemId() == R.id.menuEditL4 || item.getItemId() == R.id.menuDelL4){
                    item.setVisible(false);
                }else if(item.getItemId() == R.id.menuContactL4){
                    item.setVisible(true);
                }
            }

        }else {

            for(int i = 0; i < menu.size(); i++){
                MenuItem item = menu.getItem(i);
                if(item.getItemId() == R.id.menuEditL4 || item.getItemId() == R.id.menuDelL4){
                    item.setVisible(true);
                }else if(item.getItemId() == R.id.menuContactL4){
                    item.setVisible(false);
                }
            }
        }

        return true;
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

        tvMobile.setText(HM.RGS(this, R.string.contactPhoneCall));//data.getString(SOS_API.KEY_ACC_DATA_MOBILE));
        tvSMS.setText(HM.RGS(this, R.string.contactSMS));
        tvEmail.setText(HM.RGS(this, R.string.contactEmail));


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
        //tvPhone.setText((String) lookingFor.getProperty(SOS_API.KEY_ACC_DATA_MOBILE));
        //tvEmail.setText((String) lookingFor.getProperty(SOS_API.KEY_ACC_DATA_EMAIL));
        tvInqPriority.setText((String) lookingFor.getProperty(LookingFor.KEY_INQUIRY_RATING));

        String s = (String) lookingFor.getProperty(User.COL_SEX);
        if(s == null) s = User.COL_SEX_M;
        final  String sex = s;


        // TODO: 5/16/2018 ADD STR TO RESOURCE
        tvTitle.setText(title);
        tvDatePosted.setText((String) lookingFor.getProperty(LookingFor.KEY_DATETIME));
        tvDesc.setText(lookingFor.getMessage());

        String ppName = lookingFor.getProperty(SOS_API.KEY_ACC_DATA_MOBILE_HASH) + ".jpg";

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

                        if(sex.equals(User.COL_SEX_M)) {
                            ivPp.setImageResource(R.drawable.ic_user_m);
                        }else{
                            ivPp.setImageResource(R.drawable.ic_user_f);
                        }
                        sosApi.TADRWM(ActivityLookingForView.this,true, HM.RGS(ActivityLookingForView.this, R.string.msgFailedToLoadPP));
                    }
                });


        if(mine){
            btnContact.setVisibility(View.GONE);
            btnDelL4.setVisibility(View.VISIBLE);
        }else{
            btnDelL4.setVisibility(View.GONE);
            btnContact.setVisibility(View.VISIBLE);
        }

    }

    public void onDeleteLookingFor(View view) {

        Toast.makeText(this, HM.RGS(this, R.string.msgDelLooking4), Toast.LENGTH_LONG).show();
        deleteLookingFor();


    }

    private void deleteLookingFor() {

        final String id = data.getString(LookingFor.KEY_ID_LOOKINGFOR);


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.layout_dialog_input_password, null);
        final EditText etpwd = v.findViewById(R.id.etDgPassword);

        new AlertDialog.Builder(this)
                //.setTitle(HM.RGS(ActivityAccountSettings.this, R.string.dgTitleInputPassword))
                .setView(v)
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(etpwd.getText().toString().equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_PASSWORD))) {

                            sosApi.deleteLookingFor(new SOS_API.CallbacksLookingFor() {
                                @Override
                                public void onDeleteLookingSuccess() {
                                    Intent intent = new Intent(ActivityLookingForView.this, ActivityLookingFor.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onDeleteLookingForFailure(String message) {
                                    String title = HM.RGS(ActivityLookingForView.this, R.string.titleError);
                                    String errMsg = HM.RGS(ActivityLookingForView.this, R.string.msgFailedDeleteL4);
                                    HM.GADWMAT(ActivityLookingForView.this,title,errMsg, true, true);
                                    Log.e("L4", "onDeleteLookingForFailure: " + message );
                                }

                                @Override
                                public void onUpdateLookingForResult(int code, String data) {

                                }
                            }, id);

                        }else{
                            Toast.makeText(ActivityLookingForView.this, HM.RGS(ActivityLookingForView.this, R.string.tmsgPwdNotCorrect), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("CANCEL", null).show();



    }

    public void onEditLookingFor(View view) {

        editLookingFor();
    }

    private void editLookingFor() {

        final String id = data.getString(LookingFor.KEY_ID_LOOKINGFOR);

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.layout_dialog_input_password, null);
        final EditText etpwd = v.findViewById(R.id.etDgPassword);

        new AlertDialog.Builder(this)
                //.setTitle(HM.RGS(ActivityAccountSettings.this, R.string.dgTitleInputPassword))
                .setView(v)
                .setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(etpwd.getText().toString().equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_PASSWORD))) {

                            Intent intent = new Intent(ActivityLookingForView.this, ActivityNewLookingFor.class);
                            intent.putExtras(lookingFor.toBundle());
                            intent.putExtra(LookingFor.KEY_EDITING, true);
                            startActivity(intent);

                        }else{
                            Toast.makeText(ActivityLookingForView.this, HM.RGS(ActivityLookingForView.this, R.string.tmsgPwdNotCorrect), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setCancelable(false)
                .setNegativeButton("CANCEL", null).show();



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
        tvPosterName = findViewById(R.id.tvL4PosterName);
        //tvPhone = findViewById(R.id.tvInqPosterPhone);
        //tvEmail = findViewById(R.id.tvInqPosterEmail);
        tvDatePosted = findViewById(R.id.tvInqPostedTime);
        tvTitle = findViewById(R.id.tvInqTitle);
        ivPp = findViewById(R.id.ivInqPp);
        btnContact = findViewById(R.id.btnInqContact);
        tvDesc = findViewById(R.id.tvInqDesc);
        btnDelL4 = findViewById(R.id.btnDelL4);
        btnEditL4 = findViewById(R.id.btnEditl4);

        if(mine){
            btnContact.setVisibility(View.GONE);
            btnDelL4.setVisibility(View.VISIBLE);
            btnEditL4.setVisibility(View.VISIBLE);
        }else{
            btnDelL4.setVisibility(View.GONE);
            btnContact.setVisibility(View.VISIBLE);
            btnEditL4.setVisibility(View.GONE);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.menuEditL4:
                editLookingFor();
                break;

            case R.id.menuDelL4:
                deleteLookingFor();
                break;

            case R.id.menuContactL4:
                alertContactChoice.show();
                break;
        }

        return true;
    }

    public void contactClient(View view) {
        alertContactChoice.show();
    }
}
