package com.example.rhyfdocta.sosachat.Helpers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.R;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Franvanna on 1/21/2018.
 */

public class BitmapCacheManager {

    private static final String TAG = "EE";

    public static final int PIC_CACHE_ROOT_PATH_ID_RECENT_ITEMS = 200;
    public static final int PIC_CACHE_ROOT_PATH_ID_PRODUCTS = 200;
    public static final int PIC_CACHE_ROOT_PATH_ID_PROFILE_PIC = 201;
    public static final int PIC_CACHE_ROOT_PATH_ID_ITEMS_CATEGORIES = 202;
    public static final int PIC_CACHE_ROOT_PATH_ID_ITEMS_TYPES_IN_CATEGORIES = 203;
    public static final int RES_ID_PROGRESS_ANIMATION = R.drawable.progress_animation;
    public static final int RES_ID_IMAGE_LOAD_ERROR = R.drawable.ic_no_photo;

    //public static final int PIC_CACHE_ROOT_PATH_ID_PRODUCTS = ;

    //public static final String CACHE_DIR_NAME_PRODUCTS = "products";
    private Context context;
    public static final String CACHE_ROOT_DIR = "SOSAchat";
    private String localPath;

    public BitmapCacheManager(Context context){
        this.context = context;
        localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    }

    public static final boolean FileExists(String pathName) {


        Log.e("EE", "DA_FILE -> " + pathName );
        File file = new File(pathName);

        return file.exists();
    }

    public static void EmptyDir(File dir){



        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                File file = new File(dir, children[i]);

                if(file.isDirectory()){
                    EmptyDir(file);
                }else{
                    file.delete();
                }
            }
        }

    }

    public static double GetImagesCacheSize() {
        /*File picsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
        + BitmapCacheManager.CACHE_ROOT_DIR);*/
        double cacheSize = BitmapCacheManager.DirectorySize(SOS_API.GetSOSAchatCacheRootDir());
        cacheSize /= 1000000f;


        return cacheSize;
    }

    public static long FileSize(File file){
        long length = -1;

        if(file.exists()){
            length = file.length();

        }else{
            Log.e("FSIZE", "FileSize: Error file can't be found -> " + file.toString() );
        }

        return length;
    }

    public static long DirectorySize(File directory) {
        long length = 0;


    if(directory.exists()  ) {
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += DirectorySize(file);
        }

    }else{
        Log.e(TAG, "The dir at path -> " + directory.toString() + ", doesnt exist, cant count size!");
    }



        return length;
    }

    public static String GetImageCachePath(int PIC_CACHE_PATH_TYPE, String imgFullName){

        String path = null;
        String dirName, localPath;

        switch (PIC_CACHE_PATH_TYPE){

            case PIC_CACHE_ROOT_PATH_ID_ITEMS_TYPES_IN_CATEGORIES:
                dirName = SOS_API.DIR_NAME_PIX_CACHE_HOME_TYPES_IN_CATS;
                localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                //String imageFileName = imgName;
                //File storageDir = new File(localPath + "/" + CACHE_ROOT_DIR + "/" + dirName );

                path = localPath + "/" + CACHE_ROOT_DIR + "/" + dirName + "/" + imgFullName;

                break;

            case PIC_CACHE_ROOT_PATH_ID_ITEMS_CATEGORIES:
                dirName = SOS_API.DIR_NAME_PIX_CACHE_HOME_CATS;
                localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                //String imageFileName = imgName;
                //File storageDir = new File(localPath + "/" + CACHE_ROOT_DIR + "/" + dirName );

                path = localPath + "/" + CACHE_ROOT_DIR + "/" + dirName + "/" + imgFullName;
                break;

            case PIC_CACHE_ROOT_PATH_ID_RECENT_ITEMS:
                //path = "Tha fucking path";
                dirName = SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS;
                localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                //String imageFileName = imgName;
                //File storageDir = new File(localPath + "/" + CACHE_ROOT_DIR + "/" + dirName );

                path = localPath + "/" + CACHE_ROOT_DIR + "/" + dirName + "/" + imgFullName;

                break;

            case PIC_CACHE_ROOT_PATH_ID_PROFILE_PIC:
                //path = "Tha fucking path";
                dirName = SOS_API.DIR_NAME_PIX_CACHE_PROFILCE_PIC;
                localPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                //String imageFileName = imgName;
                //File storageDir = new File(localPath + "/" + CACHE_ROOT_DIR + "/" + dirName );

                path = localPath + "/" + CACHE_ROOT_DIR + "/" + dirName + "/" + imgFullName;

                break;
        }

            return path;
    }

    private String saveCacheImage(Bitmap image, String imgName, String dirName) {

        //iv.setImageBitmap(image);

        String savedImagePath = null;

        String imageFileName = imgName;
        File storageDir = new File(localPath + "/" + CACHE_ROOT_DIR + "/" + dirName );

        String targetFilePath = storageDir.getName() + "/" + dirName + "/" + imgName ;

        boolean fileExists = FileExists(targetFilePath);

        if (fileExists){
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

        //String cachePath = BitmapCacheManager.GetImageCachePath(BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_RECENT_ITEMS, pd.getPdUniqueName() + "_main.jpg");
        if(BitmapCacheManager.FileExists(cachePath)) {
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

    public static boolean RemoveFile(String fileCache) {

        boolean res = false;

        if(FileExists(fileCache)){
            File file = new File(fileCache);
            res = file.delete();
        }

        return res;
    }

    public static boolean DeleteCacheFile(int cacheRootPathID, String fileName) {

        boolean res = false;

        String path = GetImageCachePath(cacheRootPathID, fileName);

        File file = new File(path);

        if(file.exists()) {
            res = file.delete();
        }

        return res;
    }

    public static boolean SaveBitmapToCache(Bitmap bitmap, int cacheRootPathID, String fileName) {

        boolean res = false;

        String path = GetImageCachePath(cacheRootPathID, fileName);

        Log.e(TAG, "SaveBitmapToCache: " + path );
        File file = new File(path);

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(fileName);

            res = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try{
                if(out != null){
                    out.flush();
                    out.close();

                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return res;


    }

    public static void LoadBitmapIntoImageView(final ImageView iv, final Bitmap bmp) {

        final int[] intrinsicSize = new int[]{0,0};

        iv.post(new Runnable() {
            @Override
            public void run() {

                intrinsicSize[0] = iv.getMeasuredWidth();
                intrinsicSize[1] = iv.getMeasuredHeight();
                Bitmap b = HM.DSBFBD(bmp, intrinsicSize);

                iv.setImageBitmap(b);

            }
        });
    }

    public static void LoadBitmapFilePathIntoImageView(final ImageView iv, final String picturePath) {

        final int[] intrinsicSize = new int[]{0,0};

        iv.post(new Runnable() {
            @Override
            public void run() {

                intrinsicSize[0] = iv.getMeasuredWidth();
                intrinsicSize[1] = iv.getMeasuredHeight();
                Bitmap b = HM.DSBFF(picturePath, intrinsicSize);

                iv.setImageBitmap(b);

            }
        });
    }

    public interface CallbacksBitmapLoading {
        void onItemClicked(Product pd);
        void saveBitmapToLocalCache(Bitmap bitmap, String picUrl, String dirName);
    }

    public static void GlideUniversalLoaderLoadPathIntoImageView(Context context, final String path, long remoteMTime, final String fileName, final int PIC_CACHE_ROOT_PATH_ID, final String DIR_NAME_PIX_CACHE, final ImageView iv, final CallbacksBitmapLoading callbacks) {

        Uri picUri = Uri.parse(path);
        boolean shouldUpdateCache = false;
        //remoteMTime *= 1000;

        String cachePath = BitmapCacheManager.GetImageCachePath(PIC_CACHE_ROOT_PATH_ID, fileName);
        if(BitmapCacheManager.FileExists(cachePath)) {

            File cacheFile = new File(cachePath);
            long cacheMTime = cacheFile.lastModified();


                Date dateCache = new Date(cacheMTime);
                Date dateRemote = new Date(remoteMTime * 1000);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z");
                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT+2"));
                String formattedDateCache = simpleDateFormat.format(dateCache);
                String formattedDateRemote = simpleDateFormat.format(dateRemote);

                Log.e("FAAKK", "REMOTE DATE : " + formattedDateRemote );
                Log.e("FAAKK", "CACHE DATE : " + formattedDateCache );
                long diff = (dateRemote.getTime() - dateCache.getTime());
                shouldUpdateCache =  diff > 0;
                Log.e("FAAKK", "REM - CACHE : " +  diff);


                if(shouldUpdateCache) {
                    BitmapCacheManager.DeleteCacheFile(PIC_CACHE_ROOT_PATH_ID,fileName);
                }else{
                    picUri = Uri.fromFile(new File(cachePath));
                }

            Log.e(TAG, "GlideUniversalLoaderLoadPathIntoImageView: " );


            //Log.e("FAAKK", "REMOTE - CACHE " + (remoteMTime - cacheMTime) );



        }

        final String finalPath = picUri.toString();

        Glide.with(context)
                .load(picUri)
                .asBitmap()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_error)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>(450,450) {

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        iv.setImageResource(BitmapCacheManager.RES_ID_PROGRESS_ANIMATION);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);

                        iv.setEnabled(false);
                        iv.setImageResource(BitmapCacheManager.RES_ID_IMAGE_LOAD_ERROR);
                        e.printStackTrace();
                        Log.e("LERR", "onLoadFailed: -> " + e.getMessage() + ", url : " + finalPath );
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        callbacks.saveBitmapToLocalCache(resource, path, DIR_NAME_PIX_CACHE);

                        iv.setEnabled(true);
                        iv.setImageBitmap(resource);
                    }
                });
    }
}
