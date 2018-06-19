package com.example.rhyfdocta.sosachat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductWishList;
import com.example.rhyfdocta.sosachat.adapters.AdapterWishListItems;
import com.example.rhyfdocta.sosachat.app.SOSApplication;

import java.util.ArrayList;
import java.util.List;

public class ActivityWishlist extends AppCompatActivity implements
        AdapterWishListItems.CallBacks,
        SOS_API.CallbacksWishlist,
        SOS_API.ListenerItemsWishlist {

    private static final String TAG = "ACT WL";
    private static final String CHANNEL_ID = "chanID";
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

        sosApi = SOSApplication.getInstance().getSosApi();



        getSupportActionBar().setTitle(getResources().getString(R.string.menuWishList));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        pbWli = findViewById(R.id.pbWli);
        products = new ArrayList<>();
        queue = Volley.newRequestQueue(this);
        lvWli = findViewById(R.id.lvWli);
        tvEmptyWli = findViewById(R.id.tvEmptyWli);

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






        ////////////////


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.titleRemoveFromWishList, pd.getPdName()))
                .setMessage(  getResources().getString(R.string.dgMsgRemoveFromWishList, pd.getPdName()))
                .setPositiveButton(getResources().getString(R.string.btnYes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {





                        /////////////

                        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View v = layoutInflater.inflate(R.layout.layout_dialog_input_password, null);
                        final EditText etpwd = v.findViewById(R.id.etDgPassword);

                        new AlertDialog.Builder(ActivityWishlist.this)
                                //.setTitle(HM.RGS(ActivityAccountSettings.this, R.string.dgTitleInputPassword))
                                .setView(v)
                                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if(etpwd.getText().toString().equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_PASSWORD))) {


                                            String title = "Deleting ...";
                                            String message = "Please wait ...";
                                            SOSApplication.GI().GUPD(true,ActivityWishlist.this, title, message).show();




                                            //sosApi.removeItemFromWishList(pd.getPd)
                                            Bundle d = pd.toBundle();
                                            d.putAll(pd.getData());
                                            sosApi.removeItemFromWishlist(ActivityWishlist.this, d);



                                        }else{
                                            Toast.makeText(ActivityWishlist.this, HM.RGS(ActivityWishlist.this, R.string.tmsgPwdNotCorrect), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                })
                                .setCancelable(false)
                                .setNegativeButton("CANCEL", null).show();


                        /////////////

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
        sosApi.toggleAlertDialogResponseWithMessage(ActivityWishlist.this,true, HM.RGS(this, R.string.msgErrorInternetConnection));

        lvWli.setVisibility(View.GONE);
        tvEmptyWli.setText(getResources().getString(R.string.msgServerUnreachable));
        tvEmptyWli.setVisibility(View.VISIBLE);
        pbWli.setVisibility(View.GONE);
    }

    @Override
    public void onWishlistClearResult(boolean success, String message) {

        if(success){

            Toast.makeText(this, "Wishlist cleared successfully!", Toast.LENGTH_SHORT).show();

            sosApi.loadWishListData(this);

        }else{

            Toast.makeText(this, "Error clearing wishlist\nError : " + message, Toast.LENGTH_SHORT).show();


        }

        SOSApplication.GI().DPD();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        if(item.getItemId() == R.id.menuClearWishList){

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Clear Wishlist")
                    .setMessage("Are you sure you wanna clear your wishlist?")
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            clearWishlist();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });


                    builder.show();


        }

        return true;
    }

    private void clearWishlist() {



        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.layout_dialog_input_password, null);
        final EditText etpwd = v.findViewById(R.id.etDgPassword);

        new AlertDialog.Builder(this)
                //.setTitle(HM.RGS(ActivityAccountSettings.this, R.string.dgTitleInputPassword))
                .setView(v)
                .setPositiveButton("LOGOUT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(etpwd.getText().toString().equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_PASSWORD))) {


                            SOSApplication.GI().getUndefinedProgressDialog(true,ActivityWishlist.this,
                                    "Clearing wishlist ...", null).show();


                            sosApi.clearWishList(ActivityWishlist.this);



                        }else{
                            Toast.makeText(ActivityWishlist.this, HM.RGS(ActivityWishlist.this, R.string.tmsgPwdNotCorrect), Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setCancelable(false)
                .setNegativeButton("CANCEL", null).show();


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
    public void onWishlistItemRemoveError(Bundle pd) {
        SOSApplication.GI().DPD();
        sosApi.toggleAlertDialogResponseWithMessage(ActivityWishlist.this,true, HM.RGS(this, R.string.msgErrorAddingWishlistItem));
    }

    @Override
    public void onWishlistItemRemoveSuccess(Bundle pd) {
        String msg = HM.RGS(this, R.string.msgItemRemoveToWishlistSuccess);
        msg = String.format(msg, pd.getString(Product.KEY_PD_NAME));
        sosApi.toggleAlertDialogResponseWithMessage(ActivityWishlist.this, true, msg);
        sosApi.loadWishListData(this);

        SOSApplication.GI().DPD();


    }


}
