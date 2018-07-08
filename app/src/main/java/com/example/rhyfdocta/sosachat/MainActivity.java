package com.example.rhyfdocta.sosachat;

import android.app.Activity;
import android.app.MediaRouteButton;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.Helpers.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.HomeCategoryItem;
import com.example.rhyfdocta.sosachat.ObjectsModels.LookingFor;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.adapters.AdapterHomeCategories;
import com.example.rhyfdocta.sosachat.adapters.AdapterLookingFor;
import com.example.rhyfdocta.sosachat.adapters.AdapterRecentItems;
import com.example.rhyfdocta.sosachat.app.SOSApplication;
import com.example.rhyfdocta.sosachat.debug.SOSDebug;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.rhyfdocta.sosachat.API.SOS_API.REQ_PERMISSION_SAVE_BITMAP;

public class MainActivity extends AppCompatActivity implements SOS_API.SOSApiListener,
AdapterLookingFor.CallBacks,
        SOS_API.CallbacksLogout
{

    public static final String TAG = "SOSAchatTAG" ;
    private static final int REQ_CODE = 1001;
    public static final String KEY_CATEGORY_NAME = "catName";

    public static final String KEY_CAT_NAME_CARS = "Cars";
    public static final String KEY_CAT_NAME_ELEC = "Electronics";
    //private static final int REQ_CODE_NO_INTERNET_CONNECTION = 2342;
    public static final String KEY_SOSACHAT_FOLDER = "sdcard/SOSAchat";
    public static final String KEY_SOSACHAT_PIX_DIR = "SOSAchat";
    private static final int MAIN_SLIDESHOW_FLIP_INTERVAL = 3000;
    private static final long SLIDE_SHOW_IMAGE_DURATION = 700;


    EditText etSearch;
    //TextView tvHome, tvExpose, tvTroque, tvHelp, tvSetts;
    ArrayList<LinearLayout> featItems = new ArrayList<>();

    LayoutInflater inflater;
    //LinearLayout featProdRootView;
    List<Product> recentProducts = new ArrayList<>();

    //ProgressBar pbLoadingFeatItems;

    LinearLayout footer;

    boolean refreshing = false;

    private ScrollView rootView;

    JsonArrayRequest requestFeaturedItems;
    //private RequestQueue requestQueue;
    AlertDialog alertDialogProcessing;
    private RecyclerView rvCats;
    private RecyclerView.Adapter adapterCats;
    private RecyclerView.LayoutManager layoutManagerCats;
    SOS_API sosApi;
    public static boolean FIRST_LAUNCH = true;
    private RecyclerView rvRecentItems;
    private RecyclerView.Adapter adapterRecentItems;
    private RecyclerView.LayoutManager layoutManagerRecentItems;
    private LinearLayout llPbLoadingRecentItems ;
    private Bitmap cacheBitmap;
    private String cachePicUrl;
    private String cacheDirName;
    private String searchKeyword;
    private ViewFlipper vfMain;
    //BitmapCacheManager bitmapCacheManager;
    private TextView tvNoConn;
    private AdapterLookingFor adapterLookingFor;
    private ArrayList<LookingFor> lookingFors;
    //private RecyclerView.LayoutManager layoutManagerLookingfor;
    private ListView lvLookifor;
    private TextView tvMsgLookingFor;
    private float startX;
    private TextView tvRecentItemsEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sosApi = SOSApplication.getInstance().getSosApi();//new SOS_API(this);

        tvRecentItemsEmpty = findViewById(R.id.tvRecentItemsEmpty);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SOS_API.POST_MARSHMALLOW = true;
        }

        tvMsgLookingFor = findViewById(R.id.tvMsgLookingFor);

        lookingFors = new ArrayList<>();

        lvLookifor = findViewById(R.id.lvLookingfor);
        adapterLookingFor = new AdapterLookingFor(this, lookingFors, this);
        lvLookifor.setAdapter(adapterLookingFor);

        lvLookifor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LookingFor lookingFor = lookingFors.get(position);

                Intent intent = new Intent(MainActivity.this, ActivityLookingForView.class);
                intent.putExtras(lookingFor.toBundle());
                startActivity(intent);
            }
        });


        tvNoConn = findViewById(R.id.tvNoConn);

        vfMain = findViewById(R.id.vfMain);
        vfMain.setFlipInterval(MAIN_SLIDESHOW_FLIP_INTERVAL);
        vfMain.setAutoStart(true);
        vfMain.startFlipping();

        Animation imgAnimationIn = AnimationUtils.loadAnimation(
                this,
                android.R.anim.slide_in_left
        );
        imgAnimationIn.setDuration(SLIDE_SHOW_IMAGE_DURATION);
        vfMain.setInAnimation(imgAnimationIn);

        Animation imgAnimationOut = AnimationUtils.loadAnimation(
                this,
                android.R.anim.slide_out_right
        );
        imgAnimationIn.setDuration(SLIDE_SHOW_IMAGE_DURATION);
        vfMain.setOutAnimation(imgAnimationOut);

        vfMain.setOnTouchListener(new View.OnTouchListener() {
                                  @Override
                                  public boolean onTouch(View view, MotionEvent event) {
                                      int action = event.getActionMasked();
                                      switch (action) {
                                          case MotionEvent.ACTION_DOWN:
                                              startX = event.getX();
                                              break;
                                          case MotionEvent.ACTION_UP:
                                              float endX = event.getX();
                                              float endY = event.getY();
//swipe right
                                              if (startX < endX) {
                                                  MainActivity.this.vfMain.showNext();
                                              }
//swipe left
                                              if (startX > endX) {
                                                  MainActivity.this.vfMain.showPrevious();
                                              }
                                              break;
                                      }

                                      return true;
                                  }
                              });


        HelperMethods.GetViewSize(new HelperMethods.CallbacksViewSize() {
            @Override
            public void onSizeFound(double[] size) {
                Log.e(TAG, "onSizeFound: -> " + size );
            }
        }, vfMain);

        llPbLoadingRecentItems = findViewById(R.id.llPbLoadingRecentItems);



        rvCats = findViewById(R.id.rvCats);
        layoutManagerCats = new GridLayoutManager(this,2);
        rvCats.setLayoutManager(layoutManagerCats);

        rvRecentItems = findViewById(R.id.rvRecentItems);
        layoutManagerRecentItems = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvRecentItems.setLayoutManager(layoutManagerRecentItems);

        getSupportActionBar().setSubtitle(sosApi.GSV(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));//getResources().getString(R.string.moto));
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //requestQueue = Volley.newRequestQueue(this);
        alertDialogProcessing = HelperMethods.getAlertDialogProcessingWithMessage(this, HelperMethods.getStringResource(this, R.string.pbMsgRefreshing), false);
        rootView = findViewById(R.id.rootView);
        etSearch = findViewById(R.id.etSearch);
        footer = findViewById(R.id.footer);
        inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //featProdRootView = (LinearLayout) findViewById(R.id.featProdCont);


        // TODO: 11/10/17 REACTI VATE NO NETWORK ACTIVITY AFTER TEST




        initFeaturedCategories();

        //if(SOS_API.isOnline(this)) {
            loadRecentItems();
        //}else{
            //toggleNoConnGUI(false);
       // }


        rootView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = rootView.getScrollY(); // For ScrollView
                int scrollX = rootView.getScrollX(); // For HorizontalScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES
                int svh = rootView.getChildAt(0).getHeight()  - rootView.getHeight();
                //Log.e(TAG, "onScrollChanged: sy : " + scrollY + " sx : " + scrollX + " svh : " +  svh);
            }
        });


        /*TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        String msg = "Device ID : " + telephonyManager.getDeviceId() + "\nSim Country ISO : " +
                telephonyManager.getSimCountryIso() + "\nSim Serial Number : " +
                telephonyManager.getSimSerialNumber() + "\nNetwork Operator Name : " +
                telephonyManager.getNetworkOperatorName() + "\nSubscriberID : " +
                telephonyManager.getSubscriberId() + "\nNetwork Operator : " +
                telephonyManager.getNetworkOperator()
                ;

        showMessage("Details", msg);*/


        if(getIntent().getBooleanExtra(SOS_API.KEY_USER_IS_ADMIN, false)){
            HM.T(this, 100, HM.TLS);
        }



    }

    @Override
    public void onLookingForsLoaded(ArrayList<LookingFor> inquiries) {
        lookingFors = inquiries;


        if(inquiries.size() == 0){
            lvLookifor.setVisibility(View.GONE);
            tvMsgLookingFor.setVisibility(View.VISIBLE);
            //tvMsgLookingFor.setText(getResources().getString(R.string.msgNoLookingforsYet));
        }else{
            lvLookifor.setVisibility(View.VISIBLE);
            tvMsgLookingFor.setVisibility(View.GONE);
            adapterLookingFor = new AdapterLookingFor(this, inquiries, this);

            lvLookifor.setAdapter(adapterLookingFor);

        }

        adapterLookingFor.notifyDataSetChanged();
    }

    @Override
    public void onLookingForsLoadError(boolean isNetworkError, String message) {
        Log.e(TAG, "onLookingForsLoadError: -> " + message );
        lvLookifor.setVisibility(View.GONE);
        tvMsgLookingFor.setVisibility(View.VISIBLE);
        tvMsgLookingFor.setText(HM.RGS(this, R.string.msgErrorLoadingLookingFors));
    }

    @Override
    public void onLookingForsEmpty() {
        Log.e(TAG, "onLookingForsEmpty: " );
        lvLookifor.setVisibility(View.GONE);
        tvMsgLookingFor.setVisibility(View.VISIBLE);

    }

    private void toggleNoConnGUI(boolean connected) {

        LinearLayout llFeatCats = findViewById(R.id.contFeatCats);
        TextView tvLatestItems = findViewById(R.id.titleLatestItems);
        TextView tvAllCats = findViewById(R.id.titleAllCategories);
        Button btnSearch = findViewById(R.id.btnSearch);
        TextView tvTitleExRate = findViewById(R.id.titleExRates);
        TextView tvLatesL4s = findViewById(R.id.titleLookingFors);
        LinearLayout llLooking4 = findViewById(R.id.llLookingfors);


        if(connected){

            tvTitleExRate.setVisibility(View.VISIBLE);
            tvLatesL4s.setVisibility(View.VISIBLE);
            llLooking4.setVisibility(View.VISIBLE);
            tvAllCats.setVisibility(View.VISIBLE);
            tvNoConn.setVisibility(View.GONE);
            llFeatCats.setVisibility(View.VISIBLE);
            tvLatestItems.setVisibility(View.VISIBLE);
            footer.setVisibility(View.VISIBLE);
            btnSearch.setEnabled(true);

            //llPbLoadingRecentItems.setVisibility(View.GONE);
        }else{
            tvTitleExRate.setVisibility(View.GONE);
            tvLatesL4s.setVisibility(View.GONE);
            llLooking4.setVisibility(View.GONE);
            btnSearch.setEnabled(false);
            tvAllCats.setVisibility(View.GONE);
            tvNoConn.setVisibility(View.VISIBLE);
            llFeatCats.setVisibility(View.GONE);
            tvLatestItems.setVisibility(View.GONE);
            footer.setVisibility(View.GONE);
            //llPbLoadingRecentItems.setVisibility(View.GONE);
        }

        llPbLoadingRecentItems.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //requestQueue = null;
        requestFeaturedItems = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        etSearch.setText("");
        //loadRecentItems();
        //Toast.makeText(this, "onResume()", Toast.LENGTH_SHORT).show();
        if(!FIRST_LAUNCH) {
            //Log.e(TAG, "onResume: RES_MAIN --- LONDING");

            if(sosApi.GSV(SOS_API.KEY_AUTOREFRESH_RECENT_ITEMS).equals("true")) {
                refreshing = true;
                loadRecentItems();
            }

        }else{
            FIRST_LAUNCH = false;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Toast.makeText(this, "onStart()", Toast.LENGTH_SHORT).show();
        //loadRecentItems();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //Toast.makeText(this, "onRestart()", Toast.LENGTH_SHORT).show();
        //loadRecentItems();
    }

    private void displayRecentItems() {


        adapterRecentItems = new AdapterRecentItems(this, recentProducts, new AdapterRecentItems.Callbacks() {
            @Override
            public void onItemClicked(Product pd) {

                //HM.T(MainActivity.this, pd.getPdName(), HM.TLL);
                Intent intent = new Intent(MainActivity.this, ActivityViewItemDetails.class);
                Bundle bundle = new Bundle();
                bundle.putAll(pd.toBundle());
                bundle.putAll(pd.getData());
                //String desc = bundle.getString(Product.KEY_PD_DESC);
                boolean itemIsMine = false;

                if(bundle.getString(SOS_API.KEY_ACC_DATA_USER_ID).equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_USER_ID))){
                    itemIsMine = true;
                }
                bundle.putBoolean(SOS_API.KEY_ITEM_IS_MINE, itemIsMine);
                intent.putExtras(bundle);
                startActivity(intent);
                //HM.T(MainActivity.this, "" + rvRecentItems.computeHorizontalScrollRange(), HM.TLS);

            }
        });

        /*adapterRecentItems = new AdapterRecentItems(this, recentProducts, new CallbacksBitmapLoading() {
            @Override
            public void onItemClicked(Product pd) {
                //HM.T(MainActivity.this, pd.getPdName(), HM.TLL);
                Intent intent = new Intent(MainActivity.this, ActivityViewItemDetails.class);
                Bundle bundle = new Bundle();
                bundle.putAll(pd.toBundle());
                bundle.putAll(pd.getData());

                boolean itemIsMine = false;

                if(bundle.getString(SOS_API.KEY_ACC_DATA_USER_ID).equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_USER_ID))){
                    itemIsMine = true;
                }
                bundle.putBoolean(SOS_API.KEY_ITEM_IS_MINE, itemIsMine);
                intent.putExtras(bundle);
                startActivity(intent);
                //HM.T(MainActivity.this, "" + rvRecentItems.computeHorizontalScrollRange(), HM.TLS);

            }

            @Override
            public void saveBitmapToLocalCache(Bitmap bitmap, String picUrl, String dirName) {

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        cacheBitmap = bitmap;
                        cachePicUrl = picUrl;
                        cacheDirName = dirName;
                        Log.e(TAG, "FILE EX : -> " + sosApi.getBitmapCacheManager().saveBitmapToCache(cacheBitmap, cachePicUrl, cacheDirName));

                    } else {

                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, REQ_PERMISSION_SAVE_BITMAP);

                    }
                }else{
                    cacheBitmap = bitmap;
                    cachePicUrl = picUrl;
                    cacheDirName = dirName;
                    Log.e(TAG, "FILE EX : -> " + sosApi.getBitmapCacheManager().saveBitmapToCache(cacheBitmap, cachePicUrl, cacheDirName));

                }

            }
        });*/



        rvRecentItems.setAdapter(adapterRecentItems);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rvRecentItems.setLayoutParams(layoutParams);

        alertDialogProcessing.dismiss();
        //footer.setVisibility(View.VISIBLE);
        toggleNoConnGUI(true);

        if(refreshing == true){
            refreshing = false;
            Toast.makeText(this, getResources().getString(R.string.msgRecentItemsLoaded), Toast.LENGTH_SHORT).show();
            smoothScrollToBottom();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);



        if(requestCode == REQ_PERMISSION_SAVE_BITMAP){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                Log.e(TAG, "FILE EX : -> " +  sosApi.getBitmapCacheManager().saveBitmapToCache(cacheBitmap,  cachePicUrl, cacheDirName));

            }else{
                Toast.makeText(this, "We need your permission to access to save cache!", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void smoothScrollToBottom() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 1000ms
                int scrollY = findViewById(R.id.titleLatestItems).getTop();
                rootView.smoothScrollBy(0, scrollY);
            }
        }, 100);


    }

    private void loadRecentItems() {


        sosApi.loadLookingFors(this, false, 5);
        if(refreshing) {
            //featProdRootView.removeAllViews();
            //featProdRootView.addView(pbLoadingFeatItems);
            //pbLoadingFeatItems.setVisibility(View.VISIBLE);
            alertDialogProcessing.show();
            footer.setVisibility(View.GONE);
            //toggleNoConnGUI(fa);

        }

        featItems.clear();
        recentProducts.clear();


        sosApi.loadRecentItems(new LoadRecentItemsListener());

        Log.e(TAG, "loadRecentItems: RES_MAIN" );


    }

    private int dbgCount = 0;

    public void showDebug(View view) {

        if(dbgCount >= 5){
            dbgCount = 0;
            SOSDebug.showDebugDialog(this);
        }

        dbgCount ++;
    }

    @Override
    public void onLogoutResult(boolean logoutSuccess) {

        if(logoutSuccess){
            Intent intent = new Intent(MainActivity.this, ActivityLoginSignup.class);
            startActivity(intent);
            FIRST_LAUNCH = true;
        }else{
            Toast.makeText(this, "Failed to logout, check network connection!", Toast.LENGTH_LONG).show();
        }

        SOSApplication.GI().DPD();
    }


    private class LoadRecentItemsListener implements SOS_API.ListenerLoadRecentItems{



        @Override
        public void onRecentItemsLoaded(List<Product> products) {
            //Toast.makeText(MainActivity.this, "onRecentItemsLoaded", Toast.LENGTH_SHORT).show();
            //sosApi.TADRWM(true, "onRecentItemsLoaded" );
            Log.e(TAG, "onRecentItemsLoaded: len -> " + products.size() );
            onLoadRecentItemsResult(products, false);
            alertDialogProcessing.hide();
            llPbLoadingRecentItems.setVisibility(View.GONE);
            tvRecentItemsEmpty.setVisibility(View.GONE);
            Log.e(TAG, ":KAKI " );
        }

        @Override
        public void onRecentItemsEmpty() {

            tvRecentItemsEmpty.setVisibility(View.VISIBLE);
            llPbLoadingRecentItems.setVisibility(View.GONE);
            //toggleNoConnGUI(false);
            //Toast.makeText(MainActivity.this, "onRecentItemsEmpty", Toast.LENGTH_SHORT).show();
            String msg = HM.RGS(MainActivity.this, R.string.dgMsgNoRecentItemsPosted) ;
            String btnExposeItem = HM.RGS(MainActivity.this, R.string.btnExposeItem);
            String btnOk = HM.RGS(MainActivity.this, R.string.btnOk);

            SOS_API.DWPNB(MainActivity.this, true, msg, btnExposeItem,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(MainActivity.this, ActivityPostItem.class);
                            startActivity(intent);
                        }
                    }, btnOk,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    },
            null);
            alertDialogProcessing.hide();
        }

        @Override
        public void onNetworkError(String msg) {
            //Toast.makeText(MainActivity.this, "onNetworkError", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onNetworkError: Message -> " + msg );
            sosApi.TADRWM(MainActivity.this, true, MainActivity.this.getResources().getString(R.string.msgServerUnreachable));
            alertDialogProcessing.hide();
            //llPbLoadingRecentItems.setVisibility(View.GONE);
            toggleNoConnGUI(false);
        }

        @Override
        public void onParseJsonError(String s) {
            sosApi.TADRWM(MainActivity.this, true, "onParseJsonError:\nJSON : " + s);
            alertDialogProcessing.hide();
            //llPbLoadingRecentItems.setVisibility(View.GONE);
            toggleNoConnGUI(false);
        }
    }

    public void gotoActivityAllCats(String catId){
        Intent intent = new Intent(this, ActivityViewAllTypesInCat.class);
        startActivity(intent);
    }

    private void initFeaturedCategories() {

        sosApi.loadItemsCatsAndTypes(this);

    }

    private void startActivityCats(String keyCatName) {

        Intent intent = new Intent(this, ActivityViewAllTypesInCat.class);

        if(keyCatName == KEY_CAT_NAME_CARS){
            intent.putExtra(KEY_CATEGORY_NAME, KEY_CAT_NAME_CARS);
        }

        if(keyCatName == KEY_CAT_NAME_ELEC){
            intent.putExtra(KEY_CATEGORY_NAME, KEY_CAT_NAME_ELEC);
        }


        startActivityForResult(intent, REQ_CODE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    public void onMainMenuItemClicked(View v) {

        TextView tv = (TextView) v;

        Toast.makeText(this, tv.getText(), Toast.LENGTH_SHORT).show();




    }

    public void startSearch(View v){
        searchKeyword = etSearch.getText().toString().trim();
        //Toast.makeText(this, q, Toast.LENGTH_SHORT).show();

        /*
        if(searchKeyword.length() > 0) {

            sosApi.search(this,searchKeyword);

        }else{
            Toast.makeText(this, "Please write something!", Toast.LENGTH_SHORT).show();
        }*/

        Intent intent = new Intent(this, ActivityViewAllProducts.class);
        //intent.putExtra(SOS_API.SEARCH_Q, searchKeyword);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //Toast.makeText(this, "Menu clicked", Toast.LENGTH_SHORT).show();



        if(item.getItemId() == R.id.menuAbout){
            showMessage(getResources().getString(R.string.aboutSOS), getResources().getString(R.string.aboutMessage));
        }

        if(item.getItemId() == R.id.menuLookingFor){
            //Toast.makeText(this, "On check inquiry", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ActivityLookingFor.class);
            startActivity(intent);
        }

        // TODO: 12/28/2017 SOS DM NEXT FEATURE
        /*if(item.getItemId() == R.id.menuMessages){
            Intent intent = new Intent(this, ActivityMessages.class);
            startActivity(intent);
        }*/

        if(item.getItemId() == R.id.menuExpose){

            Intent intent = new Intent(this, ActivityPostItem.class);
            startActivity(intent);

        }
        if(item.getItemId() == R.id.menuMyAccount){

            Intent intent = new Intent(this, ActivityMyAccount.class);
            startActivity(intent);

        }

        if(item.getItemId() == R.id.menuAccountSettings){
            Intent intent = new Intent(this, ActivityAccountSettings.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.menuHelp){
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.sosachat.com/help"));
            startActivity(browserIntent);
        }

        if(item.getItemId() == R.id.menuLogout){

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View v = layoutInflater.inflate(R.layout.layout_dialog_input_password, null);
            final EditText etpwd = v.findViewById(R.id.etDgPassword);

            new AlertDialog.Builder(MainActivity.this)
                    //.setTitle(HM.RGS(ActivityAccountSettings.this, R.string.dgTitleInputPassword))
                    .setView(v)
                    .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(etpwd.getText().toString().equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_PASSWORD))) {


                                ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
                                progressDialog.setTitle("Logging out");
                                progressDialog.setMessage("Please wait ...");
                                progressDialog.setCancelable(false);
                                progressDialog.show();

                                sosApi.logout(MainActivity.this);



                            }else{
                                Toast.makeText(MainActivity.this, HM.RGS(MainActivity.this, R.string.tmsgPwdNotCorrect), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setCancelable(false)
                    .setNegativeButton("CANCEL", null).show();




        }



        if(item.getItemId() == R.id.menuViewAllProducts){

            Intent intent = new Intent(this,ActivityViewAllProducts.class );
            startActivity(intent);

        }


        if(item.getItemId() == R.id.menuReloadRecentItems){
            refreshing = true;


            // TODO: 11/10/17  REACTIVATE NO NETWORK ACTIVITY AFTER TEST


            if(!SOS_API.isOnline(this)){

                Intent intent = new Intent(this, ActivityNoNetwork.class);
                startActivity(intent);

                //pbLoadingFeatItems.setVisibility(View.GONE);

                alertDialogProcessing.dismiss();

            }


            loadRecentItems();




        }

        return true;
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


        Intent intent = new Intent(this, ActivitySearchResult.class);
        //intent.put(SOS_API.KEY_NAME_FOUND_PRODUCTS_LIST, foundProducts);
        Log.e(TAG, "onSearchResult: items ids -> " + ids );
        intent.putExtra(SOS_API.KEY_ITEMS_SEARCH_RESULT_IDS, ids);
        intent.putExtra(SOS_API.KEY_SEARCH_KEYWORD, searchKeyword);
        startActivity(intent);

    }

    @Override
    public void onSearchResultError(String error) {

        Toast.makeText(this, "Error : " + error, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPostInquiryResult(String result) {

    }

    @Override
    public void onLoadRecentItemsResult(List<Product> featureProducts, boolean networkError) {


        if(networkError){
            sosApi.toggleAlertDialogResponseWithMessage(MainActivity.this, true, HM.RGS(this, R.string.msgErrorInternetConnection));
            alertDialogProcessing.hide();

            return;
        }



        this.recentProducts = featureProducts;
        if(featureProducts.size() == 0){

            sosApi.toggleAlertDialogResponseWithMessage(MainActivity.this, true, HM.RGS(this, R.string.msgNoRecentItems));


            footer.setVisibility(View.VISIBLE);

        }else{


            displayRecentItems();

        }

        alertDialogProcessing.hide();
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

    List<HomeCategoryItem> list;

    @Override
    public void onLoadItemsCats(JSONArray jsonArray) {


        list = new ArrayList<>();

        try {
        for (int i =0; i < jsonArray.length(); i++){

                JSONObject cat = jsonArray.getJSONObject(i);
                Bundle data = new Bundle();



                String catName = cat.getString(SOS_API.KEY_ITEM_CATEGORY_NAME);
                String catPic = cat.getString(SOS_API.KEY_ITEM_CATEGORY_PIC);
                String catId = cat.getString(SOS_API.KEY_ITEM_CATEGORY_ID);

                data.putString(SOS_API.KEY_ITEM_CATEGORY_ID, catId);

                HomeCategoryItem homeCategoryItem = new HomeCategoryItem(catId, catName, catPic );
                homeCategoryItem.setData(data);

                list.add(homeCategoryItem);


        }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "CATEGORIES onLoadItemsCats: " + e.getMessage() );
        }

        adapterCats = new AdapterHomeCategories(this, list, new AdapterHomeCategories.Callbacks() {
            @Override
            public void onItemClicked(HomeCategoryItem homeCategoryItem) {
                Log.e(TAG, "Home Cat Clicked : " + homeCategoryItem.getTitle() );
                Bundle data = new Bundle();
                data.putString(SOS_API.KEY_ITEM_CATEGORY_ID, homeCategoryItem.getData().getString(SOS_API.KEY_ITEM_CATEGORY_ID));

                Intent intent = new Intent(getApplicationContext(), ActivityViewAllTypesInCat.class);
                intent.putExtras(homeCategoryItem.toBundle());
                startActivity(intent);
            }

            /*@Override
            public void onBitmapShouldBeSaved(Bitmap bitmap, String picUrl) {

                Log.e(TAG, "CATS saveBitmapToLocalCache: url -> " + picUrl );
                String[] splits = picUrl.split("/");
                String dirName = SOS_API.DIR_NAME_PIX_CACHE_HOME_CATS;
                String picName = splits[splits.length-1];
                Log.e(TAG, "saveBitmapToLocalCache:" );
            }*/
        });
        rvCats.setAdapter(adapterCats);

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


}
