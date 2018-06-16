package com.example.rhyfdocta.sosachat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.ObjectsModels.HomeCategoryItem;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.adapters.AdapterTypesItem;
import com.example.rhyfdocta.sosachat.app.SOSApplication;
import com.squareup.picasso.Clear;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewAllTypesInCat extends AppCompatActivity implements AdapterTypesItem.CallBacks, SOS_API.SOSApiListener {


    private static final String TAG = "TAG";
    JsonArrayRequest req;
    RequestQueue queue;
    String apiCall;
    List<TypesItem> objects = new ArrayList<>();
    ProgressBar pb;
    ListView lv;
    private String catPicPath;
    //AdapterTypesItem adapter;
    //private String curCat;
    private Bundle data;
    private SOS_API sosApi;
    private TextView tvError;
    AdapterTypesItem adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_types_in_cat);

        tvError = findViewById(R.id.tvTypesErrorLoading);

        sosApi = SOSApplication.getInstance().getSosApi();//new SOS_API(this);

        catPicPath = sosApi.GSA() + SOS_API.DIR_PATH_CATEGORIES;

        pb = findViewById(R.id.pbAllCats);
        lv =  findViewById(R.id.mylist);

        Intent intent = getIntent();
        data = intent.getExtras();




         adapter = new AdapterTypesItem(this, R.layout.layout_item_type, objects , this);



        getSupportActionBar().setTitle(data.getString(HomeCategoryItem.KEY_HOME_CAT_TITLE));
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        queue = SOSApplication.getInstance().getRequestQueue();//Volley.newRequestQueue(this);



        String id = data.getString(HomeCategoryItem.KEY_HOME_CAT_ID);

        sosApi.loadCategoryTypesFromCatId(this, id);





    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();

        for(int i = 0; i < types.size(); i++) {
            TypesItem type = types.get(i);
            int getItemId = i+1;
            menu.add(i, getItemId, Menu.NONE, HM.UCF(type.getTypeName()));
        }

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {




        getMenuInflater().inflate(R.menu.menu_items_types, menu);

        return true;
    }


    private void itemsAdded() {




        adapter.setCatPixPath(catPicPath);

        Log.e(TAG, "itemsAdded: " + catPicPath );

        lv.setAdapter(adapter);
        pb.setVisibility(View.GONE);
        lv.setVisibility(View.VISIBLE);
    }



    private void addCatItem(TypesItem ci) {
        objects.add(ci);
        Log.e(TAG, "addCatItem: SIZE" + objects.size() );
    }

    @Override
    protected void onStop() {
        super.onStop();


        Log.e(TAG, "onStop: ALL CATS STOPPED" );

        queue.getCache().clear();
        Clear.clearCache(Picasso.with(this));
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }






        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onItemClicked(TypesItem typesItem, Uri picUri) {

        Log.e(TAG, "onItemClicked: " + typesItem.getTypeName() );

        Bundle b = new Bundle(typesItem.toBundle());
        b.putString(TypesItem.KEY_TYPE_ITEM_IMG_PATH, picUri.toString());

        Intent intent = new Intent(this, ActivityViewAllItemsInType.class);
        intent.putExtras(b);
        startActivity(intent);

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

    private List<TypesItem> types = new ArrayList<>();

    @Override
    public void onCategoryTypesLoaded(List<TypesItem> types, boolean errorLoading) {

        //tvError.setVisibility(View.VISIBLE);
        this.types = types;

        if(types == null){

            lv.setVisibility(View.GONE);
            pb.setVisibility(View.GONE);
            tvError.setVisibility(View.VISIBLE);
            if(errorLoading){
                tvError.setText(HM.RGS(this, R.string.msgErrorInternetConnection));
            }

        }else if(types.size() > 0) {

            adapter = new AdapterTypesItem(this, R.layout.layout_item_type, types, this);
            lv.setAdapter(adapter);

            lv.setVisibility(View.VISIBLE);
            pb.setVisibility(View.GONE);
            tvError.setVisibility(View.INVISIBLE);
            invalidateOptionsMenu();

        }

    }


}
