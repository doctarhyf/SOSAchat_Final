package com.example.rhyfdocta.sosachat.ObjectsModels;

import android.os.Bundle;

import com.example.rhyfdocta.sosachat.API.SOS_API;

import java.io.Serializable;

public class LookingFor implements Serializable {

    public static final String KEY_TITLE = "inq_title";
    public static final String KEY_DATETIME = "inq_date";
    public static final String KEY_DESC = "inq_desc";
    public static final String KEY_POSTERNAME = "user_display_name";
    public static final String KEY_INQUIRY = "inquiryData";
    public static final String KEY_INQUIRY_RATING = "inq_rating";
    public static final String KEY_PATH_PP = "path_pp";
    public static final Object NO_VALUE = "NO_VALUE";
    public static final String KEY_MTIME_PP = "mtime_pp";
    public static final String KEY_IS_MINE = "isMine";
    public static final String KEY_ID_LOOKINGFOR = "inq_id";
    private String title;
    private String message;
    private String dateTime;
    private String posterName;
    private Bundle data = new Bundle();

    public LookingFor(){

    }

    public LookingFor(Bundle data){
        this.data = data;
    }

    public Object getProperty(String key){
        return data.get(key);
    }

    public LookingFor(String posterName, String dateTime, String title, String message){
        this.message = message;
        this.title = title;
        this.dateTime = dateTime;
        this.posterName = posterName;

        data.putString(KEY_DESC, message);
        data.putString(KEY_TITLE, title);
        data.putString(KEY_DATETIME, dateTime);
        data.putString(KEY_POSTERNAME, posterName);
    }

    public String getTitle() {
        return data.getString(KEY_TITLE);
    }

    public void setTitle(String title) {
        this.title =  title;
        data.putString(KEY_TITLE, title);
    }

    public String getMessage() {
        return data.getString(KEY_DESC);
    }

    public void setMessage(String message) {
        this.message = message;
        data.putString(KEY_DESC, message);
    }

    public String getDateTime() {
        return data.getString(KEY_DATETIME);
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
        data.putString(KEY_DATETIME, dateTime);
    }

    public void setData(Bundle bundle){
        this.data.putAll(bundle);
    }

    public String getPosterName() {
        return data.getString(KEY_POSTERNAME);
    }

    public void setPosterName(String posterName) {
        this.posterName = posterName;
        data.putString(KEY_POSTERNAME, posterName);
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();

        bundle.putAll(data);


        return bundle;
    }

    public Object getValue(String KEY) {

        return data.get(KEY);
    }
}
