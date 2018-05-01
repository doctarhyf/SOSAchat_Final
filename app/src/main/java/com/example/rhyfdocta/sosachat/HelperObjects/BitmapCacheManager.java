package com.example.rhyfdocta.sosachat.HelperObjects;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.rhyfdocta.sosachat.API.SOS_API;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Franvanna on 1/21/2018.
 */

public class BitmapCacheManager {

    //public static final String CACHE_DIR_NAME_PRODUCTS = "products";
    private Context context;
    public static final String CACHE_ROOT_DIR = "SOSAchat";
    private String localPath;

    public BitmapCacheManager(Context context){
        this.context = context;
        localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }


    public static final boolean FILE_EXISTS(String pathName) {


        Log.e("EE", "DA_FILE -> " + pathName );
        File file = new File(pathName);

        return file.exists();
    }

    public static String GET_PIC_CACHE_PATH(int PIC_CACHE_PATH_TYPE, String imgName){

        String path = null;

        switch (PIC_CACHE_PATH_TYPE){
            case PIC_CACHE_PATH_TYPE_RECENT_ITEMS:
                //path = "Tha fucking path";
                String dirName = SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS;
                String localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                //String imageFileName = imgName;
                //File storageDir = new File(localPath + "/" + CACHE_ROOT_DIR + "/" + dirName );

                path = localPath + "/" + CACHE_ROOT_DIR + "/" + dirName + "/" + imgName;

                break;
        }

            return path;
    }

    public static final int PIC_CACHE_PATH_TYPE_RECENT_ITEMS = 200;

    public String saveImage(Bitmap image, String imgName, String dirName) {

        //iv.setImageBitmap(image);

        String savedImagePath = null;

        String imageFileName = imgName;
        File storageDir = new File(localPath + "/" + CACHE_ROOT_DIR + "/" + dirName );

        String targetFilePath = storageDir.getName() + "/" + dirName + "/" + imgName ;

        if (FILE_EXISTS(targetFilePath)){
            return targetFilePath;
        }

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            //Toast.makeText(context, "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
