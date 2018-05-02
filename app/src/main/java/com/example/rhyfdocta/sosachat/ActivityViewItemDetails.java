package com.example.rhyfdocta.sosachat;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.HelperObjects.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.HelperObjects.HM;
import com.example.rhyfdocta.sosachat.HelperObjects.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.File;
import java.util.List;

/**
 * Created by rhyfdocta on 11/8/17.
 */

public class ActivityViewItemDetails extends AppCompatActivity implements SOS_API.SOSApiListener,
 GlideBitmapLoaderCallbacks{


    private static final String TAG = "Test Debug";
    private static final int KEY_CONTACT_BY_PHONE = 0;
    private static final int KEY_CONTACT_BY_SMS = 1;
    private static final int KEY_CONTACT_BY_EMAIL = 2;
    private static final int KEY_CONTACT_BY_SOSDM = 3;
    Bundle itemDataBundle;
    TextView tvItemName, tvItemPrice, tvDatePosted, tvItemVendor, tvItemDesc, tvItemQual, tvItemViewsCount;
    ImageView ivItemMainPic;
    Button btnAddToWishList, btnContactVendor;
    //final String[] contactChoices = new String[4];
    String phoneNumber;

    /*public static int KEY_CHOICE_PHONE_CALL = 0;
    public static int KEY_CHOICE_SMS = 1;
    public static int KEY_CHOICE_EMAIL = 2;
    public static int KEY_CHOICE_SOSACHAT = 3;*/

    AlertDialog alertContactChoice;
    //Bundle pdVendor = new Bundle();
    private String email;
    //private String vendorUID;
    boolean itemIsMine = false;
    private LinearLayout llVendorCont, llItemViewsCount, llDateSold;
    SOS_API sosApi;
    //private LinearLayout ;

    private String itemPrice;
    private View customView;
    ;


    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);


        sosApi = new SOS_API(this);

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


        btnAddToWishList = (Button) findViewById(R.id.btnAddToWishList);
        btnContactVendor = (Button) findViewById(R.id.btnContactVendor);

        setUpBtnsListeners();





        tvItemName = (TextView) findViewById(R.id.tvItemName);
        tvItemPrice = (TextView) findViewById(R.id.tvItemPrice);
        tvItemVendor = (TextView) findViewById(R.id.tvItemVendor);
        //tvItemVendor.setTag(R.id.TAG_USER_ID, itemDataBundle.getString(SOS_API.KEY_ACC_DATA_USER_ID));
        tvItemDesc = (TextView) findViewById(R.id.tvItemDesc);
        tvItemQual = (TextView) findViewById(R.id.tvItemQuality);
        ivItemMainPic = (ImageView) findViewById(R.id.ivProdMainPic);
        llVendorCont = (LinearLayout) findViewById(R.id.llVendorCont);
        llItemViewsCount = (LinearLayout) findViewById(R.id.llItemViewsCount);
        llDateSold = (LinearLayout) findViewById(R.id.llDatePosted);
        tvItemViewsCount = (TextView) findViewById(R.id.tvItemViewsCount);
        tvDatePosted = (TextView) findViewById(R.id.tvDateItemPosted);

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


    }

    private void updateItemViewsCount() {


        if(itemIsMine == false) {
            Log.e(TAG, "updateItemViewsCount: id -> " + itemDataBundle.getString(SOS_API.KEY_ITEM_ID));
            //sosApi.updateItemViewsCount(this,itemDataBundle.getString(SOS_API.KEY_ITEM_ID));

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


        }else{
            btnAddToWishList.setVisibility(View.VISIBLE);
            btnContactVendor.setVisibility(View.VISIBLE);
            llVendorCont.setVisibility(View.VISIBLE);

            //sosApi.updateItemViewsCount();
            // TODO: 1/4/2018 NEXT FEATURE USER PROFILE
            tvItemVendor.setClickable(false);
            tvItemVendor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Toast.makeText(getApplicationContext(),
                            "Show " + tvItemVendor.getText().toString() +
                    "'s profile! ID : " + itemDataBundle.getString(SOS_API.KEY_ACC_DATA_USER_ID) , Toast.LENGTH_SHORT).show();*/

                    //showVendorsProfile(itemDataBundle);
                }
            });

        }

        initContactVendorDialog();

    }

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

        tvMobile.setText(itemDataBundle.getString(SOS_API.KEY_ACC_DATA_MOBILE));
        tvSMS.setText(itemDataBundle.getString(SOS_API.KEY_ACC_DATA_MOBILE));
        tvEmail.setText(itemDataBundle.getString(SOS_API.KEY_ACC_DATA_EMAIL));


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


        getMenuInflater().inflate(R.menu.ctx_menu_product_image, menu);
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

    private void loadItemDataFromBundle(Bundle bundle) {


        tvItemName.setText(bundle.getString(Product.KEY_PD_NAME));
        tvItemPrice.setText(itemPrice);
        //tvItemVendor.setText("");
        tvItemDesc.setText(bundle.getString(Product.KEY_PD_DESC));
        tvItemVendor.setText(bundle.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));
        if(itemIsMine) tvItemVendor.setText(HM.RGS(this, R.string.postedByMe));
        tvDatePosted.setText(bundle.getString(SOS_API.KEY_ITEM_DATE_ADDED));
        String viewsCount = bundle.getString(SOS_API.KEY_ITEM_ITEM_VIEWS_ACCOUNT);

        tvItemViewsCount.setText(viewsCount);

        String qual = HM.GPQF(this, new Integer(bundle.getString(Product.KEY_PD_QUAL)).intValue());


        tvItemQual.setText(qual);

        String itemsPixPath = SOS_API.DIR_PATH_PRODUCTS_PIX;

        Uri picUri = Uri.parse(itemsPixPath.concat(bundle.getString(Product.KEY_PD_UNIQUE_NAME) + "_main.jpg"));
        //final String imgName = bundle.getString(Product.KEY_PD_UNIQUE_NAME);
        final String pixPath = SOS_API.DIR_PATH_PRODUCTS_PIX + bundle.getString(Product.KEY_PD_UNIQUE_NAME) + "_main.jpg";
        String picName = bundle.getString(Product.KEY_PD_UNIQUE_NAME);

        /*Picasso.with(getApplicationContext()).load(picUri).error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).centerInside().resize(450,450).into(ivItemMainPic, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Overr ide
            public void onError() {
                Log.e("PICASSO  ", "onError: PICASSO ITEM DETAILS ERROR \nLink : " + picUri.toString() );
            }
        });*/

        // TODO: 1/26/2018 LOAD PICTURES FROM CACHE
        String cachePath = BitmapCacheManager.GET_PIC_CACHE_PATH(BitmapCacheManager.PIC_CACHE_PATH_TYPE_RECENT_ITEMS, picName + "_main.jpg");
        if(BitmapCacheManager.FILE_EXISTS(cachePath)){
            picUri = Uri.fromFile(new File(cachePath));


            Log.e(TAG, "PIC_PATH : -> " + picUri.toString() );

            //Toast.makeText(this, "Loade from cache", Toast.LENGTH_SHORT).show();

        }else{
            Log.e(TAG, "NO_CACHE -> " + picUri.toString() );
            //Toast.makeText(this, "Loade from network", Toast.LENGTH_SHORT).show();
        }


        Glide.with(this)
                .load(picUri)
                .asBitmap()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_error)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>(450,450) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        ActivityViewItemDetails.this.onBitmapShouldBeSaved(resource, pixPath);

                        ivItemMainPic.setImageBitmap(resource);
                    }
                });

        //Log.e(TAG, "loadItemDataFromBundle: THE BUNDLE -> " + itemDataBundle.toString() );

        //prepareGUI();

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
*/
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
                sosApi.toggleAlertDialogResponseWithMessage(true, HM.RGS(ActivityViewItemDetails.this, R.string.msgErrorAddingWishlistItem));
            }

            @Override
            public void onNetworkError(String msg) {
                sosApi.toggleAlertDialogResponseWithMessage(true, HM.RGS(ActivityViewItemDetails.this, R.string.msgErrorInternetConnection));

            }

            @Override
            public void onItemRemoveError(Bundle pd) {

            }

            @Override
            public void onItemRemoveSuccess(Bundle pd) {

            }


        },itemDataBundle.getString(SOS_API.KEY_ITEM_ID));




    }

    private void onShowContactVendorDialog() {
        //showMessage(getResources().getString(R.string.titleAccNotActive), getResources().getString(R.string.msgAccNotActive));

        /*
        String phoneNumber = "4567 8987 6785 8986";
        String uri = "tel:" +"+243997475663" ;
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse(uri));

        startActivity(intent);*/




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
    public void onBitmapShouldBeSaved(Bitmap bitmap, String picUrl) {
        // TODO: 5/1/2018 CACHE PRODUCTS IMAGES

        //Log.e(TAG, "onBitmapShouldBeSaved: url -> " + picUrl );

        //Log.e(TAG, "CATS onBitmapShouldBeSaved: url -> " + picUrl );
        String[] splits = picUrl.split("/");
        String dirName = SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS;
        String picName = splits[splits.length-1];
        //Log.e(TAG, "onBitmapShouldBeSaved:" );

        Log.e(TAG, "FILE EX : -> " + sosApi.getBitmapCacheManager().saveImage(bitmap, picName,dirName));
    }
}
