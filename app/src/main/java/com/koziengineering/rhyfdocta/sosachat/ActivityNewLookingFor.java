package com.koziengineering.rhyfdocta.sosachat;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.koziengineering.rhyfdocta.sosachat.API.NETWORK_RESULT_CODES;
import com.koziengineering.rhyfdocta.sosachat.API.SOS_API;
import com.koziengineering.rhyfdocta.sosachat.Helpers.HM;
import com.koziengineering.rhyfdocta.sosachat.Helpers.HelperMethods;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.LookingFor;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.Product;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.R;

import org.json.JSONArray;

import java.util.List;

public class ActivityNewLookingFor extends AppCompatActivity implements SOS_API.SOSApiListener {


    private static final String TAG = "ACT_INQ";
    EditText etInqTitle, etInqDesc;
    SOS_API sosApi;
    private Button btnNew;
    private AlertDialog dialogProcessing;
    private Bundle data = null;
    private LookingFor lookingFor = null;
    private RatingBar rbInqPriority;
    private boolean editing = false;
    private Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_looking_for);


        btnNew = findViewById(R.id.btnNewLooking4);
        btnUpdate = findViewById(R.id.btnUpdateLooking4);

        sosApi = new SOS_API(this);

        dialogProcessing = HM.GADP(this, getResources().getString(R.string.processing), false);

        getSupportActionBar().setTitle(HelperMethods.getStringResource(getBaseContext(),R.string.titleLookingFor).toUpperCase());
        getSupportActionBar().setSubtitle(HelperMethods.getStringResource(getBaseContext(),R.string.stLookingFor));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etInqTitle = findViewById(R.id.etInqTitle);
        etInqDesc = findViewById(R.id.etInqDesc);
        rbInqPriority = findViewById(R.id.rbInqPriority);

        data = getIntent().getExtras();

        if(data != null){
            editing = data.getBoolean(LookingFor.KEY_EDITING, false);

            if(editing){
                setupEditingData();
            }
        }
    }

    private void setupEditingData() {
        lookingFor = new LookingFor(data);

        etInqTitle.setText((String) lookingFor.getProperty(LookingFor.KEY_TITLE));
        etInqDesc.setText((String) lookingFor.getProperty(LookingFor.KEY_DESC));
        rbInqPriority.setRating(Float.parseFloat((String) lookingFor.getProperty(LookingFor.KEY_PRIORITY)));

        btnUpdate.setVisibility(View.GONE);
        btnUpdate.setVisibility(View.VISIBLE);



    }

    public void onBtnUpdateLooking4Clicked(View view){

        String title = etInqTitle.getText().toString();
        String desc = etInqDesc.getText().toString();
        String updid = data.getString(LookingFor.KEY_ID_LOOKINGFOR);
        float rating = rbInqPriority.getRating();

        sosApi.updateLooking4(
                new SOS_API.CallbacksLookingFor() {
                    @Override
                    public void onDeleteLookingSuccess() {

                    }

                    @Override
                    public void onDeleteLookingForFailure(String message) {

                    }

                    @Override
                    public void onUpdateLookingForResult(int code, String data) {

                        if(NETWORK_RESULT_CODES.RESULT_CODE_SUCCESS == code){

                            Intent intent = new Intent(ActivityNewLookingFor.this, ActivityLookingFor.class);
                            startActivity(intent);

                        }else{
                            Toast.makeText(ActivityNewLookingFor.this, "Error updating looking for!", Toast.LENGTH_SHORT).show();
                        }

                    }
                },
                title,
                desc,
                rating,
                updid

        );

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }


        return super.onOptionsItemSelected(item);
    }

    public void onBtnNewLooking4Clicked(View view) {

        String title = etInqTitle.getText().toString();
        String desc = etInqDesc.getText().toString();

        if(!title.equals("") && !desc.equals("")) {
            RatingBar rb = findViewById(R.id.rbInqPriority);
            float rating = rb.getRating();
            sosApi.postLooking4(this, title, desc, rating);
            //Button btnNew = (Button) view;
            btnNew.setEnabled(false);

           dialogProcessing.show();

        }else{
            Toast.makeText(this, HM.getStringResource(this, R.string.msgInquiryEmptyFields), Toast.LENGTH_SHORT).show();
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

        btnNew.setEnabled(true);
        dialogProcessing.hide();

        if(result.equals(SOS_API.JSON_RESULT_SUCCESS)){
            Toast.makeText(this, HM.getStringResource(this, R.string.msgInquiryPostSuccess), Toast.LENGTH_SHORT).show();
            clearAllFields();
            Intent intent = new Intent(this, ActivityLookingFor.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, HM.getStringResource(this, R.string.msgInquiryPostError), Toast.LENGTH_SHORT).show();
        }

    }

    private void clearAllFields() {
        etInqDesc.setText("");
        etInqTitle.setText("");
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
}
