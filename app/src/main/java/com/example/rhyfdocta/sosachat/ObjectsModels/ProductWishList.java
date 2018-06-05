package com.example.rhyfdocta.sosachat.ObjectsModels;

import android.os.Bundle;

import java.sql.Date;

/**
 * Created by rhyfdocta on 11/10/17.
 */

public class ProductWishList extends Product {


    public static final String KEY_DATE_ADDED = "pdDateAdded";


    public Date getDateAdded() {
        return dateAdded;
    }

    private Date dateAdded;

    public ProductWishList(String name, String price, String img, String cur, String cat, String qual, String desc, Date dateAdded){
        super(name, price, img, cur, cat, qual, desc, null);
        this.dateAdded = dateAdded;
    }

    public ProductWishList(){

    }

    public String getPdUniqueNameFromIMG(){
        return getPdImg().split("_")[0] + "_" + getPdImg().split("_")[1];
    }

    @Override
    public Bundle toBundle() {
        Bundle b = super.toBundle();
        b.putSerializable(KEY_DATE_ADDED, dateAdded);


        return b;
    }
}
