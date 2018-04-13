package com.example.rhyfdocta.sosachat.ObjectsModels;

import android.os.Bundle;
import android.util.Log;

import com.example.rhyfdocta.sosachat.API.SOS_API;

/**
 * Created by Franvanna on 12/22/2017.
 */

public class HomeCategoryItem {


    public static final String KEY_HOME_CAT_TITLE = "title";
    public static final String KEY_HOME_CAT_IMAGE = "img";
    public static final String KEY_HOME_CAT_ID = "id";
    public static String TAG = "HOME_CAT";
    private String title;
    private String image;
    private String catId;
    private Bundle data;

    public Bundle toBundle(){
        Bundle bundle = new Bundle();

        bundle.putString(KEY_HOME_CAT_TITLE, this.title);
        bundle.putString(KEY_HOME_CAT_IMAGE, this.image);
        bundle.putString(KEY_HOME_CAT_ID, this.catId);
        bundle.putAll(this.data);

        return bundle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public HomeCategoryItem(String catId, String title, String image){
        this.title = title;
        this.image = image;
        this.catId = catId;

        this.image = SOS_API.DIR_PATH_CAT_PIX + this.image + ".jpg";

        Log.e(TAG, "HomeCategoryItem: imgpath -> " + this.image );
    }

    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }
}
