package com.example.rhyfdocta.sosachat.ObjectsModels;

import android.os.Bundle;

public class User {

    public static final String COL_SEX = "user_sex";
    public static final String COL_SEX_M = "m";
    public static final String COL_SEX_F = "f";
    public static final String KEY_DATA = "data";
    private Bundle data;

    public User(){

    }

    public User(Bundle data){
        this.data = data;
    }

    public interface CallbacksNetwork{

    }
}
