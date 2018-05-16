package com.example.rhyfdocta.sosachat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.HelperObjects.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.Inquiry;

public class ActivityInquiryView extends AppCompatActivity {

    private Inquiry inquiry;
    private TextView tvPosterName, tvPhone, tvEmail, tvDate, tvTitle, tvDesc;
    private Button btnContact;
    private ImageView ivPp;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inquiry_view);

        initGUI();

        Intent intent = getIntent();
        title = "No Inquiry Data";

        if(intent.getExtras() != null){

            Bundle data = intent.getExtras();
            inquiry = new Inquiry(data);

            updateGUI();


        }


        getSupportActionBar().setTitle("Looking for : " + title);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateGUI() {
        title  = inquiry.getTitle();
        tvPosterName.setText((String) inquiry.getProperty(Inquiry.KEY_POSTERNAME));
        tvPhone.setText((String)inquiry.getProperty(SOS_API.KEY_ACC_DATA_MOBILE));
        tvEmail.setText((String)inquiry.getProperty(SOS_API.KEY_ACC_DATA_EMAIL));
        // TODO: 5/16/2018 ADD STR TO RESOURCE
        tvTitle.setText(title);
        //tvDate.setText(inquiry.getDateTime());
        //tvDesc.setText(inquiry.getMessage());
    }

    private void initGUI() {
        tvPosterName = findViewById(R.id.tvInqPostername);
        tvPhone = findViewById(R.id.tvInqPosterPhone);
        tvEmail = findViewById(R.id.tvInqPosterEmail);
        tvDate = findViewById(R.id.tvInqDate);
        tvTitle = findViewById(R.id.tvInqTitle);
        ivPp = findViewById(R.id.ivInqPp);
        tvDate = findViewById(R.id.tvInqDate);
        btnContact = findViewById(R.id.btnInqContact);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}
