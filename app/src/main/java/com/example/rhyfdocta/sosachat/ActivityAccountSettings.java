package com.example.rhyfdocta.sosachat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.HelperObjects.HM;
import com.example.rhyfdocta.sosachat.HelperObjects.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;

import org.json.JSONArray;

import java.util.List;

public class ActivityAccountSettings extends AppCompatActivity implements SOS_API.SOSApiListener, View.OnKeyListener {

    private static final String TAG = "DBG";
    Resources res;

    TextView tvAccSetFullName, tvAccSetEmail,tvAccSetMobile,tvAccSetCompany,tvAccSetLocation;
    EditText etOldPassword, etNewPassword, etReNewPassword;
    CheckBox cbShowMyEmail, cbShowMyMobile, cbShowMyAddress;
    Button btnDelMyAccount, btnUpdateMyPassword;
    SOS_API sosApi;
    String sessionPwd;
    Switch swAutorefreshRecentItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        sosApi = new SOS_API(this);
        res = getResources();

        getSupportActionBar().setTitle(res.getString(R.string.accSettings));
        getSupportActionBar().setSubtitle("");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prepareGUI();
        loadAccData();

        sessionPwd = sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_PASSWORD);


    }

    private void prepareGUI() {

        swAutorefreshRecentItems = findViewById(R.id.swAutorefreshRecentItems);

        boolean autorefresh = sosApi.GSV(SOS_API.KEY_AUTOREFRESH_RECENT_ITEMS).equals("true");

        if(autorefresh) {
            swAutorefreshRecentItems.setChecked(true);
        }

        tvAccSetFullName = (TextView) findViewById(R.id.accSetFullName);
        tvAccSetEmail = (TextView) findViewById(R.id.accSetEmail);
        tvAccSetCompany = (TextView) findViewById(R.id.accSetCompany);
        tvAccSetMobile = (TextView) findViewById(R.id.accSetMobile);
        tvAccSetLocation = (TextView) findViewById(R.id.accSetLocation);

        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etNewPassword = (EditText) findViewById(R.id.etNewPassword);
        etReNewPassword = (EditText) findViewById(R.id.etReNewPassword);

        cbShowMyEmail = (CheckBox) findViewById(R.id.cbShowMyEmail);
        cbShowMyMobile = (CheckBox) findViewById(R.id.cbShowMyMobile);
        cbShowMyAddress = (CheckBox) findViewById(R.id.cbShowMyAddress);

        btnDelMyAccount = (Button) findViewById(R.id.btnDelMyAcc);
        btnUpdateMyPassword = (Button) findViewById(R.id.btnUpdateMyPassword);

        llPasswordMismatch = (LinearLayout) findViewById(R.id.llPasswordsMismatch);

        etNewPassword.setOnKeyListener(this);
        etReNewPassword.setOnKeyListener(this);
        etOldPassword.setOnKeyListener(this);

        swAutorefreshRecentItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean on = swAutorefreshRecentItems.isChecked();
                String ar = on ? "true" : "false";

                    sosApi.SSV(SOS_API.KEY_AUTOREFRESH_RECENT_ITEMS, ar);

            }
        });


    }

    private void loadAccData() {


        tvAccSetFullName.setText(sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_FULL_NAME));
        tvAccSetEmail.setText(sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_EMAIL));
        tvAccSetMobile.setText(sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_MOBILE));
        tvAccSetLocation.setText(sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_LOCATION));
        tvAccSetCompany.setText(sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_COMPANY));

        toggleCheckBox(cbShowMyEmail, sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_SHOW_MY_EMAIL).equals("1"));
        toggleCheckBox(cbShowMyMobile, sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_SHOW_MY_MOBILE).equals("1"));
        toggleCheckBox(cbShowMyAddress, sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_SHOW_MY_ADDRESS).equals("1"));



    }

    private void toggleCheckBox(CheckBox cb, boolean aTrue) {
        cb.setChecked(aTrue);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }



        return super.onOptionsItemSelected(item);
    }

    public void onSetDataClicked(View view) {

        final TextView curTextView = (TextView) view;
        String title = "Change " + view.getTag().toString();

        final String[] m_Text = {""};
        //Toast.makeText(this, "SET DATA CLICKED", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title.toUpperCase());

// Set up the input
        final EditText input = new EditText(this);
        input.setText(curTextView.getText().toString());
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT);// | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text[0] = input.getText().toString();
                //Toast.makeText(getApplicationContext(), "m_text : " + m_Text[0], Toast.LENGTH_SHORT).show();
                curTextView.setText(m_Text[0]);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();


    }

    public void onDelMyAccountButtonClicked(View view) {
        //Toast.makeText(this, "Account will be deleted", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.dgTitleDeleteAccount))
                .setMessage(HelperMethods.getStringResource(getApplication(),R.string.dgMsgDeleteAccount))
                .setNegativeButton(getResources().getString(R.string.btnCancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(getResources().getString(R.string.btnYes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        deleteMyAccount();
                        //Log.e(TAG, "ON POSITIVE BTN " );

                    }
                })
                ;

        builder.show();
    }

    private void deleteMyAccount() {

        //Log.e(TAG, "deleteMyAccount: called accdata -> :" + accData.toString() );
        SOS_API.deleAccount(this,this,sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_MOBILE));
    }

    public void onUpdateMyPassword(View view) {


        sosApi.updatePassWord(this, etNewPassword.getText().toString());


    }

    public void onCbClicked(View view) {

        int cbId = view.getId();
        boolean checked = ((CheckBox) view).isChecked();

        String val = checked == true ? "1" : "0";

        //Toast.makeText(this, "Cb Clicked", Toast.LENGTH_SHORT).show();

        switch (cbId){
            case R.id.cbShowMyEmail:


                sosApi.updateAccSettings(this, SOS_API.KEY_ACC_DATA_SHOW_MY_EMAIL, val);

                break;

            case R.id.cbShowMyAddress:
                sosApi.updateAccSettings(this,SOS_API.KEY_ACC_DATA_SHOW_MY_ADDRESS, val);
                break;

            case R.id.cbShowMyMobile:
                sosApi.updateAccSettings(this, SOS_API.KEY_ACC_DATA_SHOW_MY_MOBILE, val);
                break;
        }
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
        //Log.e(TAG, "onAccountDeleteResult: data ->" + data.toString() );


        boolean result = data.getString(SOS_API.JSON_KEY_RESULT).equalsIgnoreCase(SOS_API.JSON_RESULT_SUCCESS);

        if(result == true){
            String text = HelperMethods.getStringResource(this, R.string.msgAccoutDeletedSuccess);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            sosApi.logout();
        }else{
            String text = HelperMethods.getStringResource(this, R.string.msgAccoutDeletedFailure);
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLogoutResult() {
        Intent intent = new Intent(this, ActivityLoginSignup.class);
        startActivity(intent);
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

        if(resp.equals(SOS_API.JSON_RESULT_SUCCESS)){
            Toast.makeText(this, HM.getStringResource(this, R.string.msgPasswordUpdateSuccess), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, HM.getStringResource(this, R.string.msgPasswordUpdateFailed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpdateSettingsResult(String settingKey, String result) {

        if(result.equals(SOS_API.JSON_RESULT_SUCCESS)) {
            Toast.makeText(this, HelperMethods.getStringResource(this, R.string.msgSettingsChange), Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, HelperMethods.getStringResource(this, R.string.msgSettingsChangeError), Toast.LENGTH_SHORT).show();
        }
        //Log.e(TAG, "onUpdateSettingsResult: res - > " + result );
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

    LinearLayout llPasswordMismatch;

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {





        if(event.getAction() == KeyEvent.ACTION_UP){

            String nwPwd = etNewPassword.getText().toString();
            String reNwPwd = etReNewPassword.getText().toString();

            boolean nwPwdNotEqualsOldPwd = !nwPwd.equals(sessionPwd);
            boolean nwPwdEqualsReNwPwd = nwPwd.equals(reNwPwd);
            boolean nwPwdLenOk = nwPwd.length() >= 6 && nwPwd.length() <= 8;
            boolean nwPwdContainsCharsAndLetters = true;
            boolean oldPwdNotEmpty = !etOldPassword.getText().toString().equals("");
            boolean oldPwdEqualsSessionPwd = etOldPassword.getText().toString().equals(sessionPwd);




               if(oldPwdEqualsSessionPwd && nwPwdEqualsReNwPwd && nwPwdNotEqualsOldPwd && nwPwdLenOk && nwPwdContainsCharsAndLetters && oldPwdNotEmpty){
                   btnUpdateMyPassword.setEnabled(true);
                   llPasswordMismatch.setVisibility(View.GONE);
               }else{
                   btnUpdateMyPassword.setEnabled(false);
                   llPasswordMismatch.setVisibility(View.VISIBLE);
               }



        }

        return false;
    }

    public void idleClick(View view) {
        Log.e(TAG, "idleClick: " );
    }
}
