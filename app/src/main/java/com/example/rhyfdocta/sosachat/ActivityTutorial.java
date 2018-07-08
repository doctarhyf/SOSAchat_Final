package com.example.rhyfdocta.sosachat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.rhyfdocta.sosachat.app.SOSApplication;

public class ActivityTutorial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);


    }

    public void showMainActivity(){

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }




    public void showMainActivity(View view) {
        showMainActivity();
    }

    @Override
    public void onBackPressed() {
        Log.e("XXX", "onBackPressed: " );
    }
}
