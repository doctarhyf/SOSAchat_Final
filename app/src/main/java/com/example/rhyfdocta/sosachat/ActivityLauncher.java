package com.example.rhyfdocta.sosachat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ActivityLauncher extends AppCompatActivity {

    private Thread th;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);




         th = new Thread(){

             @Override
             public void run() {


                 try {
                     sleep(1000);
                 } catch (InterruptedException e1) {
                     e1.printStackTrace();
                 }

                 Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                 startActivity(intent);
                 finish();
             }

        };


        th.start();

    }

}
