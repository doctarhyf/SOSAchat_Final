package com.example.rhyfdocta.sosachat.API;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rhyfdocta.sosachat.HelperObjects.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.HelperObjects.HM;
import com.example.rhyfdocta.sosachat.HelperObjects.HelperDate;
import com.example.rhyfdocta.sosachat.HelperObjects.HelperMethods;

import com.example.rhyfdocta.sosachat.ObjectsModels.Inquiry;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductWishList;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.adapters.AdapterInquiry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rhyfdocta on 11/13/17.
 */

public class SOS_API {

    /*
    private $dbuser = "cpjmtin907391_casos";
	private $dbpass = "Aliceliwena1966";
	private $dbname = "cpjmtin907391_sosachat";
     */

    public static final String KEY_AUTOREFRESH_RECENT_ITEMS = "autorefreshRecentItems";
    public static final String DIR_NAME_PIX_CACHE_HOME_CATS = "cats";
    public static final String DIR_NAME_PIX_CACHE_PRODUCTS = "products";
    public static final String DIR_NAME_PIX_CACHE_PROFILCE_PIC = "pp";
    private static final String ACTION_LOAD_ALL_INQUIRIES = "checkAllInquiries";
    public static final String TAG = "SOSACHAT_DBG";
    public static boolean POST_MARSHMALLOW = false;
    public static final String DIR_PATH_CAT_PIX = "http://192.168.1.2/sosachat/img/cats/";
    public static final String KEY_USER_IS_ADMIN = "user_is_admin";
    public static final String ACTTION_LOAD_WISH_LIST = "loadWishList";
    public static final String KEY_SHOWING_VENDOR_PROFILE = "showingVendorProfile";
    public static final String KEY_SOSACHAT_PIX_DIR = "SOSAchat";

    public static String API_URL = "http://192.168.1.2/sosachat/api.php?";
    public static String DIR_PATH_CATEGORIES = "http://192.168.1.2/sosachat/img/";
    public static String DIR_PATH_PRODUCTS_PIX = "http://192.168.1.2/sosachat/img/products/";
    public static String DIR_PATH_PP = "http://192.168.1.2/sosachat/img/pp/";
    public static String ROOT_URL = "http://192.168.1.2/sosachat/";
    public static String DIR_PATH_TYPES = "img/types/";


    public static final String ACTION_LOAD_FEATURED_ITEMS = "loadFeatItems";
    public static final String ACTION_LOAD_CATEGORY_CARS = "loadCatCars";
    public static final String ACTION_LOAD_CATEGORY_ELECTRONICS = "loadCatElec";
    public static final String ACTION_LOAD_WISH_LIST = "loadWishList";
    public static final String ACTION_LOAD_ALL_MY_PRODUCTS = "loadAllMyProducts";
    public static final String ACTION_LOAD_CHAT_CONTACTS = "loadChatContacts";
    public static final String ACTION_LOGIN = "login";
    public static final String LOGIN_SUCCESS = "true";
    //private static final String TAG = "SOS_API";
    private static final String SHARED_PREF_NAME = "sosSharedPref";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_NO_USERNAME = "no_username";
    public static final String ACTION_LOAD_ACC_DATA = "loadAccData";

    public static final String KEY_ACC_DATA_DISPLAY_NAME = "user_display_name";
    public static final String KEY_ACC_DATA_ACC_TYPE = "accType";
    public static final String KEY_ACC_DATA_ACC_CITY = "city";
    public static final String KEY_ACC_DATA_ACC_PIC_NAME = "user_pic";
    public static final String KEY_ACC_DATA_FULL_NAME = "user_full_name";
    public static final String KEY_ACC_DATA_EMAIL = "user_email";
    public static final String KEY_ACC_DATA_MOBILE = "user_mobile";
    public static final String KEY_ACC_DATA_COMPANY = "user_company";
    public static final String KEY_ACC_DATA_LOCATION = "user_location";
    public static final String KEY_ACC_DATA_PASSWORD = "user_password";
    public static final String KEY_ACC_DATA_SHOW_MY_EMAIL = "user_show_email";
    public static final String KEY_ACC_DATA_SHOW_MY_MOBILE = "user_show_mobile";
    public static final String KEY_ACC_DATA_SHOW_MY_ADDRESS = "user_show_address";
    public static final String KEY_ACC_DATA_USERNAME = "username";
    public static final String KEY_NAME_FOUND_PRODUCTS_LIST = "foundProducts";

    public static final String ACTION_SIGNUP = "signup";
    public static final String SIGNUP_SUCCESS = "true";
    public static final String SIGNUP_FAILURE = "false";

    public static final String JSON_KEY_RESULT = "result";
    public static final String LOGIN_FAILURE = "false";
    public static final String JSON_KEY_RESULT_ERROR_MESSAGE = "errorMessage";
    public static final String KEY_SESSION_DATA_EMPTY = "no_session_var";
    public static final String KEY_ACC_DATA_DATE_ADDED = "user_date_added";
    public static final String KEY_ACC_DATA_ID = "user_id";
    public static final String JSON_RESULT_SUCCESS = "true";
    public static final String KEY_ACC_DATA_USER_ID = "user_id";
    public static final String ACTION_DELETE_ACCOUNT = "delAcc";
    public static final String JSON_RESULT_FAILURE = "false";
    public static final String ACTION_LOGOUT = "logout";

    public static final String KEY_ITEM_MAIN_PIC = "mainPic";
    public static final String KEY_ITEM_PIC_1 = "pic1";
    public static final String KEY_ITEM_PIC_2 = "pic2";
    public static final String KEY_ITEM_PIC_3 = "pic3";
    public static final String JSON_KEY_DATA = "jsonData";
    public static final String KEY_ITEM_PRICE_TO_DISCUSS = "pdPriceToDiscuss";
    public static final String KEY_ITEM_TYPE = "pdType";
    private static final String ACTION_EXPOSE_ITEM = "exposeItem";
    private static final String ACTION_LOAD_ALL_ITEMS_CATS_AND_TYPES = "loadAllCatsAndTypes";
    private static final String KEY_ITEMS_CATS_AND_TYPES = "itemsCatsAndTypes";
    private static final String KEY_PREF_EMPTY = "prefEmpty";
    public static final String KEY_ITEM_CATEGORY_NAME = "it_cat_name";
    public static final String KEY_ITEM_CATEGORY_ID = "it_cat_id";
    public static final String KEY_ITEM_CATEGORY_PIC = "it_cat_pic";
    public static final String KEY_ITEM_NO_PIC = "no_pic";
    public static final String ACTION_UPDATE_USER_SETTING = "updSet";
    public static final String ACTION_LOAD_ALL_PRODUCTS = "loadAllProducts";
    // TODO: 12/4/2017 CORRECT LOAD FEATURED PRODUCTS
    public static final String ACTION_LOAD_FEATURED_PRODUCTS = "loadAllFeaturedProducts";
    public static final String KEY_ITEM_MAIN_PIC_POST_FIX = "_main.jpg";
    public static final String KEY_ITEM_UNIQUE_NAME = "pdUniqueName";

    public static final String ACTION_REMOVE_PRODUCT = "rmProd";
    public static final String ACTION_POST_INQUIRY = "postInquiry";
    public static final Object DIR_PATH_SAVED_ITEMS_IMAGES = "SOSAchat Products";
    public static final String KEY_ITEM_IS_MINE = "itemIsMine";
    public static final String KEY_ITEM_MODE_EDITING = "itemModeEditing";
    public static final java.lang.String KEY_ITEM_ID = "item_id";
    public static final String ACTTION_UPDATE_ITEM_VIEWS_COUNT = "updViewsCount";
    public static final String KEY_ITEM_ITEM_VIEWS_ACCOUNT = "pdViews";
    public static final String KEY_ITEM_DATE_ADDED = "pdDateAdded";
    public static final String KEY_ITEM_NO_PRICE = "-1";

    public static final String ACTION_SEARCH_ITEMS = "search";
    public static final String KEY_ITEMS_SEARCH_RESULT_IDS = "itemsResultIds";
    public static final String ACTION_LOAD_ITEM_TYPES_FROM_CAT_ID = "loadItemsTypes";
    private static final String KEY_ITEM_TYPES = "itemTypes";
    public static final int RESULT_LOAD_IMAGE = 1340;
    public static final int REQ_CAMERA = 1341;
    public static final int REQ_PERMISSION_SAVE_BITMAP = 1342;
    public static final int REQ_PERMISSION_GALLERY = 1343;
    private static final String KEY_PROFILE_PIC_DATA = "ppData";
    private static final String ACTION_UPLOAD_PP = "uploadPP";
    public static final String KEY_ACC_DATA_MOBILE_HASH = "user_mobile_hash";
    private static final String ACTION_UPDATE_ITEM = "updItem";
    public static final String KEY_PD_MAIN_PIC_URI = "pdUri";
    public static final String KEY_POSTFIX_PIC_MAIN = "_main.jpg";
    public static final String KEY_POSTFIX_PIC_1 = "_pic1.jpg";
    public static final String KEY_POSTFIX_PIC_2 = "_pic2.jpg";
    public static final String KEY_POSTFIX_PIC_3 = "_pic3.jpg";
    public static final String KEY_ITEM_UPDATED = "itemUpdated";
    public static final String KEY_ITEM_TYPE_NAME = "items_types_name";
    public static final String KEY_ITEM_TYPE_ID = "items_types_id";
    public static final String KEY_ITEMS_CATEGORY_TYPES = "cat_types";


    //private static final String ACTION_LOAD_ITEMS_TYPES = "loadItemsTypes";


    public static String KEY_ITEM_NAME = "itemName";
    public static String KEY_ITEM_PRICE = "itemPrice";
    public static String KEY_ITEM_DESCRIPTION = "itemDescription";

    public static String KEY_ITEM_CURRENCY = "itemCurrency";
    public static String KEY_ITEM_QUALITY = "itemQuality";

    public static String KEY_ITEM_CATEGORY = "itemCategory";
    private AlertDialog alertDialogResults;
    
    /*
    public static String API_URL = "http://192.168.1.2/sosachat/api.php?";
    public static String DIR_PATH_CATEGORIES = "http://192.168.1.2/sosachat/img/";
    public static String DIR_PATH_PRODUCTS_PIX = "http://192.168.1.2/sosachat/img/products/";
    public static String DIR_PATH_PP = "http://192.168.88.30 /sosachat
    /img/users/";
    */

    public static  boolean isOnline(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }else{
            return false;
        }
    }

    private  SharedPreferences preferences;
    Context context;
    SharedPreferences.Editor editor;
    private BitmapCacheManager bitmapCacheManager;
    //AlertDialog alertDialog;
    public SOS_API(Context context){
        this.context = context;
        setBitmapCacheManager(new BitmapCacheManager(context));
        preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
        setupAlertDialogResponse();

        //alertDialog = HelperMethods.getAlertDialogProcessingWithMessage(context, HelperMethods.getStringResource(this, R.string.pbMsgProcessing),false);

        //getAllItemCats();
    }

    private void setupAlertDialogResponse() {

        AlertDialog.Builder builder;


        builder =new AlertDialog.Builder(context)
                .setPositiveButton("OK",null);
        //View view = getLayoutInflater().inflate(R.layout.layout_alert_dialog_load_response,null);
        //tvAlertDialogResponse = (TextView) view.findViewById(R.id.tvAlertDialogResponse);
        //builder.setView(view);
        alertDialogResults = builder.create();
        //alertDialogResults.setCancelable(false);



    }

    public static AlertDialog DWPNB(Context ctx, boolean show, String message, String positiveBtn, DialogInterface.OnClickListener positiveHandler,
                        String negativeBtn, DialogInterface.OnClickListener negativeHandler, String title){
        return dialogWithPositiveNegativeButtons(ctx, show, message, positiveBtn, positiveHandler, negativeBtn, negativeHandler, title);
    }

    public static AlertDialog dialogWithPositiveNegativeButtons(Context ctx, boolean show, String message, String positiveBtn, DialogInterface.OnClickListener positiveHandler,
                                           String negativeBtn, DialogInterface.OnClickListener negativeHandler, String title){

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        if(title != null) builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtn, positiveHandler);
        builder.setNegativeButton(negativeBtn, negativeHandler);

        AlertDialog alertDialog = builder.create();

        if(show) alertDialog.show();

        return alertDialog;

    }

    public void toggleAlertDialogResponseWithMessage(boolean show, String message){

        if(message != null){
            alertDialogResults.setMessage(message);

        }

        if(show){
            alertDialogResults.show();
        }else{
            alertDialogResults.hide();
        }


    }

    public void TADRWM(boolean show, String msg){
        toggleAlertDialogResponseWithMessage(show, msg);
    }

    public boolean isLoggedIn(){




        boolean result = false;




        String username = preferences.getString(KEY_ACC_DATA_MOBILE, KEY_NO_USERNAME);
        //String password = preferences.getString(KEY_PASSWORD, KEY_PASSWORD);

        if(!username.equalsIgnoreCase(KEY_NO_USERNAME) ){
            result = true;

            //Toast.makeText(context, "username : " + username, Toast.LENGTH_SHORT).show();
        }

        return result;


    }

    public  void login(final SOSApiListener SOSApiListener, final String username, final String password) {



        String loginURL = SOS_API.API_URL + "act=" + ACTION_LOGIN + "&username=" + username + "&password=" + password;

       //Log.e(TAG, "login:url -> " + loginURL );




        StringRequest request = new StringRequest(
                Request.Method.GET,
                loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {


                           //Log.e(TAG, "onResponse: login response -> " + s );

                            JSONObject jo = new JSONObject(s);
                            Bundle b = HM.JTB(jo);//HelperMethods.jsonToBundle(jo);



                            if(b.size() == 1){
                                b.putString(JSON_KEY_RESULT, SIGNUP_FAILURE);
                            }else {
                                b.putString(JSON_KEY_RESULT, SIGNUP_SUCCESS);

                                if(b.getString(KEY_USER_IS_ADMIN).equals("1")){
                                    b.putBoolean(KEY_USER_IS_ADMIN, true);
                                }

                                //String username = b.getString(SOS_API.KEY_ACC_DATA_MOBILE);
                                //String password = b.getString(SOS_API.KEY_ACC_DATA_PASSWORD);
                                setSessionData(b);

                            }

                            SOSApiListener.onLoginResult(b);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Bundle b = new Bundle();
                            b.putString(JSON_KEY_RESULT, SIGNUP_FAILURE);
                            SOSApiListener.onLoginResult(b);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Bundle b = new Bundle();
                        b.putString(SOS_API.JSON_KEY_RESULT, LOGIN_FAILURE);
                        b.putString(SOS_API.JSON_KEY_RESULT_ERROR_MESSAGE, volleyError.getMessage());
                        SOSApiListener.onLoginResult(b);

                    }
                }
        );



        Volley.newRequestQueue(context).add(request);


    }

    public static  Object[] keys;

    public String getSessionVar(String key){

        return preferences.getString(key, KEY_SESSION_DATA_EMPTY);
    }

    private void setSessionVar(String key, String val){

        editor.putString(key, val);
        editor.apply();
        editor.commit();
    }

    public void SSV(String key, String val){
        setSessionVar(key,val);
    }

    public static final int PIC_SOURCE_CAMERA = 1;
    public static final int PIC_SOURCE_GALLERY = 0;

    public String GSV(String key){
        return getSessionVar(key);
    }

    private void setSessionData(Bundle b) {

        editor.clear();
        editor.commit();
        keys = b.keySet().toArray();

        for(int i = 0; i < keys.length; i++){


            String key = keys[i].toString();
            String val = b.getString(key);

            editor.putString(key, val);

           //Log.e(TAG, "setSessionData: putting : " + keys[i] + " : " + b.getString(keys[i].toString()) );


        }

        editor.commit();

    }

    public void signup(final SOSApiListener SOSApiListener, Bundle data){



        final String mobile = data.getString(SOS_API.KEY_ACC_DATA_MOBILE);
        final String email = data.getString(SOS_API.KEY_ACC_DATA_EMAIL);
        final String password = data.getString(SOS_API.KEY_ACC_DATA_PASSWORD);
        final String fullName = data.getString(SOS_API.KEY_ACC_DATA_FULL_NAME);
        final String displayName =data.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME);
        final String location = data.getString(SOS_API.KEY_ACC_DATA_LOCATION);

        //Log.e(TAG, "bundle -> " + data.toString() );

        String url = SOS_API.API_URL + "act=" + ACTION_SIGNUP;



        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                       Log.e("signup res -> ", response);




                        try {


                            JSONObject jo = new JSONObject(response);
                            Bundle b = HelperMethods.jsonToBundle(jo);

                            if(b.size() == 1){
                                b.putString(JSON_KEY_RESULT, SIGNUP_FAILURE);

                            }else {


                                b.putString(JSON_KEY_RESULT, SIGNUP_SUCCESS);


                                //SOSApiListener.onSignUpResult(b);

                            }

                            SOSApiListener.onSignUpResult(b);
                           ////Log.e(TAG, "my bundle from jo -->> " + b.toString());


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Bundle b = new Bundle();
                            b.putString(JSON_KEY_RESULT, SIGNUP_FAILURE);
                            SOSApiListener.onSignUpResult(b);
                        }




                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                       //Log.e("signup err res -> ", error.getMessage());
                        Bundle b = new Bundle();
                        b.putString(JSON_KEY_RESULT, SIGNUP_FAILURE);
                        SOSApiListener.onSignUpResult(b);


                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("password", password);
                params.put("email", email);
                params.put("fullName", fullName);
                params.put("displayName", displayName);
                params.put("location", location);
                return params;
            }



        };
        Volley.newRequestQueue(context).add(postRequest);


    }

    public static void deleAccount(Context context, final SOSApiListener sosApiListener, String mobile) {


        String url = API_URL + "act=" + ACTION_DELETE_ACCOUNT + "&" + KEY_ACC_DATA_MOBILE + "=" + mobile;
       //Log.e(TAG, "deleAccount: url -> " + url );

        StringRequest request = new StringRequest(

                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Bundle b = new Bundle();
                        if(s.equalsIgnoreCase(JSON_RESULT_SUCCESS)){

                            b.putString(JSON_KEY_RESULT, JSON_RESULT_SUCCESS);
                            sosApiListener.onLogoutResult();
                        }else{
                            b.putString(JSON_KEY_RESULT, JSON_RESULT_FAILURE);
                        }
                        sosApiListener.onAccountDeleteResult(b);
                       //Log.e(TAG, "ON RESPONSE FROM VOLLEY -> : " + s );
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Bundle b = new Bundle();
                        b.putString(JSON_KEY_RESULT, JSON_RESULT_FAILURE);
                        sosApiListener.onAccountDeleteResult(b);

                    }
                }

        );

        Volley.newRequestQueue(context).add(request);


    }

    public void exposeItem(final SOSApiListener listener, final Bundle data) {


        //Log.e(TAG, "exposeItem: data -> " + data.toString() );
        //String pdUniqueName = data.getString("");

        String url = SOS_API.API_URL;
        String itemId = data.getString(KEY_ITEM_ID);
        final boolean itemUpdating = itemId != null;

        if( itemUpdating  ){ //UPDATING ITEM
            url = url.concat("act=" + ACTION_UPDATE_ITEM + "&itemId=" + itemId + "&un=" + data.getString(Product.KEY_PD_UNIQUE_NAME) );
        }else{
            url = url.concat("act=" + ACTION_EXPOSE_ITEM);
        }

        //Log.e(TAG, "exposeItem: url -> " + url );
        //Log.e(TAG, "exposeItem: item data -> " + data.toString() );



        StringRequest requestItemDetails = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                       Log.e(TAG, "ON THA RES -> " + s );

                        Bundle b = new Bundle();
                        b.putString(JSON_KEY_DATA, s);
                        try {
                            JSONObject jo = new JSONObject(s);


                            String result = jo.getString(JSON_KEY_RESULT);

                            if(result.equals(JSON_RESULT_SUCCESS)){
                                b.putString(JSON_KEY_RESULT, JSON_RESULT_SUCCESS);

                                b.putString(KEY_ITEM_UNIQUE_NAME, jo.getString(KEY_ITEM_UNIQUE_NAME));

                                if(itemUpdating){
                                    b.putBoolean(KEY_ITEM_UPDATED, true);
                                }
                            }else{
                                b.putString(JSON_KEY_RESULT, JSON_RESULT_FAILURE);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            b.putString(JSON_KEY_RESULT, JSON_RESULT_FAILURE);
                           //Log.e(TAG, "THA FUCKING EXCEPTION" + e.getMessage() );
                        }



                        listener.onExposeItemResult(b);





                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {



                        Bundle b = new Bundle();
                        b.putString(JSON_KEY_RESULT, JSON_RESULT_FAILURE);
                        b.putString(JSON_KEY_DATA, volleyError.getMessage());

                        listener.onExposeItemResult(b);
                       //Log.e(TAG, "expose volley item -> " + volleyError.toString() );

                    }
                }
        ){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();



                params.put(KEY_ITEM_NAME,data.getString(KEY_ITEM_NAME));
                params.put(KEY_ITEM_PRICE,data.getString(KEY_ITEM_PRICE));
                params.put(KEY_ITEM_CURRENCY,data.getString(KEY_ITEM_CURRENCY));
                params.put(KEY_ITEM_PRICE_TO_DISCUSS, data.getString(KEY_ITEM_PRICE_TO_DISCUSS));
                params.put(KEY_ITEM_DESCRIPTION,data.getString(KEY_ITEM_DESCRIPTION));
                params.put(KEY_ITEM_CATEGORY,data.getString(KEY_ITEM_CATEGORY));
                params.put(KEY_ITEM_TYPE,data.getString(KEY_ITEM_TYPE));
                params.put(Product.KEY_PD_OWNER_ID, data.getString(Product.KEY_PD_OWNER_ID));
                params.put(KEY_ITEM_MAIN_PIC,  data.getString(KEY_ITEM_MAIN_PIC));
                params.put(KEY_ITEM_PIC_1, data.getString(KEY_ITEM_PIC_1));
                params.put(KEY_ITEM_PIC_2,  data.getString(KEY_ITEM_PIC_2));
                params.put(KEY_ITEM_PIC_3,  data.getString(KEY_ITEM_PIC_3));
                params.put(KEY_ITEM_QUALITY, data.getString(KEY_ITEM_QUALITY));


                return params;
            }



        };

        Volley.newRequestQueue(context).add(requestItemDetails);
    }

    public void loadMyProducts(final ListenerLoadMyProducts listener){

        String url = SOS_API.API_URL + "act=" + SOS_API.ACTION_LOAD_ALL_MY_PRODUCTS +
                "&uid=" + getSessionVar(KEY_ACC_DATA_USER_ID);

        Log.e(TAG, "myproducs url : " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {



                        if(s.equals(JSON_RESULT_FAILURE)){
                            listener.onMyProductsEmpty();
                        }else{

                            List<ProductMyProducts> products = new ArrayList<>();

                            try {
                                JSONArray ja = new JSONArray(s);

                                if(ja.length() == 0){
                                    listener.onMyProductsEmpty();
                                }else{


                                    for (int i = 0; i < ja.length(); i++) {

                                        JSONObject jo = ja.getJSONObject(i);

                                        ProductMyProducts pd = new ProductMyProducts(
                                                jo.getString(Product.KEY_PD_NAME),
                                                jo.getString(Product.KEY_PD_PRICE),
                                                jo.getString(Product.KEY_PD_IMG) + SOS_API.KEY_ITEM_MAIN_PIC_POST_FIX,
                                                jo.getString(Product.KEY_PD_CUR),
                                                jo.getString(Product.KEY_PD_CAT),
                                                jo.getString(Product.KEY_PD_QUAL),
                                                jo.getString(Product.KEY_PD_DESC),
                                                jo.getString(Product.KEY_PD_UNIQUE_NAME));

                                        Bundle b = new Bundle();


                                        String dateStart = jo.getString(KEY_ITEM_DATE_ADDED);

                                        //HelperDate.DateDiff dateDiff = HelperDate.dateDiff(dateStart, dateEnd );//new Date().toString());

                                        String postedDate = HM.CLDTAS(context,
                                                HelperDate.getLongDateFromDateStr(dateStart), HelperDate.getCurrentLondDate());//dateDiff.toSocialFormat();//HM.FD(dateDiff, dateStart);

                                        b.putString(KEY_ITEM_ID, jo.getString(KEY_ITEM_ID));
                                        b.putString(KEY_ITEM_UNIQUE_NAME, jo.getString(KEY_ITEM_UNIQUE_NAME));
                                        b.putString(KEY_ITEM_DATE_ADDED, postedDate);
                                        //b.putString(KEY_ITEM_DATE_ADDED, postedDate);
                                        b.putString(KEY_ITEM_ITEM_VIEWS_ACCOUNT, jo.getString(KEY_ITEM_ITEM_VIEWS_ACCOUNT));
                                        b.putString(KEY_ACC_DATA_USER_ID, jo.getString(KEY_ACC_DATA_USER_ID));
                                        b.putString(KEY_ACC_DATA_DISPLAY_NAME, jo.getString(KEY_ACC_DATA_DISPLAY_NAME));
                                        b.putString(KEY_ACC_DATA_EMAIL, jo.getString(KEY_ACC_DATA_EMAIL));
                                        b.putString(KEY_ACC_DATA_MOBILE, jo.getString(KEY_ACC_DATA_MOBILE));

                                        pd.setDataBundle(b);
                                        products.add(pd);

                                    }
                                    listener.onMyProductsLoaded(products);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onParseJsonError(s);
                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onNetworkError(volleyError.getMessage());
                    }
                }
        );



        Volley.newRequestQueue(context).add(request);


    }

    public BitmapCacheManager getBitmapCacheManager() {
        return bitmapCacheManager;
    }

    public void setBitmapCacheManager(BitmapCacheManager bitmapCacheManager) {
        this.bitmapCacheManager = bitmapCacheManager;
    }

    public static File getSOSAchatRootDir() {
        return new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + BitmapCacheManager.CACHE_ROOT_DIR + "/");
    }

    public static void deletePP() {

        BitmapCacheManager.emptyDir(new File(getSOSAchatRootDir().toString() + "/" + DIR_NAME_PIX_CACHE_PROFILCE_PIC));
        Log.e(TAG, "deletePP: " );
    }

    public void loadAllInquiries(final AdapterInquiry.CallBacks callBacks) {

        String url = SOS_API.API_URL + "act=" + SOS_API.ACTION_LOAD_ALL_INQUIRIES;
        Log.e(TAG, "loadRecentItems: url -> " + url );

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONArray array = new JSONArray(s);
                            ArrayList<Inquiry> inquiries = new ArrayList<>();

                            for (int i = 0; i < array.length(); i++){

                                JSONObject object = array.getJSONObject(i);
                                Inquiry inquiry = new Inquiry();

                                inquiry.setTitle(object.getString(Inquiry.KEY_TITLE));
                                inquiry.setMessage(object.getString(Inquiry.KEY_DESC));
                                inquiry.setDateTime(object.getString(Inquiry.KEY_DATETIME));
                                inquiry.setPosterName(object.getString(Inquiry.KEY_POSTERNAME));

                                Bundle data = new Bundle();

                                data.putString(SOS_API.KEY_ACC_DATA_MOBILE, object.getString(SOS_API.KEY_ACC_DATA_MOBILE));
                                data.putString(SOS_API.KEY_ACC_DATA_EMAIL, object.getString(SOS_API.KEY_ACC_DATA_EMAIL));
                                data.putString(Inquiry.KEY_DATETIME, object.getString(Inquiry.KEY_DATETIME));
                                data.putString(SOS_API.KEY_ACC_DATA_MOBILE_HASH, object.getString(SOS_API.KEY_ACC_DATA_MOBILE_HASH));

                                inquiry.setData(data);
                                inquiries.add(inquiry);

                            }

                            callBacks.onInquiriesLoaded(inquiries);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            callBacks.onInquiriesLoadError(false, e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callBacks.onInquiriesLoadError(true, volleyError.getMessage());
                    }
                }
        );

        Volley.newRequestQueue(context).add(request);

    }

    public interface  ListenerLoadMyProducts{
        void onMyProductsLoaded(List<ProductMyProducts> products);
        void onMyProductsEmpty();
        void onNetworkError(String msg);
        void onParseJsonError(String s);
    }

    public interface  ListenerLoadRecentItems{
        void onRecentItemsLoaded(List<Product> products);
        void onRecentItemsEmpty();
        void onNetworkError(String msg);

        void onParseJsonError(String s);
    }

    public void loadRecentItems(final ListenerLoadRecentItems listener) {

        String url = SOS_API.API_URL + "act=" + SOS_API.ACTION_LOAD_FEATURED_PRODUCTS + "&uid=" + getSessionVar(KEY_ACC_DATA_USER_ID);
        Log.e(TAG, "loadRecentItems: url -> " + url );


        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {



                        if(s.equals(JSON_RESULT_FAILURE)){
                            listener.onRecentItemsEmpty();
                        }else{

                            List<Product> products = new ArrayList<>();

                            try {
                                JSONArray ja = new JSONArray(s);

                                if(ja.length() == 0){
                                    listener.onRecentItemsEmpty();
                                }else{


                                    for (int i = 0; i < ja.length(); i++) {

                                        JSONObject jo = ja.getJSONObject(i);

                                        Product pd = new Product(
                                                jo.getString(Product.KEY_PD_NAME),
                                                jo.getString(Product.KEY_PD_PRICE),
                                                jo.getString(Product.KEY_PD_IMG) + SOS_API.KEY_ITEM_MAIN_PIC_POST_FIX,
                                                jo.getString(Product.KEY_PD_CUR),
                                                jo.getString(Product.KEY_PD_CAT),
                                                jo.getString(Product.KEY_PD_QUAL),
                                                jo.getString(Product.KEY_PD_DESC),
                                                jo.getString(Product.KEY_PD_UNIQUE_NAME));

                                        Bundle b = new Bundle();


                                        String dateStart = jo.getString(KEY_ITEM_DATE_ADDED);

                                        //HelperDate.DateDiff dateDiff = HelperDate.dateDiff(dateStart, dateEnd );//new Date().toString());

                                        String postedDate = HM.CLDTAS(context,
                                                HelperDate.getLongDateFromDateStr(dateStart), HelperDate.getCurrentLondDate());//dateDiff.toSocialFormat();//HM.FD(dateDiff, dateStart);

                                        b.putString(KEY_ITEM_ID, jo.getString(KEY_ITEM_ID));
                                        b.putString(KEY_ITEM_UNIQUE_NAME, jo.getString(KEY_ITEM_UNIQUE_NAME));
                                        b.putString(KEY_ITEM_DATE_ADDED, postedDate);
                                        //b.putString(KEY_ITEM_DATE_ADDED, postedDate);
                                        b.putString(KEY_ITEM_ITEM_VIEWS_ACCOUNT, jo.getString(KEY_ITEM_ITEM_VIEWS_ACCOUNT));
                                        b.putString(KEY_ACC_DATA_USER_ID, jo.getString(KEY_ACC_DATA_USER_ID));
                                        b.putString(KEY_ACC_DATA_DISPLAY_NAME, jo.getString(KEY_ACC_DATA_DISPLAY_NAME));
                                        b.putString(KEY_ACC_DATA_EMAIL, jo.getString(KEY_ACC_DATA_EMAIL));
                                        b.putString(KEY_ACC_DATA_MOBILE, jo.getString(KEY_ACC_DATA_MOBILE));

                                        pd.setData(b);
                                        products.add(pd);

                                    }
                                    listener.onRecentItemsLoaded(products);


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                listener.onParseJsonError(s);
                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onNetworkError(volleyError.getMessage());
                    }
                }
        );



        Volley.newRequestQueue(context).add(request);

    }

    public void loadAllProducts(final SOSApiListener listener, String uid) {


        final List<ProductMyProducts> allProducts = new ArrayList<>();
        allProducts.clear();
        String url = SOS_API.API_URL + "act=" + SOS_API.ACTION_LOAD_ALL_PRODUCTS + "&uid=" + uid ;

        Log.e(TAG, "loadAllProducts: url -> " + url );

        JsonArrayRequest request = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {

                        if(jsonArray.toString().equals("{result:'false'}")){
                            Log.e(TAG, "DADIK" );
                        }


                        //Log.e(TAG, "resp my pds -> " + jsonArray.length() );


                        //listener.onLoadAllMyProductsResult(products);

                        if(jsonArray.length() > 0){

                            for(int i =0; i < jsonArray.length(); i++) {

                                try {




                                    JSONObject jo = jsonArray.getJSONObject(i);

                                    ProductMyProducts pd = new ProductMyProducts(
                                            jo.getString(Product.KEY_PD_NAME),
                                            jo.getString(Product.KEY_PD_PRICE),
                                            jo.getString(Product.KEY_PD_IMG) + KEY_ITEM_MAIN_PIC_POST_FIX,
                                            jo.getString(Product.KEY_PD_CUR),
                                            jo.getString(Product.KEY_PD_CAT),
                                            jo.getString(Product.KEY_PD_QUAL),
                                            jo.getString(Product.KEY_PD_DESC),
                                            jo.getString(ProductWishList.KEY_DATE_ADDED));



                                    //Log.e(TAG, "date -> " + dateJson );

                                    Bundle b = new Bundle();
                                    b.putString(KEY_ITEM_ID, jo.getString(KEY_ITEM_ID));
                                    b.putString(KEY_ITEM_ITEM_VIEWS_ACCOUNT, jo.getString(KEY_ITEM_ITEM_VIEWS_ACCOUNT));
                                    b.putString(KEY_ITEM_UNIQUE_NAME, jo.getString(KEY_ITEM_UNIQUE_NAME));
                                    b.putString(KEY_ACC_DATA_DISPLAY_NAME, jo.getString(KEY_ACC_DATA_DISPLAY_NAME));
                                    b.putString(KEY_ACC_DATA_USER_ID, jo.getString(KEY_ACC_DATA_USER_ID));
                                    b.putString(KEY_ACC_DATA_MOBILE, jo.getString(KEY_ACC_DATA_MOBILE));
                                    b.putString(KEY_ACC_DATA_EMAIL, jo.getString(KEY_ACC_DATA_EMAIL));
                                    String dateStart = jo.getString(Product.KEY_PD_DATE_ADDED);

                                    //HelperDate.DateDiff dateDiff = HelperDate.dateDiff(dateStart, dateEnd );//new Date().toString());

                                    String postedDate = HM.CLDTAS(context,
                                            HelperDate.getLongDateFromDateStr(dateStart), HelperDate.getCurrentLondDate());//dateDiff.toSocialFormat();//HM.FD(dateDiff, dateStart);


                                    b.putString(Product.KEY_PD_DATE_ADDED, postedDate);

                                    pd.setDataBundle(b);

                                    allProducts.add(pd);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    listener.onLoadAllProductsError(e.getMessage());
                                }


                            }

                        }

                        listener.onLoadAllProductsResult(allProducts);




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       //Log.e("LOAD ALL PRODS", "onErrorResponse: " + volleyError.getMessage() );
                        //listener.onLoadAllProductsResult(new ArrayList<ProductMyProducts>());
                        listener.onLoadAllProductsError(volleyError.getMessage());
                    }
                }
        );

        Volley.newRequestQueue(context).add(request);


    }

    public void updateAccSettings(final SOSApiListener listener, final String settingKey, final String newValue) {


        String userid = getSessionVar(KEY_ACC_DATA_USER_ID);
        String url = API_URL + "act=" + ACTION_UPDATE_USER_SETTING + "&setName=" + settingKey + "&newVal=" + newValue +
                "&user_id=" + userid;

       //Log.e(TAG, "updateSignleSetting: url -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        if(s.equals(JSON_RESULT_SUCCESS)) {
                            listener.onUpdateSettingsResult(settingKey, s);
                            setSessionVar(settingKey, newValue);
                        }else{
                            listener.onUpdateSettingsResult(settingKey, JSON_RESULT_FAILURE);
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onUpdateSettingsResult(settingKey, JSON_RESULT_FAILURE);
                    }
                }
        );

        Volley.newRequestQueue(context).add(request);

    }

    public void updateUserPassword(final OnUpdatePasswordListenerm listener, final String newPassword){

        String url = API_URL + "act=" + ACTION_UPDATE_USER_SETTING + "&newVal=" + newPassword +
                "&" + KEY_ACC_DATA_USER_ID + "=" + GSV(KEY_ACC_DATA_USER_ID) + "&setName=" + KEY_ACC_DATA_PASSWORD;

        Log.e(TAG, "updateUserPassword: url -> " + url );

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        String resMsg;

                        if(s.equals(JSON_RESULT_SUCCESS)){

                            // TODO: 5/1/2018 SEND CONFIRMATION SMS

                            resMsg = newPassword;

                            SSV(KEY_ACC_DATA_PASSWORD, newPassword);

                        }else{
                            resMsg = s;
                        }

                        listener.onPasswordUpdateResult(s, resMsg);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        listener.onPasswordUpdateResult(JSON_RESULT_FAILURE, volleyError.getMessage());

                    }
                }
        );

        Volley.newRequestQueue(context).add(stringRequest);

    }

    public interface OnUpdatePasswordListenerm{
        void onPasswordUpdateResult(String resStatus, String resMessage);
    }

    public static String getSOSAchatItemPixCameraPath() {
        return Environment.getExternalStorageDirectory().toString()+"/" + KEY_SOSACHAT_PIX_DIR;
    }

    public static String GSAIPCP(){
        return getSOSAchatItemPixCameraPath();
    }

    public void removeProduct(final SOSApiListener listener, ProductWishList pd) {

        String myid = getSessionVar(KEY_ACC_DATA_USER_ID);

        String uniqueName = pd.getPdUniqueNameFromIMG();
        String url = SOS_API.API_URL + "act=" + ACTION_REMOVE_PRODUCT + "&unq=" + uniqueName + "&myid=" + myid ;

       //Log.e(TAG, "removeProduct: url -> " + url );



        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Bundle b = new Bundle();
                        if(s.equals(JSON_RESULT_SUCCESS)) {

                            b.putString(JSON_KEY_RESULT, JSON_RESULT_SUCCESS);

                        }else{
                            b.putString(JSON_KEY_RESULT, JSON_RESULT_FAILURE);
                        }

                        listener.onRemoveProductResult(b);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Bundle b = new Bundle();
                        b.putString(JSON_KEY_RESULT, JSON_RESULT_FAILURE);
                        listener.onRemoveProductResult(b);
                    }
                }
        );


        Volley.newRequestQueue(context).add(request);

    }

    public void postInquiry(final SOSApiListener listener, String title, String desc) {

        title = Uri.encode(title);
        desc = Uri.encode(desc);

        String url = API_URL + "act=" + ACTION_POST_INQUIRY + "&title=" + title + "&desc=" + desc + "&myid=" + getSessionVar(KEY_ACC_DATA_USER_ID);
       //Log.e(TAG, "postInquiry: url -> " + url );
        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                       //Log.e(TAG, "onResponse: postinq -> " + s );
                        listener.onPostInquiryResult(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       //Log.e(TAG, "onErrorResponse: postinq -> " + volleyError.getMessage() );
                        listener.onPostInquiryResult(JSON_RESULT_FAILURE);
                    }
                }

        );

        Volley.newRequestQueue(context).add(request);

    }

    public void updateItemViewsCount(final SOSApiListener listener, String itemId) {

        String url = API_URL + "act=" + ACTTION_UPDATE_ITEM_VIEWS_COUNT + "&itemid=" + itemId;
       //Log.e(TAG, "updateItemViewsCount: url -> " + url );
        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                       if(s.equals(JSON_RESULT_FAILURE) == false){

                           String newCount = s;

                           listener.onUpdateItemViewsCountResult(newCount);
                       }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       //Log.e(TAG, "onErrorResponse: upd view count -> " + volleyError.getMessage() );
                    }
                }
        );

        Volley.newRequestQueue(context).add(request);

    }

    public void search(final  SOSApiListener listener, String q) {


        String uid = getSessionVar(KEY_ACC_DATA_USER_ID);
        final List<ProductMyProducts> allProducts = new ArrayList<>();
        allProducts.clear();
        String url = SOS_API.API_URL + "act=" + ACTION_SEARCH_ITEMS + "&uid=" + uid + "&q=" + q;

       //Log.e(TAG, "search url : " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {



                        if(s.equals(JSON_RESULT_FAILURE)){
                            listener.onSearchResultError(s);
                        }else{

                            String[] itemsIds = s.split(",");

                            listener.onSearchResult(HM.SATS(itemsIds));
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onSearchResultError(volleyError.getMessage());
                    }
                }
        );

        Volley.newRequestQueue(context).add(request);

        /*JsonArrayRequest request = new JsonArrayRequest(
                url,
                new Response.CallBacks<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {


                        //Log.e(TAG, "resp my pds -> " + jsonArray.length() );


                        //listener.onLoadAllMyProductsResult(products);

                        if(jsonArray.length() > 0){

                            for(int i =0; i < jsonArray.length(); i++) {

                                try {




                                    JSONObject jo = jsonArray.getJSONObject(i);

                                    ProductMyProducts pd = new ProductMyProducts(
                                            jo.getString(Product.KEY_PD_NAME),
                                            jo.getString(Product.KEY_PD_PRICE),
                                            jo.getString(Product.KEY_PD_IMG) + KEY_ITEM_MAIN_PIC_POST_FIX,
                                            jo.getString(Product.KEY_PD_CUR),
                                            jo.getString(Product.KEY_PD_CAT),
                                            jo.getString(Product.KEY_PD_QUAL),
                                            jo.getString(Product.KEY_PD_DESC),
                                            jo.getString(ProductWishList.KEY_DATE_ADDED));



                                    //Log.e(TAG, "date -> " + dateJson );

                                    Bundle b = new Bundle();
                                    b.putString(KEY_ITEM_ID, jo.getString(KEY_ITEM_ID));
                                    b.putString(KEY_ITEM_ITEM_VIEWS_ACCOUNT, jo.getString(KEY_ITEM_ITEM_VIEWS_ACCOUNT));
                                    b.putString(KEY_ITEM_UNIQUE_NAME, jo.getString(KEY_ITEM_UNIQUE_NAME));
                                    b.putString(KEY_ACC_DATA_DISPLAY_NAME, jo.getString(KEY_ACC_DATA_DISPLAY_NAME));
                                    b.putString(KEY_ACC_DATA_USER_ID, jo.getString(KEY_ACC_DATA_USER_ID));
                                    b.putString(KEY_ACC_DATA_MOBILE, jo.getString(KEY_ACC_DATA_MOBILE));
                                    b.putString(KEY_ACC_DATA_EMAIL, jo.getString(KEY_ACC_DATA_EMAIL));

                                    pd.setDataBundle(b);

                                    allProducts.add(pd);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                   //Log.e(TAG, "search pds exception -> " + e.toString() );
                                }


                            }

                        }

                        listener.onSearchResult(allProducts);




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       //Log.e("LOAD ALL PRODS", "onErrorResponse: " + volleyError.getMessage() );
                        //listener.onLoadAllProductsResult(new ArrayList<ProductMyProducts>());
                        listener.onSearchResultError(volleyError.getMessage());
                    }
                }
        );*/



    }

    public void loadSearchResultItems(final SOSApiListener listener, String ids) {

        String url = API_URL + "act=loadSearchRes&ids=" + ids;

       //Log.e(TAG, "loadSearchResultItems: url -> " + url );


        final List<ProductMyProducts> allProducts = new ArrayList<>();
        allProducts.clear();


        JsonArrayRequest request = new JsonArrayRequest(
                url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray jsonArray) {


                        //Log.e(TAG, "resp my pds -> " + jsonArray.length() );


                        //listener.onLoadAllMyProductsResult(products);

                        if(jsonArray.length() > 0){

                            for(int i =0; i < jsonArray.length(); i++) {

                                try {




                                    JSONObject jo = jsonArray.getJSONObject(i);

                                    ProductMyProducts pd = new ProductMyProducts(
                                            jo.getString(Product.KEY_PD_NAME),
                                            jo.getString(Product.KEY_PD_PRICE),
                                            jo.getString(Product.KEY_PD_IMG) + KEY_ITEM_MAIN_PIC_POST_FIX,
                                            jo.getString(Product.KEY_PD_CUR),
                                            jo.getString(Product.KEY_PD_CAT),
                                            jo.getString(Product.KEY_PD_QUAL),
                                            jo.getString(Product.KEY_PD_DESC),
                                            jo.getString(ProductWishList.KEY_DATE_ADDED));



                                    //Log.e(TAG, "date -> " + dateJson );

                                    Bundle b = new Bundle();
                                    b.putString(KEY_ITEM_ID, jo.getString(KEY_ITEM_ID));
                                    b.putString(KEY_ITEM_ITEM_VIEWS_ACCOUNT, jo.getString(KEY_ITEM_ITEM_VIEWS_ACCOUNT));
                                    b.putString(KEY_ITEM_UNIQUE_NAME, jo.getString(KEY_ITEM_UNIQUE_NAME));
                                    b.putString(KEY_ACC_DATA_DISPLAY_NAME, jo.getString(KEY_ACC_DATA_DISPLAY_NAME));
                                    b.putString(KEY_ACC_DATA_USER_ID, jo.getString(KEY_ACC_DATA_USER_ID));
                                    b.putString(KEY_ACC_DATA_MOBILE, jo.getString(KEY_ACC_DATA_MOBILE));
                                    b.putString(KEY_ACC_DATA_EMAIL, jo.getString(KEY_ACC_DATA_EMAIL));
                                    String dateStart = jo.getString(Product.KEY_PD_DATE_ADDED);

                                    //HelperDate.DateDiff dateDiff = HelperDate.dateDiff(dateStart, dateEnd );//new Date().toString());

                                    String postedDate = HM.CLDTAS(context,
                                            HelperDate.getLongDateFromDateStr(dateStart), HelperDate.getCurrentLondDate());//dateDiff.toSocialFormat();//HM.FD(dateDiff, dateStart);


                                    b.putString(Product.KEY_PD_DATE_ADDED, postedDate);

                                    pd.setDataBundle(b);

                                    allProducts.add(pd);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                   //Log.e(TAG, "load pds exception -> " + e.toString() );
                                }


                            }

                        }

                        listener.onLoadSearchResultItemsResult(allProducts);




                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                       //Log.e("LOAD ALL PRODS", "onErrorResponse: " + volleyError.getMessage() );
                        //listener.onLoadAllProductsResult(new ArrayList<ProductMyProducts>());
                        listener.onLoadSearchResultItemsError(volleyError.getMessage());
                    }
                }
        );

        Volley.newRequestQueue(context).add(request);

    }

    public void loadItemsTypesFromCatName(final  SOSApiListener listener, String catName){
        String catsAndTypes = GSV(KEY_ITEMS_CATS_AND_TYPES);

        if(catsAndTypes.equals(KEY_SESSION_DATA_EMPTY)){

            //Log.e(TAG, "loadItemsTypesFromCatId: error " );

        }else{


            try {
                JSONArray cats = new JSONArray(catsAndTypes);

                for(int i = 0; i < cats.length(); i++){

                    JSONObject cat = (JSONObject) cats.get(i);
                    String curCatId = cat.getString(KEY_ITEM_CATEGORY_NAME);

                    if(curCatId.equals("" + catName)){

                        String types = cat.getString(KEY_ITEMS_CATEGORY_TYPES) ;

                        JSONArray jsonArray = new JSONArray(types);

                        if(jsonArray.length() > 0){

                            String[] typesArr = new String[jsonArray.length()];

                            for(int j = 0; j < jsonArray.length(); j++){

                                JSONObject type = jsonArray.getJSONObject(j);
                                typesArr[j] = HM.UCF(type.getString(KEY_ITEM_TYPE_NAME));

                            }

                            listener.onLoadItemsTypesResult(typesArr);

                        }else{
                            listener.onLoadItemsTypesResultError();
                        }

                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "loadItemsTypesFromCatId: json exception -> " + e.getMessage() );
            }

        }
    }

    public void loadItemsTypesFromCatId(final SOSApiListener listener, final int categoryId) {


        String catsAndTypes = GSV(KEY_ITEMS_CATS_AND_TYPES);

        if(catsAndTypes.equals(KEY_SESSION_DATA_EMPTY)){

            //Log.e(TAG, "loadItemsTypesFromCatId: error " );

        }else{


            try {
                JSONArray cats = new JSONArray(catsAndTypes);

                for(int i = 0; i < cats.length(); i++){

                    JSONObject cat = (JSONObject) cats.get(i);
                    String curCatId = cat.getString(KEY_ITEM_CATEGORY_ID);

                    if(curCatId.equals("" + categoryId)){

                        String types = cat.getString(KEY_ITEMS_CATEGORY_TYPES) ;

                        JSONArray jsonArray = new JSONArray(types);

                        if(jsonArray.length() > 0){

                            String[] typesArr = new String[jsonArray.length()];

                            for(int j = 0; j < jsonArray.length(); j++){

                                JSONObject type = jsonArray.getJSONObject(j);
                                typesArr[j] = HM.UCF(type.getString(KEY_ITEM_TYPE_NAME));

                            }

                            listener.onLoadItemsTypesResult(typesArr);

                        }else{
                            listener.onLoadItemsTypesResultError();
                        }

                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "loadItemsTypesFromCatId: json exception -> " + e.getMessage() );
            }

        }

    }

    public void uploadProfilePic(final SOSApiListener listener, final String imgStr) {

        String url = API_URL + "act=" + ACTION_UPLOAD_PP + "&mobile=" + GSV(KEY_ACC_DATA_MOBILE);
       //Log.e("UPDPP", "uploadProfilePic: -> " + url );

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                       //Log.e(TAG, "UPDPP RESP -> " + s );



                        try {
                            JSONObject jsonObject = new JSONObject(s);

                            Bundle b = new Bundle();


                            if(jsonObject.getString(JSON_KEY_RESULT).equals(JSON_RESULT_SUCCESS)) {

                                b.putString(JSON_KEY_RESULT, JSON_RESULT_SUCCESS);


                            }else{
                                b.putString(JSON_KEY_RESULT, JSON_RESULT_FAILURE);
                            }

                            listener.onUploadPPResult(b);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        Bundle b = new Bundle();
                        b.putString(JSON_KEY_RESULT, JSON_RESULT_FAILURE);
                        b.putString(JSON_KEY_RESULT_ERROR_MESSAGE, volleyError.getMessage());

                        listener.onUploadPPResult(b);
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();

                params.put(KEY_PROFILE_PIC_DATA, imgStr);

                return params;

            }
        };

        Volley.newRequestQueue(context).add(request);
    }

    public void loadCatTypeNames(SOSApiListener listener, String catId, String typeId) {

        if(preferences.getString(KEY_ITEMS_CATS_AND_TYPES, KEY_PREF_EMPTY).equals(KEY_PREF_EMPTY)) {


            /*String url = API_URL + "act=" + ACTION_LOAD_ALL_ITEMS_CATS_AND_TYPES;

            //Log.e(TAG, "loadItemsCatsAndTypes: url -> " + url );

            StringRequest request = new StringRequest(
                    url,
                    new Response.CallBacks<String>() {
                        @Override
                        public void onResponse(String s) {
                            //Log.e(TAG, "ALL CATS RESP -> " + s);


                            try {
                                JSONArray jsonArray = new JSONArray(s);

                                if (jsonArray.length() > 0) {
                                    listener.onLoadItemsCats(jsonArray);
                                    editor.putString(KEY_ITEMS_CATS_AND_TYPES, s);
                                    editor.commit();
                                    //Log.e(TAG, "CATS LOADED FROM SERVER" );
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            //Log.e(TAG, "ALL CAT RESP ERR ->  " + volleyError.getMessage());
                            //listener.onLoadItemsCats(null);
                        }
                    }
            );


            Volley.newRequestQueue(context).add(request);*/

        }else{
            try {
                String s = preferences.getString(KEY_ITEMS_CATS_AND_TYPES, KEY_PREF_EMPTY);
                JSONArray cats = new JSONArray(s);

                if (cats.length() > 0) {

                    for(int i = 0; i < cats.length(); i ++){


                        JSONObject cat = (JSONObject) cats.get(i);

                        if(cat.getString(KEY_ITEM_CATEGORY_ID).equals(catId)){

                            String cn = cat.getString(KEY_ITEM_CATEGORY_NAME);
                            //Log.e(TAG, "THA CAT NAME BE -> " + cn );

                            String typesStr = cat.getString(KEY_ITEMS_CATEGORY_TYPES);

                            //Log.e(TAG, "DA TYPES -> " +  typesStr );

                            JSONArray types = new JSONArray(typesStr);

                            String tn = "N/A";
                            if(types.length() > 0){

                                for(int j =0; j < types.length(); j++){
                                    JSONObject type = types.getJSONObject(j);

                                    if(type.getString(KEY_ITEM_TYPE_ID).equals(typeId)) {
                                        tn = type.getString(KEY_ITEM_TYPE_NAME);
                                            //Log.e(TAG, "THA TYPE NAME BE -> " + tn);
                                    }
                                   // break;
                                }

                            }else{

                                //Log.e(TAG, "TYPES LEN IS ZERO SO TN -> " + tn );
                            }
                            //break;
                            //Log.e(TAG, "CAT -> " + cn + " TYPE -> " + tn );
                            listener.onLoadCatsTypesNames(cn, tn);

                        }

                    }

                }else{
                    //Log.e(TAG, "CATS LEN IS ZERO" );
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "loadCatTypeNames: " + e.getMessage() );
            }

        }

    }

    public void loadCategoryTypesFromCatId(final SOSApiListener listener, String id) {

        Log.e(TAG, "loadCatData: id -> " + id );


        //sosApi.loadItemsTypesFromCatId(this, new Integer(id).intValue());

        // TODO: 12/26/2017 TO MOVE IN SOS API CLASS

        String url = SOS_API.API_URL + "act=" + SOS_API.ACTION_LOAD_ITEM_TYPES_FROM_CAT_ID + "&catId=" + id;
        Log.e(TAG, "loadCatData: -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        List<TypesItem> types = null;
                        try {
                            JSONArray jsonArray = new JSONArray(s);


                            if(jsonArray.length() > 0 ){

                                //List<TypesItem> types = new ArrayList<>();
                                types = new ArrayList<>();

                                for(int i =0; i < jsonArray.length(); i++){

                                    JSONObject type = jsonArray.getJSONObject(i);
                                    Log.e(TAG, "TYPE JO -> " + type.toString() );


                                    String id = type.getString(TypesItem.KEY_TYPE_ITEM_ID);
                                    String name = type.getString(TypesItem.KEY_TYPE_ITEM_NAME);
                                    String imgPath = ROOT_URL + DIR_PATH_TYPES + type.getString(TypesItem.KEY_TYPE_ITEM_IMG_PATH);

                                    TypesItem typeItem = new TypesItem(id, name, imgPath);



                                    types.add(typeItem);


                                }

                                listener.onCategoryTypesLoaded(types, false);

                            }else{
                                Log.e(TAG, "TYPES JSON ARRAY LEN = 0" );
                                listener.onCategoryTypesLoaded(types, false);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "LOAD TYPES JSON ERROR -> " + e.getMessage() );

                            if(s.equals(JSON_RESULT_FAILURE)){
                                listener.onCategoryTypesLoaded(types, false);
                            }else{
                                listener.onCategoryTypesLoaded(types, true);
                            }


                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onCategoryTypesLoaded(null, true);
                    }
                }
        );

        Volley.newRequestQueue(context).add(request);


    }

    public static final String ACTION_ADD_ITEM_TO_WISHLIST = "addItemToWishlist";

    public static final String ACTION_REMOVE_ITEM_FROM_WISHLIST = "removeItemFromWishlist";

    public void removeItemFromWishlist(final ListenerItemsWishlist listener, final Bundle itemData) {

        String wid = itemData.getString(SOS_API.KEY_ITEM_ID);
        //final String itemName = itemData.getString(Product.KEY_PD_NAME);
        String url = SOS_API.API_URL + "act=" + ACTION_REMOVE_ITEM_FROM_WISHLIST + "&wid=" + wid + "&uid=" + GSV(KEY_ACC_DATA_USER_ID);

        Log.e(TAG, "removeItemFromWishlist: url -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        if(s.equals(JSON_RESULT_FAILURE)){
                            listener.onItemRemoveError(itemData);
                        }else{
                            listener.onItemRemoveSuccess(itemData);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onNetworkError(volleyError.getMessage());
                    }
                }
        );

        Volley.newRequestQueue(context).add(request);
    }

    public interface ListenerItemsWishlist {
        public void onItemAddedSuccess();
        public void onItemAddedError(String msg);
        public void onNetworkError(String msg);

        void onItemRemoveError(Bundle pd);

        void onItemRemoveSuccess(Bundle pd);
    }

    public void addItemToWishlist(final ListenerItemsWishlist listener, String itemId) {

        String url = SOS_API.API_URL + "act=" + ACTION_ADD_ITEM_TO_WISHLIST + "&uid=" +
                GSV(KEY_ACC_DATA_USER_ID) + "&wid=" + itemId;

        Log.e(TAG, "addItemToWishlist: url -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        if(s.equals(JSON_RESULT_FAILURE)){
                            listener.onItemAddedError(s);
                        }else{
                            listener.onItemAddedSuccess();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onNetworkError(volleyError.getMessage());
                    }
                }
        );

        Volley.newRequestQueue(context).add(request);

    }

    public  interface ListenerOnWishlistItemsLoaded {
         void onWishLisItemsLoaded(List<ProductWishList> wishlistItems);

         void onNoItemsInWishlist();

        void onErrorLoadWishList(String message);
    }

    public void loadWishListData(final ListenerOnWishlistItemsLoaded listener) {



        String url = SOS_API.API_URL + "act=" + ACTTION_LOAD_WISH_LIST + "&uid=" + GSV(KEY_ACC_DATA_USER_ID);
        //Log.e(TAG, "wishlist: url -> " + url );

        StringRequest requestItems = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONArray jsonArray = new JSONArray(s);


                            if(jsonArray.length() > 0){


                                List<ProductWishList> prods = new ArrayList<>();
                                //prods.clear();

                                for(int i =0; i < jsonArray.length(); i++) {

                                    JSONObject jo = jsonArray.getJSONObject(i);

                                    ProductWishList pd = new ProductWishList(
                                            jo.getString(Product.KEY_PD_NAME),
                                            jo.getString(Product.KEY_PD_PRICE),
                                            jo.getString(Product.KEY_PD_IMG),
                                            jo.getString(Product.KEY_PD_CUR),
                                            jo.getString(Product.KEY_PD_CAT),
                                            jo.getString(Product.KEY_PD_QUAL),
                                            jo.getString(Product.KEY_PD_DESC),
                                            null
                                    );

                                    Bundle data = new Bundle();


                                    String dateStart = jo.getString(Product.KEY_PD_DATE_ADDED);

                                    //HelperDate.DateDiff dateDiff = HelperDate.dateDiff(dateStart, dateEnd );//new Date().toString());

                                    String postedDate = HM.CLDTAS(context,
                                            HelperDate.getLongDateFromDateStr(dateStart), HelperDate.getCurrentLondDate());//dateDiff.toSocialFormat();//HM.FD(dateDiff, dateStart);


                                    data.putString(SOS_API.KEY_ITEM_ID, jo.getString(SOS_API.KEY_ITEM_ID));
                                    data.putString(Product.KEY_PD_DATE_ADDED, postedDate);
                                    data.putString(Product.KEY_PD_UNIQUE_NAME, jo.getString(Product.KEY_PD_UNIQUE_NAME));
                                    data.putString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME,jo.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));
                                    data.putString(SOS_API.KEY_ITEM_ITEM_VIEWS_ACCOUNT,jo.getString(SOS_API.KEY_ITEM_ITEM_VIEWS_ACCOUNT));
                                    data.putString(Product.KEY_PD_DATE_SOLD, jo.getString(Product.KEY_PD_DATE_SOLD));
                                    data.putString(Product.KEY_PD_UNIQUE_NAME, jo.getString(Product.KEY_PD_UNIQUE_NAME));
                                    data.putString(Product.KEY_PD_OWNER_ID, jo.getString(Product.KEY_PD_OWNER_ID));
                                    data.putString(SOS_API.KEY_ACC_DATA_USER_ID, jo.getString(SOS_API.KEY_ACC_DATA_USER_ID));
                                    pd.setData(data);
                                    //Log.e(TAG, "PD TOB " + pd.toBundle().toString() );

                                    prods.add(pd);

                                }

                                listener.onWishLisItemsLoaded(prods);



                            }else{
                                listener.onNoItemsInWishlist();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onNoItemsInWishlist();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onErrorLoadWishList(volleyError.getMessage());
                    }
                }
        );


        Volley.newRequestQueue(context).add(requestItems);

    }

    public interface SOSApiListener {
        void onSearchResult(String ids);
        void onSearchResultError(String error);
        void onPostInquiryResult(String result);
        void onLoadRecentItemsResult(List<Product> featuredProducts, boolean networkError);
        void onLoginResult(Bundle data);
        void onSignUpResult(Bundle data);
        void onAccountDeleteResult(Bundle data);
        void onLogoutResult();
        void onExposeItemResult(Bundle data);
        void onLoadAllMyProductsResult(List<ProductMyProducts> myProducts, boolean networkError);
        void onLoadItemsCats(JSONArray jsonArray);
        void onUpdatePasswordResult(String resp);
        void onUpdateSettingsResult(String settingKey, String result);
        void onLoadAllProductsResult(List<ProductMyProducts> allProducts);
        void onRemoveProductResult(Bundle b);
        void onUpdateItemViewsCountResult(String newCount);
        void onLoadAllProductsError(String message);

        void onLoadSearchResultItemsResult(List<ProductMyProducts> searchResultProducts);

        void onLoadSearchResultItemsError(String message);

        void onItemsTypesLoaded(JSONArray itemCats);

        void onUploadPPResult(Bundle data);

        void onLoadItemsTypesResultError();

        void onLoadItemsTypesResult(String[] types);

        void onLoadCatsTypesNames(String cn, String tn);

        void onCategoryTypesLoaded(List<TypesItem> types, boolean errorLoading);
    }

    public void loadItemsCatsAndTypes(final SOSApiListener listener){


        if(preferences.getString(KEY_ITEMS_CATS_AND_TYPES, KEY_PREF_EMPTY).equals(KEY_PREF_EMPTY)) {


            String url = API_URL + "act=" + ACTION_LOAD_ALL_ITEMS_CATS_AND_TYPES;

           //Log.e(TAG, "loadItemsCatsAndTypes: url -> " + url );

            StringRequest request = new StringRequest(
                    url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                           //Log.e(TAG, "ALL CATS RESP -> " + s);


                            try {
                                JSONArray jsonArray = new JSONArray(s);

                                if (jsonArray.length() > 0) {
                                    listener.onLoadItemsCats(jsonArray);
                                    editor.putString(KEY_ITEMS_CATS_AND_TYPES, s);
                                    editor.commit();
                                   //Log.e(TAG, "CATS LOADED FROM SERVER" );
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                           //Log.e(TAG, "ALL CAT RESP ERR ->  " + volleyError.getMessage());
                            //listener.onLoadItemsCats(null);
                        }
                    }
            );


            Volley.newRequestQueue(context).add(request);

        }else{
            try {
                String s = preferences.getString(KEY_ITEMS_CATS_AND_TYPES, KEY_PREF_EMPTY);
                JSONArray jsonArray = new JSONArray(s);

                if (jsonArray.length() > 0) {
                    listener.onLoadItemsCats(jsonArray);
                    //editor.putStringSet(KEY_ITEMS_CATS_AND_TYPES, s);
                   //Log.e(TAG, "CATS LOADED FROM PREFS" );
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }

    public static String ACTION_LOAD_ITEMS_IN_TYPE = "loadItemsInType";

    public static interface CallBacksItemsInTypes {

        void onItemsInTypeLoaded(List<Product> prods);
        void onNoProdsInType();
        void onErrorLoadProdsInType(String msg);
    }

    public void loadItemsInType(final CallBacksItemsInTypes listener, String typeId){







        String url = SOS_API.API_URL + "act=" + ACTION_LOAD_ITEMS_IN_TYPE + "&typeId=" + typeId;
        Log.e(TAG, "loadItemsInType: url -> " + url );

        Log.e(TAG, "onResponse: \nurl : " + url );

        StringRequest requestItems = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONArray jsonArray = new JSONArray(s);


                            if(jsonArray.length() > 0){


                                List<Product> prods = new ArrayList<>();
                                //prods.clear();

                                    for(int i =0; i < jsonArray.length(); i++) {

                                        JSONObject jo = jsonArray.getJSONObject(i);

                                        Product pd = new Product(
                                                jo.getString(Product.KEY_PD_NAME),
                                                jo.getString(Product.KEY_PD_PRICE),
                                                jo.getString(Product.KEY_PD_IMG),
                                                jo.getString(Product.KEY_PD_CUR),
                                                jo.getString(Product.KEY_PD_CAT),
                                                jo.getString(Product.KEY_PD_QUAL),
                                                jo.getString(Product.KEY_PD_DESC),
                                                null
                                        );

                                        Bundle data = new Bundle();


                                        String dateStart = jo.getString(Product.KEY_PD_DATE_ADDED);

                                        //HelperDate.DateDiff dateDiff = HelperDate.dateDiff(dateStart, dateEnd );//new Date().toString());

                                        String postedDate = HM.CLDTAS(context,
                                                HelperDate.getLongDateFromDateStr(dateStart), HelperDate.getCurrentLondDate());//dateDiff.toSocialFormat();//HM.FD(dateDiff, dateStart);


                                        data.putString(Product.KEY_PD_DATE_ADDED, postedDate);
                                        data.putString(Product.KEY_PD_UNIQUE_NAME, jo.getString(Product.KEY_PD_UNIQUE_NAME));
                                        data.putString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME,jo.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));
                                        data.putString(SOS_API.KEY_ITEM_ITEM_VIEWS_ACCOUNT,jo.getString(SOS_API.KEY_ITEM_ITEM_VIEWS_ACCOUNT));
                                        data.putString(Product.KEY_PD_DATE_SOLD, jo.getString(Product.KEY_PD_DATE_SOLD));
                                        data.putString(SOS_API.KEY_ACC_DATA_USER_ID, jo.getString(SOS_API.KEY_ACC_DATA_USER_ID));
                                        pd.setData(data);
                                        //Log.e(TAG, "PD TOB " + pd.toBundle().toString() );

                                        prods.add(pd);

                                    }

                                    listener.onItemsInTypeLoaded(prods);



                            }else{
                                listener.onNoProdsInType();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.onNoProdsInType();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        listener.onErrorLoadProdsInType(volleyError.getMessage());
                    }
                }
        );


        Volley.newRequestQueue(context).add(requestItems);

    }

    public  void logout(){



        // TODO: 12/1/17 EMPTY SESSION INPHP

        String url = API_URL + "act=" + ACTION_LOGOUT + "&uid=" + getSessionVar(KEY_ACC_DATA_USER_ID);

       //Log.e(TAG, "logout: url -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                       //Log.e(TAG, "onResponse: logout resp -> " + s );

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "onError logout restp ->  " + volleyError.getMessage() );
                    }
                }
        );

        Volley.newRequestQueue(context).add(request);

        editor.clear();
        editor.commit();

    }

}
