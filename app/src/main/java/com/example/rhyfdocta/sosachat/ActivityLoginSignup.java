package com.example.rhyfdocta.sosachat;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.Helpers.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;

import org.json.JSONArray;

import java.util.List;

public class ActivityLoginSignup extends AppCompatActivity implements SOS_API.SOSApiListener {


    private static final String TAG = "ACT_LOG";
    LinearLayout loginForm, signupForm;
    TextView btnShowLoginForm, btnShowSignupForm, tvWelcomeMsg;
    EditText etUsername, etPassword, etSuMobile, etSuEmail, etSuFullName, etSuDisplayName, etSuPassword, etSuLocation;
    ImageView ivLogo;

    SOS_API sosApi;
    ScrollView svRoot;
    ImageView ivLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);


        svRoot = findViewById(R.id.svRoot);
        ivLauncher = findViewById(R.id.ivLauncher);


        alertDialogProcessing = HelperMethods.getAlertDialogProcessingWithMessage(this, HelperMethods.getStringResource(this,R.string.pbMsgProcessing),false);



        sosApi = new SOS_API(this);

        cbTermsAndCond = findViewById(R.id.cbSignupAgreeTerms);

        ivLogo = (ImageView) findViewById(R.id.ivWelcomLogo);
        tvWelcomeMsg = (TextView) findViewById(R.id.tvWelcomeMsg);

        btnShowLoginForm = (TextView) findViewById(R.id.tvShowLoginForm);
        btnShowSignupForm = (TextView) findViewById(R.id.tvShowSignupForm);

        loginForm = (LinearLayout) findViewById(R.id.formLogin);
        signupForm = (LinearLayout) findViewById(R.id.formSignup);

        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);

        etSuDisplayName = (EditText) findViewById(R.id.etSignupDisplayName);
        etSuFullName = (EditText) findViewById(R.id.etSignupFullName);
        etSuEmail = (EditText) findViewById(R.id.etSignupEmail);
        etSuMobile = (EditText) findViewById(R.id.etSignupMobile);
        etSuPassword = (EditText) findViewById(R.id.etSignupPassword);
        etSuLocation = (EditText) findViewById(R.id.etSignupLocation);

        getSupportActionBar().hide();



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Do something after 100ms
                if(sosApi.isLoggedIn()){
                    fireLoginIntent();
                }else{
                    ivLauncher.setVisibility(View.GONE);
                    svRoot.setVisibility(View.VISIBLE);
                }
            }
        }, 3000);


    }

    //private TextView tvAlertDialogResponse;

    public void showSignUpForm(View view) {

        signupForm.setVisibility(View.VISIBLE);
        loginForm.setVisibility(View.GONE);
        ivLogo.setVisibility(View.GONE);
        tvWelcomeMsg.setVisibility(View.GONE);


        //signupForm.setAlpha(0f);
        //signupForm.animate().alpha(1f).setDuration(500).start();

    }

    public void showLoginForm(View view) {

        signupForm.setVisibility(View.GONE);
        loginForm.setVisibility(View.VISIBLE);
        ivLogo.setVisibility(View.VISIBLE);
        tvWelcomeMsg.setVisibility(View.VISIBLE);

    }

    private AlertDialog alertDialogProcessing;

    public void login(View view) {

        Boolean isOnline = SOS_API.isOnline(this);

        //Log.e(TAG, "loginResult " + loginResult.toString() + " isOnlie : " + isOnline.toString() );

        // TODO: 11/30/17 REMOVE BACK DOOR
        if(isOnline) {

            alertDialogProcessing.show();
            String user = etUsername.getText().toString();

            if(HM.PIV(user) || HM.EIV(user)) {
                sosApi.login(this, etUsername.getText().toString(), etPassword.getText().toString());
            }else{
                alertDialogProcessing.hide();
                String msg = HM.RGS(this,R.string.msgInvalideEmailOrPhone);
                sosApi.toggleAlertDialogResponseWithMessage(true, msg);
                //Toast.makeText(this, , Toast.LENGTH_SHORT).show();
            }
        }else{
            alertDialogProcessing.hide();
            sosApi.toggleAlertDialogResponseWithMessage(true, HM.RGS(this, R.string.msgErrorInternetConnection));
        }



    }

    private void fireLoginIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        //intent.putExtras(data);
        startActivity(intent);
    }

    CheckBox cbTermsAndCond;

    public void signUp(View view) {
        /*Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);*/

        if(!cbTermsAndCond.isChecked()){

            sosApi.TADRWM(true, HM.RGS(this, R.string.msgNeedToAgreeTerms));

            return;
        }

        alertDialogProcessing.show();

        String mobile = GTTS(etSuMobile);
        String email = GTTS(etSuEmail);
        String password = GTTS(etSuPassword);
        String fullName = GTTS(etSuFullName);
        String displayName = GTTS(etSuDisplayName);
        String location = GTTS(etSuLocation);

        //Toast.makeText(this, "Will signup", Toast.LENGTH_SHORT).show();

        Bundle data = new Bundle();

        data.putString(SOS_API.KEY_ACC_DATA_MOBILE, mobile);
        data.putString(SOS_API.KEY_ACC_DATA_EMAIL, email);
        data.putString(SOS_API.KEY_ACC_DATA_PASSWORD, password);
        data.putString(SOS_API.KEY_ACC_DATA_FULL_NAME, fullName);
        data.putString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME, displayName);
        data.putString(SOS_API.KEY_ACC_DATA_LOCATION, location);


        if(HM.EIV(email) && HM.PIV(mobile)) {
            sosApi.signup(this, data);
        }else{

            sosApi.toggleAlertDialogResponseWithMessage(true, HM.RGS(this,R.string.msgInvalideEmailOrPhone ));
        }


    }

    //GETTEXT().TOSTRING()
    private String GTTS(EditText et) {
        return et.getText().toString();
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


        boolean loginResult = data.getString(SOS_API.JSON_KEY_RESULT).equalsIgnoreCase(SOS_API.LOGIN_SUCCESS);

        if (loginResult) {

            alertDialogProcessing.hide();
            fireLoginIntent();

        } else {
            alertDialogProcessing.hide();
           String msg =  HM.RGS(this, R.string.msgLoginFailed);
           sosApi.toggleAlertDialogResponseWithMessage(true,msg);
        }

    }

    @Override
    public void onSignUpResult(Bundle data) {

        alertDialogProcessing.hide();
        Log.e(TAG, "onSignUpResult: data -> " + data.toString() );

        boolean success = data.getString(SOS_API.JSON_KEY_RESULT).equalsIgnoreCase(SOS_API.SIGNUP_SUCCESS);



        if(success){
            Toast.makeText(this, "SIGN UP SUCCESS", Toast.LENGTH_SHORT).show();


            String username = data.getString(SOS_API.KEY_ACC_DATA_MOBILE);
            String password = data.getString(SOS_API.KEY_ACC_DATA_PASSWORD);

            //Log.e(TAG, "username  " + username + );

            sosApi.login(this, username, password);

        }else{
            String msg = HM.RGS(this, R.string.msgFailedSignup);
            sosApi.toggleAlertDialogResponseWithMessage(true,msg);
        }




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
    public void onLoadItemsCats(JSONArray cats) {

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

    @Override
    public void onCategoryTypesLoaded(List<TypesItem> types, boolean errorLoading) {

    }


    @Override
    public void onBackPressed() {

    }

    public void showTermsAndCond(View view) {

        //HM.T(this, "Terms And Conds", HM.TLL);
        Intent intent = new Intent(this, ActivityTermsAndCond.class);
        startActivity(intent);
    }

    public void onForgotPasswordClicked(View view) {

        Log.e(TAG, "onForgotPasswordClicked: " );
    }
}
