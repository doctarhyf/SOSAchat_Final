package com.example.rhyfdocta.sosachat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ActivityNoNetwork extends AppCompatActivity {

    private String TAG = "RETRY";
    //Button btnRetryConn;

    ProgressBar pbRetryConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);


        getSupportActionBar().setSubtitle(getResources().getString(R.string.msgNoNetworkTitle));

        pbRetryConn = (ProgressBar) findViewById(R.id.pbRetryNetwork);

    }


    public void onBtnRetryClicked(View view){

        pbRetryConn.setVisibility(View.VISIBLE);

        Log.e(TAG, "onBtnRetryClicked: " );

        if(isOnline() == true){


            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else {

            Toast.makeText(this, getResources().getString(R.string.msgStillNoConnection), Toast.LENGTH_SHORT).show();
            pbRetryConn.setVisibility(View.GONE);

        }



    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }else{
            return false;
        }
    }
}
