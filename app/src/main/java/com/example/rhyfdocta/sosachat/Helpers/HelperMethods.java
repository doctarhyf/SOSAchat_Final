package com.example.rhyfdocta.sosachat.Helpers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rhyfdocta on 11/16/17.
 */

public class HelperMethods {

    public static final int TLS = Toast.LENGTH_SHORT;
    public static final int TLL = Toast.LENGTH_LONG;
    //matches numbers only
    public static  String VALID_NUMBERS_ONLY = "^[0-9]*$";

    //matches 10-digit numbers only
    public static  String VALID_10_DIGIT_NUMBERS = "^[0-9]{10}$";

    //matches numbers and dashes, any order really.
    public static String VALID_NUMBERS_AND_DASHES_ANY_ORDER = "^[0-9\\-]*$";

    public static boolean phoneIsValid(String phoneStr){

        return validateStringWithPattern(phoneStr, VALID_10_DIGIT_NUMBERS, true);
    }

    protected static String formatDate(HelperDate.DateDiff date, String dateOff) {




        return date.toString();
    }

    public static String convertLongDateToAgoString(Context ctx, Long createdDate, Long timeNow){
        Long timeElapsed = timeNow - createdDate;

        // For logging in Android for testing purposes
        /*
        Date dateCreatedFriendly = new Date(createdDate);
        Log.d("MicroR", "dateCreatedFriendly: " + dateCreatedFriendly.toString());
        Log.d("MicroR", "timeNow: " + timeNow.toString());
        Log.d("MicroR", "timeElapsed: " + timeElapsed.toString());*/

        // Lengths of respective time durations in Long format.
        Long oneMin = 60000L;
        Long oneHour = 3600000L;
        Long oneDay = 86400000L;
        Long oneWeek = 604800000L;

        String finalString = "0sec";
        String unit;

        if (timeElapsed < oneMin){
            // Convert milliseconds to seconds.
            double seconds = (double) ((timeElapsed / 1000));
            // Round up
            seconds = Math.round(seconds);
            // Generate the friendly unit of the ago time
            if (seconds == 1) {
                unit = HM.RGS(ctx, R.string.DATE_UNIT_SEC);
            } else {
                unit = HM.RGS(ctx, R.string.DATE_UNIT_SECS);
            }
            finalString = String.format("%.0f", seconds) + unit;
        } else if (timeElapsed < oneHour) {
            double minutes = (double) ((timeElapsed / 1000) / 60);
            minutes = Math.round(minutes);
            if (minutes == 1) {
                unit = HM.RGS(ctx, R.string.DATE_UNIT_MIN);
            } else {
                unit = HM.RGS(ctx, R.string.DATE_UNIT_MINS);
            }
            finalString = String.format("%.0f", minutes) + unit;
        } else if (timeElapsed < oneDay) {
            double hours   = (double) ((timeElapsed / 1000) / 60 / 60);
            hours = Math.round(hours);
            if (hours == 1) {
                unit = HM.RGS(ctx, R.string.DATE_UNIT_HOUR);
            } else {
                unit = HM.RGS(ctx, R.string.DATE_UNIT_HOURS);
            }
            finalString = String.format("%.0f", hours) + unit;
        } else if (timeElapsed < oneWeek) {
            double days   = (double) ((timeElapsed / 1000) / 60 / 60 / 24);
            days = Math.round(days);
            if (days == 1) {
                unit = HM.RGS(ctx, R.string.DATE_UNIT_DAY);
            } else {
                unit = HM.RGS(ctx, R.string.DATE_UNIT_DAYS);
            }
            finalString = String.format("%.0f", days) + unit;
        } else if (timeElapsed > oneWeek) {
            double weeks = (double) ((timeElapsed / 1000) / 60 / 60 / 24 / 7);
            weeks = Math.round(weeks);
            if (weeks == 1) {
                unit = HM.RGS(ctx, R.string.DATE_UNIT_WEEK);
            } else {
                unit = HM.RGS(ctx, R.string.DATE_UNIT_WEEKS);
            }
            finalString = String.format("%.0f", weeks) + unit;
        }
        return finalString;
    }

    public static String getStringResource(Context context, int resourceStringID){

        return context.getResources().getString(resourceStringID);
    }

    public static String[] RGSA(Context context, int resourceStringArrayID){
        return context.getResources().getStringArray(resourceStringArrayID);
    }

    public static AlertDialog getAlertDialogWithMessageAndTitle(Context context, String title, String message, boolean show,
                                                                boolean okBtn){

        AlertDialog.Builder builder;
        AlertDialog alertDialog;

        builder =new AlertDialog.Builder(context);

        builder.setTitle(title);
        builder.setMessage(message);

        if(okBtn){
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
        }

        alertDialog = builder.create();
        alertDialog.setCancelable(false);

        if(show)
            alertDialog.show();

        return alertDialog;

    }


    public static AlertDialog getAlertDialogProcessingWithMessage(Context context, String message, boolean show){

        AlertDialog.Builder builder;
        AlertDialog alertDialog;

        builder =new AlertDialog.Builder(context);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.layout_alert_dialog_processing,null);

        TextView tv = view.findViewById(R.id.tvAlertDialogProcessing);

        if(message != null){
            tv.setText(message);
        }

        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);

        if(show)
            alertDialog.show();

        return alertDialog;

    }

    public static AlertDialog alertDialogNewInstance(Context context){
        return getAlertDialogProcessingWithMessage(context, getStringResource(context, R.string.pbMsgProcessing), true);
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public static boolean emailIsValid(String emailStr) {
        String email = emailStr.trim();
        email = email.replaceAll(" ", "");
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(email);
        return matcher.find();
    }

    public static void toast(Context ctx, int strId, int toastLen){
        Toast.makeText(ctx, getStringResource(ctx,strId), toastLen).show();
    }

    public static void toast(Context ctx, String msg, int toastLen){
        Toast.makeText(ctx, msg, toastLen).show();
    }

    public static boolean validateStringWithPattern(String string, String pattern, boolean removeSpace) {
        if(removeSpace) {
            string = string.trim();
            //string = string.replaceAll(" ", "");
        }
        Matcher matcher = Pattern.compile(pattern).matcher(string);
        return matcher.find();
    }

    public static Bundle jsonStringToBundle(String jsonString){
        try {
            JSONObject jsonObject = toJsonObject(jsonString);
            return jsonToBundle(jsonObject);
        } catch (JSONException ignored) {

        }
        return null;
    }
    public static JSONObject toJsonObject(String jsonString) throws JSONException {
        return new JSONObject(jsonString);
    }
    public static Bundle jsonToBundle(JSONObject jsonObject) throws JSONException {
        Bundle bundle = new Bundle();
        Iterator iter = jsonObject.keys();
        while(iter.hasNext()){
            String key = (String)iter.next();
            String value = jsonObject.getString(key);
            bundle.putString(key,value);
        }
        return bundle;
    }

    public static JSONObject bundleToJson(Bundle bundle){
        //String json;
        JSONObject  jsonObject = null;

        for (String key: bundle.keySet()) {
            try {
                jsonObject.put(key, bundle.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        return jsonObject;
    }


    public static void GetViewSize(CallbacksViewSize listener, final View v) {
        //final Size size = new Size(0,0);
        final double[] dSize = new double[2];

        v.post(new Runnable() {
            @Override
            public void run() {
                dSize[0] = v.getWidth();
                dSize[1] = v.getHeight();
            }
        });

        listener.onSizeFound(dSize);
    }

    public static void selectSpinnerItemWithName(Spinner spinner, String itemName){
        for(int i = 0; i < spinner.getAdapter().getCount(); i++) {
            String curItem = spinner.getItemAtPosition(i).toString();
            if(curItem.equalsIgnoreCase(itemName)){
                //idx = i;
                spinner.setSelection(i, false);
                break;
            }



        }
    }

    public static byte[] getByteArrayFromBitmap(Bitmap bitmap){

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static String Base64FromBitmap(Bitmap bitmap){
        return Base64.encodeToString(getByteArrayFromBitmap(bitmap), Base64.DEFAULT);
    }

    public static  byte[] getByteArrayFromImageView(ImageView imageView)
    {


        BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView.getDrawable());
        Bitmap bitmap;
        if(bitmapDrawable==null){
            imageView.buildDrawingCache();
            bitmap = imageView.getDrawingCache();
            imageView.buildDrawingCache(false);
        }else
        {
            bitmap = bitmapDrawable .getBitmap();
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
    public static Bitmap decodeFile(File f){
        try {
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            FileInputStream stream1=new FileInputStream(f);
            BitmapFactory.decodeStream(stream1,null,o);
            stream1.close();

            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }

            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
            FileInputStream stream2=new FileInputStream(f);
            Bitmap bitmap=BitmapFactory.decodeStream(stream2, null, o2);
            stream2.close();
            return bitmap;
        } catch (FileNotFoundException e) {
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void showMessageDialog(Context context, String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setIcon(R.drawable.logo);
        builder.setPositiveButton("OK", new
                DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();
    }



    public static String getProductQualityFromIndex(Context context, int i) {
        String[] qualities = HelperMethods.RGSA(context, R.array.newItemQuality);
        String quality = qualities[i];
        return  quality;
    }

    // TODO: 12/22/2017 DATE FUNCTIONS
    public static String convertDate(String prefix, String date){

        if(null == prefix){
            prefix = "";
        }

        String chagedDate = date;

        return prefix + chagedDate;
    }

    public static String getItemPriceByCurrency(Context context, int resId, String price, String cur) {

        String priceNCur;
        String pdPrice = price;
        if(pdPrice.equals(SOS_API.KEY_ITEM_NO_PRICE)){
            pdPrice = "";
            priceNCur = HelperMethods.getStringResource(context, resId);
        }else{
            priceNCur = pdPrice + " " + cur;
        }

        return priceNCur;
    }

    public static String stringArrayaToString(String[] sa) {
        String s = "";

        for(int i = 0; i < sa.length; i++){
            s += " " + sa[i];
            if(i < sa.length -1){
                s+=",";
            }
        }

        return s;
    }

    public static String getRandomNumber(int min, int max) {
        Random rand = new Random();

        int  n = rand.nextInt(max) + min;

        return "" + n;
    }

    public static String getTimeStamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.toString();
    }

    public static void loadSpinner(Spinner spinner, String[] data, SpinnerLoaderListener listener) {


        String[] spData = new String[1];

        if(data.length == 0){
            spData[0] = "N/A";
        }else{
            spData = UCFirstSA(data);
        }

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(spinner.getContext(), android.R.layout.simple_spinner_item, spData);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapter);
        if(null != listener) {
            listener.onLoadComplete();
        }
    }

    private static String[] UCFirstSA(String[] data) {
        String[] res = new String[data.length];

        for(int i = 0; i < data.length; i++){
            String str = UCFirst(data[i]);
            res[i] = str;
        }

        return res;
    }

    public static String UCFirst(String string){

        /*String[] strings = string.split(" ");
        String[] ucfStrings = new String[string.length()];

        for(int i = 0; i < string.length(); i++){
            String str = strings[i].substring(0,1).toUpperCase() + strings[i].substring(1);
            ucfStrings[i] = str + " ";
        }*/


        return string.substring(0,1).toUpperCase() + string.substring(1);
    }

    public static String getItemsTypesID(List<Bundle> list, String name) {
        String id = "-1";

        for(int i = 0; i < list.size(); i++ ){

            Bundle item = list.get(i);
            if(item.getString(SOS_API.KEY_ITEM_TYPE_NAME).equals(name)){
                id = item.getString(SOS_API.KEY_ITEM_TYPE_ID);
            }
        }

        return id;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap DecodeSampledBitmapFromFile(String picturePath, int reqWidth, int reqHeight){
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, options);
    }

    public static Bitmap DecodeSampledBitmapFromBitmapData(Bitmap bitmap, int reqWidth, int reqHeight){
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;


        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        BitmapFactory.decodeByteArray(byteArray, 0, bitmap.getByteCount());

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(byteArray, 0, bitmap.getByteCount());
    }

    public static boolean checkPasswordChars(String string) {

        boolean sizeOk = string.length() >= 6;

        // TODO: 5/1/2018 CHECK PASSWORD CHARS
        boolean charsOk = true;

        return sizeOk && charsOk;
    }

    public static void initBooleanArrayWithValue(boolean[] array, boolean val) {
        for(int i = 0; i < array.length; i++){
            array[i] = val;
        }
    }

    public static int ArraySum(int[] array) {

        int sum = 0;

        for(int i = 0; i < array.length; i++){
            sum += array[i];
        }

        return sum;

    }

    public static Uri UriFromBitmap(Context context, Bitmap mBitmap) {
        Uri uri = null;
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, 100, 100);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            Bitmap newBitmap = Bitmap.createScaledBitmap(mBitmap, 200, 200,
                    true);
            File file = new File(context.getFilesDir(), "Image"
                    + new Random().nextInt() + ".jpeg");
            FileOutputStream out = context.openFileOutput(file.getName(),
                    Context.MODE_WORLD_READABLE);
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            //get absolute path
            String realPath = file.getAbsolutePath();
            File f = new File(realPath);
            uri = Uri.fromFile(f);

        } catch (Exception e) {
            Log.e("Your Error Message", e.getMessage());
        }
        return uri;
    }

    public static interface SpinnerLoaderListener {
        public void onLoadComplete();
    }

    public static interface CallbacksViewSize {
        public void onSizeFound(double[] size);
    }
}
