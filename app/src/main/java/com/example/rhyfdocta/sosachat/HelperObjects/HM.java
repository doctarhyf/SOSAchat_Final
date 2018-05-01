package com.example.rhyfdocta.sosachat.HelperObjects;


import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.rhyfdocta.sosachat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Franvanna on 12/2/2017.
 */

public class HM extends HelperMethods {

    public static String CLDTAS(Context ctx, Long createdDate, Long timeNow){
        return convertLongDateToAgoString(ctx, createdDate, timeNow);
    }

    public static String GPQF(Context context, int i) {
        return getProductQualityFromIndex(context,i);
    }

    public static String GIPB(Context context, int resId, String price, String cur){
        return getItemPriceByCurrency(context, resId, price, cur);
    }

    public static String SATS(String[] sa) {


        return  stringArrayaToString(sa);



    }

    public static AlertDialog GADP(Context context,String message, boolean show){
        return getAlertDialogProcessingWithMessage(context, message, show);
    }

    public static Bundle JTB(JSONObject jo) throws JSONException {

        return jsonToBundle(jo);
    }

    public static boolean EIV(String email){
        return emailIsValid(email);
    }

    public static boolean PIV(String phone){
        return phoneIsValid(phone);
    }

    public static void T(Context ctx, int id, int toastLen){
        toast(ctx, id, toastLen);
    }

    public static void T(Context ctx, String msg, int toastLen){
        toast(ctx, msg, toastLen);
    }

    public static void SSIWN(Spinner spinner, String itemName){
        selectSpinnerItemWithName(spinner,itemName);
    }

    public static String GRN(int min, int max){


        return getRandomNumber(min,max);

    }

    public static String RGS(Context context, int id){
        return getStringResource(context, id);
    }

    public static String GTS(){
        return getTimeStamp();
    }

    public static String CD(String prefix, String date){
        return convertDate(prefix, date);
    }

    public static void LSP(Spinner spinner, String[] data, SpinnerLoaderListener listener) {

        loadSpinner(spinner, data, listener);


    }

    public static String UCF(String string){
        return UCFirst(string);
    }

    public static String GTID(List<Bundle> list, String name) {
        return getItemsTypesID(list, name);
    }

    public static int CISS(BitmapFactory.Options options, int reqWidth, int reqHeight){
        return calculateInSampleSize(options, reqWidth, reqHeight);
    }
    
    public static Bitmap DSBFR(Resources res, int resId,
                               int reqWidth, int reqHeight){
        return decodeSampledBitmapFromResource(res, resId, reqWidth, reqHeight);
    }

    public static AlertDialog GADWMAT(Context context, String title, String message, boolean show, boolean okBtn){
        return getAlertDialogWithMessageAndTitle(context, title, message, show, okBtn);
    }

    public static Bitmap DSBFF(String picPath, int reqWidth, int reqHeight){
        return decodeSampledBitmapFromFile(picPath, reqWidth, reqHeight);
    }

    public static Bitmap DBFBD(Bitmap bitmap, int reqWidth, int reqHeight){
        return decodeBitmapFromBitmapData(bitmap, reqWidth, reqHeight);
    }

    public static void GIVS(ImageViewSizeListener listener, ImageView imageView) {
         getImageViewSize(listener, imageView);
    }


    public static String FD(HelperDate.DateDiff date, String dateOff) {
        return formatDate(date, dateOff);
    }


    public static boolean CPC(String string) {
        return HelperMethods.checkPasswordChars(string);
    }
}
