package com.koziengineering.rhyfdocta.sosachat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.koziengineering.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.R;

public class ActivityNoNetwork extends AppCompatActivity {

    private String TAG = "RETRY";
    //Button btnRetryConn;

    ProgressBar pbRetryConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);


        getSupportActionBar().setTitle(getResources().getString(R.string.msgNoNetworkTitle));

        pbRetryConn = findViewById(R.id.pbRetryNetwork);

    }


    public void onBtnRetryClicked(View view){

        pbRetryConn.setVisibility(View.VISIBLE);

        Log.e(TAG, "onBtnRetryClicked: " );

        if(SOS_API.isOnline(this) == true){


            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else {

            Toast.makeText(this, getResources().getString(R.string.msgStillNoConnection), Toast.LENGTH_SHORT).show();
            pbRetryConn.setVisibility(View.GONE);

        }



    }


}
