package com.koziengineering.rhyfdocta.sosachat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.rhyfdocta.sosachat.R;

public class ActivityTermsAndCond extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_cond);


        getSupportActionBar().setTitle(getResources().getString(R.string.titleTermsAndCondition));
        //getSupportActionBar().setSubtitle("Expose Item");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;


        }
        return super.onOptionsItemSelected(item);
    }
}
