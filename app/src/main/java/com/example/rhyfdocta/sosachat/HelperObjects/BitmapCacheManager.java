package com.example.rhyfdocta.sosachat.HelperObjects;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.rhyfdocta.sosachat.API.SOS_API;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Franvanna on 1/21/2018.
 */

public class BitmapCacheManager {

    private static final String TAG = "EE";
    private static final int PIC_CACHE_PATH_TYPE_PRODUCTS = 200;

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

    public static void emptyDir(File dir){



        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                File file = new File(dir, children[i]);

                if(file.isDirectory()){
                    emptyDir(file);
                }else{
                    file.delete();
                }
            }
        }

    }

    public static double getImagesCacheSize() {
        /*File picsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
        + BitmapCacheManager.CACHE_ROOT_DIR);*/
        double cacheSize = BitmapCacheManager.folderSize(SOS_API.getSOSAchatRootDir());
        cacheSize /= 1000000f;


        return cacheSize;
    }

    public static long folderSize(File directory) {
        long length = 0;


    if(directory.exists()  ) {
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }

    }else{
        Log.e(TAG, "The dir at path -> " + directory.toString() + ", doesnt exist, cant count size!");
    }



        return length;
    }

    public static String getImageCachePath(int PIC_CACHE_PATH_TYPE, String imgName){

        String path = null;
        String dirName, localPath;

        switch (PIC_CACHE_PATH_TYPE){


            case PIC_CACHE_PATH_TYPE_RECENT_ITEMS:
                //path = "Tha fucking path";
                dirName = SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS;
                localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                //String imageFileName = imgName;
                //File storageDir = new File(localPath + "/" + CACHE_ROOT_DIR + "/" + dirName );

                path = localPath + "/" + CACHE_ROOT_DIR + "/" + dirName + "/" + imgName;

                break;

            case PIC_CACHE_PATH_TYPE_PROFILE_PIC:
                //path = "Tha fucking path";
                dirName = SOS_API.DIR_NAME_PIX_CACHE_PROFILCE_PIC;
                localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                //String imageFileName = imgName;
                //File storageDir = new File(localPath + "/" + CACHE_ROOT_DIR + "/" + dirName );

                path = localPath + "/" + CACHE_ROOT_DIR + "/" + dirName + "/" + imgName;

                break;
        }

            return path;
    }

    public static final int PIC_CACHE_PATH_TYPE_RECENT_ITEMS = 200;
    public static final int PIC_CACHE_PATH_TYPE_PROFILE_PIC = 201;

    public String saveCacheImage(Bitmap image, String imgName, String dirName) {

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

            // TODO: 5/17/2018 CHECK CACHE FILE REWRITE
            if(imageFile.exists()){
                return imageFile.toString();
            }

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

    public static Uri loadImageFromCacheOrNetwork(Uri picUri, String cachePath) {

        Uri uri = picUri;

        //String cachePath = BitmapCacheManager.getImageCachePath(BitmapCacheManager.PIC_CACHE_PATH_TYPE_RECENT_ITEMS, pd.getPdUniqueName() + "_main.jpg");
        if(BitmapCacheManager.FILE_EXISTS(cachePath)) {
            uri = Uri.fromFile(new File(cachePath));
        }
        /*

            Log.e(TAG, "PIC_PATH : -> " + picUri.toString() );

            //Toast.makeText(context, "Loade from cache", Toast.LENGTH_SHORT).show();

        }else{
            Log.e(TAG, "NO_CACHE -> " + picUri.toString() );
            //Toast.makeText(context, "Loade from network", Toast.LENGTH_SHORT).show();
        }*/

        return uri;

    }

    public String saveBitmapToCache(Bitmap bitmap, String picUrl, String dirName) {

        String[] splits = picUrl.split("/");
        //String dirName = SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS;
        String picName = splits[splits.length-1];
        return saveCacheImage(bitmap, picName,dirName);

    }
}
