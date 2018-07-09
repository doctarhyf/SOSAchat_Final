package com.koziengineering.rhyfdocta.sosachat.debug;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.koziengineering.rhyfdocta.sosachat.API.SOS_API;
import com.koziengineering.rhyfdocta.sosachat.ActivityLoginSignup;
import com.example.rhyfdocta.sosachat.R;
import com.koziengineering.rhyfdocta.sosachat.app.SOSApplication;

import java.util.Map;
import java.util.Set;

public class SOSDebug {

    private static final String TAG = "SDBG";
    public static boolean IS_DEBUG_MODE = true;


    public static void showDebugDialog(final Activity activity) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_debug, null);

        final EditText input = view.findViewById(R.id.dbgEtNewHost);
        final TextView tv = view.findViewById(R.id.tvSSV);
        final Button btnForceLogout = view.findViewById(R.id.btnForceLogout);


        String curIP = SOSApplication.getInstance().getSosApi().GSV(SOS_API.SERVER_ADD);
        builder.setTitle(curIP);
        input.setText(curIP);



        builder.setView(view);

        DEBUG_DG_LOAD_SESSION_DATA(tv);


// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nip = input.getText().toString();
                Log.e(TAG, "NEW IP: -> " + nip );
                SOSApplication.getInstance().getSosApi().SSV(SOS_API.SERVER_ADD, nip);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        btnForceLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SOSApplication.GI().getSosApi().clearSessionData();
                SOSApplication.GI().getSosApi().CSD();
                Context context = (Context) activity;
                Intent intent = new Intent(context, ActivityLoginSignup.class);
                context.startActivity(intent);
            }
        });

        builder.setCancelable(false);
        builder.setTitle("");
        builder.show();

    }

    public static void DEBUG_DG_LOAD_SESSION_DATA(TextView tv) {


        tv.setText("");
        tv.setText("SERVER ADD : " + SOSApplication.getInstance().getSosApi().GSV(SOS_API.SERVER_ADD) + "\n");

        Map<String,?> prefs = SOSApplication.getInstance().getSosApi().getPreferences().getAll();
        Set<String> keys = prefs.keySet();



        for(String key : keys){

            String str = key + " : " + prefs.get(key) + "\n";
            tv.append(str);
        }


    }

}
