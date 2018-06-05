package com.example.rhyfdocta.sosachat.ObjectsModels;

import android.os.Bundle;

/**
 * Created by rhyfdocta on 11/10/17.
 */

public class ProductMyProducts extends ProductWishList {

    private String dateStr = "";

    public Bundle getDataBundle() {
        return dataBundle;
    }

    public void setDataBundle(Bundle dataBundle) {
        this.dataBundle = dataBundle;
    }

    private Bundle dataBundle;

    public ProductMyProducts(){

    }

    public ProductMyProducts(String name, String price, String img, String cur, String cat, String qual, String desc, String dateStr){
        super(name, price, img, cur, cat, qual, desc,null);
        this.setDateStr(dateStr);

    }



    public String getDateStr() {
        return dateStr;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
}
