package com.example.rhyfdocta.sosachat.app;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.rhyfdocta.sosachat.API.SOS_API;


/**
 * Created by Franvanna on 1/8/2018.
 */

public class SOSApplication extends Application {

    public static final String TAG = SOSApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private ProgressDialog progressDialog;

    private static SOSApplication mInstance;

    private SOS_API sosApi;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static String GSA(){
        return getInstance().getSosApi().GSA();
    }

    public static synchronized SOSApplication GI(){
        return getInstance();
    }

    public void dissmissProgressDialog(){
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public void DPD(){
        dissmissProgressDialog();
    }

    public static synchronized SOSApplication getInstance(){
        return mInstance;
    }

    public SOS_API getSosApi(){
        if(sosApi == null){
            sosApi = new SOS_API(getApplicationContext());
        }

        return sosApi;
    }

    public ProgressDialog GUPD(boolean forceContext, Context context, String title, String message){
        return getUndefinedProgressDialog(forceContext, context, title, message);
    }

    public ProgressDialog getUndefinedProgressDialog(boolean forceContext, Context context, String title, String message){


        if(progressDialog == null){
            progressDialog = new ProgressDialog(context);
        }

        if(forceContext){
            progressDialog = new ProgressDialog(context);
        }

        progressDialog.setCancelable(false);

        progressDialog.setTitle(title);

        if(message != null){
            progressDialog.setMessage(message);
        }



        return progressDialog;

    }



    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequest(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
