package com.example.rhyfdocta.sosachat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rhyfdocta.sosachat.API.NETWORK_RESULT_CODES;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.adapters.AdapterAP;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewAllProducts extends AppCompatActivity implements
        SOS_API.SOSApiListener,
        AdapterAP.ListenerAllProducts,
        SearchView.OnQueryTextListener,
        SOS_API.CallbacksSearch,
        SOS_API.CallbacksProduct
{

    private static final String TAG = "TAG_VIEW_ALL";
    List<ProductMyProducts> products = new ArrayList<>();
    SOS_API sosApi;
    private String q;

    //AdapterAllProducts adapter;
    //ListView lv;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    AdapterAP adapter;
    TextView tvAllProductsEmptyList;
    ProgressBar pbAllProducts;
    TextView tvAllProductsLoadingError;
    Button btnLoadMore;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_products);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("View All Products");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvAllProductsLoadingError = findViewById(R.id.tvAllProductsLoadingError);
        tvAllProductsEmptyList = findViewById(R.id.tvAllProductsEmptyList);
        pbAllProducts = findViewById(R.id.pbAllProducts);
        //btnLoadMore = (Button) findViewById(R.id.btnLoadMore);

        //lv = (ListView) findViewById(R.id.rvAllProducts);
        adapter = new AdapterAP(this, products, this);
        recyclerView = findViewById(R.id.rvAllProducts);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);//this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        sosApi = new SOS_API(this);

        if(!SOS_API.isOnline(this)) {

            Intent intent = new Intent(this, ActivityNoNetwork.class);
            startActivity(intent);
        }

        sosApi.loadAllProducts(this, sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_USER_ID));

        Bundle data = getIntent().getExtras();

        if(data != null){
            q = data.getString(SOS_API.SEARCH_Q, "");
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.menuViewAllProductsRefresh:


                tvAllProductsEmptyList.setVisibility(View.GONE);
                pbAllProducts.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                sosApi.loadAllProducts(this, sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_USER_ID));
                break;
        }

        return super.onOptionsItemSelected(item);
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

        products.clear();

        //Log.e(TAG, "onLoadAllProductsResult: " + allProducts.size() );
        if(allProducts.size() > 0) {


            tvAllProductsEmptyList.setVisibility(View.GONE);
            pbAllProducts.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            //btnLoadMore.setVisibility(View.VISIBLE);


            products.addAll(allProducts);
            adapter.notifyDataSetChanged();
            //recyclerView.setAdapter(adapter);
        }else{

            tvAllProductsEmptyList.setVisibility(View.VISIBLE);
            pbAllProducts.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            //btnLoadMore.setVisibility(View.GONE);

            //setSubTitle("Item(s) : 0 ");
        }

        tvAllProductsLoadingError.setVisibility(View.GONE);


    }

    @Override
    public void onRemoveProductResult(Bundle b) {

    }

    @Override
    public void onUpdateItemViewsCountResult(String newCount) {

    }

    @Override
    public void onLoadAllProductsError(String message) {

        //Toast.makeText(this, "Load products error, please try again later", Toast.LENGTH_SHORT).show();
        products.clear();
        tvAllProductsEmptyList.setVisibility(View.GONE);
        //tvAllProductsEmptyList.setText(getResources().getString(R.string.msgServerUnreachable));
        pbAllProducts.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tvAllProductsLoadingError.setText(getResources().getString(R.string.msgServerUnreachable));
        tvAllProductsLoadingError.setVisibility(View.VISIBLE);


    }

    /*
    @Override
    public void onLoadAllServerUnreachable(String message) {
        tvAllProductsEmptyList.setVisibility(View.GONE);
        //tvAllProductsEmptyList.setText(getResources().getString(R.string.msgServerUnreachable));
        pbAllProducts.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tvAllProductsLoadingError.setText(getResources().getString(R.string.msgServerUnreachable));
        tvAllProductsLoadingError.setVisibility(View.VISIBLE);
    }*/

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
    public void onItemClicked(ProductMyProducts pd, Uri picUri) {

        Intent intent = new Intent(this, ActivityViewItemDetails.class);

        Bundle b = new Bundle(pd.toBundle());
        b.putString(Product.KEY_PD_PIC_URI, picUri.toString());
        b.putAll(pd.getDataBundle());

        intent.putExtras(b);

        startActivity(intent);

    }

    @Override
    public void onItemAddedToFavorite(final ProductMyProducts pd, Uri picUri) {

        //Toast.makeText(this, "Will be added to favorites", Toast.LENGTH_SHORT).show();
        sosApi.addItemToWishlist(new SOS_API.ListenerItemsWishlist() {
            @Override
            public void onItemAddedSuccess() {
                String msg = "The item " + pd.getPdName() + " has been added to your wishlist!";
                sosApi.TADRWM(ActivityViewAllProducts.this, true,msg);
            }

            @Override
            public void onItemAddedError(String msg) {

            }

            @Override
            public void onNetworkError(String msg) {

            }

            @Override
            public void onWishlistItemRemoveError(Bundle pd) {

            }

            @Override
            public void onWishlistItemRemoveSuccess(Bundle pd) {

            }
        }, pd.getDataBundle().getString(SOS_API.KEY_ITEM_ID));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_view_all_products, menu);


        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        /*if(!q.equals("")) {
            searchView.setIconified(false);
            onQueryTextChange(q);
        }*/

        searchView.setOnQueryTextListener(this);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        filterAdapter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {




        filterAdapter(newText);



        return true;
    }

    private void filterAdapter(String newText) {
        newText = newText.toLowerCase();
        ArrayList<ProductMyProducts> newList = new ArrayList<>();

        for(ProductMyProducts product : products){
            String pdTitle = product.getPdName().toLowerCase();
            String pdDesc = product.getPdDesc().toLowerCase();

            if(pdTitle.contains(newText) || pdDesc.contains(newText)){
                newList.add(product);
            }
        }

        adapter.setFilter(newList);
    }

    @Override
    public void onSearchResult(Context context, List<ProductMyProducts> products) {

    }

    @Override
    public void onItemPublishResult(int code, String data) {

    }

    public void onEmptyList(){
        pbAllProducts.setVisibility(View.GONE);
        tvAllProductsEmptyList.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        tvAllProductsLoadingError.setVisibility(View.GONE);
    }

    @Override
    public void onLoadAllItemsResult(int code, List<ProductMyProducts> products) {
        Log.e(TAG, "onLoadAllItemsResult: -> code : " + code );
        pbAllProducts.setVisibility(View.GONE);



        if(code == NETWORK_RESULT_CODES.RESULT_CODE_EMPTY_LIST){
            onEmptyList();
        }

        if(code == NETWORK_RESULT_CODES.RESULT_CODE_SUCCESS && products != null){

            if(products.size() == 0){
                onEmptyList();
            }else{

                this.products.clear();
                this.products.addAll(products);
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(View.VISIBLE);
                tvAllProductsLoadingError.setVisibility(View.GONE);
                tvAllProductsEmptyList.setVisibility(View.GONE);

            }
        }
    }

    @Override
    public void onLoadAllItemsNetworkError(String message){
        tvAllProductsEmptyList.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        tvAllProductsLoadingError.setText(getResources().getString(R.string.msgLoadingError));
        tvAllProductsLoadingError.setVisibility(View.VISIBLE);

        Log.e(TAG, "onLoadAllItemsNetworkError: message -> " + message );
    }
}
