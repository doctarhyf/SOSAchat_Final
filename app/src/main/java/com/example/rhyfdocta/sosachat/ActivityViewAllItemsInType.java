package com.example.rhyfdocta.sosachat;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.adapters.AdapterAllItemsInType;
import com.example.rhyfdocta.sosachat.adapters.AdapterAllProducts;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ActivityViewAllItemsInType extends AppCompatActivity implements AdapterAllProducts.CallBacks  {


    public static final String CATEGORY_NAME = "catName";
    String catName, catIcon, catIconUri, catDescription;
    ImageView ivCatSplash;
    //AdapterAllProducts adapterProducts;
    private List<Product> prods = new ArrayList<>();
    //ListView lvProds;
    private String TAG = "LOAD_PRODS";

    //AlertDialog.Builder builder;
    AlertDialog alertDialog;
    TextView tvNoItemsInCat;
    SOS_API sosApi;
    String typeId;
    Bundle data;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items_in_category);

        sosApi = new SOS_API(this);
        recyclerView = findViewById(R.id.rvItemsInType);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        String msg = HM.RGS(this, R.string.dgMsgLoadingItems);
        alertDialog = HM.GADP(this, msg, true);

        //queue = Volley.newRequestQueue(this);

        tvNoItemsInCat = findViewById(R.id.tvNoItems);

        ivCatSplash = findViewById(R.id.ivCatSplash);
        //lvProds = (ListView) findViewById(R.id.lvProds);



        //progressDialog.hide();


        Intent intent = getIntent();
        data = intent.getExtras();

        catName = data.getString(TypesItem.KEY_TYPE_ITEM_NAME);
        catIcon = data.getString(TypesItem.KEY_TYPE_ITEM_IMG_PATH);
        typeId = data.getString(TypesItem.KEY_TYPE_ITEM_ID);
        //catIconUri = data.getString(TypesItem.KEY_CAT_ICON_URI);

        getSupportActionBar().setTitle(HM.UCF(catName));
        getSupportActionBar().setSubtitle("All " + catName.toLowerCase() + " deals");
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loadSplash();

        sosApi.loadItemsInType(new SOS_API.CallBacksItemsInTypes() {
            @Override
            public void onItemsInTypeLoaded(List<Product> prods) {

                loadProds(prods);
            }

            @Override
            public void onNoProdsInType() {
                //ActivityViewAllItemsInType.this.onNoProdsInType();
                //loadProds(null);
                //onLoadNoProdsInType();
                onLoadNoProdsInType();
            }

            @Override
            public void onErrorLoadProdsInType(String msg) {
                //Toast.makeText(getApplicationContext(), "Error loading : \n" + msg, Toast.LENGTH_SHORT);
                //Log.e(TAG, "onErrorLoadProdsInType: " );
                errorLoadProdInType(msg);
            }
        }, typeId);

        //Intent intent = getIntent();

        //Log.e(TAG, "onCreate: " + intent.toString() );






    }

    private void loadProds(List<Product> prods) {


        alertDialog.hide();
        adapter = new AdapterAllItemsInType(this,prods, new AdapterAllItemsInType.ListenerItemsInType() {
            @Override
            public void onItemClicked(Product pd) {
                Intent intent = new Intent(ActivityViewAllItemsInType.this, ActivityViewItemDetails.class);
                Bundle data = new Bundle();
                data.putAll(pd.toBundle());
                data.putAll(pd.getData());
                data.putBoolean(SOS_API.KEY_ITEM_IS_MINE, false);
                intent.putExtras(data);
                startActivity(intent);
            }

            @Override
            public void onItemAddToWishlistClicked(Product pd) {
                Log.e(TAG, "onItemAddToWishlistClicked: " );
            }
        });
                //(ActivityViewAllItemsInType.this, R.layout.list_item_prod, prods, ActivityViewAllItemsInType.this);
        recyclerView.setAdapter(adapter);
        tvNoItemsInCat.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        Log.e(TAG, "ADP LEN -> " + prods.size() );
    }

    @Override
    protected void onPause() {
        super.onPause();
        alertDialog.hide();
    }

    private void onLoadNoProdsInType() {


        alertDialog.hide();

        String msg = getResources().getString(R.string.msgErrorNoProdInType);
        //Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        //Log.e(TAG, "onNoProdsInType: \n" +  msg);
        //sosApi.TADRWM(true,msg);
        recyclerView.setVisibility(View.GONE);
        tvNoItemsInCat.setVisibility(View.VISIBLE);



    }

    private void errorLoadProdInType(String message) {

        alertDialog.hide();
        String msg = getResources().getString(R.string.msgErrorInternetConnection ) + "\n" + "Error message : " + message;
        //Toast.makeText(this, getResources().getString(R.string.msgNoProdInCat), Toast.LENGTH_SHORT).show();
        //Log.e(TAG, "onNoProdsInType: \n" +  msg);
        sosApi.TADRWM(true, msg);
        recyclerView.setVisibility(View.GONE);
        tvNoItemsInCat.setVisibility(View.VISIBLE);



    }

    private void loadSplash() {
        Uri picUri = Uri.parse(data.getString(TypesItem.KEY_TYPE_ITEM_IMG_PATH));
        Picasso.with(this).load(picUri).centerInside().error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation).resize(300,300).into(ivCatSplash, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.e("PICASSO ALL IT", "onError: " );
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;


            case R.id.menuSortProdsLatest:
                Toast.makeText(this, "Sorting by latest", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuSortProdsCheapest:
                Toast.makeText(this, "Sorting by cheapest", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menuSortProdsQuality:
                Toast.makeText(this, "Sorting by quality", Toast.LENGTH_SHORT).show();
                break;


        }




        item.setChecked(true);




        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_all_items_in_cat, menu);

        return true;
    }

    @Override
    public void onItemClicked(ProductMyProducts pd, Uri picUri) {
        /*Intent intent = new Intent(this, ActivityViewItemDetails.class);
        Bundle data = new Bundle();
        data.putAll(pd.toBundle());
        data.putAll(pd.getDataBundle());
        data.putBoolean(SOS_API.KEY_ITEM_IS_MINE, false);
        intent.putExtras(data);
        startActivity(intent);*/
    }

    @Override
    public void onItemFavorite(ProductMyProducts pd, Uri picUri) {

    }
}
