package com.example.rhyfdocta.sosachat;

import android.graphics.Bitmap;

import com.example.rhyfdocta.sosachat.ObjectsModels.Product;


    public interface GlideBitmapLoaderCallbacks {
        void onItemClicked(Product pd);
        void onBitmapShouldBeSaved(Bitmap bitmap, String picUrl);
    }

