package com.example.rhyfdocta.sosachat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.adapters.AdapterAP;
import com.example.rhyfdocta.sosachat.adapters.AdapterAllProducts;

import org.json.JSONArray;

import java.util.List;

public class ActivityViewAllProducts extends AppCompatActivity implements SOS_API.SOSApiListener, AdapterAP.ListenerAllProducts {

    private static final String TAG = "TAG_VIEW_ALL";
    List<ProductMyProducts> products;
    SOS_API sosApi;
    //AdapterAllProducts adapter;
    //ListView lv;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    TextView tvAllProductsEmptyList;
    ProgressBar pbAllProducts;
    TextView tvAllProductsLoadingError;
    Button btnLoadMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_products);



        getSupportActionBar().setTitle("View All Products");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvAllProductsLoadingError = (TextView) findViewById(R.id.tvAllProductsLoadingError);
        tvAllProductsEmptyList = (TextView) findViewById(R.id.tvAllProductsEmptyList);
        pbAllProducts = (ProgressBar) findViewById(R.id.pbAllProducts);
        //btnLoadMore = (Button) findViewById(R.id.btnLoadMore);

        //lv = (ListView) findViewById(R.id.rvAllProducts);
        recyclerView = findViewById(R.id.rvAllProducts);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        sosApi = new SOS_API(this);

        if(!SOS_API.isOnline(this)) {

            Intent intent = new Intent(this, ActivityNoNetwork.class);
            startActivity(intent);
        }

        sosApi.loadAllProducts(this, sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_USER_ID));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_view_all_products, menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.menuViewAllProductsRefresh:
                /*
                tvAllProductsEmptyList.setVisibility(View.GONE);
                pbAllProducts.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                sosApi.loadAllProducts(this, sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_USER_ID));*/
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


        //Log.e(TAG, "onLoadAllProductsResult: " + allProducts.size() );
        if(allProducts.size() > 0) {


            tvAllProductsEmptyList.setVisibility(View.GONE);
            pbAllProducts.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            //btnLoadMore.setVisibility(View.VISIBLE);

            products = allProducts;
            //adapter.notifyDataSetChanged();
            adapter = new AdapterAP(this, allProducts, this);
            recyclerView.setAdapter(adapter);
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
    public void onItemFavorite(ProductMyProducts pd, Uri picUri) {

        Toast.makeText(this, "Will be added to favorites", Toast.LENGTH_SHORT).show();
    }
}
