package com.example.rhyfdocta.sosachat.ObjectsModels;

import android.os.Bundle;

/**
 * Created by rhyfdocta on 11/10/17.
 */

public class Product {


    public static final String KEY_PD_TYPE = "pdType";
    public static final String KEY_PD_DATE_ADDED = "pdDateAdded";
    public static final String KEY_PD_DATE_SOLD = "pdDateSold";
    public static final String KEY_PD_UNIQUE_ID = "un";

    public String getPdName() {
        return pdName;
    }

    public void setPdName(String pdName) {
        this.pdName = pdName;
    }

    public String getPdPrice() {
        return pdPrice;
    }

    public void setPdPrice(String pdPrice) {
        this.pdPrice = pdPrice;
    }

    public String getPdImg() {
        return pdImg;
    }

    public void setPdImg(String pdImg) {
        this.pdImg = pdImg;
    }



    public String getPdCur() {
        return pdCur;
    }

    public void setPdCur(String pdCur) {
        this.pdCur = pdCur;
    }

    public String getPdCat() {
        return pdCat;
    }

    public void setPdCat(String pdCat) {
        this.pdCat = pdCat;
    }

    private String pdName;
    private String pdPrice;
    private String pdImg;
    private String pdCur;
    private String pdCat;


    public Bundle getData() {
        return data;
    }

    public void setData(Bundle data) {
        this.data = data;
    }

    private Bundle data;

    public String getPdUniqueName() {
        return pdUniqueName;
    }

    public void setPdUniqueName(String pdUniqueName) {
        this.pdUniqueName = pdUniqueName;
    }

    private String pdUniqueName;

    public String getPdDesc() {
        return pdDesc;
    }

    public void setPdDesc(String pdDesc) {
        this.pdDesc = pdDesc;
    }

    private String pdDesc;

    public String getPdQual() {
        return pdQual;
    }

    public void setPdQual(String pdQual) {
        this.pdQual = pdQual;
    }

    private String pdQual;


    public static String KEY_PD_NAME = "pdName";
    public static String KEY_PD_PRICE = "pdPrice";
    public static String KEY_PD_IMG = "pdImg";
    public static String KEY_PD_CUR = "pdCur";
    public static String KEY_PD_CAT = "pdCat";
    public static String KEY_PD_DESC = "pdDesc";
    public static String KEY_PD_QUAL = "pdQual";
    public static String KEY_PD_PIC_URI = "pdUri";
    public static final String KEY_PD_OWNER_ID = "pdOwnerId";
    public static final String KEY_PD_UNIQUE_NAME = "pdUniqueName";

    public Product(String name, String price, String img, String cur, String cat, String qual, String desc, String pdUniqueName){
        this.pdName = name;
        this.pdPrice = price;
        this.pdImg = img;
        this.pdCur = cur;
        this.pdCat = cat;
        this.pdQual = qual;
        this.pdDesc = desc;
        this.pdUniqueName = pdUniqueName;
    }

    public Bundle toBundle(){
        Bundle b = new Bundle();

        b.putString(KEY_PD_NAME, pdName);
        b.putString(KEY_PD_PRICE, pdPrice);
        b.putString(KEY_PD_IMG, pdImg);
        b.putString(KEY_PD_CUR, pdCur);
        b.putString(KEY_PD_CAT, pdCat);
        b.putString(KEY_PD_QUAL, pdQual);
        b.putString(KEY_PD_DESC, pdDesc);
        b.putString(KEY_PD_UNIQUE_NAME, pdUniqueName);

        return b;
    }
}
