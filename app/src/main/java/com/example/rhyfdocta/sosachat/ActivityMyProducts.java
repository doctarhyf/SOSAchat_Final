package com.example.rhyfdocta.sosachat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rhyfdocta.sosachat.API.NETWORK_RESULT_CODES;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductWishList;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.adapters.AdapterMyProducts;
import com.example.rhyfdocta.sosachat.app.SOSApplication;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ActivityMyProducts extends AppCompatActivity implements
    AdapterMyProducts.CallBacks, SOS_API.SOSApiListener, SOS_API.ListenerLoadMyProducts,
        SearchView.OnQueryTextListener
{


    private static final String TAG = "ACT_MYPD";
    public static final String KEY_NEW_ITEM_POSTED = "newItemPosted";
    List<ProductMyProducts> products = new ArrayList<>();
    AdapterMyProducts adapterMyProducts;
    ListView lvMyProducts;
    RequestQueue queue;
    JsonArrayRequest request;
    private TextView tvEmptyList;
    ProgressBar progressBar;
    boolean newItemPosted = false;
    SOS_API sosApi;
    String itemUniqueName;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        itemUniqueName = intent.getStringExtra(SOS_API.KEY_ITEM_UNIQUE_NAME);
        if(itemUniqueName != null){
            newItemPosted = true;
        }
        //Log.e(TAG, "NEW_IT" + extraNewItemPosted );

        getSupportActionBar().setTitle(getResources().getString(R.string.menuMyProducts));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        progressBar = findViewById(R.id.pbMyProducts);
        products = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        lvMyProducts = findViewById(R.id.lvMyProducts);
        tvEmptyList = findViewById(R.id.tvEmptyList);
        sosApi = SOSApplication.getInstance().getSosApi();//new SOS_API(this);

        adapterMyProducts = new AdapterMyProducts(this, R.layout.list_item_my_products, products, this);
        lvMyProducts.setAdapter(adapterMyProducts);


        loadMyProductsData();
    }


        @Override
        public void onMyProductsLoaded(List<ProductMyProducts> products) {

            loadItems(products);
            //sosApi.TADRWM(true,"onMyProductsLoaded: " );
            progressBar.setVisibility(View.INVISIBLE);
            tvEmptyList.setVisibility(View.VISIBLE);
        }

        @Override
        public void onMyProductsEmpty() {

            final Context ctx = this;
            String msg = HM.RGS(ctx, R.string.msgNoProducts);
            String btnOk = HM.RGS(ctx, R.string.btnOk);
            String btnNwItem = HM.RGS(ctx, R.string.btnNewItem);


            SOS_API.DWPNB(this, true, msg, btnNwItem,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(ctx, ActivityPostItem.class);
                            startActivity(intent);
                        }
                    }, btnOk,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    },null);

            progressBar.setVisibility(View.INVISIBLE);
            tvEmptyList.setVisibility(View.VISIBLE);
        }

        @Override
        public void onNetworkError(String msg) {
            String netEr = HM.RGS(this, R.string.msgServerUnreachable );
            //msg = netEr + ":\n" + msg;
            sosApi.TADRWM(ActivityMyProducts.this,true, netEr);
            progressBar.setVisibility(View.INVISIBLE);
            tvEmptyList.setText(netEr);
            tvEmptyList.setVisibility(View.VISIBLE);
        }

        @Override
        public void onParseJsonError(String s) {
            s = "JSON ERROR :\n" + s;
            sosApi.TADRWM(ActivityMyProducts.this,true, s);
            tvEmptyList.setText(s);
            progressBar.setVisibility(View.INVISIBLE);
            tvEmptyList.setVisibility(View.VISIBLE);
        }


    public void  loadMyProductsData(){

        products.clear();

        sosApi.loadMyProducts(this);
        //Log.e(TAG, "loadMyProductsData: ON RESUME -->>" );

    }




    private void loadItems(List<ProductMyProducts> products) {

        //Log.e(TAG, "loadItems: " );



        this.products.clear();

        this.products.addAll(products);

        getSupportActionBar().setSubtitle(products.size() + " item(s).");



        adapterMyProducts.notifyDataSetChanged();
        getSupportActionBar().setSubtitle(products.size() + " Item(s)");


        tvEmptyList.setVisibility(View.GONE);
        lvMyProducts.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

        //LinearLayout ll = (LinearLayout) lvMyProducts.getChildAt(0);
        //ll.setBackgroundColor(getResources().getColor(R.color.colorAccent));


        if(newItemPosted){
            Toast.makeText(this, "New Item posted", Toast.LENGTH_LONG).show();
            newItemPosted = false;
        }

        if(products.size() == 0 ){
            onMyProductsEmpty();
        }


    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_products, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        /*if(!q.equals("")) {
            searchView.setIconified(false);
            onQueryTextChange(q);
        }*/

        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public void onItemClicked(ProductMyProducts pd, Uri picUri) {


        Intent intent = new Intent(this, ActivityViewItemDetails.class);

        Bundle b = new Bundle(pd.toBundle());
        b.putString(Product.KEY_PD_PIC_URI, picUri.toString());
        b.putBoolean(SOS_API.KEY_ITEM_IS_MINE, true);
        b.putAll(pd.getDataBundle());

        intent.putExtras(b);

        startActivity(intent);

    }

    @Override
    public void onItemRemoveClicked(final ProductWishList pd, Uri picUri) {

        //String title = String.format(getResources().getString(R.string.titleRemoveFromWishList), pd.getPdName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.titleRemoveFromMyProducts, pd.getPdName()))
                .setMessage(  getResources().getString(R.string.dgMsgRemoveFromMyProducts, pd.getPdName()))
                .setPositiveButton(getResources().getString(R.string.btnYes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View v = layoutInflater.inflate(R.layout.layout_dialog_input_password, null);
                        final EditText etpwd = v.findViewById(R.id.etDgPassword);

                        new AlertDialog.Builder(ActivityMyProducts.this)
                                //.setTitle(HM.RGS(ActivityAccountSettings.this, R.string.dgTitleInputPassword))
                                .setView(v)
                                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(etpwd.getText().toString().equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_PASSWORD))) {

                                            sosApi.removeProduct(ActivityMyProducts.this,pd);

                                        }else{
                                            Toast.makeText(ActivityMyProducts.this, HM.RGS(ActivityMyProducts.this, R.string.tmsgPwdNotCorrect), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setCancelable(false)
                                .setNegativeButton("CANCEL", null).show();






                    }
                })
                .setNegativeButton(getResources().getString(R.string.btnNo), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });


        builder.show();

    }

    @Override
    public void onItemEditClicked(final ProductMyProducts pd, final Uri picUri) {



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


                            Intent intent = new Intent(ActivityMyProducts.this, ActivityPostItem.class);

                            Bundle b = new Bundle(pd.toBundle());
                            b.putString(Product.KEY_PD_PIC_URI, picUri.toString());
                            b.putBoolean(SOS_API.KEY_ITEM_IS_MINE, true);
                            b.putBoolean(SOS_API.KEY_ITEM_MODE_EDITING, true);
                            b.putAll(pd.getDataBundle());

                            //String test = b.getString(SOS_API.KEY_ITEM_ID);

                            intent.putExtras(b);
                            Log.e(TAG, "onItemEditClicked: data -> " + b.toString() );
                            startActivity(intent);


                        }else{
                            Toast.makeText(ActivityMyProducts.this, HM.RGS(ActivityMyProducts.this, R.string.tmsgPwdNotCorrect), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setCancelable(false)
                .setNegativeButton("CANCEL", null).show();










    }

    @Override
    public void onItemPublishClicked(ProductMyProducts pd, Uri picUri) {
        //Toast.makeText(this, "On Item : " + pd.getPdName() + " sold!", Toast.LENGTH_SHORT).show();
        sosApi.publishItem(new SOS_API.CallbacksProduct() {
            @Override
            public void onItemPublishResult(int resultCode, String resultData) {
                //Log.e(TAG, "onItemPublishResult: code -> " + resultCode + ", resultData -> " + resultData );
                if(resultCode == NETWORK_RESULT_CODES.RESULT_CODE_SUCCESS){

                    Toast.makeText(ActivityMyProducts.this, "Item published waiting for granting", Toast.LENGTH_SHORT).show();

                    //itemDataBundle.putString(Product.KEY_PD_STAT, Product.PD_STAT_WAITING + "");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            sosApi.loadMyProducts(ActivityMyProducts.this);
                        }
                    }, 2000);
                }else{
                    Toast.makeText(ActivityMyProducts.this, "Failed to pubish item", Toast.LENGTH_LONG).show();
                }
            }
        }, pd.getDataBundle().getString(SOS_API.KEY_ITEM_ID));
    }

    @Override
    public void onItemInfoClicked(final  ProductMyProducts pd,final Uri picUri) {

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_item_info, null);

        TextView tvTitle = view.findViewById(R.id.tvDialogItemInfoTitle);
        tvTitle.setText(pd.getPdName());

        //View viewPublish = view.findViewById(R.id.)



        final AlertDialog.Builder builder = new AlertDialog.Builder(this)

                .setView(view)
                .setNegativeButton(HM.RGS(this, R.string.strCancel), null);


        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        View infoRempProd = view.findViewById(R.id.infoRemProd);
        infoRempProd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ActivityMyProducts.this.onItemRemoveClicked(pd, picUri);
                alertDialog.dismiss();
            }
        });

        View infoEditProd = view.findViewById(R.id.infoEditProd);
        infoEditProd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ActivityMyProducts.this.onItemEditClicked(pd, picUri);
                alertDialog.dismiss();
            }
        });

        View infoPublishProd = view.findViewById(R.id.infoPublishProd);
        infoPublishProd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ActivityMyProducts.this.onItemPublishClicked(pd, picUri);
                alertDialog.dismiss();
            }
        });

        String stat = pd.getDataBundle().getString(Product.KEY_PD_STAT);
        int stati = Integer.parseInt(stat);

        if(stati == Product.PD_STAT_PUBLISHED || stati == Product.PD_STAT_DENIED || stati == Product.PD_STAT_WAITING){
            infoPublishProd.setVisibility(View.GONE);
            infoEditProd.setVisibility(View.GONE);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        if (item.getItemId() == android.R.id.home) {
            finish();
        }


        if(item.getItemId() == R.id.menuExpose){

            intent = new Intent(this, ActivityPostItem.class);
            startActivity(intent);

        }


        return true;
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


        /*Log.e(TAG, "DAFAK NET " + networkError );

        if(networkError){

            //sosApi.toggleAlertDialogResponseWithMessage(true, HM.RGS(this,R.string.msgErrorInternetConnection));
            progressBar.setVisibility(View.INVISIBLE);
            tvEmptyList.setText(HM.RGS(this,R.string.msgErrorInternetConnection));
            tvEmptyList.setVisibility(View.VISIBLE);
            return;
        }

        if(myProducts.size() == 0){
            onEmptyList();
        }else{*/
            //this.products = myProducts;
            //loadItems();
        //}
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
    public void onRemoveProductResult(final Bundle b) {








        if(b.getString(SOS_API.JSON_KEY_RESULT).equals(SOS_API.JSON_RESULT_SUCCESS)){

            //adapterMyProducts.removeProductFromAdapter(b.getInt(Product.KEY_PD_ADAPTER_POSITION));
            //getSupportActionBar().setSubtitle(products.size() + "item(s)");
            loadMyProductsData();

            Toast.makeText(ActivityMyProducts.this, HM.getStringResource(ActivityMyProducts.this, R.string.msgProductRemoved), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(ActivityMyProducts.this, HM.getStringResource(ActivityMyProducts.this, R.string.msgProductRemovedError), Toast.LENGTH_SHORT).show();
        }

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


    @Override
    protected void onResume() {
        super.onResume();
        loadMyProductsData();



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

        adapterMyProducts.setFilter(newList);
    }
}
