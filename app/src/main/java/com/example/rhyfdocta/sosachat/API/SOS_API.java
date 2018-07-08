package com.example.rhyfdocta.sosachat.API;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.rhyfdocta.sosachat.ActivityNoNetwork;
import com.example.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.Helpers.HelperDate;
import com.example.rhyfdocta.sosachat.Helpers.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.LookingFor;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductWishList;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.adapters.AdapterLookingFor;
import com.example.rhyfdocta.sosachat.app.SOSApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import android.support.v7.app.AlertDialog;

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
    public static final String DIR_NAME_PIX_CACHE_HOME_TYPES_IN_CATS = "typesInCats";
    public static final String DIR_NAME_PIX_CACHE_PRODUCTS = "products";
    public static final String DIR_NAME_PIX_CACHE_PROFILCE_PIC = "pp";
    public static final String ACTION_LOAD_ALL_LOOKINGFORS = "checkAllInquiries";
    public static final String TAG = "SA_DBG";
    public static final int KEY_CONTACT_BY_PHONE = 250;
    public static final int KEY_CONTACT_BY_SMS = 251;
    public static final int KEY_CONTACT_BY_EMAIL = 252;
    public static final int KEY_CONTACT_BY_SOSDM = 253;
    public static final int REQ_CODE_NO_INTERNET_CONNECTION = 2234;
    public static final int IMG_W_ADP_RECENT_ITEMS = 900;
    public static final int IMG_H_ADP_RECENT_ITEMS = 900;
    public static final String DIR_NAME_PIX_CACHE_HOME_CAT_TYPES = "catTypes";
    public static final String KEY_SEARCH_KEYWORD = "skw";
    public static final String KEY_NEW_ITEM_IMG_TYPE_MAIN = "_main.jpg";
    public static final String KEY_NEW_ITEM_IMG_TYPE_PIC1 = "_pic1.jpg";
    public static final String KEY_NEW_ITEM_IMG_TYPE_PIC2 = "_pic2.jpg";
    public static final String KEY_NEW_ITEM_IMG_TYPE_PIC3 = "_pic3.jpg";

    public static final String KEY_PRODUCT_IMAGE_POST_FIX_MAIN = "_main";
    public static final String KEY_PRODUCT_IMAGE_POST_FIX_PIC1 = "_pic1";
    public static final String KEY_PRODUCT_IMAGE_POST_FIX_PIC2 = "_pic2";
    public static final String KEY_PRODUCT_IMAGE_POST_FIX_PIC3 = "_pic3";
    public static final String KEY_PRODUCT_IMAGE_POST_FIX_LOGGEDIN_USER = "_loggedin_user";

    public static final String TRUE = "true";
    public static final String ACTION_UPLOAD_PRODUCT_IMAGE_FILE = "uploadProductImageFile";
    public static final String KEY_NEW_ITEM_IMG_TYPE = "imageType";
    public static final String DIR_NAME_PIX_ROOT = "img";
    public static final String ACTION_GET_UNIQUE_ID = "getUniqueId";
    public static final float IMAGEVIEW_ALPHA_DISABLED = .5f;
    public static final float IMAGEVIEW_ALPHA_ENABLED = 1f;
    public static final String KEY_NEW_ITEM_UNIQUE_ID = "uniqueID";
    public static final String KEY_ITEM_POST_FIX_PIC_1 = "_pic1.jpg";
    public static final String KEY_ITEM_POST_FIX_PIC_2 = "_pic2.jpg";
    public static final String KEY_ITEM_POST_FIX_PIC_3 = "_pic3.jpg";
    public static final int KEY_ITEM_PIC_IDX_MAIN = 0;
    public static final int KEY_ITEM_PIC_IDX_P1 = 1;
    public static final int KEY_ITEM_PIC_IDX_P2 = 2;
    public static final int KEY_ITEM_PIC_IDX_P3 = 3;
    public static final String SERVER_REL_ROOT_DIR_PATH_PROFILE_PICTURES = "img/pp/";
    public static final String ACTION_UPLOAD_IMAGE = "uploadImage";
    public static final String IMAGE_UPLOAD_FORM_NAME = "uploaded_file";
    public static final String ACTION_DELETE_LOOKING_FOR = "dell4";
    public static final String KEY_LAST_USERNAME = "luname";
    public static final String TIME_STAMP = "timeStamp";
    public static final String SEARCH_Q = "q";
    public static final String FALSE = "false";
    public static final String SERVER_ADD = "serverAdd";
    public static final int NO_LIMIT = -1;
    public static final String KEY_USER_IS_ADMIN = "user_is_admin";
    public static final String ACTTION_LOAD_WISH_LIST = "loadWishList";
    public static final String KEY_SHOWING_VENDOR_PROFILE = "showingVendorProfile";
    public static final String KEY_SOSACHAT_PIX_DIR = "SOSAchat";
    public static final String DIR_PATH_CAT_PIX = "sosachat/img/cats/";
    public static final String ACTION_LOAD_FEATURED_ITEMS = "loadFeatItems";
    public static final String ACTION_LOAD_CATEGORY_CARS = "loadCatCars";
    public static final String ACTION_LOAD_CATEGORY_ELECTRONICS = "loadCatElec";
    public static final String ACTION_LOAD_WISH_LIST = "loadWishList";
    public static final String ACTION_LOAD_ALL_MY_PRODUCTS = "loadAllMyProducts";
    public static final String ACTION_LOAD_CHAT_CONTACTS = "loadChatContacts";
    public static final String ACTION_LOGIN = "login";
    public static final String LOGIN_SUCCESS = "true";
    //public static final String TAG = "SOS_API";
    public static final String SHARED_PREF_NAME = "sosSharedPref";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_NO_USERNAME = "no_username";
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
    public static final String ACTION_EXPOSE_ITEM = "exposeItem";
    public static final String ACTION_LOAD_ALL_ITEMS_CATS_AND_TYPES = "loadAllCatsAndTypes";
    public static final String KEY_ITEMS_CATS_AND_TYPES = "itemsCatsAndTypes";
    public static final String KEY_PREF_EMPTY = "prefEmpty";
    public static final String KEY_ITEM_CATEGORY_NAME = "it_cat_name";
    public static final String KEY_ITEM_CATEGORY_ID = "it_cat_id";
    public static final String KEY_ITEM_CATEGORY_PIC = "it_cat_pic";
    public static final String KEY_ITEM_NO_PIC = "no_pic";
    public static final String ACTION_UPDATE_USER_SETTING = "updSet";
    public static final String ACTION_LOAD_ALL_PRODUCTS = "loadAllProducts";
    // TODO: 12/4/2017 CORRECT LOAD FEATURED PRODUCTS
    public static final String ACTION_LOAD_FEATURED_PRODUCTS = "loadAllFeaturedProducts";
    public static final String KEY_ITEM_POST_FIX_MAIN_PIC = "_main.jpg";
    public static final String KEY_ITEM_UNIQUE_NAME = "pdUniqueName";
    public static final String ACTION_REMOVE_PRODUCT = "rmProd";
    public static final String ACTION_POST_INQUIRY = "postLooking4";
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
    public static final String KEY_ITEM_TYPES = "itemTypes";
    public static final int RESULT_LOAD_IMAGE = 1340;
    public static final int REQ_CAMERA = 1341;
    public static final int REQ_PERMISSION_SAVE_BITMAP = 1342;
    public static final int REQ_PERMISSION_GALLERY = 1343;
    public static final String KEY_PROFILE_PIC_DATA = "ppData";
    public static final String ACTION_UPLOAD_PP = "uploadPP";
    public static final String KEY_ACC_DATA_MOBILE_HASH = "user_mobile_hash";
    public static final String ACTION_UPDATE_ITEM = "updItem";
    public static final String KEY_PD_MAIN_PIC_URI = "pdUri";
    public static final String KEY_POSTFIX_PIC_MAIN = "_main.jpg";
    public static final String KEY_POSTFIX_PIC_1 = "_pic1.jpg";
    public static final String KEY_POSTFIX_PIC_2 = "_pic2.jpg";
    public static final String KEY_POSTFIX_PIC_3 = "_pic3.jpg";
    public static final String KEY_ITEM_UPDATED = "itemUpdated";
    public static final String KEY_ITEM_TYPE_NAME = "items_types_name";
    public static final String KEY_ITEM_TYPE_ID = "items_types_id";
    public static final String KEY_ITEMS_CATEGORY_TYPES = "cat_types";
    public static final int PIC_SOURCE_CAMERA = 1;
    public static final int PIC_SOURCE_GALLERY = 0;
    public static final String ACTION_ADD_ITEM_TO_WISHLIST = "addItemToWishlist";
    public static final String ACTION_REMOVE_ITEM_FROM_WISHLIST = "removeItemFromWishlist";
    private static final String RES_EMPTY = "resEmpty";
    private static final String ACTION_CLEAR_WISHLIST = "clearWishlist";
    private static final String ACTION_PUBLISH_ITEM = "publishItem";
    public static boolean POST_MARSHMALLOW = false;
    public static String API_URL = "sosachat/api.php?";
    public static String DIR_PATH_CATEGORIES = "sosachat/img/";


    //public static final String ACTION_LOAD_ITEMS_TYPES = "loadItemsTypes";
    public static String DIR_PATH_PRODUCTS_PIX = "sosachat/img/products/";
    public static String DIR_PATH_PP = "sosachat/img/pp/";
    public static String ROOT_URL = "sosachat/";
    public static String DIR_PATH_TYPES = "img/types/";
    public static String KEY_ITEM_NAME = "itemName";
    public static String KEY_ITEM_PRICE = "itemPrice";
    public static String KEY_ITEM_DESCRIPTION = "itemDescription";
    public static String KEY_ITEM_CURRENCY = "itemCurrency";
    public static String KEY_ITEM_QUALITY = "itemQuality";
    public static String KEY_ITEM_CATEGORY = "itemCategory";
    public static Object[] keys;
    public static String ACTION_LOAD_ITEMS_IN_TYPE = "loadItemsInType";
    Context context;
    SharedPreferences.Editor editor;
    private AlertDialog alertDialogResults;
    private SharedPreferences preferences;
    private BitmapCacheManager bitmapCacheManager;

    //AlertDialog alertDialog;
    public SOS_API(Context context) {
        this.context = context;
        setBitmapCacheManager(new BitmapCacheManager(context));
        preferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = getPreferences().edit();
        setupAlertDialogResponse();
        //SSV(KEY_NEW_ITEM_UNIQUE_ID, null);

        Log.e(TAG, "FUCK -> " + GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID));

        //alertDialog = HelperMethods.getAlertDialogProcessingWithMessage(context, HelperMethods.getStringResource(this, R.string.pbMsgProcessing),false);

        //getAllItemCats();
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    public static String GetPicExtTagByIndex(int idx) {
        String tag = null;

        switch (idx) {
            case SOS_API.KEY_ITEM_PIC_IDX_MAIN:
                tag = SOS_API.KEY_NEW_ITEM_IMG_TYPE_MAIN;
                break;

            case SOS_API.KEY_ITEM_PIC_IDX_P1:
                tag = SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC1;
                break;

            case SOS_API.KEY_ITEM_PIC_IDX_P2:
                tag = SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC2;
                break;

            case SOS_API.KEY_ITEM_PIC_IDX_P3:
                tag = SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC3;
                break;
        }


        return tag;
    }

    public static AlertDialog DWPNB(Context ctx, boolean show, String message, String positiveBtn, DialogInterface.OnClickListener positiveHandler,
                                    String negativeBtn, DialogInterface.OnClickListener negativeHandler, String title) {
        return dialogWithPositiveNegativeButtons(ctx, show, message, positiveBtn, positiveHandler, negativeBtn, negativeHandler, title);
    }

    public static AlertDialog dialogWithPositiveNegativeButtons(Context ctx, boolean show, String message, String positiveBtn, DialogInterface.OnClickListener positiveHandler,
                                                                String negativeBtn, DialogInterface.OnClickListener negativeHandler, String title) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);

        if (title != null) builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveBtn, positiveHandler);
        builder.setNegativeButton(negativeBtn, negativeHandler);

        AlertDialog alertDialog = builder.create();

        if (show) alertDialog.show();

        return alertDialog;

    }

    public static File GetSOSAchatCacheRootDir() {
        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + BitmapCacheManager.CACHE_ROOT_DIR + "/";
        return new File(path);
    }

    public static void deletePP() {

        BitmapCacheManager.EmptyDir(new File(GetSOSAchatCacheRootDir().toString() + "/" + DIR_NAME_PIX_CACHE_PROFILCE_PIC));
        Log.e(TAG, "deletePP: ");
    }

    public static String getSOSAchatItemPixCameraPath() {
        return Environment.getExternalStorageDirectory().toString() + "/" + KEY_SOSACHAT_PIX_DIR;
    }

    public static String GSAIPCP() {
        return getSOSAchatItemPixCameraPath();
    }

    private void setupAlertDialogResponse() {

        /*
        AlertDialog.Builder builder;


        builder =new AlertDialog.Builder(context)
                .setPositiveButton("OK",null);
        //View view = getLayoutInflater().inflate(R.layout.layout_alert_dialog_load_response,null);
        //tvAlertDialogResponse = (TextView) view.findViewById(R.id.tvAlertDialogResponse);
        //builder.setView(view);
        alertDialogResults = builder.create();
        //alertDialogResults.setCancelable(false);*/


    }

    public void toggleAlertDialogResponseWithMessage(Context context, boolean show, String message) {

        AlertDialog.Builder builder;


        builder = new AlertDialog.Builder(context)
                .setPositiveButton("OK", null);
        //View view = getLayoutInflater().inflate(R.layout.layout_alert_dialog_load_response,null);
        //tvAlertDialogResponse = (TextView) view.findViewById(R.id.tvAlertDialogResponse);
        //builder.setView(view);
        alertDialogResults = builder.create();
        //alertDialogResults.setCancelable(false);

        if (message != null) {
            alertDialogResults.setMessage(message);

        }

        if (show) {
            alertDialogResults.show();
        } else {
            alertDialogResults.hide();
        }


    }

    public void TADRWM(Context context, boolean show, String msg) {
        toggleAlertDialogResponseWithMessage(context, show, msg);
    }

    public boolean isLoggedIn() {


        boolean result = false;


        String username = getPreferences().getString(KEY_ACC_DATA_MOBILE, KEY_NO_USERNAME);
        //String password = preferences.getString(KEY_PASSWORD, KEY_PASSWORD);

        if (!username.equalsIgnoreCase(KEY_NO_USERNAME)) {
            result = true;

            //Toast.makeText(context, "username : " + username, Toast.LENGTH_SHORT).show();
        }

        return result;


    }

    public void clearSessionData() {
        String ip = GSV(SERVER_ADD);
        editor.clear();
        editor.putString(SERVER_ADD, ip);
        editor.commit();
        editor.commit();
    }

    public void CSD(){
        clearSessionData();
    }

    public interface CallbacksLoginSignup{


        void loginSuccess(Bundle userData);

        void loginFailedUserPasswordError(String username);

        void loginFailedUserDontExist(String username);

        void signupSuccess(Bundle userData);

        void signupFailureUserExist();

        void signupFailure(String message);

        void onJSONException(String message);

        void onNetworkError(String message);
    }

    public void login(final CallbacksLoginSignup callbacks, final String username, final String password) {

        String loginURL = GetServerAddress() + SOS_API.API_URL + "act=" + ACTION_LOGIN + "&username=" + username + "&password=" + password;

        Log.e("UDT", "login:url -> " + loginURL);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Log.e("XYZ", "onResponse: " + s );
                        try {
                            JSONObject data = new JSONObject(s);

                            int code = data.getInt(NETWORK_RESULT_CODES.KEY_RESULT_CODE) ;

                            switch (code) {
                                case NETWORK_RESULT_CODES.RESULT_CODE_USER_CONNECTION_SUCCESS:

                                    String userJSON = data.getString(NETWORK_RESULT_CODES.KEY_RESULT_DATA);
                                    JSONObject userJO = new JSONObject(userJSON);
                                    //String userDataStr = userJO.getString(NETWORK_RESULT_CODES.KEY_RESULT_DATA);

                                    Log.e("UDT", "udata : " + userJSON );
                                    Bundle userData = HM.JTB(userJO);
                                    setSessionData(userData);
                                    callbacks.loginSuccess(userData);
                                    break;

                                case NETWORK_RESULT_CODES.RESULT_CODE_USER_CONNECTION_FAILURE_PASSWORD_ERROR:
                                    callbacks.loginFailedUserPasswordError(username);
                                break;

                                case NETWORK_RESULT_CODES.RESULT_CODE_USER_DONT_EXIST:
                                    callbacks.loginFailedUserDontExist(username);
                                    break;

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                            callbacks.onJSONException(e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.e("XYZ", "onResponse: " + volleyError.getMessage() );
                        callbacks.onNetworkError(volleyError.getMessage());
                    }
                });

        SOSApplication.GI().addToRequestQueue(request);


    }

    /*public void login(final SOSApiListener SOSApiListener, final String username, final String password) {

        String loginURL = GetServerAddress() + SOS_API.API_URL + "act=" + ACTION_LOGIN + "&username=" + username + "&password=" + password;

        Log.e(TAG, "login:url -> " + loginURL);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {


                            Log.e(TAG, "onResponse: login response -> " + s);

                            JSONObject jo = new JSONObject(s);
                            Bundle b = HM.JTB(jo);//HelperMethods.jsonToBundle(jo);


                            if (b.size() == 1) {
                                b.putString(JSON_KEY_RESULT, SIGNUP_FAILURE);
                            } else {
                                b.putString(JSON_KEY_RESULT, SIGNUP_SUCCESS);

                                if (b.getString(KEY_USER_IS_ADMIN).equals("1")) {
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


        //SOSApplication.getInstance().addToRequestQueue(request);
        SOSApplication.getInstance().addToRequestQueue(request);


    }*/

    public String GetServerAddress() {
        return "http://" + GSV(SERVER_ADD) + "/";
    }

    public String getSessionVar(String key) {

        return getPreferences().getString(key, KEY_SESSION_DATA_EMPTY);
    }

    public void setSessionVar(String key, String val) {

        editor.putString(key, val);
        editor.apply();
        editor.commit();
    }

    public void SSV(String key, String val) {
        setSessionVar(key, val);
    }

    public String GSV(String key) {
        return getSessionVar(key);
    }

    private void SSD(Bundle data) {
        setSessionData(data);
    }

    private void setSessionData(Bundle b) {

        String serverAdd = GSV(SERVER_ADD);
        String lun = GSV(KEY_LAST_USERNAME);
        editor.clear();
        editor.putString(SERVER_ADD, serverAdd);
        editor.putString(KEY_LAST_USERNAME, lun);
        editor.commit();

        keys = b.keySet().toArray();

        for (int i = 0; i < keys.length; i++) {


            String key = keys[i].toString();
            String val = b.getString(key);

            editor.putString(key, val);

            //Log.e(TAG, "setSessionData: putting : " + keys[i] + " : " + b.getString(keys[i].toString()) );


        }

        editor.commit();

    }

    public void signup(final CallbacksLoginSignup callbacks, Bundle data) {


        final String mobile = data.getString(SOS_API.KEY_ACC_DATA_MOBILE);
        final String email = data.getString(SOS_API.KEY_ACC_DATA_EMAIL);
        final String password = data.getString(SOS_API.KEY_ACC_DATA_PASSWORD);
        final String fullName = data.getString(SOS_API.KEY_ACC_DATA_FULL_NAME);
        final String displayName = data.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME);
        final String location = data.getString(SOS_API.KEY_ACC_DATA_LOCATION);

        //Log.e(TAG, "bundle -> " + data.toString() );

        String url = GSA() + API_URL + "act=" + ACTION_SIGNUP;


        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            int code = jsonObject.getInt(NETWORK_RESULT_CODES.KEY_RESULT_CODE);
                            String dataStr = jsonObject.getString(NETWORK_RESULT_CODES.KEY_RESULT_DATA);
                            JSONObject userJSon = new JSONObject(dataStr);
                            Bundle userData = HM.JTB(userJSon);
                            //userData = HM.JTB(new JSONObject(userData.getString(NETWORK_RESULT_CODES.KEY_RESULT_DATA)));

                            switch (code){
                                case NETWORK_RESULT_CODES.RESULT_CODE_SIGNUP_SUCCESS:


                                    String data = userData.getString("data");
                                    JSONObject jo = new JSONObject(data);
                                    Bundle b = HM.JTB(jo);
                                    setSessionData(b);
                                    callbacks.signupSuccess(userData);
                                    break;

                                case NETWORK_RESULT_CODES.RESULT_CODE_SIGNUP_FAILURE:
                                    callbacks.signupFailure(dataStr);
                                    break;

                                case NETWORK_RESULT_CODES.RESULT_CODE_SIGNUP_FAILURE_USER_EXISTS:
                                    callbacks.signupFailureUserExist();
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("XXX", "onResponse: -> " + e.getMessage() );
                            callbacks.onJSONException(e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        callbacks.onNetworkError(error.getMessage());

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("mobile", mobile);
                params.put("password", password);
                params.put("email", email);
                params.put("fullName", fullName);
                params.put("displayName", displayName);
                params.put("location", location);
                return params;
            }


        };
        SOSApplication.getInstance().addToRequestQueue(postRequest);


    }

    public String GSA() {
        return GetServerAddress();
    }

    public void deleAccount(final SOSApiListener sosApiListener) {


        final String mobile = GSV(SOS_API.KEY_ACC_DATA_MOBILE);
        String url = GSA() + API_URL + "act=" + ACTION_DELETE_ACCOUNT + "&" + KEY_ACC_DATA_MOBILE + "=" + mobile;
        //Log.e(TAG, "deleAccount: url -> " + url );

        StringRequest request = new StringRequest(

                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Bundle b = new Bundle();
                        if (s.equalsIgnoreCase(JSON_RESULT_SUCCESS)) {

                            b.putString(JSON_KEY_RESULT, JSON_RESULT_SUCCESS);
                            sosApiListener.onLogoutResult();
                        } else {
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

        SOSApplication.getInstance().addToRequestQueue(request);


    }

    public void getNewItemUniqueId(final CallbacksUniqueID callbacksUniqueID) {
        String url = GSA() + API_URL + "act=" + ACTION_GET_UNIQUE_ID;
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        callbacksUniqueID.onUniqueIDLoaded(s);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callbacksUniqueID.onError(volleyError.getMessage());
                    }
                }
        );

        SOSApplication.getInstance().addToRequestQueue(request);
    }

    public void gotoNoNetworkActivity() {
        Intent intent = new Intent(context, ActivityNoNetwork.class);
        context.startActivity(intent);
    }

    public void deleteLookingFor(final CallbacksLookingFor callbacksLookingFor, String id) {

        String url = GSA() + API_URL + "act=" + ACTION_DELETE_LOOKING_FOR + "&iid=" + id + "&uid=" + GSV(KEY_ACC_DATA_USER_ID);


        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.equals(TRUE)) {
                            callbacksLookingFor.onDeleteLookingSuccess();
                        } else {
                            callbacksLookingFor.onDeleteLookingForFailure(s);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callbacksLookingFor.onDeleteLookingForFailure(volleyError.getMessage());
                    }
                }
        );

        SOSApplication.getInstance().addToRequestQueue(request);

    }

    public SharedPreferences getPreferences() {
        return preferences;
    }

    public void clearWishList(final CallbacksWishlist callbacks) {


        String url = GSA() + API_URL + "act=" + ACTION_CLEAR_WISHLIST + "&uid=" + GSV(KEY_ACC_DATA_USER_ID);

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        callbacks.onWishlistClearResult(s.equals(TRUE), null);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callbacks.onWishlistClearResult(false, volleyError.getMessage());
                    }
                }
        );

        SOSApplication.GI().addToRequestQueue(request);

    }

    /*public void uploadPicFile(final CallbacksImageFileUpload callbacksImageFileUpload, String filePath, String fileName, String dirPath, final String tag, final Bundle metaData) {

        String url =GSA() + API_URL + "act=" + ACTION_UPLOAD_PRODUCT_IMAGE_FILE + "&dirPath=" + dirPath + "&fname=" + fileName;
        final Bundle data = new Bundle(metaData);

        UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(
                context,
                filePath,
                url,

                new UploadAsyncTask.Callbacks() {
                    @Override
                    public void onProgress(int progress) {
                        callbacksImageFileUpload.CBIFUonUploadProgress(tag, progress);
                        if(progress == 100){
                            callbacksImageFileUpload.CBIFUonUploadSuccess(tag, data);
                        }
                    }

                    @Override
                    public void onPostExecute(String result) {
                        callbacksImageFileUpload.CBIFUonPostExecute(tag, result);
                        callbacksImageFileUpload.CBIFUdidUpload(tag);

                    }

                    @Override
                    public void onPreExecute() {
                        callbacksImageFileUpload.CBIFUonFileWillUpload(tag);
                    }
                }
        );

        uploadAsyncTask.execute();
    }

    public interface CallbacksImageFileUpload{

        void CBIFUonFileWillUpload(String tag);
        void CBIFUonUploadProgress(String tag, int progress);
        void CBIFUdidUpload(String tag);
        void CBIFUonUploadFailed(String tag, Bundle data);
        void CBIFUonUploadSuccess(String tag, Bundle data);
        void CBIFUonPostExecute(String tag, String result);
    }*/

    public void updateLooking4(final CallbacksLookingFor listener, String title, String desc, float rating, String updid) {

        try {
            title = URLEncoder.encode(title, "utf-8");
            desc = URLEncoder.encode(desc, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, "updateLooking4: excetion -> " + e.getMessage());
        }


        String url = GSA() + API_URL + "act=" + ACTION_POST_INQUIRY + "&title=" + title + "&desc=" + desc + "&myid=" + getSessionVar(KEY_ACC_DATA_USER_ID) + "&rating=" + rating;

        if (!updid.equals(LookingFor.NO_ID)) url = url.concat("&updid=" + updid);

        Log.e(TAG, "updateLooking4: -> " + url);

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Log.e("XXX", "updl4 -> " + s);
                        boolean success = s.equals(TRUE);
                        int code = success ? NETWORK_RESULT_CODES.RESULT_CODE_SUCCESS : NETWORK_RESULT_CODES.RESULT_CODE_FAILURE;

                        String message = "success";

                        if (code == NETWORK_RESULT_CODES.RESULT_CODE_FAILURE) message = s;

                        listener.onUpdateLookingForResult(code, message);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                        listener.onUpdateLookingForResult(NETWORK_RESULT_CODES.RESULT_CODE_NETWORK_ERROR, volleyError.getMessage());
                    }
                }

        );

        SOSApplication.getInstance().addToRequestQueue(request);
    }

    public void publishItem(final CallbacksProduct callBacksProduct, String itemID) {

        String url = GSA() + API_URL + "act=" + ACTION_PUBLISH_ITEM + "&itid=" + itemID;

        Log.e(TAG, "publishItem: -> " + url);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {

                            Log.e(TAG, "onResponse: -> " + s);
                            JSONObject object = new JSONObject(s);
                            int resCode = object.getInt(NETWORK_RESULT_CODES.KEY_RESULT_CODE);
                            String data = object.getString(NETWORK_RESULT_CODES.KEY_RESULT_DATA);

                            callBacksProduct.onItemPublishResult(resCode, data);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "onResponse: Exception -> " + e.getMessage());

                            callBacksProduct.onItemPublishResult(NETWORK_RESULT_CODES.RESULT_CODE_EXCEPTION, e.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                }
        );

        SOSApplication.GI().addToRequestQueue(request);
    }

    public void exposeItem(final SOSApiListener listener, final Bundle data) {


        //Log.e(TAG, "exposeItem: data -> " + data.toString() );
        //String pdUniqueName = data.getString("");

        String url = GSA() + API_URL;
        String itemId = data.getString(KEY_ITEM_ID);
        final boolean itemUpdating = itemId != null;

        if (itemUpdating) { //UPDATING ITEM
            url = url.concat("act=" + ACTION_UPDATE_ITEM + "&itemId=" + itemId + "&un=" + data.getString(Product.KEY_PD_UNIQUE_NAME));
        } else {
            url = url.concat("act=" + ACTION_EXPOSE_ITEM) + "&un=" + data.getString(Product.KEY_PD_UNIQUE_ID);
        }

        //Log.e(TAG, "exposeItem: url -> " + url );
        //Log.e(TAG, "exposeItem: item data -> " + data.toString() );


        StringRequest requestItemDetails = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        Log.e(TAG, "ON THA RES -> " + s);

                        Bundle b = new Bundle();
                        b.putString(JSON_KEY_DATA, s);
                        try {
                            JSONObject jo = new JSONObject(s);


                            String result = jo.getString(JSON_KEY_RESULT);

                            if (result.equals(JSON_RESULT_SUCCESS)) {
                                b.putString(JSON_KEY_RESULT, JSON_RESULT_SUCCESS);

                                b.putString(KEY_ITEM_UNIQUE_NAME, jo.getString(KEY_ITEM_UNIQUE_NAME));

                                if (itemUpdating) {
                                    b.putBoolean(KEY_ITEM_UPDATED, true);
                                }
                            } else {
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
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();


                params.put(KEY_ITEM_NAME, data.getString(KEY_ITEM_NAME));
                params.put(KEY_ITEM_PRICE, data.getString(KEY_ITEM_PRICE));
                params.put(KEY_ITEM_CURRENCY, data.getString(KEY_ITEM_CURRENCY));
                params.put(KEY_ITEM_PRICE_TO_DISCUSS, data.getString(KEY_ITEM_PRICE_TO_DISCUSS));
                params.put(KEY_ITEM_DESCRIPTION, data.getString(KEY_ITEM_DESCRIPTION));
                params.put(KEY_ITEM_CATEGORY, data.getString(KEY_ITEM_CATEGORY));
                params.put(KEY_ITEM_TYPE, data.getString(KEY_ITEM_TYPE));
                params.put(Product.KEY_PD_OWNER_ID, data.getString(Product.KEY_PD_OWNER_ID));

                params.put(KEY_ITEM_MAIN_PIC, KEY_ITEM_NO_PIC);//data.getString(KEY_ITEM_MAIN_PIC));

                params.put(KEY_ITEM_PIC_1, KEY_ITEM_NO_PIC);//data.getString(KEY_ITEM_PIC_1));
                params.put(KEY_ITEM_PIC_2, KEY_ITEM_NO_PIC);//data.getString(KEY_ITEM_PIC_2));
                params.put(KEY_ITEM_PIC_3, KEY_ITEM_NO_PIC);//data.getString(KEY_ITEM_PIC_3));
                params.put(KEY_ITEM_QUALITY, data.getString(KEY_ITEM_QUALITY));


                return params;
            }


        };

        SOSApplication.getInstance().addToRequestQueue(requestItemDetails);
    }

    public void loadMyProducts(final ListenerLoadMyProducts listener) {

        String url = GSA() + API_URL + "act=" + SOS_API.ACTION_LOAD_ALL_MY_PRODUCTS +
                "&uid=" + getSessionVar(KEY_ACC_DATA_USER_ID);

        Log.e(TAG, "myproducs url : " + url);

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        if (s.equals(JSON_RESULT_FAILURE)) {
                            listener.onMyProductsEmpty();
                        } else {

                            List<ProductMyProducts> products = new ArrayList<>();

                            try {
                                JSONArray ja = new JSONArray(s);

                                if (ja.length() == 0) {
                                    listener.onMyProductsEmpty();
                                } else {


                                    for (int i = 0; i < ja.length(); i++) {

                                        JSONObject jo = ja.getJSONObject(i);

                                        ProductMyProducts pd = new ProductMyProducts(
                                                jo.getString(Product.KEY_PD_NAME),
                                                jo.getString(Product.KEY_PD_PRICE),
                                                jo.getString(Product.KEY_PD_IMG) + SOS_API.KEY_ITEM_POST_FIX_MAIN_PIC,
                                                jo.getString(Product.KEY_PD_CUR),
                                                jo.getString(Product.KEY_PD_CAT),
                                                jo.getString(Product.KEY_PD_QUAL),
                                                jo.getString(Product.KEY_PD_DESC),
                                                jo.getString(Product.KEY_PD_UNIQUE_NAME));

                                        Bundle b = new Bundle();

                                        HelperMethods.PutAllJSONIntoBundle(jo, b);
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


        SOSApplication.getInstance().addToRequestQueue(request);


    }

    public BitmapCacheManager getBitmapCacheManager() {
        return bitmapCacheManager;
    }

    public void setBitmapCacheManager(BitmapCacheManager bitmapCacheManager) {
        this.bitmapCacheManager = bitmapCacheManager;
    }

    public void loadLookingFors(final AdapterLookingFor.CallBacks callbacks, boolean mine, int limit) {

        String url = GSA() + API_URL + "act=" + ACTION_LOAD_ALL_LOOKINGFORS + "&uid=" + GSV(KEY_ACC_DATA_USER_ID);

        if (mine) url = url.concat("&mine");

        if (limit != NO_LIMIT) url = url.concat("&limit=" + limit);


        Log.e(TAG, "KAAK: -> " + url);

        StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        Log.e("XXX", "XXX -> " + s);

                        if (s.equals(SOS_API.RES_EMPTY)) {
                            callbacks.onLookingForsEmpty();
                        } else {

                            try {
                                JSONArray jsonArray = new JSONArray(s);
                                ArrayList<LookingFor> looking4s = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject object = jsonArray.getJSONObject(i);
                                    LookingFor lookingFor = new LookingFor();

                                    lookingFor.setTitle(object.getString(LookingFor.KEY_TITLE));
                                    lookingFor.setMessage(object.getString(LookingFor.KEY_DESC));

                                    lookingFor.setPosterName(object.getString(LookingFor.KEY_POSTERNAME));

                                    Bundle data = new Bundle();
                                    HelperMethods.PutAllJSONIntoBundle(object, data);

                                    String dateStart = object.getString(LookingFor.KEY_DATETIME);

                                    String postedDate = HM.CLDTAS(context,
                                            HelperDate.getLongDateFromDateStr(dateStart), HelperDate.getCurrentLondDate());//dateDiff.toSocialFormat();//HM.FD(dateDiff, dateStart);


                                    lookingFor.setDateTime(postedDate);
                                    data.putString(LookingFor.KEY_DATETIME, postedDate);
                                    data.putString(TIME_STAMP, object.getString(LookingFor.KEY_DATETIME));
                                    data.putString(LookingFor.KEY_DATETIME, postedDate);
                                    /*
                                    data.putString(SOS_API.KEY_ACC_DATA_MOBILE, object.getString(SOS_API.KEY_ACC_DATA_MOBILE));
                                    data.putString(SOS_API.KEY_ACC_DATA_EMAIL, object.getString(SOS_API.KEY_ACC_DATA_EMAIL));
                                    data.putString(LookingFor.KEY_DATETIME, object.getString(LookingFor.KEY_DATETIME));
                                    data.putString(SOS_API.KEY_ACC_DATA_MOBILE_HASH, object.getString(SOS_API.KEY_ACC_DATA_MOBILE_HASH));
                                    data.putString(LookingFor.KEY_INQUIRY_RATING, object.getString(LookingFor.KEY_INQUIRY_RATING));
                                    data.putString(SOS_API.KEY_ACC_DATA_MOBILE, object.getString(SOS_API.KEY_ACC_DATA_MOBILE));
                                    data.putString(SOS_API.KEY_ACC_DATA_EMAIL, object.getString(SOS_API.KEY_ACC_DATA_EMAIL));*/

                                    lookingFor.setData(data);

                                    String l4uid = object.getString(SOS_API.KEY_ACC_DATA_USER_ID);

                                    if (l4uid.equals(GSV(SOS_API.KEY_ACC_DATA_USER_ID))) {
                                        lookingFor.setIsMine(true);
                                    }


                                    looking4s.add(lookingFor);

                                }

                                callbacks.onLookingForsLoaded(looking4s);

                            } catch (JSONException e) {
                                callbacks.onLookingForsLoadError(false, e.getMessage());
                            }

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callbacks.onLookingForsLoadError(true, volleyError.getMessage());
                    }
                }
        );

        SOSApplication.getInstance().addToRequestQueue(request);


    }

    public void loadRecentItems(final ListenerLoadRecentItems listener) {

        String url = GSA() + API_URL + "act=" + SOS_API.ACTION_LOAD_FEATURED_PRODUCTS + "&uid=" + getSessionVar(KEY_ACC_DATA_USER_ID);
        Log.e(TAG, "loadRecentItems: url -> " + url);


        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        if (s.equals(JSON_RESULT_FAILURE)) {
                            listener.onRecentItemsEmpty();
                        } else {

                            List<Product> products = new ArrayList<>();

                            try {
                                JSONArray ja = new JSONArray(s);

                                if (ja.length() == 0) {
                                    listener.onRecentItemsEmpty();
                                } else {


                                    for (int i = 0; i < ja.length(); i++) {

                                        JSONObject jo = ja.getJSONObject(i);

                                        Product pd = new Product(
                                                jo.getString(Product.KEY_PD_NAME),
                                                jo.getString(Product.KEY_PD_PRICE),
                                                jo.getString(Product.KEY_PD_IMG) + SOS_API.KEY_ITEM_POST_FIX_MAIN_PIC,
                                                jo.getString(Product.KEY_PD_CUR),
                                                jo.getString(Product.KEY_PD_CAT),
                                                jo.getString(Product.KEY_PD_QUAL),
                                                jo.getString(Product.KEY_PD_DESC),
                                                jo.getString(Product.KEY_PD_UNIQUE_NAME));

                                        Bundle b = new Bundle();
                                        HelperMethods.PutAllJSONIntoBundle(jo, b);

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


                                        String desc = jo.getString(Product.KEY_PD_DESC);
                                        String cat = jo.getString(Product.KEY_PD_CAT);
                                        String type = jo.getString(Product.KEY_PD_TYPE);

                                        b.putString(Product.KEY_PD_DESC, desc);
                                        pd.setPdDesc(desc);
                                        pd.setPdCat(cat);
                                        b.putString(KEY_ACC_DATA_MOBILE_HASH, jo.getString(KEY_ACC_DATA_MOBILE_HASH));
                                        b.putString(Product.KEY_PD_TYPE, type);
                                        b.putString(Product.KEY_PD_CAT, cat);


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


        SOSApplication.getInstance().addToRequestQueue(request);

    }

    public void loadAllProducts(final CallbacksProduct callbacks, String uid) {

        String url = GSA() + API_URL + "act=" + SOS_API.ACTION_LOAD_ALL_PRODUCTS + "&uid=" + uid;

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONObject res = new JSONObject(s);
                            int code = res.getInt(NETWORK_RESULT_CODES.KEY_RESULT_CODE);
                            List<ProductMyProducts> products = null;

                            if (code == NETWORK_RESULT_CODES.RESULT_CODE_SUCCESS) {

                                String data = res.getString(NETWORK_RESULT_CODES.KEY_RESULT_DATA);

                                JSONArray items = new JSONArray(data);

                                if (items.length() == 0) {
                                    callbacks.onLoadAllItemsResult(NETWORK_RESULT_CODES.RESULT_CODE_EMPTY_LIST, null);
                                } else {

                                    products = new ArrayList<>();
                                    for (int i = 0; i < items.length(); i++) {

                                        JSONObject jo = items.getJSONObject(i);

                                        ProductMyProducts pd = new ProductMyProducts(
                                                jo.getString(Product.KEY_PD_NAME),
                                                jo.getString(Product.KEY_PD_PRICE),
                                                jo.getString(Product.KEY_PD_IMG) + KEY_ITEM_POST_FIX_MAIN_PIC,
                                                jo.getString(Product.KEY_PD_CUR),
                                                jo.getString(Product.KEY_PD_CAT),
                                                jo.getString(Product.KEY_PD_QUAL),
                                                jo.getString(Product.KEY_PD_DESC),
                                                jo.getString(ProductWishList.KEY_DATE_ADDED));


                                        //Log.e(TAG, "date -> " + dateJson );

                                        Bundle b = new Bundle();
                                        HelperMethods.PutAllJSONIntoBundle(jo, b);
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


                                        products.add(pd);


                                    }

                                }


                            }

                            callbacks.onLoadAllItemsResult(code, products);


                        } catch (JSONException e) {
                            e.printStackTrace();


                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callbacks.onLoadAllItemsNetworkError(volleyError.getMessage());
                    }
                }
        );

        SOSApplication.GI().addToRequestQueue(request);
    }

    /*
    public void loadAllProducts(final SOSApiListener listener, String uid) {


        final List<ProductMyProducts> allProducts = new ArrayList<>();
        //allProducts.clear();
        String url =GSA() + API_URL + "act=" + SOS_API.ACTION_LOAD_ALL_PRODUCTS + "&uid=" + uid ;

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
                                            jo.getString(Product.KEY_PD_IMG) + KEY_ITEM_POST_FIX_MAIN_PIC,
                                            jo.getString(Product.KEY_PD_CUR),
                                            jo.getString(Product.KEY_PD_CAT),
                                            jo.getString(Product.KEY_PD_QUAL),
                                            jo.getString(Product.KEY_PD_DESC),
                                            jo.getString(ProductWishList.KEY_DATE_ADDED));



                                    //Log.e(TAG, "date -> " + dateJson );

                                    Bundle b = new Bundle();
                                    HelperMethods.PutAllJSONIntoBundle(jo, b);
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
                       Log.e("LOAD ALL PRODS", "onErrorResponse: " + volleyError.getMessage() );
                        //listener.onLoadAllProductsResult(new ArrayList<ProductMyProducts>());
                        listener.onLoadAllProductsError(volleyError.getMessage());
                    }
                }
        );

        SOSApplication.getInstance().addToRequestQueue(request);


    }
    */


    public void updateAccSettings(final SOSApiListener listener, final String settingKey, final String newValue) {


        String userid = getSessionVar(KEY_ACC_DATA_USER_ID);
        String url = GSA() + API_URL + "act=" + ACTION_UPDATE_USER_SETTING + "&setName=" + settingKey + "&newVal=" + newValue +
                "&user_id=" + userid;

        //Log.e(TAG, "updateSignleSetting: url -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        if (s.equals(JSON_RESULT_SUCCESS)) {
                            listener.onUpdateSettingsResult(settingKey, s);
                            setSessionVar(settingKey, newValue);
                        } else {
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

        SOSApplication.getInstance().addToRequestQueue(request);

    }

    public void updateUserPassword(final OnUpdatePasswordListenerm listener, final String newPassword) {

        String url = GSA() + API_URL + "act=" + ACTION_UPDATE_USER_SETTING + "&newVal=" + newPassword +
                "&" + KEY_ACC_DATA_USER_ID + "=" + GSV(KEY_ACC_DATA_USER_ID) + "&setName=" + KEY_ACC_DATA_PASSWORD;

        Log.e(TAG, "updateUserPassword: url -> " + url);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        String resMsg;

                        if (s.equals(JSON_RESULT_SUCCESS)) {

                            // TODO: 5/1/2018 SEND CONFIRMATION SMS

                            resMsg = newPassword;

                            SSV(KEY_ACC_DATA_PASSWORD, newPassword);

                        } else {
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

        SOSApplication.getInstance().addToRequestQueue(stringRequest);

    }

    public void removeProduct(final SOSApiListener listener, final ProductWishList pd) {

        String myid = getSessionVar(KEY_ACC_DATA_USER_ID);

        final String uniqueName = pd.getPdUniqueNameFromIMG();
        String url = GSA() + API_URL + "act=" + ACTION_REMOVE_PRODUCT + "&unq=" + uniqueName + "&myid=" + myid;

        //Log.e(TAG, "removeProduct: url -> " + url );


        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        Bundle b = new Bundle();
                        b.putInt(Product.KEY_PD_ADAPTER_POSITION, pd.getData().getInt(Product.KEY_PD_ADAPTER_POSITION));
                        b.putString(Product.KEY_PD_UNIQUE_NAME, uniqueName);
                        if (s.equals(JSON_RESULT_SUCCESS)) {

                            b.putString(JSON_KEY_RESULT, JSON_RESULT_SUCCESS);
                            b.putInt(Product.KEY_PD_ADAPTER_POSITION, pd.getData().getInt(Product.KEY_PD_ADAPTER_POSITION));

                        } else {
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


        SOSApplication.getInstance().addToRequestQueue(request);

    }

    public void postLooking4(final SOSApiListener listener, String title, String desc, float rating) {

        try {
            title = URLEncoder.encode(title, "utf-8");
            desc = URLEncoder.encode(desc, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.e(TAG, "postLooking4: exception -> " + e.getMessage());
        }


        String url = GSA() + API_URL + "act=" + ACTION_POST_INQUIRY + "&title=" + title + "&desc=" + desc + "&myid=" + getSessionVar(KEY_ACC_DATA_USER_ID) + "&rating=" + rating + "&updid=-1";
        Log.e(TAG, "postLooking4: url -> " + url);
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

        SOSApplication.getInstance().addToRequestQueue(request);

    }

    public void updateItemViewsCount(final SOSApiListener listener, String itemId) {

        String url = GSA() + API_URL + "act=" + ACTTION_UPDATE_ITEM_VIEWS_COUNT + "&itemid=" + itemId;
        //Log.e(TAG, "updateItemViewsCount: url -> " + url );
        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (s.equals(JSON_RESULT_FAILURE) == false) {

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

        SOSApplication.getInstance().addToRequestQueue(request);

    }

    public void searchq(final Context context, String q, final CallbacksSearch callbacksSearch) {


    }

    public void search(final SOSApiListener listener, String q) {


        String uid = getSessionVar(KEY_ACC_DATA_USER_ID);
        final List<ProductMyProducts> allProducts = new ArrayList<>();
        allProducts.clear();
        String url = GSA() + API_URL + "act=" + ACTION_SEARCH_ITEMS + "&uid=" + uid + "&q=" + q;

        //Log.e(TAG, "search url : " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {


                        if (s.equals(JSON_RESULT_FAILURE)) {
                            listener.onSearchResultError(s);
                        } else {

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

        SOSApplication.getInstance().addToRequestQueue(request);

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
                                            jo.getString(Product.KEY_PD_IMG) + KEY_ITEM_POST_FIX_MAIN_PIC,
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

        String url = GSA() + API_URL + "act=loadSearchRes&ids=" + ids;

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

                        if (jsonArray.length() > 0) {

                            for (int i = 0; i < jsonArray.length(); i++) {

                                try {


                                    JSONObject jo = jsonArray.getJSONObject(i);

                                    ProductMyProducts pd = new ProductMyProducts(
                                            jo.getString(Product.KEY_PD_NAME),
                                            jo.getString(Product.KEY_PD_PRICE),
                                            jo.getString(Product.KEY_PD_IMG) + KEY_ITEM_POST_FIX_MAIN_PIC,
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

        SOSApplication.getInstance().addToRequestQueue(request);

    }

    public void loadItemsTypesFromCatName(final SOSApiListener listener, String catName) {
        String catsAndTypes = GSV(KEY_ITEMS_CATS_AND_TYPES);

        if (catsAndTypes.equals(KEY_SESSION_DATA_EMPTY)) {

            //Log.e(TAG, "loadItemsTypesFromCatId: error " );

        } else {


            try {
                JSONArray cats = new JSONArray(catsAndTypes);

                for (int i = 0; i < cats.length(); i++) {

                    JSONObject cat = (JSONObject) cats.get(i);
                    String curCatId = cat.getString(KEY_ITEM_CATEGORY_NAME);

                    if (curCatId.equals("" + catName)) {

                        String types = cat.getString(KEY_ITEMS_CATEGORY_TYPES);

                        JSONArray jsonArray = new JSONArray(types);

                        if (jsonArray.length() > 0) {

                            String[] typesArr = new String[jsonArray.length()];

                            for (int j = 0; j < jsonArray.length(); j++) {

                                JSONObject type = jsonArray.getJSONObject(j);
                                typesArr[j] = HM.UCF(type.getString(KEY_ITEM_TYPE_NAME));

                            }

                            listener.onLoadItemsTypesResult(typesArr);

                        } else {
                            listener.onLoadItemsTypesResultError();
                        }

                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "loadItemsTypesFromCatId: json exception -> " + e.getMessage());
            }

        }
    }

    public void loadItemsTypesFromCatId(final SOSApiListener listener, final int categoryId) {


        String catsAndTypes = GSV(KEY_ITEMS_CATS_AND_TYPES);

        if (catsAndTypes.equals(KEY_SESSION_DATA_EMPTY)) {

            //Log.e(TAG, "loadItemsTypesFromCatId: error " );

        } else {


            try {
                JSONArray cats = new JSONArray(catsAndTypes);

                for (int i = 0; i < cats.length(); i++) {

                    JSONObject cat = (JSONObject) cats.get(i);
                    String curCatId = cat.getString(KEY_ITEM_CATEGORY_ID);

                    if (curCatId.equals("" + categoryId)) {

                        String types = cat.getString(KEY_ITEMS_CATEGORY_TYPES);

                        JSONArray jsonArray = new JSONArray(types);

                        if (jsonArray.length() > 0) {

                            String[] typesArr = new String[jsonArray.length()];

                            for (int j = 0; j < jsonArray.length(); j++) {

                                JSONObject type = jsonArray.getJSONObject(j);
                                typesArr[j] = HM.UCF(type.getString(KEY_ITEM_TYPE_NAME));

                            }

                            listener.onLoadItemsTypesResult(typesArr);

                        } else {
                            listener.onLoadItemsTypesResultError();
                        }

                    }


                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "loadItemsTypesFromCatId: json exception -> " + e.getMessage());
            }

        }

    }

    public void uploadProfilePic(final SOSApiListener listener, final String imgStr) {

        String url = GSA() + API_URL + "act=" + ACTION_UPLOAD_PP + "&mobile=" + GSV(KEY_ACC_DATA_MOBILE);
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


                            if (jsonObject.getString(JSON_KEY_RESULT).equals(JSON_RESULT_SUCCESS)) {

                                b.putString(JSON_KEY_RESULT, JSON_RESULT_SUCCESS);


                            } else {
                                b.putString(JSON_KEY_RESULT, JSON_RESULT_FAILURE);
                            }

                            listener.onUploadPPResult(b);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Bundle b = new Bundle();
                            b.putString(JSON_KEY_RESULT, JSON_RESULT_FAILURE);
                            b.putString(JSON_KEY_RESULT_ERROR_MESSAGE, e.getMessage());

                            listener.onUploadPPResult(b);
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
        ) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<>();

                params.put(KEY_PROFILE_PIC_DATA, imgStr);

                return params;

            }
        };

        SOSApplication.getInstance().addToRequestQueue(request);
    }

    public void loadCatTypeNames(SOSApiListener listener, String catId, String typeId) {

        if (getPreferences().getString(KEY_ITEMS_CATS_AND_TYPES, KEY_PREF_EMPTY).equals(KEY_PREF_EMPTY)) {


            /*String url =GSA() + API_URL + "act=" + ACTION_LOAD_ALL_ITEMS_CATS_AND_TYPES;

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


            SOSApplication.getInstance().addToRequestQueue(request);*/

        } else {
            try {
                String s = getPreferences().getString(KEY_ITEMS_CATS_AND_TYPES, KEY_PREF_EMPTY);
                JSONArray cats = new JSONArray(s);

                if (cats.length() > 0) {

                    for (int i = 0; i < cats.length(); i++) {


                        JSONObject cat = (JSONObject) cats.get(i);

                        if (cat.getString(KEY_ITEM_CATEGORY_ID).equals(catId)) {

                            String cn = cat.getString(KEY_ITEM_CATEGORY_NAME);
                            //Log.e(TAG, "THA CAT NAME BE -> " + cn );

                            String typesStr = cat.getString(KEY_ITEMS_CATEGORY_TYPES);

                            //Log.e(TAG, "DA TYPES -> " +  typesStr );

                            JSONArray types = new JSONArray(typesStr);

                            String tn = "N/A";
                            if (types.length() > 0) {

                                for (int j = 0; j < types.length(); j++) {
                                    JSONObject type = types.getJSONObject(j);

                                    if (type.getString(KEY_ITEM_TYPE_ID).equals(typeId)) {
                                        tn = type.getString(KEY_ITEM_TYPE_NAME);
                                        //Log.e(TAG, "THA TYPE NAME BE -> " + tn);
                                    }
                                    // break;
                                }

                            } else {

                                //Log.e(TAG, "TYPES LEN IS ZERO SO TN -> " + tn );
                            }
                            //break;
                            //Log.e(TAG, "CAT -> " + cn + " TYPE -> " + tn );
                            listener.onLoadCatsTypesNames(cn, tn);

                        }

                    }

                } else {
                    //Log.e(TAG, "CATS LEN IS ZERO" );
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Log.e(TAG, "loadCatTypeNames: " + e.getMessage());
            }

        }

    }

    public void loadCategoryTypesFromCatId(final SOSApiListener listener, String id) {

        Log.e(TAG, "loadCatData: id -> " + id);


        //sosApi.loadItemsTypesFromCatId(this, new Integer(id).intValue());

        // TODO: 12/26/2017 TO MOVE IN SOS API CLASS

        String url = GSA() + API_URL + "act=" + SOS_API.ACTION_LOAD_ITEM_TYPES_FROM_CAT_ID + "&catId=" + id;
        Log.e(TAG, "loadCatData: -> " + url);

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        List<TypesItem> types = null;
                        try {
                            JSONArray jsonArray = new JSONArray(s);


                            if (jsonArray.length() > 0) {

                                //List<TypesItem> types = new ArrayList<>();
                                types = new ArrayList<>();

                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject type = jsonArray.getJSONObject(i);
                                    Log.e(TAG, "TYPE JO -> " + type.toString());


                                    String id = type.getString(TypesItem.KEY_TYPE_ITEM_ID);
                                    String name = type.getString(TypesItem.KEY_TYPE_ITEM_NAME);
                                    String imgPath = GSA() + ROOT_URL + DIR_PATH_TYPES + type.getString(TypesItem.KEY_TYPE_ITEM_IMG_PATH);

                                    TypesItem typeItem = new TypesItem(id, name, imgPath);


                                    types.add(typeItem);


                                }

                                listener.onCategoryTypesLoaded(types, false);

                            } else {
                                Log.e(TAG, "TYPES JSON ARRAY LEN = 0");
                                listener.onCategoryTypesLoaded(types, false);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e(TAG, "LOAD TYPES JSON ERROR -> " + e.getMessage());

                            if (s.equals(JSON_RESULT_FAILURE)) {
                                listener.onCategoryTypesLoaded(types, false);
                            } else {
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

        SOSApplication.getInstance().addToRequestQueue(request);


    }

    public void removeItemFromWishlist(final ListenerItemsWishlist listener, final Bundle itemData) {

        String wid = itemData.getString(SOS_API.KEY_ITEM_ID);
        //final String itemName = itemData.getString(Product.KEY_PD_NAME);
        String url = GSA() + API_URL + "act=" + ACTION_REMOVE_ITEM_FROM_WISHLIST + "&wid=" + wid + "&uid=" + GSV(KEY_ACC_DATA_USER_ID);

        Log.e(TAG, "removeItemFromWishlist: url -> " + url);

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        if (s.equals(JSON_RESULT_FAILURE)) {
                            listener.onWishlistItemRemoveError(itemData);
                        } else {
                            listener.onWishlistItemRemoveSuccess(itemData);
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

        SOSApplication.getInstance().addToRequestQueue(request);
    }

    public void addItemToWishlist(final ListenerItemsWishlist listener, String itemId) {

        String url = GSA() + API_URL + "act=" + ACTION_ADD_ITEM_TO_WISHLIST + "&uid=" +
                GSV(KEY_ACC_DATA_USER_ID) + "&wid=" + itemId;

        Log.e(TAG, "addItemToWishlist: url -> " + url);

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        if (s.equals(JSON_RESULT_FAILURE)) {
                            listener.onItemAddedError(s);
                        } else {
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

        SOSApplication.getInstance().addToRequestQueue(request);

    }

    public void loadWishListData(final CallbacksWishlist callbacks) {


        String url = GSA() + API_URL + "act=" + ACTTION_LOAD_WISH_LIST + "&uid=" + GSV(KEY_ACC_DATA_USER_ID);
        Log.e(TAG, "wishlist: url -> " + url);

        StringRequest requestItems = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONArray jsonArray = new JSONArray(s);


                            if (jsonArray.length() > 0) {


                                List<ProductWishList> prods = new ArrayList<>();
                                //prods.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {

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
                                    HelperMethods.PutAllJSONIntoBundle(jo, data);

                                    String dateStart = jo.getString(Product.KEY_PD_DATE_ADDED);

                                    //HelperDate.DateDiff dateDiff = HelperDate.dateDiff(dateStart, dateEnd );//new Date().toString());

                                    String postedDate = HM.CLDTAS(context,
                                            HelperDate.getLongDateFromDateStr(dateStart), HelperDate.getCurrentLondDate());//dateDiff.toSocialFormat();//HM.FD(dateDiff, dateStart);


                                    data.putString(SOS_API.KEY_ITEM_ID, jo.getString(SOS_API.KEY_ITEM_ID));
                                    data.putString(Product.KEY_PD_DATE_ADDED, postedDate);
                                    data.putString(Product.KEY_PD_UNIQUE_NAME, jo.getString(Product.KEY_PD_UNIQUE_NAME));
                                    data.putString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME, jo.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));
                                    data.putString(SOS_API.KEY_ITEM_ITEM_VIEWS_ACCOUNT, jo.getString(SOS_API.KEY_ITEM_ITEM_VIEWS_ACCOUNT));
                                    data.putString(Product.KEY_PD_DATE_SOLD, jo.getString(Product.KEY_PD_DATE_SOLD));
                                    data.putString(Product.KEY_PD_UNIQUE_NAME, jo.getString(Product.KEY_PD_UNIQUE_NAME));
                                    data.putString(Product.KEY_PD_OWNER_ID, jo.getString(Product.KEY_PD_OWNER_ID));
                                    data.putString(SOS_API.KEY_ACC_DATA_USER_ID, jo.getString(SOS_API.KEY_ACC_DATA_USER_ID));
                                    pd.setData(data);
                                    //Log.e(TAG, "PD TOB " + pd.toBundle().toString() );

                                    prods.add(pd);

                                }

                                callbacks.onWishLisItemsLoaded(prods);


                            } else {
                                callbacks.onNoItemsInWishlist();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            callbacks.onNoItemsInWishlist();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        callbacks.onErrorLoadWishList(volleyError.getMessage());
                    }
                }
        );


        SOSApplication.getInstance().addToRequestQueue(requestItems);

    }

    public void loadItemsCatsAndTypes(final SOSApiListener listener) {


        if (getPreferences().getString(KEY_ITEMS_CATS_AND_TYPES, KEY_PREF_EMPTY).equals(KEY_PREF_EMPTY)) {


            String url = GSA() + API_URL + "act=" + ACTION_LOAD_ALL_ITEMS_CATS_AND_TYPES;

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


            SOSApplication.getInstance().addToRequestQueue(request);

        } else {
            try {
                String s = getPreferences().getString(KEY_ITEMS_CATS_AND_TYPES, KEY_PREF_EMPTY);
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

    public void loadItemsInType(final CallBacksItemsInTypes listener, String typeId) {


        String url = GSA() + API_URL + "act=" + ACTION_LOAD_ITEMS_IN_TYPE + "&typeId=" + typeId;
        Log.e(TAG, "loadItemsInType: url -> " + url);

        Log.e(TAG, "onResponse: \nurl : " + url);

        StringRequest requestItems = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {

                        try {
                            JSONArray jsonArray = new JSONArray(s);


                            if (jsonArray.length() > 0) {


                                List<Product> prods = new ArrayList<>();
                                //prods.clear();

                                for (int i = 0; i < jsonArray.length(); i++) {

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
                                    data.putString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME, jo.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));
                                    data.putString(SOS_API.KEY_ITEM_ITEM_VIEWS_ACCOUNT, jo.getString(SOS_API.KEY_ITEM_ITEM_VIEWS_ACCOUNT));
                                    data.putString(Product.KEY_PD_DATE_SOLD, jo.getString(Product.KEY_PD_DATE_SOLD));
                                    data.putString(SOS_API.KEY_ACC_DATA_USER_ID, jo.getString(SOS_API.KEY_ACC_DATA_USER_ID));
                                    pd.setData(data);
                                    //Log.e(TAG, "PD TOB " + pd.toBundle().toString() );

                                    prods.add(pd);

                                }

                                listener.onItemsInTypeLoaded(prods);


                            } else {
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


        SOSApplication.getInstance().addToRequestQueue(requestItems);

    }

    public void logout(final CallbacksLogout callbacks) {


        // TODO: 12/1/17 EMPTY SESSION INPHP

        String url = GSA() + API_URL + "act=" + ACTION_LOGOUT + "&uid=" + getSessionVar(KEY_ACC_DATA_USER_ID);

        //Log.e(TAG, "logout: url -> " + url );

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Log.e("XXX", "onResponse: logout resp -> " + s);

                        boolean success = s.equals(TRUE);


                        if (success) {
                            String serverAdd = GSV(SERVER_ADD);
                            String lun = GSV(KEY_LAST_USERNAME);
                            String autorefresh = GSV(KEY_AUTOREFRESH_RECENT_ITEMS);
                            editor.clear();
                            SSV(SERVER_ADD, serverAdd);
                            SSV(KEY_LAST_USERNAME, lun);
                            SSV(KEY_AUTOREFRESH_RECENT_ITEMS, autorefresh);
                            editor.commit();
                        }

                        callbacks.onLogoutResult(success);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "onError logout restp ->  " + volleyError.getMessage());
                        callbacks.onLogoutResult(false);
                    }
                }
        );

        SOSApplication.getInstance().addToRequestQueue(request);


    }

    public interface CallbacksProduct {
        void onItemPublishResult(int code, String data);

        void onLoadAllItemsResult(int code, List<ProductMyProducts> products);

        void onLoadAllItemsNetworkError(String message);
    }

    public interface CallbacksLookingFor {
        void onDeleteLookingSuccess();

        void onDeleteLookingForFailure(String message);

        void onUpdateLookingForResult(int code, String data);
    }

    public interface CallbacksUniqueID {
        void onUniqueIDLoaded(String un);

        void onError(String error);
    }

    public interface ListenerLoadMyProducts {
        void onMyProductsLoaded(List<ProductMyProducts> products);

        void onMyProductsEmpty();

        void onNetworkError(String msg);

        void onParseJsonError(String s);
    }

    public interface ListenerLoadRecentItems {
        void onRecentItemsLoaded(List<Product> products);

        void onRecentItemsEmpty();

        void onNetworkError(String msg);

        void onParseJsonError(String s);
    }

    public interface OnUpdatePasswordListenerm {
        void onPasswordUpdateResult(String resStatus, String resMessage);
    }

    public interface CallbacksSearch {
        void onSearchResult(Context context, List<ProductMyProducts> products);
    }

    public interface ListenerItemsWishlist {
        void onItemAddedSuccess();

        void onItemAddedError(String msg);

        void onNetworkError(String msg);

        void onWishlistItemRemoveError(Bundle pd);

        void onWishlistItemRemoveSuccess(Bundle pd);
    }

    public interface CallbacksWishlist {
        void onWishLisItemsLoaded(List<ProductWishList> wishlistItems);

        void onNoItemsInWishlist();

        void onErrorLoadWishList(String message);

        void onWishlistClearResult(boolean success, String message);
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

    public interface CallBacksItemsInTypes {

        void onItemsInTypeLoaded(List<Product> prods);

        void onNoProdsInType();

        void onErrorLoadProdsInType(String msg);
    }

    public interface CallbacksLogout {
        public void onLogoutResult(final boolean logoutSuccess);
    }

}
