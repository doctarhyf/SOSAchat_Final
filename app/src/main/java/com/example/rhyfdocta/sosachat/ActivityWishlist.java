package com.example.rhyfdocta.sosachat;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.HelperObjects.HM;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductWishList;
import com.example.rhyfdocta.sosachat.adapters.AdapterWishListItems;

import java.util.ArrayList;
import java.util.List;

public class ActivityWishlist extends AppCompatActivity implements AdapterWishListItems.CallBacks, SOS_API.ListenerOnWishlistItemsLoaded, SOS_API.ListenerItemsWishlist {

    private static final String TAG = "ACT WL";
    List<ProductWishList> products;
    AdapterWishListItems adapterWishListItems;
    ListView lvWli;
    RequestQueue queue;
    JsonArrayRequest request;
    private TextView tvEmptyWli;
    ProgressBar pbWli;
    SOS_API sosApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        sosApi = new SOS_API(this);

        getSupportActionBar().setTitle(getResources().getString(R.string.menuWishList));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pbWli = (ProgressBar) findViewById(R.id.pbWli);
        products = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        lvWli = (ListView) findViewById(R.id.lvWli);
        tvEmptyWli = (TextView) findViewById(R.id.tvEmptyWli);

        sosApi.loadWishListData(this);


    }

    @Override
    public void onItemClicked(ProductWishList pd, Uri picUri) {

        Intent intent = new Intent(this, ActivityViewItemDetails.class);

        Bundle b = new Bundle();
        b.putAll(pd.toBundle());
        b.putAll(pd.getData());
        b.putString(Product.KEY_PD_PIC_URI, picUri.toString());

        intent.putExtras(b);


        startActivity(intent);

    }

    @Override
    public void onItemRemoveClicked(final ProductWishList pd, Uri picUri) {

        //String title = String.format(getResources().getString(R.string.titleRemoveFromWishList), pd.getPdName());

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.titleRemoveFromWishList, pd.getPdName()))
                .setMessage(  getResources().getString(R.string.dgMsgRemoveFromWishList, pd.getPdName()))
                .setPositiveButton(getResources().getString(R.string.btnYes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //Log.e(TAG, "removeFromWishList: " );
                        Toast.makeText(ActivityWishlist.this, "Item Removed", Toast.LENGTH_SHORT).show();
                        //sosApi.removeItemFromWishList(pd.getPd)
                        Bundle d = pd.toBundle();
                        d.putAll(pd.getData());
                        sosApi.removeItemFromWishlist(ActivityWishlist.this, d);

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
            public void onWishLisItemsLoaded(List<ProductWishList> wishlistItems){

        //Log.e(TAG, "loadItems: " );
        products = wishlistItems;
        getSupportActionBar().setSubtitle(products.size() + " item(s).");
        adapterWishListItems = new AdapterWishListItems(this, R.layout.list_item_wishlist, products, this);
        lvWli.setAdapter(adapterWishListItems);

        tvEmptyWli.setVisibility(View.GONE);
        lvWli.setVisibility(View.VISIBLE);
        pbWli.setVisibility(View.GONE);


    }




    @Override
    public void onNoItemsInWishlist() {


        String msg = getResources().getString(R.string.msgErrorNoProdInType);
        Toast.makeText(this, getResources().getString(R.string.msgErrorNoProdInType), Toast.LENGTH_SHORT).show();
        Log.e(TAG, "onNoProdsInCategory: \n" +  msg);

        lvWli.setVisibility(View.GONE);
        tvEmptyWli.setVisibility(View.VISIBLE);
        pbWli.setVisibility(View.GONE);

    }

    @Override
    public void onErrorLoadWishList(String message) {
        sosApi.toggleAlertDialogResponseWithMessage(true, HM.RGS(this, R.string.msgErrorInternetConnection));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if(item.getItemId() == R.menu.menu_wishlist){
            emptyWishList();
        }

        return true;
    }

    private void emptyWishList() {
        Toast.makeText(this, "Will empty wishlist", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_wishlist, menu);

        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // TODO: 11/15/17 CLEAR CASH
        //queue.getCache().clear();
    }


    @Override
    public void onItemAddedSuccess() {

    }

    @Override
    public void onItemAddedError(String msg) {

    }

    @Override
    public void onNetworkError(String msg) {

    }

    @Override
    public void onItemRemoveError(Bundle pd) {
        sosApi.toggleAlertDialogResponseWithMessage(true, HM.RGS(this, R.string.msgErrorAddingWishlistItem));
    }

    @Override
    public void onItemRemoveSuccess(Bundle pd) {
        String msg = HM.RGS(this, R.string.msgItemRemoveToWishlistSuccess);
        msg = String.format(msg, pd.getString(Product.KEY_PD_NAME));
        sosApi.toggleAlertDialogResponseWithMessage(true, msg);
        sosApi.loadWishListData(this);


    }
}
