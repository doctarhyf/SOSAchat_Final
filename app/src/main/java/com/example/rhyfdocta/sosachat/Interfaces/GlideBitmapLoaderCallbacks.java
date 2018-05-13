package com.example.rhyfdocta.sosachat.Interfaces;

import android.graphics.Bitmap;

import com.example.rhyfdocta.sosachat.ObjectsModels.Product;


    public interface GlideBitmapLoaderCallbacks {
        void onItemClicked(Product pd);
        void saveBitmapToLocalCache(Bitmap bitmap, String picUrl, String dirName);
    }

