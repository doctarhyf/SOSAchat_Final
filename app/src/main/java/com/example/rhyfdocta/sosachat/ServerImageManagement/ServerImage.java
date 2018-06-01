package com.example.rhyfdocta.sosachat.ServerImageManagement;

import android.content.Context;
import android.net.Uri;

import com.example.rhyfdocta.sosachat.Helpers.BitmapCacheManager;

import java.io.File;

public class ServerImage {

    private static final int NO_ID = -1;
    private static final String NO_IMAGE_CACHE = "no_cache";
    private Context context;
    private String localPath = null;
    private String remotePath = null;


    private String imagePostfix = null;
    private int imageGalleryPriorityID = NO_ID;
    private int imageViewID = NO_ID;
    private boolean imageUploaded = false;
    private boolean imageLoaded = false;
    //private boolean imageCached = false;
    private String fileNameOnServer = null;
    private boolean isNewFileToUploadLoaded = false;
    private String uniqueName = null;
    private String uploadToken = null;
    private String serverRootPath = null;
    private int cacheRootPathID = -1;

    public ServerImage(Context context, int imageGalleryPriorityID, String serverRootPath, int cacheRootPathID){

        this.context = context;
        this.setImageGalleryPriorityID(imageGalleryPriorityID);
        this.serverRootPath = serverRootPath;
        this.cacheRootPathID = cacheRootPathID;

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
        data = data.concat("Image Uploaded : " + getImageUploaded() + "\n");
        data = data.concat("Image cached : " + isImageCached() + "\n");
        data = data.concat("FileName on SERVER : " + fileNameOnServer + "\n");
        data = data.concat("Token : " + uploadToken + "\n");
        data = data.concat("=====================================");

        return data;
    }

    public boolean isImageCached() {


        return !imageCachePath().equals(NO_IMAGE_CACHE);
    }

    public String imageCachePath() {
        String cachePath = NO_IMAGE_CACHE;


        //String picName = uniqueName + imagePostfix;
        final String picCachePath = BitmapCacheManager.GetImageCachePath(cacheRootPathID, fileNameOnServer);

        if(BitmapCacheManager.FileExists(picCachePath)){

            //Log.e("ABC", "pname : " + picName );
            cachePath = picCachePath;

        }


        return cachePath;
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



    public void setImageLoaded(boolean imageLoaded, String imageLocalPath) {
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

    public Uri localURI(){
        return Uri.parse(localPath);
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

    public String getUniqueName() {
        return uniqueName;
    }

    public void setUniqueName(String uniqueName) {
        this.uniqueName = uniqueName;
    }

    public void setUploadToken(String uploadToken) {
        fileNameOnServer = uploadToken + imagePostfix;
        setRemotePath(serverRootPath + fileNameOnServer);
        this.uploadToken = uploadToken;
    }

    public void setServerRootPath(String serverRootPath) {
        this.serverRootPath = serverRootPath;
    }

    public String getRemotePath() {
        return serverRootPath + fileNameOnServer;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public Uri getRemoteOrCacheURI(){
        String imagePath = isImageCached() ? imageCachePath() : getRemotePath();
        return Uri.parse(imagePath);

    }

    public void setImageUploaded(boolean imageUploaded) {
        this.imageUploaded = imageUploaded;
    }

    public boolean isNewFileToUploadLoaded() {
        return isNewFileToUploadLoaded;
    }

    public void setNewFileToUploadLoaded(boolean newFileToUploadLoaded) {
        isNewFileToUploadLoaded = newFileToUploadLoaded;
    }

    public boolean deleteLocalCache() {
        File file = new File(imageCachePath());
        boolean res = false;

        if(file.exists()){
            res = file.delete();
        }

        return res;
    }

    public boolean getImageUploaded() {
        return imageUploaded;
    }
}
