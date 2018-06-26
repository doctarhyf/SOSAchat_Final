package com.example.rhyfdocta.sosachat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhyfdocta.sosachat.API.NETWORK_RESULT_CODES;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.ServerImageManagement.ServerImage;
import com.example.rhyfdocta.sosachat.app.SOSApplication;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by rhyfdocta on 11/8/17.
 */

public class ActivityViewItemDetails extends AppCompatActivity implements SOS_API.SOSApiListener,
        BitmapCacheManager.CallbacksBitmapLoading {


    private static final String TAG = "Test Debug";
    private static final int KEY_CONTACT_BY_PHONE = 0;
    private static final int KEY_CONTACT_BY_SMS = 1;
    private static final int KEY_CONTACT_BY_EMAIL = 2;
    private static final int KEY_CONTACT_BY_SOSDM = 3;
    private static final String CHANNEL_ID = "id";
    Bundle itemDataBundle;
    TextView tvItemName, tvItemPrice, tvDatePosted, tvItemDesc, tvItemQual, tvItemViewsCount, tvItemCat, tvItemType;
    TextView tvSellerDislayName;
    ImageView ivItemMainPic, ivSellerPP;
    Button btnAddToWishList, btnContactVendor;
    //final String[] contactChoices = new String[4];
    String phoneNumber;

    Button btnPusblishItem;


    /*public static int KEY_CHOICE_PHONE_CALL = 0;
    public static int KEY_CHOICE_SMS = 1;
    public static int KEY_CHOICE_EMAIL = 2;
    public static int KEY_CHOICE_SOSACHAT = 3;*/

    AlertDialog alertContactChoice;
    //Bundle pdVendor = new Bundle();
    private String email;
    //private String vendorUID;
    boolean itemIsMine = false;
    //private LinearLayout llVendorCont;
    private LinearLayout llItemViewsCount;
    //llDateSold;
    SOS_API sosApi;
    //private LinearLayout ;

    private String itemPrice;
    private View customView;
    private TextView tvSellerMobile;
    private TextView tvSellerEmail;
    private TextView tvItemCurStat;



    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);


        sosApi = SOSApplication.GI().getSosApi();

        tvItemCurStat = findViewById(R.id.tvItemCurStat);
        btnPusblishItem = findViewById(R.id.btnPusblishItem);



        Intent intent = getIntent();
        itemDataBundle = intent.getExtras();

        itemIsMine = itemDataBundle.getBoolean(SOS_API.KEY_ITEM_IS_MINE);
        String myUid = sosApi.GSV(SOS_API.KEY_ACC_DATA_USER_ID);

        if(!itemIsMine){
            itemIsMine = itemDataBundle.getString(SOS_API.KEY_ACC_DATA_USER_ID).equals(myUid);
        }



        phoneNumber = itemDataBundle.getString(SOS_API.KEY_ACC_DATA_MOBILE);
        email = itemDataBundle.getString(SOS_API.KEY_ACC_DATA_EMAIL);
        //vendorUID = itemDataBundle.getString(SOS_API.KEYAU)

        //Log.e(TAG, "onCreate: " + "Test debug " );
        Resources res = getResources();
        /*contactChoices[0] = res.getString(R.string.contactPhoneCall);
        contactChoices[1] = res.getString(R.string.contactSMS);
        contactChoices[2] = res.getString(R.string.contactEmail);
        contactChoices[3] = res.getString(R.string.contactSOSDM);*/


        btnAddToWishList = findViewById(R.id.btnAddToWishList);
        btnContactVendor = findViewById(R.id.btnContactVendor);

        setUpBtnsListeners();



        ivSellerPP = findViewById(R.id.ivSeller);

        //tvSellerDisplayName = findViewById(R.id.);
        //tvSellerEmail = findViewById(R.id.);
        //tvSellerMobile = findViewById(R.id);

        tvItemType = findViewById(R.id.tvItemType);
        tvItemCat = findViewById(R.id.tvItemCat);
        tvItemName = findViewById(R.id.tvItemName);
        tvItemPrice = findViewById(R.id.tvItemPrice);

        tvSellerDislayName = findViewById(R.id.tvSellerDislayName);
        tvSellerMobile = findViewById(R.id.tvSellerMobile);
        tvSellerEmail = findViewById(R.id.tvSellerEmail);

        tvItemDesc = findViewById(R.id.tvItemDesc);
        tvItemQual = findViewById(R.id.tvItemQuality);
        ivItemMainPic = findViewById(R.id.ivProdMainPic);
        //llVendorCont = (LinearLayout) findViewById(R.id.llVendorCont);
        llItemViewsCount = findViewById(R.id.llItemViewsCount);
        //llDateSold = (LinearLayout) findViewById(R.id.llDatePosted);
        tvItemViewsCount = findViewById(R.id.tvItemViewsCount);
        tvDatePosted = findViewById(R.id.tvDateItemPosted);

        itemPrice = HM.GIPB(this, R.string.priceToDiscuss, itemDataBundle.getString(Product.KEY_PD_PRICE), itemDataBundle.getString(Product.KEY_PD_CUR));

        getSupportActionBar().setTitle(itemDataBundle.getString(Product.KEY_PD_NAME));
        getSupportActionBar().setSubtitle(itemPrice);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        loadItemDataFromBundle(itemDataBundle);


        //registerForContextMenu(ivItemMainPic);

        ivItemMainPic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if(event.getAction() == 0){
                    v.setAlpha(0.5f);
                    Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vibrator.vibrate(100);

                }

                if(event.getAction() == 1 || event.getAction() == 3){
                    v.setAlpha(1f);

                    if(event.getAction() == 1){
                        openContextMenu(v);
                        //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
                        //TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

                        fireShowAllPicsIntent();

                    }
                }





                return true;
            }
        });

        prepareGUI();
        updateItemViewsCount();


        updateItemStatInfo();
    }

    private void updateItemStatInfo() {

        int stat = Integer.parseInt(itemDataBundle.getString(Product.KEY_PD_STAT));
        Resources res = getResources();
        String text = "UNPUBLISHED";

        if(stat != Product.PD_STAT_UNPUBLISHED){
            btnPusblishItem.setVisibility(View.GONE);
        }

        Log.e("XXX", "STATS: " + stat );

        int statColID = 0;

        switch (stat){
            case Product.PD_STAT_UNPUBLISHED:
                statColID = res.getColor(R.color.gray);

                break;

            case Product.PD_STAT_WAITING:
                statColID = res.getColor(R.color.yellow);
                text = "WAITING";
                break;

            case Product.PD_STAT_PUBLISHED:
                statColID = res.getColor(R.color.green);
                text = "PUBLISHED";
                break;

            case Product.PD_STAT_DENIED:
                statColID = res.getColor(R.color.red);
                text = "DENIED";
                break;
        }

        tvItemCurStat.setBackgroundColor(statColID);
        tvItemCurStat.setText(text);

    }

    private void updateItemViewsCount() {


        if(itemIsMine == false) {
            Log.e(TAG, "updateItemViewsCount: id -> " + itemDataBundle.getString(SOS_API.KEY_ITEM_ID));
            sosApi.updateItemViewsCount(this,itemDataBundle.getString(SOS_API.KEY_ITEM_ID));

        }else{
            Log.e(TAG, "updateItemViewsCount: Item is mine!!!" );
        }

    }

    private void prepareGUI() {

        Log.e(TAG, "prepareGUI: mine " + itemIsMine );
        if(itemIsMine == true) {
            btnAddToWishList.setVisibility(View.GONE);
            btnContactVendor.setVisibility(View.GONE);
            //llVendorCont.setVisibility(View.GONE);

            View tvStat = findViewById(R.id.tvItemStats);
            View cvStat = findViewById(R.id.cvItemStats);

            tvStat.setVisibility(View.VISIBLE);
            cvStat.setVisibility(View.VISIBLE);


        }else{


            btnAddToWishList.setVisibility(View.VISIBLE);
            btnContactVendor.setVisibility(View.VISIBLE);
            //llVendorCont.setVisibility(View.VISIBLE);

            //sosApi.updateItemViewsCount();
            // TODO: 1/4/2018 NEXT FEATURE USER PROFILE
            tvSellerDislayName.setClickable(false);
            tvSellerDislayName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Toast.makeText(getApplicationContext(),
                            "Show " + tvSellerDislayName.getText().toString() +
                    "'s profile! ID : " + itemDataBundle.getString(SOS_API.KEY_ACC_DATA_USER_ID) , Toast.LENGTH_SHORT).show();*/

                    //showVendorsProfile(itemDataBundle);
                }
            });

        }

        initContactVendorDialog();

    }

    // TODO: 6/4/2018 SHOW SELLER PROFILE
    private void showVendorsProfile(Bundle itemDataBundle) {
        Intent intent = new Intent(this, ActivityMyAccount.class);
        intent.putExtras(itemDataBundle);
        intent.putExtra(SOS_API.KEY_SHOWING_VENDOR_PROFILE, true);
        startActivity(intent);
    }

    private void initContactVendorDialog(){

        customView = getLayoutInflater().inflate(R.layout.alert_dialog_choose_contact_by, null);

        View rowContactByPhone = customView.findViewById(R.id.llDialogContactByPhone);
        View rowContactBySMS = customView.findViewById(R.id.llDialogContactBySMS);
        View rowContactByEmail = customView.findViewById(R.id.llDialogContactByEmail);
        // TODO: 12/28/2017 SOS DM NEXT FEATURE
        //View rowContactBySOSDM = customView.findViewById(R.id.llDialogContactBySOSDM);

        TextView tvMobile = customView.findViewById(R.id.tvContactVendorMobile);
        TextView tvSMS = customView.findViewById(R.id.tvContactVendorSMS);
        TextView tvEmail = customView.findViewById(R.id.tvContactVendorEmail);

        //tvMobile.setText(itemDataBundle.getString(SOS_API.KEY_ACC_DATA_MOBILE));
        //tvSMS.setText(itemDataBundle.getString(SOS_API.KEY_ACC_DATA_MOBILE));
        //tvEmail.setText(itemDataBundle.getString(SOS_API.KEY_ACC_DATA_EMAIL));


        rowContactByPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConctactChoiceMade(KEY_CONTACT_BY_PHONE);
            }
        });

        rowContactBySMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConctactChoiceMade(KEY_CONTACT_BY_SMS);
            }
        });

        rowContactByEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConctactChoiceMade(KEY_CONTACT_BY_EMAIL);
            }
        });

        // TODO: 12/28/2017 SOS DM NEXT FEATURE
        /*rowContactBySOSDM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onConctactChoiceMade(KEY_CONTACT_BY_SOSDM);
            }
        });*/


        String vendor = itemDataBundle.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME);
        String contactBy = HM.RGS(this, R.string.dgTitleContactBy);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(String.format(contactBy, vendor))
                .setView(customView);



        alertContactChoice = builder.create();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if(itemIsMine == false) {
            getMenuInflater().inflate(R.menu.menu_item_details, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);


        getMenuInflater().inflate(R.menu.menu_ctx_product_image, menu);
    }

    private void setUpBtnsListeners() {
        btnContactVendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onShowContactVendorDialog();
            }
        });

        btnAddToWishList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddToWishList();
            }
        });
    }

    private void loadItemDataFromBundle( Bundle bundle) {



        tvItemName.setText(bundle.getString(Product.KEY_PD_NAME));

        tvItemPrice.setText(itemPrice);
        //tvSellerDislayName.setText("");
        tvItemDesc.setText(bundle.getString(Product.KEY_PD_DESC));

        tvSellerDislayName.setText(bundle.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));
        tvSellerEmail.setText(bundle.getString(SOS_API.KEY_ACC_DATA_EMAIL));
        tvSellerMobile.setText(bundle.getString(SOS_API.KEY_ACC_DATA_MOBILE));

        if(itemIsMine) tvSellerDislayName.setText(HM.RGS(this, R.string.strMe));
        tvDatePosted.setText(bundle.getString(SOS_API.KEY_ITEM_DATE_ADDED));
        String viewsCount = bundle.getString(SOS_API.KEY_ITEM_ITEM_VIEWS_ACCOUNT);

        tvItemViewsCount.setText(viewsCount);

        String qual = HM.GPQF(this, new Integer(bundle.getString(Product.KEY_PD_QUAL)).intValue());
        tvItemCat.setText(bundle.getString(Product.KEY_PD_CAT));
        tvItemType.setText(bundle.getString(Product.KEY_PD_TYPE));


        tvItemQual.setText(qual);

        String sellerDisplayName = bundle.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME);
        String sellerEmail = bundle.getString(SOS_API.KEY_ACC_DATA_EMAIL);
        String sellerMobile = bundle.getString(SOS_API.KEY_ACC_DATA_MOBILE);


        String remoteDirProductPix = sosApi.GSA() + SOS_API.DIR_PATH_PRODUCTS_PIX;


        String fileNameProductPix = bundle.getString(Product.KEY_PD_UNIQUE_NAME) + "_main.jpg";
        final String remotePathProductPix = remoteDirProductPix + fileNameProductPix;
        String remoteMTime = bundle.getString(ServerImage.KEY_LAST_MOD_TIME + SOS_API.KEY_PRODUCT_IMAGE_POST_FIX_MAIN);
        long remoteMTimeL = remoteMTime.equals(SOS_API.FALSE) ? 0 : Long.parseLong(remoteMTime);

        //Log.e("FAAKK", "REMOTE MTIME MAIN : " + remoteMTimeL   );

        BitmapCacheManager.GlideUniversalLoaderLoadPathIntoImageView(
                this,
                 remotePathProductPix,
                remoteMTimeL,
                fileNameProductPix,
                BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_PRODUCTS,
                SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS, ivItemMainPic, this);


        String remoteDirPP = SOS_API.DIR_PATH_PP;
        String fileNamePP = bundle.getString(SOS_API.KEY_ACC_DATA_MOBILE_HASH) + ".jpg";
        if(itemIsMine) fileNamePP = sosApi.GSV(SOS_API.KEY_ACC_DATA_MOBILE_HASH) + ".jpg";

        final String remotePathPP = remoteDirPP + fileNamePP;

        String remoteMTimePP = bundle.getString(ServerImage.KEY_LAST_MOD_TIME + SOS_API.KEY_PRODUCT_IMAGE_POST_FIX_LOGGEDIN_USER);
        long remoteMTimePPL = remoteMTimePP.equals(SOS_API.FALSE) ? 0 : Long.parseLong(remoteMTimePP);


        //Log.e("FAAKK", "REMOTE MTIME PP : " + remoteMTimePPL   );

        BitmapCacheManager.GlideUniversalLoaderLoadPathIntoImageView(this,
                remotePathPP,
                remoteMTimePPL, fileNamePP,
                BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_PROFILE_PIC,
                SOS_API.DIR_NAME_PIX_CACHE_PROFILCE_PIC,
                ivSellerPP,
                this
                );



    }

    private void fireShowAllPicsIntent() {
        Intent intent = new Intent(this, ActivityViewItemPics.class);
        intent.putExtras(itemDataBundle);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume: DETS" );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        if(item.getItemId() == R.id.menuItemDetailsContactVendor){

            onShowContactVendorDialog();

        }

        if(item.getItemId() == R.id.menuItemDetailsAddToWishList){
            onAddToWishList();
        }


        return super.onOptionsItemSelected(item);
    }

    private void onAddToWishList() {

        String pdName = itemDataBundle.getString(Product.KEY_PD_NAME);

        String pdPrice = " " + itemDataBundle.getString(Product.KEY_PD_PRICE) + " " + itemDataBundle.getString(Product.KEY_PD_CUR);

        /*
        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_not)
                //.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.iphone))
                .setContentTitle(pdName + pdPrice)
                .setContentText(pdName + " has been added to favorites list.");

        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);

        Notification notification = notificationBuilder.build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(1, notificationBuilder.build());

        ///////////
        */

        createNotificationChannel();
        Intent intent = new Intent(this, ActivityWishlist.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Notification")
                .setContentText("This is the notification")
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText("This is the fucking longtext ..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);



        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

// notificationId is a unique int for each notification that you must define
        int notificationId = 100;
        notificationManager.notify(notificationId, builder.build());

        //////////

        sosApi.addItemToWishlist(new SOS_API.ListenerItemsWishlist() {
            @Override
            public void onItemAddedSuccess() {
                String msg = HM.RGS(ActivityViewItemDetails.this, R.string.msgItemAddedToWishlistSuccess);

                String itName = itemDataBundle.getString(Product.KEY_PD_NAME);


                msg = String.format(msg, itName);
                //sosApi.toggleAlertDialogResponseWithMessage(true, msg);

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityViewItemDetails.this);
                builder.setMessage(Html.fromHtml(msg));
                builder.setPositiveButton(HM.RGS(ActivityViewItemDetails.this, R.string.btnGoToWishlist),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ActivityViewItemDetails.this, ActivityWishlist.class);
                                startActivity(intent);
                            }
                        });

                builder.setNegativeButton(HM.RGS(ActivityViewItemDetails.this, R.string.btnOk), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }

            @Override
            public void onItemAddedError(String msg) {
                sosApi.toggleAlertDialogResponseWithMessage(ActivityViewItemDetails.this,true, HM.RGS(ActivityViewItemDetails.this, R.string.msgErrorAddingWishlistItem));
            }

            @Override
            public void onNetworkError(String msg) {
                sosApi.toggleAlertDialogResponseWithMessage(ActivityViewItemDetails.this,true, HM.RGS(ActivityViewItemDetails.this, R.string.msgErrorInternetConnection));

            }

            @Override
            public void onWishlistItemRemoveError(Bundle pd) {

            }

            @Override
            public void onWishlistItemRemoveSuccess(Bundle pd) {

            }


        },itemDataBundle.getString(SOS_API.KEY_ITEM_ID));




    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void onShowContactVendorDialog() {
        alertContactChoice.show();
    }
    protected void onConctactChoiceMade(int choice) {
        Log.e(TAG, "onConctactChoiceMade: " + choice );
        Intent intent;

        switch (choice){

            case KEY_CONTACT_BY_PHONE:

                String uri = "tel:" + phoneNumber ;
                intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse(uri));

                startActivity(intent);

                break;

            case KEY_CONTACT_BY_SMS:

                String message = "Hello Im interrested in your product " + itemDataBundle.getString(Product.KEY_PD_NAME) + ", how can we get in touch?";
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + phoneNumber));
                intent.putExtra("sms_body", message);
                startActivity(intent);
                //alertContactChoice.dismiss();
                break;

            case KEY_CONTACT_BY_EMAIL:
                intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",email, null));
                intent.putExtra(Intent.EXTRA_SUBJECT, itemDataBundle.getString(Product.KEY_PD_NAME));
                intent.putExtra(Intent.EXTRA_TEXT, "Hello there im interested in the " + itemDataBundle.getString(Product.KEY_PD_NAME) + ", how can we get in touch?");
                startActivity(Intent.createChooser(intent, "Send email..."));
                //alertContactChoice.dismiss();
                break;

            case KEY_CONTACT_BY_SOSDM:
                Toast.makeText(this, phoneNumber, Toast.LENGTH_SHORT).show();
                //alertContactChoice.dismiss();
                break;

        }

        alertContactChoice.dismiss();
        //alertContactChoice = null;


    }

    @Override
    protected void onStop() {
        super.onStop();
        alertContactChoice = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        alertContactChoice = null;
    }

    public void showMessage(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.drawable.logo);
        builder.setPositiveButton("OK", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }

    @Override
    public void onSearchResult(String ids) {

    }

    @Override
    public void onSearchResultError(String error) {

    }

    @Override
    public void onPostInquiryResult(String result) {

    }

    @Override
    public void onLoadRecentItemsResult(List<Product> featuredProducts, boolean networkError) {

    }

    @Override
    public void onLoginResult(Bundle data) {

    }

    @Override
    public void onSignUpResult(Bundle data) {

    }

    @Override
    public void onAccountDeleteResult(Bundle data) {

    }

    @Override
    public void onLogoutResult() {

    }

    @Override
    public void onExposeItemResult(Bundle data) {

    }

    @Override
    public void onLoadAllMyProductsResult(List<ProductMyProducts> myProducts, boolean networkError) {

    }

    @Override
    public void onLoadItemsCats(JSONArray jsonArray) {

    }

    @Override
    public void onUpdatePasswordResult(String resp) {

    }

    @Override
    public void onUpdateSettingsResult(String settingKey, String result) {

    }

    @Override
    public void onLoadAllProductsResult(List<ProductMyProducts> allProducts) {

    }

    @Override
    public void onRemoveProductResult(Bundle b) {

    }

    @Override
    public void onUpdateItemViewsCountResult(String newCount) {
        if(!newCount.equals(SOS_API.JSON_RESULT_FAILURE)){
            tvItemViewsCount.setText(newCount);
        }


    }

    @Override
    public void onLoadAllProductsError(String message) {

    }

    @Override
    public void onLoadSearchResultItemsResult(List<ProductMyProducts> searchResultProducts) {

    }

    @Override
    public void onLoadSearchResultItemsError(String message) {

    }

    @Override
    public void onItemsTypesLoaded(JSONArray itemCats) {

    }

    @Override
    public void onUploadPPResult(Bundle data) {

    }

    @Override
    public void onLoadItemsTypesResultError() {

    }

    @Override
    public void onLoadItemsTypesResult(String[] types) {

    }

    @Override
    public void onLoadCatsTypesNames(String cn, String tn) {

    }

    @Override
    public void onCategoryTypesLoaded(List<TypesItem> types, boolean errorLoading) {

    }

    @Override
    public void onItemClicked(Product pd) {

    }

    @Override
    public void saveBitmapToLocalCache(Bitmap bitmap, String picUrl, String dirName) {


        Log.e(TAG, "FILE EX : -> " + sosApi.getBitmapCacheManager().saveBitmapToCache(bitmap, picUrl,dirName));
    }

    public void showSellerProfile(View view) {

        Log.e(TAG, "showSellerProfile: " );
    }

    public void onClickPublish(View view) {

        publishItem();
    }

    private void publishItem() {

        ////////


        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.layout_dialog_input_password, null);
        final EditText etpwd = v.findViewById(R.id.etDgPassword);

        new AlertDialog.Builder(this)
                //.setTitle(HM.RGS(ActivityAccountSettings.this, R.string.dgTitleInputPassword))
                .setView(v)
                .setPositiveButton("PUBLISH", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(etpwd.getText().toString().equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_PASSWORD))) {


                            final ProgressDialog progressDialog = new ProgressDialog(ActivityViewItemDetails.this);
                            //progressDialog.setTitle("Publishing ");
                            progressDialog.setMessage("Punlishing ...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();

                            String itemID = itemDataBundle.getString(SOS_API.KEY_ITEM_ID);
                            sosApi.publishItem(new SOS_API.CallbacksProduct() {
                                @Override
                                public void onItemPublishResult(int resultCode, String resultData) {
                                    //Log.e(TAG, "onItemPublishResult: code -> " + resultCode + ", resultData -> " + resultData );
                                    if(resultCode == NETWORK_RESULT_CODES.RESULT_CODE_SUCCESS){

                                        Toast.makeText(ActivityViewItemDetails.this, "Item published waiting for granting", Toast.LENGTH_SHORT).show();
                                        itemDataBundle.putString(Product.KEY_PD_STAT, Product.PD_STAT_WAITING + "");
                                        updateItemStatInfo();
                                    }else{
                                        Toast.makeText(ActivityViewItemDetails.this, "Failed to pubish item", Toast.LENGTH_LONG).show();
                                    }

                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onLoadAllItemsResult(int code, List<ProductMyProducts> products) {

                                }

                                @Override
                                public void onLoadAllItemsNetworkError(String message) {

                                }
                            }, itemID);



                        }else{
                            Toast.makeText(ActivityViewItemDetails.this, HM.RGS(ActivityViewItemDetails.this, R.string.tmsgPwdNotCorrect), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setCancelable(false)
                .setNegativeButton("CANCEL", null).show();





        ////////


    }
}
