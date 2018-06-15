package com.example.rhyfdocta.sosachat.app;

import android.app.Application;

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

    public static synchronized SOSApplication getInstance(){
        return mInstance;
    }

    public SOS_API getSosApi(){
        if(sosApi == null){
            sosApi = new SOS_API(getApplicationContext());
        }

        return sosApi;
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
