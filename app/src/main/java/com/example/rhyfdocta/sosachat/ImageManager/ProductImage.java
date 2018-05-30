package com.example.rhyfdocta.sosachat.ImageManager;

import android.content.Context;
import android.net.Uri;

import java.io.File;

public class ProductImage {

    private static final int NO_ID = -1;
    private Context context;
    private String cacheURL = null;
    private Uri cacheURI = null;
    private String localPath = null;
    private String remotePath = null;
    private String localURI = null;
    private String uniqueName = null;
    private String imagePostfix = null;
    private int imageGalleryPriorityID = NO_ID;
    private int imageViewID = NO_ID;
    private boolean imageUploaded = false;
    private boolean imageLoaded = false;
    private boolean imageCached = false;
    private String fileNameOnServer = null;
    private String tag = null;
    private String uploadToken = null;
    private String serverImagesRootPath = null;

    public ProductImage(Context context, int imageGalleryPriorityID, String serverImagesRootPath){

        this.context = context;
        this.setImageGalleryPriorityID(imageGalleryPriorityID);
        this.serverImagesRootPath = serverImagesRootPath;

    }

    @Override
    public String toString() {
        String data = "\n========== PRODUCT IMAGE ==========\n";
        data = data.concat("GALLERY ID : " + imageGalleryPriorityID + "\n");
        data = data.concat("Local Path : " + localPath + "\n");
        data = data.concat("Server Root Path : " + remotePath + "\n");
        //data = data.concat("ON SERVER : " + onServer() + "\n");
        data = data.concat("File Size : " + imageSize() + " byte(s)\n");
        data = data.concat("Postfix : " + imagePostfix + "\n");
        data = data.concat("ImageViewID : " + imageViewID + "\n");
        data = data.concat("Image Loaded : " + imageLoaded + "\n");
        data = data.concat("Image Uploaded : " + isImageUploaded() + "\n");
        data = data.concat("Image cached : " + imageCached + "\n");
        data = data.concat("FileName on SERVER : " + fileNameOnServer + "\n");
        data = data.concat("Token : " + uploadToken + "\n");
        data = data.concat("=====================================");

        return data;
    }



    public long imageSize(){

        long size = 0;

        if(isImageLoaded()){
            File file = new File(localPath);
            size = file.length();
        }

        return size;
    }

    public int getImageViewID() {
        return imageViewID;
    }

    public void setImageViewID(int imageViewID) {
        this.imageViewID = imageViewID;
    }

    public boolean isImageLoaded() {
        return imageLoaded;
    }

    public boolean isImageUploaded(){
        return imageUploaded;
    }

    protected void setImageLoaded(boolean imageLoaded, String imageLocalPath) {
        this.imageLoaded = imageLoaded;
        this.localPath = imageLocalPath;

    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public int getImageGalleryPriorityID() {
        return imageGalleryPriorityID;
    }

    public void setImageGalleryPriorityID(int imageGalleryPriorityID) {
        this.imageGalleryPriorityID = imageGalleryPriorityID;
    }

    public String getImagePostfix() {
        return imagePostfix;
    }

    public void setImagePostfix(String imagePostfix) {
        this.imagePostfix = imagePostfix;
    }

    public String getFileNameOnServer() {
        return fileNameOnServer;
    }

    public void setFileNameOnServer(String fileNameOnServer) {
        this.fileNameOnServer = fileNameOnServer;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setUploadToken(String uploadToken) {
        fileNameOnServer = uploadToken + imagePostfix;
        setRemotePath(serverImagesRootPath + fileNameOnServer);
        this.uploadToken = uploadToken;
    }

    public void setServerImagesRootPath(String serverImagesRootPath) {
        this.serverImagesRootPath = serverImagesRootPath;
    }

    public String getRemotePath() {
        return serverImagesRootPath + fileNameOnServer;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public void setImageUploaded(boolean imageUploaded) {
        this.imageUploaded = imageUploaded;
    }
}
