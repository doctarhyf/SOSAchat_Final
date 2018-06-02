package com.example.rhyfdocta.sosachat.Helpers;


import android.content.Context;
import android.support.v7.app.AlertDialog;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Spinner;

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

    public static AlertDialog GADP(Context context, String message, boolean show){
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
        return DecodeSampledBitmapFromFile(picPath, reqWidth, reqHeight);
    }

    public static Bitmap DSBFF(String picPath, int[] size){
        return DecodeSampledBitmapFromFile(picPath, size[0], size[1]);
    }

    public static Bitmap DSBFBD(Bitmap bitmap, int reqWidth, int reqHeight){
        return DecodeSampledBitmapFromBitmapData(bitmap, reqWidth, reqHeight);
    }

    public static Bitmap DSBFBD(Bitmap bitmap, int[] size){
        return DecodeSampledBitmapFromBitmapData(bitmap, size[0], size[1]);
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

    public static void IBWAV(boolean[] array, boolean val) {
        initBooleanArrayWithValue(array, val);
    }

    public static String BATOS(boolean[] array) {
        return booleanArrayToString(array);
    }

    private static String booleanArrayToString(boolean[] array) {
        String s= "[ ";

        for(int i = 0; i < array.length; i ++){

            String v = array[i] ? "true" : "false";
            String end = ", ";

            if(i == array.length -1) end = " ]";

            s = s.concat(v + end);
        }



        return s;

    }
}
