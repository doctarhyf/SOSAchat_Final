package com.example.rhyfdocta.sosachat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.HelperObjects.HM;
import com.example.rhyfdocta.sosachat.HelperObjects.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;

import org.json.JSONArray;

import java.util.List;

public class ActivityPostInquiry extends AppCompatActivity implements SOS_API.SOSApiListener {


    private static final String TAG = "ACT_INQ";
    EditText etInqTitle, etInqDesc;
    SOS_API sosApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry);

        sosApi = new SOS_API(this);

        getSupportActionBar().setTitle(HelperMethods.getStringResource(getBaseContext(),R.string.titleInquiry).toUpperCase());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etInqTitle = (EditText) findViewById(R.id.etInqTitle);
        etInqDesc = (EditText) findViewById(R.id.etInqDesc);
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

    public void onBtnPostInq(View view) {

        String title = etInqTitle.getText().toString();
        String desc = etInqDesc.getText().toString();

        if(!title.equals("") && !desc.equals("")) {
            sosApi.postInquiry(this, title, desc);
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

        if(result.equals(SOS_API.JSON_RESULT_SUCCESS)){
            Toast.makeText(this, HM.getStringResource(this, R.string.msgInquiryPostSuccess), Toast.LENGTH_SHORT).show();
            clearAllFields();
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
