package com.koziengineering.rhyfdocta.sosachat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.rhyfdocta.sosachat.R;

public class ActivityAccountActivation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_activation);

        getSupportActionBar().setTitle(getResources().getString(R.string.titleAccountActivation));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        if (item.getItemId() == android.R.id.home) {
            finish();
        }


        return true;

    }
}
