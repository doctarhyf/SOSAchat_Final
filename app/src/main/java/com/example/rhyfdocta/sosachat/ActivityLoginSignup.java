package com.example.rhyfdocta.sosachat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.example.rhyfdocta.sosachat.ObjectsModels.User;
import com.example.rhyfdocta.sosachat.app.SOSApplication;
import com.example.rhyfdocta.sosachat.debug.SOSDebug;

import org.json.JSONArray;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ActivityLoginSignup extends AppCompatActivity implements SOS_API.SOSApiListener,
        SOS_API.CallbacksLoginSignup{


    private static final String TAG = "ACT_LOG";
    LinearLayout loginForm, signupForm;
    TextView btnShowLoginForm, btnShowSignupForm, tvWelcomeMsg;
    EditText etUsername, etPassword, etSuMobile, etSuEmail, etSuFullName, etSuDisplayName, etSuPassword, etSuLocation;
    ImageView ivLogo;

    SOS_API sosApi;
    ScrollView svRoot;
    ImageView ivLauncher;
    private int dbgCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);


        svRoot = findViewById(R.id.svRoot);
        ivLauncher = findViewById(R.id.ivLauncher);


        alertDialogProcessing = HelperMethods.getAlertDialogProcessingWithMessage(this, HelperMethods.getStringResource(this,R.string.pbMsgProcessing),false);



        sosApi = SOSApplication.GI().getSosApi();

        if(sosApi.isLoggedIn()){
            fireLoginIntent();
        }

        String serverAdd = sosApi.GSV(SOS_API.SERVER_ADD);

        if(serverAdd.equals("") || serverAdd.equals(SOS_API.KEY_SESSION_DATA_EMPTY)){
            sosApi.SSV(SOS_API.SERVER_ADD, "jmtinvestment.com");
        }


        cbTermsAndCond = findViewById(R.id.cbSignupAgreeTerms);

        ivLogo = findViewById(R.id.ivWelcomLogo);
        tvWelcomeMsg = findViewById(R.id.tvWelcomeMsg);

        btnShowLoginForm = findViewById(R.id.tvShowLoginForm);
        btnShowSignupForm = findViewById(R.id.tvShowSignupForm);

        loginForm = findViewById(R.id.formLogin);
        signupForm = findViewById(R.id.formSignup);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        etSuDisplayName = findViewById(R.id.etSignupDisplayName);
        etSuFullName = findViewById(R.id.etSignupFullName);
        etSuEmail = findViewById(R.id.etSignupEmail);
        etSuMobile = findViewById(R.id.etSignupMobile);
        etSuPassword = findViewById(R.id.etSignupPassword);
        etSuLocation = findViewById(R.id.etSignupLocation);

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


        etUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();

                sosApi.SSV(SOS_API.KEY_LAST_USERNAME, text);
                Log.e("L4", "afterTextChanged: -> " + sosApi.GSV(SOS_API.KEY_LAST_USERNAME) );
            }
        });

        String lun = sosApi.GSV(SOS_API.KEY_LAST_USERNAME);
        if(!lun.equals(SOS_API.KEY_SESSION_DATA_EMPTY)) etUsername.setText(lun);
    }



    @Override
    protected void onResume() {
        super.onResume();
        String lun = sosApi.GSV(SOS_API.KEY_LAST_USERNAME);
        if(!lun.equals(SOS_API.KEY_SESSION_DATA_EMPTY)) etUsername.setText(lun);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        String lun = sosApi.GSV(SOS_API.KEY_LAST_USERNAME);
        if(!lun.equals(SOS_API.KEY_SESSION_DATA_EMPTY)) etUsername.setText(lun);
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

    public void showDebug(View view) {

        if(dbgCount >= 5){
            dbgCount = 0;
            SOSDebug.showDebugDialog(this);
        }

        dbgCount ++;
    }



    /*
    private void showDebugDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_debug, null);

        final EditText input = view.findViewById(R.id.dbgEtNewHost);
        final TextView tv = view.findViewById(R.id.tvSSV);


        String curIP = sosApi.GSV(SOS_API.SERVER_ADD);
        builder.setTitle(curIP);
        input.setText(curIP);



        builder.setView(view);

        DEBUG_DG_LOAD_SESSION_DATA(tv);


// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nip = input.getText().toString();
                Log.e(TAG, "NEW IP: -> " + nip );
                sosApi.SSV(SOS_API.SERVER_ADD, nip);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setCancelable(false);
        builder.show();

    }

    private void DEBUG_DG_LOAD_SESSION_DATA(TextView tv) {


        tv.setText("");
        tv.setText("SERVER ADD : " + sosApi.GSV(SOS_API.SERVER_ADD) + "\n");

        Map<String,?> prefs = sosApi.getPreferences().getAll();
        Set<String> keys = prefs.keySet();



        for(String key : keys){

            String str = key + " : " + prefs.get(key) + "\n";
            tv.append(str);
        }


    }*/

    public void login(View view) {

        Boolean isOnline = SOS_API.isOnline(this);

        //Log.e(TAG, "loginResult " + loginResult.toString() + " isOnlie : " + isOnline.toString() );

        // TODO: 11/30/17 REMOVE BACK DOOR

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        if(isOnline) {

            if(username.isEmpty() || password.isEmpty()){

                String msg = HM.RGS(this, R.string.msgEmailOrPasswordEmpty);
                sosApi.toggleAlertDialogResponseWithMessage(ActivityLoginSignup.this, true, msg);

            }else {
                alertDialogProcessing.show();
                String user = etUsername.getText().toString();

                if (HM.PIV(user) || HM.EIV(user)) {
                    sosApi.login(this, username, password);
                } else {
                    alertDialogProcessing.hide();
                    String msg = HM.RGS(this, R.string.msgInvalideEmailOrPhone);
                    sosApi.toggleAlertDialogResponseWithMessage(ActivityLoginSignup.this, true, msg);
                    //Toast.makeText(this, , Toast.LENGTH_SHORT).show();
                }
            }

        }else{
            alertDialogProcessing.hide();
            sosApi.toggleAlertDialogResponseWithMessage(ActivityLoginSignup.this,true, HM.RGS(this, R.string.msgErrorInternetConnection));
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

            sosApi.TADRWM(ActivityLoginSignup.this,true, HM.RGS(this, R.string.msgNeedToAgreeTerms));

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

            sosApi.toggleAlertDialogResponseWithMessage(ActivityLoginSignup.this,true, HM.RGS(this,R.string.msgInvalideEmailOrPhone ));
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
/*


        boolean loginResult = data.getString(SOS_API.JSON_KEY_RESULT).equalsIgnoreCase(SOS_API.LOGIN_SUCCESS);

        if (loginResult) {

            alertDialogProcessing.hide();
            fireLoginIntent();

        } else {
            alertDialogProcessing.hide();
           String msg =  HM.RGS(this, R.string.msgLoginFailed);
           sosApi.toggleAlertDialogResponseWithMessage(ActivityLoginSignup.this,true,msg);
        }*/

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
            sosApi.toggleAlertDialogResponseWithMessage(ActivityLoginSignup.this,true,msg);
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

    @Override
    public void loginSuccess(Bundle userData) {

        /*
        boolean loginResult = data.getString(SOS_API.JSON_KEY_RESULT).equalsIgnoreCase(SOS_API.LOGIN_SUCCESS);

        if (loginResult) {

            alertDialogProcessing.hide();
            fireLoginIntent();

        } else {
            alertDialogProcessing.hide();
           String msg =  HM.RGS(this, R.string.msgLoginFailed);
           sosApi.toggleAlertDialogResponseWithMessage(ActivityLoginSignup.this,true,msg);
        }*/
        alertDialogProcessing.hide();
        fireLoginIntent();
    }

    @Override
    public void loginFailedUserPasswordError(String username) {
        alertDialogProcessing.hide();
        String msg =  HM.RGS(this, R.string.msgLoginFailedPasswordError);
        sosApi.toggleAlertDialogResponseWithMessage(ActivityLoginSignup.this,true,msg);
    }

    @Override
    public void loginFailedUserDontExist(String username) {
        alertDialogProcessing.hide();
        String msg =  HM.RGS(this, R.string.msgLoginFailedUserDontExist);
        sosApi.toggleAlertDialogResponseWithMessage(ActivityLoginSignup.this,true,msg);
    }

    @Override
    public void signupSuccess(Bundle userData) {
        Toast.makeText(this, "SIGN UP SUCCESS", Toast.LENGTH_SHORT).show();


        //String username = userData.getString(SOS_API.KEY_ACC_DATA_MOBILE);
        //String password = userData.getString(SOS_API.KEY_ACC_DATA_PASSWORD);


        //fireLoginIntent();

        Intent intent = new Intent(this, ActivityTutorial.class);
        intent.putExtra(User.KEY_DATA, userData);
        startActivity(intent);

        Log.e(TAG, "session data ->  " + SOSApplication.GI().getSosApi().GSV(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));
        //Log.e(TAG, "signupSuccess: " + userData.toString() );
    }



    @Override
    public void signupFailureUserExist() {
        alertDialogProcessing.hide();
        String msg =  HM.RGS(this, R.string.msgSignupFailedUserExists);
        sosApi.toggleAlertDialogResponseWithMessage(ActivityLoginSignup.this,true,msg);
    }

    @Override
    public void signupFailure(String message) {
        Log.e(TAG, "signupFailure: msg -> " + message );
        alertDialogProcessing.hide();
        String msg =  HM.RGS(this, R.string.msgSignupFailedMsg);
        sosApi.toggleAlertDialogResponseWithMessage(ActivityLoginSignup.this,true,msg);
    }

    @Override
    public void onJSONException(String message) {
        sosApi.TADRWM(this, true, message);
        alertDialogProcessing.hide();
        Log.e("UDT", "onJSONException: " + message );
    }

    @Override
    public void onNetworkError(String message) {
        sosApi.TADRWM(this, true, message);
        alertDialogProcessing.hide();
        Log.e("UDT", "onNetworkError: " + message );
    }
}
