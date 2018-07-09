package com.koziengineering.rhyfdocta.sosachat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koziengineering.rhyfdocta.sosachat.API.SOS_API;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.Product;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.R;
import com.koziengineering.rhyfdocta.sosachat.adapters.AdapterAllProducts;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by rhyfdocta on 11/8/17.
 */

public class ActivitySearchResult extends AppCompatActivity implements SOS_API.SOSApiListener, AdapterAllProducts.CallBacks {


    private static final String TAG = "ACT_SRCH";
    //JsonArrayRequest request;
    ConstraintLayout clLoadingResult;
    ListView lvItemsSearchResult;
    SOS_API sosApi;
    TextView tvLoadingSearchResultError;
    private List<ProductMyProducts> products;
    private AdapterAllProducts adapter;
    private String searchKeyWord;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_search);

        sosApi = new SOS_API(this);

        Intent intent = getIntent();
        String ids = intent.getStringExtra(SOS_API.KEY_ITEMS_SEARCH_RESULT_IDS);
        searchKeyWord = intent.getStringExtra(SOS_API.KEY_SEARCH_KEYWORD);

        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.strSearcingFor), searchKeyWord));
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TextView tv = (TextView) findViewById(R.id.textView);
        //tv.setText(getIntent().getStringExtra("Q"));

        clLoadingResult = findViewById(R.id.clLoadingResults);
        lvItemsSearchResult = findViewById(R.id.lvItemsSearchResult);
        tvLoadingSearchResultError = findViewById(R.id.tvLoadingSearchResultError);



        Log.e(TAG, "onCreate: the fucks ids -> " + ids );

        sosApi.loadSearchResultItems(this, cleanIds(ids));



    }

    private String cleanIds(String ids) {
        return ids.replace(", ", "_").trim();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
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

        //Log.e(TAG, "onLoadAllProductsResult: ", );
        if(searchResultProducts.size() > 0) {


            tvLoadingSearchResultError.setVisibility(View.GONE);
            clLoadingResult.setVisibility(View.GONE);
            lvItemsSearchResult.setVisibility(View.VISIBLE);

            products = searchResultProducts;
            adapter = new AdapterAllProducts(this, R.layout.list_item_my_products, products, this);
            lvItemsSearchResult.setAdapter(adapter);
        }else{

            tvLoadingSearchResultError.setVisibility(View.VISIBLE);
            clLoadingResult.setVisibility(View.GONE);
            lvItemsSearchResult.setVisibility(View.GONE);

            //setSubTitle("Item(s) : 0 ");
        }

        tvLoadingSearchResultError.setVisibility(View.GONE);

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
