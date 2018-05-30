package com.example.rhyfdocta.sosachat.ImageManager;

import android.content.Context;
import android.os.Bundle;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.Helpers.HelperMethods;
import com.example.rhyfdocta.sosachat.Helpers.UploadAsyncTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductImageManager {


    private static final String SERVER_DIR_PATH_IMAGES_PRODUCTS = "img/products/";
    private int numImages = 0;
    private Context context;
    private List<ProductImage> productImages = new ArrayList<>();
    private HashMap<Integer, String> imagesIDsAndPostfix = new HashMap<>();
    private int[] imageViewsIDS;
    private String[] imagesPostfixes;
    private SOS_API sosApi;


    public static final int KEY_GET_IMAGE_BY_POSTFIX = 0;
    public static final int KEY_GET_IMAGE_BY_IMAGEVIEWS_IDS = 1;
    public static final int KEY_GET_IMAGE_BY_IMAGEGALLERY_PRIORITY_ID = 2;
    public static final int KEY_MANIFEST_IMAGES_LOADED = 1;
    public static final int KEY_MANIFEST_IMAGES_UPLOADED = 2;
    public static final String BUNDLE_KEY = "key";
    public static final String IMAGES_PATH = SOS_API.DIR_NAME_PIX_ROOT + "/" + SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS+ "/";
    private int totalUploadProgress = 0;
    private int[] imagesProgess = new int[]{};
    private String uploadToken = null;


    public ProductImageManager(Context context, int[] imageViewsIDS, String[] imagesPostfixes, int numImages){
        this.context = context;
        this.numImages = numImages;
        this.imageViewsIDS = imageViewsIDS;
        this.imagesPostfixes = imagesPostfixes;
        this.sosApi = new SOS_API(context);

        resetImagesProgress();

        try {
            createProductImages();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean setImageLoaded(int imageViewID, String imageLocalPath){
        //ProductImage productImage = null;
        boolean set = false;

        for(int i = 0; i < productImages.size(); i++){
            ProductImage pi = productImages.get(i);

            if(pi.getImageViewID() == imageViewID){
                pi.setImageLoaded(true, imageLocalPath);
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
                ProductImage productImage = new ProductImage(context, i);
                String postfix = imagesPostfixes[i];
                int ivid = imageViewsIDS[i];
                imagesIDsAndPostfix.put(ivid, postfix);

                productImage.setImagePostfix(postfix);
                productImage.setImageViewID(ivid);
                productImages.add(productImage);

            }
        }
    }

    public ProductImage getProductImageByKey(final int KEY_GET_IMAGE_BY, Bundle value){

        ProductImage productImage = null;


        switch (KEY_GET_IMAGE_BY){
            case KEY_GET_IMAGE_BY_IMAGEVIEWS_IDS:

                int ivID = value.getInt(BUNDLE_KEY);
                for (int i = 0; i < numImages; i++) {
                    ProductImage tmppi = productImages.get(i);
                    if(tmppi.getImageViewID() == ivID){
                        productImage = tmppi;
                    }
                }
                break;

            case KEY_GET_IMAGE_BY_IMAGEGALLERY_PRIORITY_ID:

                int galPriorID = value.getInt(BUNDLE_KEY);
                productImage = productImages.get(galPriorID);
                break;

            case KEY_GET_IMAGE_BY_POSTFIX:
                String postfix = value.getString(BUNDLE_KEY);
                int id = -1;
                int i = 0;
                for (String k : imagesPostfixes) {

                    if(k.equals(postfix)) id = i;
                    i ++;

                }
                productImage = productImages.get(id);
                break;
        }


        return productImage;

    }

    public boolean[] getManifestImages(int KEY_MANIFEST){
        boolean[] manifest = new boolean[numImages];
        HM.IBWAV(manifest, false);

        for(int i = 0; i < numImages; i++){
            ProductImage pi = productImages.get(i);
            switch (KEY_MANIFEST){
                case KEY_MANIFEST_IMAGES_LOADED:
                    manifest[i] = pi.isImageLoaded();
                    break;

                case KEY_MANIFEST_IMAGES_UPLOADED:
                    manifest[i] = pi.isImageUploaded();
                    break;
            }
        }

        return manifest;
    }

    @Override
    public String toString() {

        String status = "\n========== PRODUCT IMAGES MANAGER ==========\n";
        status = status.concat("NUM PICS LOADED : " + numImagesLoaded() + "\n");
        status = status.concat("MANIFEST LOADED : " + HM.BATOS(getManifestImages(KEY_MANIFEST_IMAGES_LOADED)) + "\n");
        status = status.concat("NUM PICS UPLOADED : " + numImagesUploaded() + "\n");
        status = status.concat("MANIFEST UPLOADED : " + HM.BATOS(getManifestImages(KEY_MANIFEST_IMAGES_UPLOADED)) + "\n");
        status = status.concat("NUM TOTAL PICS : " + productImages.size() + "\n");
        status = status.concat("IMGS TOTAL SIZE : " + imagesTotalSize() + " byte(s)\n");
        status = status.concat("===================================");

        return status;
    }

    public String toStringAllProducImages(){

        String s = productImages.get(0).toString() + "\n\n";

        for(int i = 1; i < productImages.size(); i++){
            s = s.concat(productImages.get(i).toString() + "\n\n");
        }

        return s;
    }

    private int numImagesLoaded() {
        int imgsLoaded = 0;

        for(int i = 0; i < productImages.size(); i++){
            ProductImage productImage = productImages.get(i);
            if(productImage.isImageLoaded()) imgsLoaded++;
        }

        return imgsLoaded;
    }

    private int numImagesUploaded() {
        int imgsUploaded = 0;

        for(int i = 0; i < productImages.size(); i++){
            ProductImage productImage = productImages.get(i);
            if(productImage.isImageUploaded()) imgsUploaded++;
        }

        return imgsUploaded;
    }

    public boolean isImageLoaded(int imageViewID) {
        Bundle b = new Bundle();
        b.putInt(BUNDLE_KEY, imageViewID);
        ProductImage productImage = getProductImageByKey(KEY_GET_IMAGE_BY_IMAGEVIEWS_IDS, b);

        return productImage.isImageLoaded();
    }

    public void uploadAllImagesToServer(final Callbacks callbacks) {

        if(uploadToken == null) return;
        resetImagesProgress();



        boolean[] indexesImagesLoaded = getManifestImages(KEY_MANIFEST_IMAGES_LOADED);



        for(int i = 0; i < indexesImagesLoaded.length; i++) {

            ProductImage pi = productImages.get(i);
            String serverFileName = pi.getFileNameOnServer();
            String localPath = pi.getLocalPath();
            String tag = pi.getTag();
            Bundle metaData = new Bundle();

            uploadFile(
                    callbacks,
                    localPath,
                    serverFileName,
                    SERVER_DIR_PATH_IMAGES_PRODUCTS,
                    tag,
                    metaData
            );


        }


    }

    public long imagesTotalSize(){
        long size = 0;
        for(int i =0 ; i < getManifestImages(KEY_MANIFEST_IMAGES_LOADED).length; i++){

            ProductImage productImage = productImages.get(i);

            if(productImage.isImageLoaded()){
                size += productImage.imageSize();
            }

        }

        return size;
    }


    private void uploadFile(
            final Callbacks callbacks,
            String fileLocalPath,
            String fileNameOnServer,
            String dirPathOnServer,
            final String tag,
            final Bundle metaData) {

        /*
        String scriptURL = SOS_API.API_URL + "act=" + SOS_API.ACTION_UPLOAD_IMAGE_FILE + "&dirPath=" + dirPathOnServer + "&fname=" + fileNameOnServer;
        final Bundle data = new Bundle(metaData);

        UploadAsyncTask uploadAsyncTask = new UploadAsyncTask(
                context,
                filePath,
                url,

                new UploadAsyncTask.Callbacks() {
                    @Override
                    public void onProgress(int progress) {
                        callbacksImageFileUpload.CBIFUonUploadProgress(tag, progress);
                        if(progress == 100){
                            callbacksImageFileUpload.CBIFUonUploadSuccess(tag, data);
                        }
                    }

                    @Override
                    public void onPostExecute(String result) {
                        callbacksImageFileUpload.CBIFUonPostExecute(tag, result);
                        callbacksImageFileUpload.CBIFUdidUpload(tag);

                    }

                    @Override
                    public void onPreExecute() {
                        callbacksImageFileUpload.CBIFUonFileWillUpload(tag);
                    }
                }
        );

        uploadAsyncTask.execute();*/
    }


    private void resetImagesProgress(){
        for(int i = 0; i < imagesProgess.length; i++){
            imagesProgess[i] = 0;
        }
    }

    public String getUploadToken() {
        return uploadToken;
    }

    public void setUploadToken(String uploadToken) {
        this.uploadToken = uploadToken;

        for(int i = 0; i < numImages; i++){
            ProductImage pi = productImages.get(i);

            pi.setUploadToken(uploadToken);
        }

    }

    public interface Callbacks{

    }
}
