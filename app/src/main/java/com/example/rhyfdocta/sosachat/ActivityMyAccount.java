package com.example.rhyfdocta.sosachat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.HelperObjects.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.HelperObjects.HM;
import com.example.rhyfdocta.sosachat.HelperObjects.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ActivityMyAccount extends AppCompatActivity implements SOS_API.SOSApiListener {


    private static final String TAG = "ACT_MY_ACC";
    private static final int REQ_CAMERA = 1;
    ImageView ivProfilePic;
    TextView tvDisplayName, tvAccType, tvCity, tvMobile, tvEmail;
    SOS_API sosApi;
    Bundle accDataBundle = new Bundle();
    private Bitmap b;
    ImageView curImageView;
    AlertDialog alertDialogPictureSource;
    AlertDialog alertDialog;
    String fileName;
    Bundle profileBundle;
    boolean showingProfile;
    Button btnClearCache;
    boolean firstLaunch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        firstLaunch = savedInstanceState == null ;

        btnClearCache = findViewById(R.id.btnClearImgsCache);

        sosApi = new SOS_API(this);

        alertDialog = HM.GADP(this, HM.getStringResource(this, R.string.pbMsgUpdatingPP),false);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_my_account));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivProfilePic = (ImageView) findViewById(R.id.ivSettingsPP);
        tvDisplayName = (TextView) findViewById(R.id.tvDisplayName);
        tvAccType = (TextView) findViewById(R.id.tvAccType);
        tvCity = (TextView) findViewById(R.id.tvCity);
        tvMobile = (TextView) findViewById(R.id.tvMobile);
        tvEmail = (TextView) findViewById(R.id.tvEmail);



        ivProfilePic.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){
                    case 0:
                        v.setAlpha((float) 0.5);
                        break;


                    case 1:
                        v.setAlpha(1);
                        //Toast.makeText(getApplicationContext(), "Changing picture ...", Toast.LENGTH_SHORT).show();
                        loadPic(ivProfilePic);
                        break;
                }

                return true;
            }
        });




        profileBundle = getIntent().getExtras();
        showingProfile = false;

        if(profileBundle != null){

            if(profileBundle.getBoolean(SOS_API.KEY_SHOWING_VENDOR_PROFILE)){
                showingProfile = true;
            }

        }

        if(showingProfile){
            //HM.T(this, R.string.testOk, HM.TLS);
            loadProfileData();
        }else {
            loadAccountData();
        }

        calculateImgsCachec();

    }

    @Override
    protected void onResume() {
        super.onResume();

        //if(!firstLaunch){
            calculateImgsCachec();
        //}


    }

    private void calculateImgsCachec() {
        /*File picsFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()
        + BitmapCacheManager.CACHE_ROOT_DIR);*/
        double cacheSize = BitmapCacheManager.folderSize(new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + BitmapCacheManager.CACHE_ROOT_DIR + "/"));
b
        btnClearCache.setText(String.format(getResources().getString(R.string.strBtnClearImgsCache), cacheSize / 1000000f));
    }

    private void loadProfileData() {



        LinearLayout setRowMyProd = findViewById(R.id.setRowMyProducts);
        LinearLayout setRowWl = findViewById(R.id.setRowWishList);
        LinearLayout setRowAccSet = findViewById(R.id.setRowAccSettings);

        setRowMyProd.setVisibility(View.GONE);
        setRowWl.setVisibility(View.GONE);
        setRowAccSet.setVisibility(View.GONE);

        //Bundle b = new Bundle();

        //b.putString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME,profileBundle.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));
        //b.putString(SOS_API.KEY_ACC_DATA_ACC_TYPE,sosApi.GSV(SOS_API.KEY_ACC_DATA_ACC_TYPE));
        /*b.putString(SOS_API.KEY_ACC_DATA_LOCATION,sosApi.GSV(SOS_API.KEY_ACC_DATA_LOCATION));
        b.putString(SOS_API.KEY_ACC_DATA_ACC_PIC_NAME, sosApi.GSV(SOS_API.KEY_ACC_DATA_ACC_PIC_NAME));
        b.putString(SOS_API.KEY_ACC_DATA_COMPANY, sosApi.GSV(SOS_API.KEY_ACC_DATA_COMPANY));
        b.putString(SOS_API.KEY_ACC_DATA_SHOW_MY_MOBILE, sosApi.GSV(SOS_API.KEY_ACC_DATA_SHOW_MY_MOBILE));
        b.putString(SOS_API.KEY_ACC_DATA_DATE_ADDED, sosApi.GSV(SOS_API.KEY_ACC_DATA_DATE_ADDED));
        b.putString(SOS_API.KEY_ACC_DATA_SHOW_MY_ADDRESS, sosApi.GSV(SOS_API.KEY_ACC_DATA_SHOW_MY_ADDRESS));
        b.putString(SOS_API.KEY_ACC_DATA_SHOW_MY_EMAIL, sosApi.GSV(SOS_API.KEY_ACC_DATA_SHOW_MY_EMAIL));
        b.putString(SOS_API.KEY_ACC_DATA_MOBILE, sosApi.GSV(SOS_API.KEY_ACC_DATA_MOBILE));
        b.putString(SOS_API.KEY_ACC_DATA_ID, sosApi.GSV(SOS_API.KEY_ACC_DATA_ID));
        b.putString(SOS_API.KEY_ACC_DATA_FULL_NAME, sosApi.GSV(SOS_API.KEY_ACC_DATA_FULL_NAME));
        b.putString(SOS_API.KEY_ACC_DATA_PASSWORD, sosApi.GSV(SOS_API.KEY_ACC_DATA_PASSWORD));
        b.putString(SOS_API.KEY_ACC_DATA_EMAIL, sosApi.GSV(SOS_API.KEY_ACC_DATA_EMAIL));*/


        //String s = sosApi.GSV(SOS_API.KEY_ACC_DATA_MOBILE_HASH);


        this.accDataBundle = profileBundle;


        onAccountDataLoaded();
    }

    private void loadAccountData() {

        Bundle b = new Bundle();

        b.putString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME,sosApi.GSV(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));
        //b.putString(SOS_API.KEY_ACC_DATA_ACC_TYPE,sosApi.GSV(SOS_API.KEY_ACC_DATA_ACC_TYPE));
        b.putString(SOS_API.KEY_ACC_DATA_LOCATION,sosApi.GSV(SOS_API.KEY_ACC_DATA_LOCATION));
        b.putString(SOS_API.KEY_ACC_DATA_ACC_PIC_NAME, sosApi.GSV(SOS_API.KEY_ACC_DATA_ACC_PIC_NAME));
        b.putString(SOS_API.KEY_ACC_DATA_COMPANY, sosApi.GSV(SOS_API.KEY_ACC_DATA_COMPANY));
        b.putString(SOS_API.KEY_ACC_DATA_SHOW_MY_MOBILE, sosApi.GSV(SOS_API.KEY_ACC_DATA_SHOW_MY_MOBILE));
        b.putString(SOS_API.KEY_ACC_DATA_DATE_ADDED, sosApi.GSV(SOS_API.KEY_ACC_DATA_DATE_ADDED));
        b.putString(SOS_API.KEY_ACC_DATA_SHOW_MY_ADDRESS, sosApi.GSV(SOS_API.KEY_ACC_DATA_SHOW_MY_ADDRESS));
        b.putString(SOS_API.KEY_ACC_DATA_SHOW_MY_EMAIL, sosApi.GSV(SOS_API.KEY_ACC_DATA_SHOW_MY_EMAIL));
        b.putString(SOS_API.KEY_ACC_DATA_MOBILE, sosApi.GSV(SOS_API.KEY_ACC_DATA_MOBILE));
        b.putString(SOS_API.KEY_ACC_DATA_ID, sosApi.GSV(SOS_API.KEY_ACC_DATA_ID));
        b.putString(SOS_API.KEY_ACC_DATA_FULL_NAME, sosApi.GSV(SOS_API.KEY_ACC_DATA_FULL_NAME));
        b.putString(SOS_API.KEY_ACC_DATA_PASSWORD, sosApi.GSV(SOS_API.KEY_ACC_DATA_PASSWORD));
        b.putString(SOS_API.KEY_ACC_DATA_EMAIL, sosApi.GSV(SOS_API.KEY_ACC_DATA_EMAIL));


        //String s = sosApi.GSV(SOS_API.KEY_ACC_DATA_MOBILE_HASH);


        this.accDataBundle = b;


        onAccountDataLoaded();

    }

    public void onAccountDataLoaded() {


        getSupportActionBar().setTitle(accDataBundle.getString(SOS_API.KEY_ACC_DATA_FULL_NAME));
        tvDisplayName.setText(accDataBundle.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));
        tvAccType.setText(accDataBundle.getString(SOS_API.KEY_ACC_DATA_ACC_TYPE));
        tvCity.setText(accDataBundle.getString(SOS_API.KEY_ACC_DATA_LOCATION));
        tvMobile.setText(accDataBundle.getString(SOS_API.KEY_ACC_DATA_MOBILE));
        tvEmail.setText(accDataBundle.getString(SOS_API.KEY_ACC_DATA_EMAIL));


        String ppName = sosApi.GSV(SOS_API.KEY_ACC_DATA_MOBILE_HASH) + ".jpg";

        String path = SOS_API.DIR_PATH_PP + ppName ;//+ "?ts=" + HM.getTimeStamp();//accDataBundle.get(SOS_API.KEY_ACC_DATA_ACC_PIC_NAME);
        Uri uri = Uri.parse(path);

        Log.e(TAG, "PPNAME -> " + path );





        Picasso picasso = new Picasso.Builder(this)
                .listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        //Here your log
                        ivProfilePic.setImageResource(R.drawable.placeholder_user_pic);
                        sosApi.TADRWM(true, HM.RGS(ActivityMyAccount.this, R.string.msgFailedToLoadPP));
                    }
                })
                .build();
        picasso.load(uri).placeholder(R.drawable.progress_animation).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).centerCrop().resize(300,300).into(ivProfilePic);




    }

    private void onPictureSourceSelected(int which) {

        Intent intent;

        switch (which){
            case SOS_API.PIC_SOURCE_GALLERY:


                if(SOS_API.POST_MARSHMALLOW) {
                    startGallery();
                }else {
                    fireGalleryIntent();
                }
                break;

            case SOS_API.PIC_SOURCE_CAMERA:

                if(SOS_API.POST_MARSHMALLOW) {
                    startCameraApp();
                }else {
                    fireCameraIntent();
                }

                break;

        }

        alertDialogPictureSource.dismiss();
        alertDialogPictureSource = null;
    }

    public void loadPic(View view){

        Log.e(TAG, "loadPic: " + view.getId() );
        curImageView = (ImageView)view;
        String[] sources = {getResources().getString(R.string.sourceGallery), getResources().getString(R.string.sourceCamera)};

        View viewDialog = getLayoutInflater().inflate(R.layout.alert_dialog_choose_pic_source, null);


        View viewSourceGallery = viewDialog.findViewById(R.id.llDialogPicSourceGalley);
        View viewSourceCamera = viewDialog.findViewById(R.id.llDialogPicSourceCamera);

        viewSourceGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPictureSourceSelected(SOS_API.PIC_SOURCE_GALLERY);
            }
        });

        viewSourceCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPictureSourceSelected(SOS_API.PIC_SOURCE_CAMERA);
            }
        });



        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.msgChooseYourPictureSource))
                .setView(viewDialog);


        alertDialogPictureSource = builder.create();
        alertDialogPictureSource.show();



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SOS_API.RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn,null,null,null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=4;      // 1/8 of original image
            b = BitmapFactory.decodeFile(picturePath, options);

            // TODO: 11/16/17 COMPRESS ON BYTES COUNT THRESHOLD

            Log.e(TAG, "BYTES CT " + b.getByteCount());

            //curImageView.setImageBitmap(b);
            //uploadProfilePic(b);

            uploadProfilePic();
        }


        if(requestCode == SOS_API.REQ_CAMERA && resultCode == RESULT_OK){

            String path = SOS_API.GSAIPCP() + "/" + fileName;
            //curImageView.setImageDrawable(Drawable.createFromPath(path));


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=4;      // 1/8 of original image
            b = BitmapFactory.decodeFile(path, options);

            curImageView.setImageBitmap(b);

            uploadProfilePic();


        }
    }

    private static final int RESULT_LOAD_IMAGE = 1200;
    private static final String KEY_TAG_PICTURE_LOADED = "picLoaded";

    private static final int REQ_PERMISSION_GALLERY = 1300;
    private static final int REQ_PERMISSION_CAMERA = 1301;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQ_PERMISSION_GALLERY){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                fireCameraIntent();
            }else{
                Toast.makeText(this, "We need your permission to access the gallery!", Toast.LENGTH_LONG).show();
            }

        }

        if(requestCode == REQ_PERMISSION_CAMERA){

            if(grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                fireCameraIntent();
            }else{
                Toast.makeText(this, "We need your permission to access the camera!", Toast.LENGTH_LONG).show();
            }

        }

    }

    private void uploadProfilePic() {



        String imgStr = HelperMethods.getBase64StringFromBitmap(b);


        alertDialog.show();
        sosApi.uploadProfilePic(this,imgStr);




    }

    private void fireGalleryIntent() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, SOS_API.RESULT_LOAD_IMAGE);
    }

    private void fireCameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getCameraPictureFile();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        startActivityForResult(intent, SOS_API.REQ_CAMERA);

    }

    private File getCameraPictureFile(){
        //File folder = new File(MainActivity.KEY_SOSACHAT_FOLDER);
        String path = SOS_API.GSAIPCP();
        File folder = new File(path);

        boolean folderExists = folder.exists();

        if(!folderExists){
            boolean success = folder.mkdir();
            //Log.e(TAG, "getCameraPictureFile: " + success );
        }

        fileName = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        fileName = fileName.concat(".jpg");
        File imgFile = new File(folder, fileName);

        return  imgFile;
    }

    private void startCameraApp() {



        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

            fireCameraIntent();

        }else{

            String[] permissions = { Manifest.permission.CAMERA};
            requestPermissions(permissions, SOS_API.REQ_PERMISSION_CAMERA);

        }

    }

    private void startGallery() {


        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

            fireGalleryIntent();

        }else{

            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions, SOS_API.REQ_PERMISSION_GALLERY);

        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        System.gc();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Intent intent;

        if(item.getItemId() == android.R.id.home){
            finish();
        }


        /*

        if(item.getItemId() == R.id.setRowAccSettings){
            intent = new Intent(this, ActivityAccountSettings.class);
            startActivity(intent);
        }*/


        return super.onOptionsItemSelected(item);
    }

    public void onRowClicked(View view){

        if(null != accDataBundle) {
            Intent intent;

            switch (view.getId()) {
                /*case R.id.setRowAccActivation:
                    intent = new Intent(this, ActivityAccountActivation.class);
                    //Toast.makeText(this, "ON ACC ACT", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    break;*/

                case R.id.setRowAccSettings:
                    intent = new Intent(this, ActivityAccountSettings.class);
                    intent.putExtras(accDataBundle);
                    //Toast.makeText(this, "ON ACC ACT", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    break;

                case R.id.setRowWishList:
                    intent = new Intent(this, ActivityWishlist.class);
                    startActivity(intent);
                    break;

                case R.id.setRowMyProducts:
                    intent = new Intent(this, ActivityMyProducts.class);
                    startActivity(intent);
                    break;
            }

            //Toast.makeText(this, "ON ACC ACT", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No data loaded", Toast.LENGTH_SHORT).show();
        }



    }

    @Override
    public void onSearchResult(String ids) {

    }

    @Override
    public void onSearchResultError(String error) {

    }

    @Override
    public void onPostInquiryResult(String result) {

    }

    @Override
    public void onLoadRecentItemsResult(List<Product> featuredProducts, boolean networkError) {

    }

    @Override
    public void onLoginResult(Bundle data) {

    }

    @Override
    public void onSignUpResult(Bundle data) {

    }

    @Override
    public void onAccountDeleteResult(Bundle data) {

    }

    @Override
    public void onLogoutResult() {

    }

    @Override
    public void onExposeItemResult(Bundle data) {

    }

    @Override
    public void onLoadAllMyProductsResult(List<ProductMyProducts> myProducts, boolean networkError) {

    }

    @Override
    public void onLoadItemsCats(JSONArray jsonArray) {

    }

    @Override
    public void onUpdatePasswordResult(String resp) {

    }

    @Override
    public void onUpdateSettingsResult(String settingKey, String result) {

    }

    @Override
    public void onLoadAllProductsResult(List<ProductMyProducts> allProducts) {

    }

    @Override
    public void onRemoveProductResult(Bundle b) {

    }

    @Override
    public void onUpdateItemViewsCountResult(String newCount) {

    }

    @Override
    public void onLoadAllProductsError(String message) {

    }

    @Override
    public void onLoadSearchResultItemsResult(List<ProductMyProducts> searchResultProducts) {

    }

    @Override
    public void onLoadSearchResultItemsError(String message) {

    }

    @Override
    public void onItemsTypesLoaded(JSONArray itemCats) {

    }

    @Override
    public void onUploadPPResult(Bundle data) {

        alertDialog.hide();

        Log.e(TAG, "onUploadPPResult: result -> " + data.toString() );

        if(data.getString(SOS_API.JSON_KEY_RESULT).equals(SOS_API.JSON_RESULT_SUCCESS)){
            curImageView.setImageBitmap(b);
            Toast.makeText(this, "Profile Picture set!", Toast.LENGTH_SHORT).show();
        }else{
            String errMsg = data.getString(SOS_API.JSON_KEY_RESULT_ERROR_MESSAGE);
            Toast.makeText(this, "Error setting profile pic!\nError : " + errMsg, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onLoadItemsTypesResultError() {

    }

    @Override
    public void onLoadItemsTypesResult(String[] types) {

    }

    @Override
    public void onLoadCatsTypesNames(String cn, String tn) {

    }

    @Override
    public void onCategoryTypesLoaded(List<TypesItem> types, boolean errorLoading) {

    }

    public void clearImgsCache(View view) {



    }
}
