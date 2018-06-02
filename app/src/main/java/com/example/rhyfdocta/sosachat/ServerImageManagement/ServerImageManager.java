package com.example.rhyfdocta.sosachat.ServerImageManagement;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.Helpers.HelperMethods;
import com.example.rhyfdocta.sosachat.Helpers.UploadAsyncTask;
import com.example.rhyfdocta.sosachat.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerImageManager {


    public static final String KEY_REQ_SERVER_FILE_NAME = "sfn";
    public static final String KEY_REQ_REL_ROOT_DIR = "relRootDir";

    private String serverRootPath = "img/products/";
    private int numImages = 0;
    private Context context;
    private List<ServerImage> serverImages = new ArrayList<>();
    private HashMap<Integer, String> imagesIDsAndPostfix = new HashMap<>();
    private int[] imageViewsIDS;
    private boolean editing = false;
    private String[] imagesPostfixes;
    private SOS_API sosApi;


    public static final int KEY_GET_IMAGE_BY_POSTFIX = 0;
    public static final int KEY_GET_IMAGE_BY_IMAGEVIEWS_IDS = 1;
    public static final int KEY_GET_IMAGE_BY_IMAGEGALLERY_PRIORITY_ID = 2;
    public static final int KEY_MANIFEST_IMAGES_LOADED = 1;
    public static final int KEY_MANIFEST_IMAGES_UPLOADED = 2;
    public static final int KEY_MANIFEST_IMAGES_EDITED = 3;
    public static final String BUNDLE_KEY = "key";

    //private int totalUploadProgress = 0;
    private int[] imagesProgress;
    private String uploadToken = null;
    private int totalProgress = 0;
    //private long totalProgress = 0;


    public ServerImageManager(Context context, int[] imageViewsIDS, String[] imagesPostfixes, int numImages, String serverRootPath){
        this.context = context;
        this.numImages = numImages;
        this.imageViewsIDS = imageViewsIDS;
        this.imagesPostfixes = imagesPostfixes;
        this.serverRootPath = serverRootPath;
        this.sosApi = new SOS_API(context);
        imagesProgress = new int[numImages];


        resetImagesProgressArray();

        try {
            createProductImages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setImageLoaded(int imageViewID, String imageLocalPath){
        //ServerImage productImage = null;
        boolean set = false;

        for(int i = 0; i < serverImages.size(); i++){
            ServerImage pi = serverImages.get(i);

            if(pi.getImageViewID() == imageViewID){
                pi.setImageLoaded(true, imageLocalPath);
                set = true;
            }
        }

        return set;

    }

    public boolean setImageEdited(int imageViewID, String imageLocalPath){
        //ServerImage productImage = null;
        boolean set = false;

        for(int i = 0; i < serverImages.size(); i++){
            ServerImage pi = serverImages.get(i);

            if(pi.getImageViewID() == imageViewID){
                pi.setEditedLocalPath( imageLocalPath);
                set = true;
            }
        }

        return set;

    }

    private void createProductImages() throws Exception {

        if(imageViewsIDS.length != numImages || imagesPostfixes.length != numImages){
            throw new Exception("Number of [imageViewsIDS not equal to numImages] => [\"" + imageViewsIDS.length  + " : \"" + numImages + "\" ] ");

        }else {

            for (int i = 0; i < numImages; i++) {
                ServerImage serverImage = new ServerImage(context, i, serverRootPath, BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_PRODUCTS);
                String postfix = imagesPostfixes[i];
                int ivid = imageViewsIDS[i];
                imagesIDsAndPostfix.put(ivid, postfix);

                serverImage.setImagePostfix(postfix);
                serverImage.setImageViewID(ivid);
                serverImages.add(serverImage);

            }
        }
    }

    public ServerImage getServerImageByKey(final int KEY_GET_IMAGE_BY, Bundle value){

        ServerImage serverImage = null;


        switch (KEY_GET_IMAGE_BY){
            case KEY_GET_IMAGE_BY_IMAGEVIEWS_IDS:

                int ivID = value.getInt(BUNDLE_KEY);
                for (int i = 0; i < numImages; i++) {
                    ServerImage tmppi = serverImages.get(i);
                    if(tmppi.getImageViewID() == ivID){
                        serverImage = tmppi;
                    }
                }
                break;

            case KEY_GET_IMAGE_BY_IMAGEGALLERY_PRIORITY_ID:

                int galPriorID = value.getInt(BUNDLE_KEY);
                serverImage = serverImages.get(galPriorID);
                break;

            case KEY_GET_IMAGE_BY_POSTFIX:
                String postfix = value.getString(BUNDLE_KEY);
                int id = -1;
                int i = 0;
                for (String k : imagesPostfixes) {

                    if(k.equals(postfix)) id = i;
                    i ++;

                }
                serverImage = serverImages.get(id);
                break;
        }


        return serverImage;

    }

    public boolean[] getManifestImages(int KEY_MANIFEST){
        boolean[] manifest = new boolean[numImages];
        HM.IBWAV(manifest, false);

        for(int i = 0; i < numImages; i++){
            ServerImage pi = serverImages.get(i);
            switch (KEY_MANIFEST){
                case KEY_MANIFEST_IMAGES_LOADED:
                    manifest[i] = pi.isImageLoaded();
                    break;

                case KEY_MANIFEST_IMAGES_UPLOADED:
                    manifest[i] = pi.getImageUploaded();
                    break;

                case KEY_MANIFEST_IMAGES_EDITED:
                    manifest[i] = pi.isImageEdited();
                    break;
            }
        }

        return manifest;
    }

    @Override
    public String toString() {

        String status = "\n========== PRODUCT IMAGES MANAGER ==========\n";
        status = status.concat("NUM PICS LOADED : " + numImagesLoaded() + "\n");
        status = status.concat("SERVER ROOT PATH : " + serverRootPath + "\n");
        status = status.concat("MANIFEST LOADED : " + HM.BATOS(getManifestImages(KEY_MANIFEST_IMAGES_LOADED)) + "\n");
        status = status.concat("NUM PICS UPLOADED : " + numImagesUploaded() + "\n");
        status = status.concat("MANIFEST UPLOADED : " + HM.BATOS(getManifestImages(KEY_MANIFEST_IMAGES_UPLOADED)) + "\n");
        status = status.concat("NUM PICS EDITED : " + numImagesEdited() + "\n");
        status = status.concat("MANIFEST EDITED : " + HM.BATOS(getManifestImages(KEY_MANIFEST_IMAGES_EDITED)) + "\n");
        status = status.concat("NUM TOTAL PICS : " + serverImages.size() + "\n");
        status = status.concat("IMGS TOTAL SIZE : " + imagesTotalSize() + " byte(s)\n");
        status = status.concat("===================================");

        return status;
    }

    private int numImagesEdited() {
        int imgsEdited = 0;

        for(int i = 0; i < serverImages.size(); i++){
            ServerImage serverImage = serverImages.get(i);
            if(serverImage.isImageEdited()) imgsEdited++;
        }

        return imgsEdited;
    }

    public String toStringAllProducImages(){

        String s = serverImages.get(0).toString() + "\n\n";

        for(int i = 1; i < serverImages.size(); i++){
            s = s.concat(serverImages.get(i).toString() + "\n\n");
        }

        return s;
    }

    private int numImagesLoaded() {
        int imgsLoaded = 0;

        for(int i = 0; i < serverImages.size(); i++){
            ServerImage serverImage = serverImages.get(i);
            if(serverImage.isImageLoaded()) imgsLoaded++;
        }

        return imgsLoaded;
    }

    private int numImagesUploaded() {
        int imgsUploaded = 0;

        for(int i = 0; i < serverImages.size(); i++){
            ServerImage serverImage = serverImages.get(i);
            if(serverImage.getImageUploaded()) imgsUploaded++;
        }

        return imgsUploaded;
    }

    public boolean isImageLoaded(int imageViewID) {
        Bundle b = new Bundle();
        b.putInt(BUNDLE_KEY, imageViewID);
        ServerImage serverImage = getServerImageByKey(KEY_GET_IMAGE_BY_IMAGEVIEWS_IDS, b);

        return serverImage.isImageLoaded();
    }

    public boolean isImageEdited(int imageViewID) {
        Bundle b = new Bundle();
        b.putInt(BUNDLE_KEY, imageViewID);
        ServerImage serverImage = getServerImageByKey(KEY_GET_IMAGE_BY_IMAGEVIEWS_IDS, b);

        return serverImage.isImageEdited();
    }

    public void uploadAllImagesToServer(final Callbacks callbacks, boolean editiing) {

        if(uploadToken == null) return;

        this.editing = editiing;
        resetImagesProgressArray();



        boolean[] indexesImagesLoaded = getManifestImages(KEY_MANIFEST_IMAGES_LOADED);
        boolean[] indexesImagesEdited = getManifestImages(KEY_MANIFEST_IMAGES_EDITED);


        if(editiing){
            Log.e("BOOM", "UPLOADING EDITED IMAGES: -> " + HM.BATOS(indexesImagesEdited) );
        }else{
            Log.e("BOOM", "UPLOADING LOADED IMAGES: -> " + HM.BATOS(indexesImagesLoaded) );
        }

        if(numImagesEdited() == 0 && editiing){
            callbacks.onProducImageAllImagesUploadedComplete();
            return;
        }


        for(int i = 0; i < serverImages.size(); i++) {

                ServerImage pi = serverImages.get(i);
                String serverFileName = pi.getFileNameOnServer();
                String localPath;

                if(editiing && pi.isImageEdited()){

                    localPath = pi.getEditedLocalPath();
                    uploadImageFile(context, callbacks, localPath, serverFileName, pi);

                }else if (pi.isImageLoaded()) {
                    localPath = pi.getLocalPath();
                    uploadImageFile(context, callbacks, localPath, serverFileName, pi);




                }





        }


    }

    public long imagesTotalSize(){
        long size = 0;
        for(int i =0 ; i < getManifestImages(KEY_MANIFEST_IMAGES_LOADED).length; i++){

            ServerImage serverImage = serverImages.get(i);

            if(serverImage.isImageLoaded()){
                size += serverImage.imageSize();
            }

        }

        return size;
    }

    private void uploadImageFile(
            Context context,
            final Callbacks callbacks,
            String fileLocalPath,
            String fileNameOnServer,
            final ServerImage si) {


        //Log.e("UPD", "uploadImageFile: ..." );


        String serverPath = SOS_API.API_URL + "act=" + SOS_API.ACTION_UPLOAD_PRODUCT_IMAGE_FILE + "&fn=" + fileNameOnServer;
        //final Bundle data = new Bundle(metaData);

        Log.e("SPATH", "uploadImageFile: -> " + serverPath );

        UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(
                context,
                fileLocalPath,
                serverPath,

                new UploadAsyncTask.Callbacks() {



                    @Override
                    public void onProgress(int progress) {

                        //Log.e("PROG", "onProgress: " + );

                        Log.e("PICPROG", "pic -> " + si.getUniqueName() + ", " + progress + " %" );

                        if(progress == 100){
                            si.setImageUploaded(true);
                            updateTotalProgress(si, progress);
                        }
                        callbacks.onProducImageManagerProgress(si, progress, getTotalProgress());
                    }

                    @Override
                    public void onPostExecute(String result) {
                        //callbacksImageFileUpload.CBIFUonPostExecute(tag, result);
                        //callbacksImageFileUpload.CBIFUdidUpload(tag);
                        callbacks.onProducImageManagerPostExecute(si);
                        if(si.deleteLocalCache()){
                            Log.e("DELKASH", "deleted cache -> " + si.imageCachePath() );
                        }

                        //if(getManifestImages(KEY_MANIFEST_IMAGES_UPLOADED) == getManifestImages(KEY_MANIFEST_IMAGES_LOADED)){
                            //callbacks.onProducImageAllImagesUploadedComplete();

                            Log.e("BAAM", "onPostExecute: -> " + si.getImagePostfix() );
                        Log.e("BAAM", "onPostExecute: -> " + numImagesLoaded() + ", " + numImagesUploaded() + ", " + si.getImagePostfix() );
                        //}
                        Log.e("BAAM", "totProg : " + getTotalProgress() );

                        if( (editing && numImagesEdited() == numImagesUploaded()) || (!editing && numImagesLoaded() == numImagesUploaded())){
                            callbacks.onProducImageAllImagesUploadedComplete();

                            resetImagesProgressArray();
                        }

                    }

                    @Override
                    public void onPreExecute() {
                        //callbacksImageFileUpload.CBIFUonFileWillUpload(tag);
                        callbacks.onProducImageManagerPreExecute(si);
                        resetImagesProgressArray();
                        initTotalProgress();
                    }
                }
        );

        uploadAsyncTask.execute();
    }

    private void initTotalProgress() {
        totalProgress = 0;

    }

    private void updateTotalProgress(ServerImage pi, int progress) {
        imagesProgress[pi.getImageGalleryPriorityID()] = progress;

        int divider = editing ? numImagesEdited() : numImagesLoaded();

        totalProgress = HelperMethods.ArraySum(imagesProgress) / divider;
    }

    public int getTotalProgress(){
        return totalProgress;
    }


    private void resetImagesProgressArray(){
        for(int i = 0; i < imagesProgress.length; i++){
            imagesProgress[i] = 0;
        }
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;

        for(int i = 0; i < numImages; i++){
            ServerImage pi = serverImages.get(i);

            pi.setUploadToken(uploadToken);
        }

    }

    public String getServerRootPath() {
        return serverRootPath;
    }

    public void setServerRootPath(String serverRootPath) {
        this.serverRootPath = serverRootPath;
    }

    public void loadImagesForEdit(AppCompatActivity activity) {

        //ivs.get(0).setImageResource(R.drawable.mpesa);
        /*ServerImage si = serverImages.get(0);
        */


        for(int i = 0; i < numImages; i++){

            ServerImage si = serverImages.get(i);
            final ImageView iv = activity.findViewById(si.getImageViewID());

            if(si.isImageCached()){
                //si.setEditedLocalPath();

                iv.setImageDrawable(Drawable.createFromPath(si.imageCachePath()));
            }else{

                final Uri picUri = Uri.parse(si.getRemotePath());
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
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                iv.setImageDrawable(errorDrawable);
                                Log.e("LOGF", "onLoadFailed: -> " + e.getMessage() );
                            }



                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                                iv.setImageBitmap(resource);

                                sosApi.getBitmapCacheManager().saveBitmapToCache(resource, picUri.toString(), SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS);

                            }
                        });

            }

            Log.e("SIMG", si.toString() );

        }
    }



    private void glideLoadPic(final Uri picUri, final int idx, final ImageView iv) {

        Log.e("PASH", "glideLoadPic: -> " + picUri.toString() );

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
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        //String localPath = sosApi.getBitmapCacheManager().saveBitmapToCache(resource, url, SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS);

                        Bundle b = new Bundle();
                        b.putInt(ServerImageManager.BUNDLE_KEY, idx);
                        ServerImage pi = getServerImageByKey(ServerImageManager.KEY_GET_IMAGE_BY_IMAGEGALLERY_PRIORITY_ID, b);
                        //ImageView iv = findViewById(pi.getImageViewID());
                        iv.setImageBitmap(resource);

                        Log.e("PICURI", "onResourceReady: \n\n" + pi.toString() );
                        //ITEM_IMAGEVIEWS_IDS_ARRAY.get(idx).setImageBitmap(resource);
                        //imagesLoaded[idx] = true;
                        /*String tag = SOS_API.GetPicExtTagByIndex(idx);
                        Bundle b = new Bundle();
                        b.putInt(ServerImageManager.BUNDLE_KEY, idx);
                        ServerImage pi = productImageManager.getServerImageByKey(ServerImageManager.KEY_GET_IMAGE_BY_IMAGEGALLERY_PRIORITY_ID, b);

                        pi.setImageLoaded(true, localPath);
                        pi.setUniqueName(tag);
                        pi.setUploadToken(tag);

                        Log.e("TAAR", "onResourceReady: -> \n\n" + pi.toString() );*/

                        //NEW_ITEM_IMAGES_TYPES_AND_URLS.put(tag, cachePath);

                        //Log.e(TAG, "onResourceReady: -> " + NEW_ITEM_IMAGES_TYPES_AND_URLS );

                    }
                });
    }

    public static void UploadFileToServer(Context context, UploadAsyncTask.Callbacks callbacks, String scriptPath, String localPath) {

        UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(context, localPath, scriptPath, callbacks);
        uploadAsyncTask.execute();
    }

    public boolean isEditing() {
        return editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public interface Callbacks{

        void onProducImageManagerProgress(ServerImage serverImage, int progress, int totalProgress);


        void onProducImageManagerPostExecute(ServerImage pi);

        void onProducImageManagerPreExecute(ServerImage pi);

        void onProducImageAllImagesUploadedComplete();
    }
}
