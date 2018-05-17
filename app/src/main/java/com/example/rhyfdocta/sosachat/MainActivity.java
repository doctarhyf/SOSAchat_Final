package com.example.rhyfdocta.sosachat;

import android.Manifest;
import android.app.Activity;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonArrayRequest;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.HelperObjects.HM;
import com.example.rhyfdocta.sosachat.HelperObjects.HelperMethods;
import com.example.rhyfdocta.sosachat.Interfaces.GlideBitmapLoaderCallbacks;
import com.example.rhyfdocta.sosachat.ObjectsModels.HomeCategoryItem;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.adapters.AdapterHomeCategories;
import com.example.rhyfdocta.sosachat.adapters.AdapterRecentItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.rhyfdocta.sosachat.API.SOS_API.REQ_PERMISSION_SAVE_BITMAP;

public class MainActivity extends AppCompatActivity implements SOS_API.SOSApiListener {

    public static final String TAG = "SOSAchatTAG" ;
    private static final int REQ_CODE = 1001;
    public static final String KEY_CATEGORY_NAME = "catName";

    public static final String KEY_CAT_NAME_CARS = "Cars";
    public static final String KEY_CAT_NAME_ELEC = "Electronics";
    //private static final int REQ_CODE_CON = 2342;
    public static final String KEY_SOSACHAT_FOLDER = "sdcard/SOSAchat";
    public static final String KEY_SOSACHAT_PIX_DIR = "SOSAchat";


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
    private static boolean FIRST_LAUNCH = true;
    private RecyclerView rvRecentItems;
    private RecyclerView.Adapter adapterRecentItems;
    private RecyclerView.LayoutManager layoutManagerRecentItems;
    private LinearLayout llPbLoadingRecentItems ;
    private Bitmap cacheBitmap;
    private String cachePicUrl;
    private String cacheDirName;
    //BitmapCacheManager bitmapCacheManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SOS_API.POST_MARSHMALLOW = true;
        }

        //bitmapCacheManager = new BitmapCacheManager(this);

        llPbLoadingRecentItems = findViewById(R.id.llPbLoadingRecentItems);

        sosApi = new SOS_API(this);

        rvCats = findViewById(R.id.rvCats);
        layoutManagerCats = new GridLayoutManager(this,2);
        rvCats.setLayoutManager(layoutManagerCats);

        rvRecentItems = findViewById(R.id.rvRecentItems);
        layoutManagerRecentItems = new LinearLayoutManager(this);
        rvRecentItems.setLayoutManager(layoutManagerRecentItems);

        getSupportActionBar().setSubtitle(sosApi.GSV(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));//getResources().getString(R.string.moto));
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //requestQueue = Volley.newRequestQueue(this);
        alertDialogProcessing = HelperMethods.getAlertDialogProcessingWithMessage(this, HelperMethods.getStringResource(this, R.string.pbMsgRefreshing), false);
        rootView = (ScrollView) findViewById(R.id.rootView);
        etSearch = (EditText) findViewById(R.id.etSearch);
        footer = (LinearLayout) findViewById(R.id.footer);
        inflater = (LayoutInflater) getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        //featProdRootView = (LinearLayout) findViewById(R.id.featProdCont);


        // TODO: 11/10/17 REACTI VATE NO NETWORK ACTIVITY AFTER TEST

        /*
        if(isOnline() == false){


            Intent intent = new Intent(this, ActivityNoNetwork.class);
            startActivityForResult(intent, REQ_CODE_CON);
            //pbLoadingFeatItems.setVisibility(View.GONE);
            alertDialogProcessing.dismiss();
            return;
        }*/

        //alertDialogProcessing.dismiss();

        initFeaturedCategories();
        loadRecentItems();


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


        //sosApi.getSessionData();


        if(getIntent().getBooleanExtra(SOS_API.KEY_USER_IS_ADMIN, false)){
            HM.T(this, 100, HM.TLS);
        }


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


        adapterRecentItems = new AdapterRecentItems(this, recentProducts, new GlideBitmapLoaderCallbacks() {
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
        });



        rvRecentItems.setAdapter(adapterRecentItems);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rvRecentItems.setLayoutParams(layoutParams);

        alertDialogProcessing.dismiss();
        footer.setVisibility(View.VISIBLE);

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
                int scrollY = ((TextView) findViewById(R.id.titleLatestItems)).getTop();
                rootView.smoothScrollBy(0, scrollY);
            }
        }, 100);


    }

    private void loadRecentItems() {


        if(refreshing) {
            //featProdRootView.removeAllViews();
            //featProdRootView.addView(pbLoadingFeatItems);
            //pbLoadingFeatItems.setVisibility(View.VISIBLE);
            alertDialogProcessing.show();
            footer.setVisibility(View.GONE);

        }

        featItems.clear();
        recentProducts.clear();


        sosApi.loadRecentItems(new LoadRecentItemsListener());

        Log.e(TAG, "loadRecentItems: RES_MAIN" );


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
        }

        @Override
        public void onRecentItemsEmpty() {
            llPbLoadingRecentItems.setVisibility(View.GONE);
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
            sosApi.TADRWM(true, "onNetworkError:\nError : " + msg);
            alertDialogProcessing.hide();
            llPbLoadingRecentItems.setVisibility(View.GONE);
        }

        @Override
        public void onParseJsonError(String s) {
            sosApi.TADRWM(true, "onParseJsonError:\nJSON : " + s);
            alertDialogProcessing.hide();
            llPbLoadingRecentItems.setVisibility(View.GONE);
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
        String q = etSearch.getText().toString().trim();
        //Toast.makeText(this, q, Toast.LENGTH_SHORT).show();

        if(q.length() > 0) {

            sosApi.search(this,q);

        }else{
            Toast.makeText(this, "Please write something!", Toast.LENGTH_SHORT).show();
        }
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

        if(item.getItemId() == R.id.menuCheckInquiry){
            //Toast.makeText(this, "On check inquiry", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ActivityInquiriesList.class);
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

            sosApi.logout();

            Intent intent = new Intent(this, ActivityLoginSignup.class);
            startActivity(intent);
            FIRST_LAUNCH = true;

        }

        if(item.getItemId() == R.id.menuInquire){
            Intent intent = new Intent(this, ActivityInquiryPost.class);
            startActivity(intent);
        }

        if(item.getItemId() == R.id.menuViewAllProducts){

            Intent intent = new Intent(this,ActivityViewAllProducts.class );
            startActivity(intent);

        }


        if(item.getItemId() == R.id.menuReloadRecentItems){
            refreshing = true;


            // TODO: 11/10/17  REACTIVATE NO NETWORK ACTIVITY AFTER TEST

            /*
            if(isOnline() == false){

                Intent intent = new Intent(this, ActivityNoNetwork.class);
                startActivityForResult(intent, REQ_CODE_CON);

                //pbLoadingFeatItems.setVisibility(View.GONE);

                alertDialogProcessing.dismiss();

            }*/


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
            sosApi.toggleAlertDialogResponseWithMessage(true, HM.RGS(this, R.string.msgErrorInternetConnection));
            alertDialogProcessing.hide();
            return;
        }



        this.recentProducts = featureProducts;
        if(featureProducts.size() == 0){

            sosApi.toggleAlertDialogResponseWithMessage(true, HM.RGS(this, R.string.msgNoRecentItems));


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
        }

        adapterCats = new AdapterHomeCategories(list, new AdapterHomeCategories.HomeCategoryItemListener() {
            @Override
            public void onItemClicked(HomeCategoryItem homeCategoryItem) {
                Log.e(TAG, "Home Cat Clicked : " + homeCategoryItem.getTitle() );
                Bundle data = new Bundle();
                data.putString(SOS_API.KEY_ITEM_CATEGORY_ID, homeCategoryItem.getData().getString(SOS_API.KEY_ITEM_CATEGORY_ID));

                Intent intent = new Intent(getApplicationContext(), ActivityViewAllTypesInCat.class);
                intent.putExtras(homeCategoryItem.toBundle());
                startActivity(intent);
            }

            @Override
            public void onBitmapShouldBeSaved(Bitmap bitmap, String picUrl) {

                Log.e(TAG, "CATS saveBitmapToLocalCache: url -> " + picUrl );
                String[] splits = picUrl.split("/");
                String dirName = SOS_API.DIR_NAME_PIX_CACHE_HOME_CATS;
                String picName = splits[splits.length-1];
                Log.e(TAG, "saveBitmapToLocalCache:" );
            }
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
