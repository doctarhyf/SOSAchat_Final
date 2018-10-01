package com.koziengineering.rhyfdocta.sosachat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.koziengineering.rhyfdocta.sosachat.API.SOS_API;
import com.koziengineering.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.koziengineering.rhyfdocta.sosachat.Helpers.HM;
import com.koziengineering.rhyfdocta.sosachat.Helpers.UploadAsyncTask;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.Product;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.R;
import com.koziengineering.rhyfdocta.sosachat.ServerImageManagement.ServerImageManager;
import com.koziengineering.rhyfdocta.sosachat.app.SOSApplication;

import org.json.JSONArray;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ActivityMyAccount extends AppCompatActivity implements
        SOS_API.SOSApiListener,
        UploadAsyncTask.Callbacks {


    private static final String TAG = "ACT_MY_ACC";
    private static final int REQ_CAMERA = 1;
    ImageView ivProfilePic;
    TextView tvDisplayName, tvAccType, tvCity, tvMobile, tvEmail;
    SOS_API sosApi;
    Bundle accDataBundle = new Bundle();
    private Bitmap b;
    ImageView curImageView;
    AlertDialog alertDialogPictureSource;
    ProgressDialog progressDialog;
    String fileName;
    Bundle profileBundle;
    boolean showingProfile;
    Button btnClearCache;
    boolean firstLaunch = false;
    private MyGlideBitmapLoaderCallbacks glideBitmapLoaderCallbacks;
    private int uploadProgress = 0;
    private String localPath = null;
    //private BitmapCacheManager bitmapCacheManager;
    ImageView ivPPLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        //bitmapCacheManager = new BitmapCacheManager(this);

        ivPPLarge = findViewById(R.id.ivPPLarge);

        glideBitmapLoaderCallbacks = new MyGlideBitmapLoaderCallbacks(this);


        firstLaunch = savedInstanceState == null ;

        btnClearCache = findViewById(R.id.btnClearImgsCache);

        sosApi = new SOS_API(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(HM.RGS(this, R.string.pbMsgUpdatingPP));
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(true);
        //HM.GADP(this, HM.getStringResource(this, R.string.pbMsgUpdatingPP),false);

        getSupportActionBar().setTitle(getResources().getString(R.string.title_activity_my_account));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivProfilePic = findViewById(R.id.ivSettingsPP);
        tvDisplayName = findViewById(R.id.tvDisplayName);
        tvAccType = findViewById(R.id.tvAccType);
        tvCity = findViewById(R.id.tvCity);
        tvMobile = findViewById(R.id.tvMobile);
        tvEmail = findViewById(R.id.tvEmail);



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
            // Loading a profile of another user
            loadProfileData();
        }else {

            //load account data of the current logged in user
            loadAccountData();
        }

        double cacheSize = BitmapCacheManager.GetImagesCacheSize();
        Log.e(TAG, "CACHE SIZE : " + cacheSize );
        updateBtnTextCacheSize(cacheSize);

    }

    private void updateBtnTextCacheSize(double cacheSize) {
        btnClearCache.setText(String.format(getResources().getString(R.string.strBtnClearImgsCache), cacheSize));
    }

    @Override
    protected void onResume() {
        super.onResume();

        //if(!firstLaunch){
            updateBtnTextCacheSize(BitmapCacheManager.GetImagesCacheSize());
        //}


    }

    @Override
    public void onProgress(int progress) {
        this.uploadProgress = progress;
        progressDialog.setProgress(progress);
    }

    @Override
    public void onPostExecute(String result) {
        if(uploadProgress == 100){
            uploadProgress = 0;
            progressDialog.dismiss();

            Log.e("DAREZ", "onPostExecute: -> " + result );
            //curImageView.setImageBitmap(b);
            String pp = sosApi.GSV(SOS_API.KEY_ACC_DATA_MOBILE_HASH) + ".jpg";

            String path = SOS_API.ROOT_URL + SOS_API.SERVER_REL_ROOT_DIR_PATH_PROFILE_PICTURES + pp;
            final Uri picUri = Uri.parse(path);

            if(BitmapCacheManager.DeleteCacheFile(BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_PROFILE_PIC, pp)){



                BitmapCacheManager.LoadBitmapFilePathIntoImageView(curImageView, localPath );
                glideBitmapLoaderCallbacks.saveBitmapToLocalCache(b, picUri.toString(), SOS_API.DIR_NAME_PIX_CACHE_PROFILCE_PIC);

            }else{
                Log.e(TAG, "Cant delete cache -> " + pp );
            }
        }
    }

    @Override
    public void onPreExecute() {
        progressDialog.show();
    }

    private class MyGlideBitmapLoaderCallbacks implements BitmapCacheManager.CallbacksBitmapLoading{

        private final Context context;

        public MyGlideBitmapLoaderCallbacks(Context context){
            this.context = context;
        }

        @Override
        public void onItemClicked(Product pd) {

        }

        @Override
        public void saveBitmapToLocalCache(Bitmap bitmap, String picUrl, String dirName) {

            Log.e(TAG, "FILE EX : -> " +  sosApi.getBitmapCacheManager().saveBitmapToCache(bitmap,  picUrl, dirName));

        }
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

        this.accDataBundle = b;

        onAccountDataLoaded();

    }

    public void onAccountDataLoaded() throws NullPointerException {

        //Log.e(TAG, "onAccountDataLoaded: " );

        getSupportActionBar().setTitle(accDataBundle.getString(SOS_API.KEY_ACC_DATA_FULL_NAME));
        tvDisplayName.setText(accDataBundle.getString(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));
        tvAccType.setText(accDataBundle.getString(SOS_API.KEY_ACC_DATA_ACC_TYPE));
        tvCity.setText(accDataBundle.getString(SOS_API.KEY_ACC_DATA_LOCATION));
        tvMobile.setText(accDataBundle.getString(SOS_API.KEY_ACC_DATA_MOBILE));
        tvEmail.setText(accDataBundle.getString(SOS_API.KEY_ACC_DATA_EMAIL));


        String ppName = sosApi.GSV(SOS_API.KEY_ACC_DATA_MOBILE_HASH) + ".jpg";

        String path = sosApi.GSA() + SOS_API.DIR_PATH_PP + ppName ;//+ "?ts=" + HM.getTimeStamp();//accDataBundle.get(SOS_API.KEY_ACC_DATA_ACC_PIC_NAME);


        String cachePath = BitmapCacheManager.GetImageCachePath(BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_PROFILE_PIC, ppName);
        final Uri picUri = BitmapCacheManager.loadImageFromCacheOrNetwork(Uri.parse(path), cachePath);

        //final Uri picUri = Uri.parse(path);
        ivProfilePic.setImageResource(R.drawable.progress_animation);

        Log.e(TAG, "onAccountDataLoaded: picUri -> " + picUri.toString() );


        Glide.with(this)
                .load(picUri)

                .asBitmap()
                .placeholder(R.drawable.progress_animation)
                .error(R.drawable.ic_error)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(new SimpleTarget<Bitmap>(300,300) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {




                        glideBitmapLoaderCallbacks.saveBitmapToLocalCache(resource, picUri.toString(), SOS_API.DIR_NAME_PIX_CACHE_PROFILCE_PIC);
                        ivProfilePic.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                        ivProfilePic.setImageResource(R.drawable.ic_user_m);
                        //sosApi.TADRWM(ActivityMyAccount.this,true, HM.RGS(ActivityMyAccount.this, R.string.msgFailedToLoadPP));
                    }
                });



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

        TextView tvTitle = viewDialog.findViewById(R.id.tvDialogChoosePicTitle);
        tvTitle.setText(sosApi.GSV(SOS_API.KEY_ACC_DATA_DISPLAY_NAME));



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
                //.setTitle()//getResources().getString(R.string.msgChooseYourPictureSource))
                .setView(viewDialog);

        ImageView iv = viewDialog.findViewById(R.id.ivPPLarge);

        iv.setImageDrawable(ivProfilePic.getDrawable());

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
            localPath = cursor.getString(columnIndex);
            cursor.close();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=4;      // 1/8 of original image
            b = BitmapFactory.decodeFile(localPath, options);

            //curImageView.setImageBitmap(b);
            uploadProfilePic();
        }


        if(requestCode == SOS_API.REQ_CAMERA && resultCode == RESULT_OK){

            localPath = SOS_API.GSAIPCP() + "/" + fileName;
            //curImageView.setImageDrawable(Drawable.createFromPath(path));


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=4;      // 1/8 of original image
            b = BitmapFactory.decodeFile(localPath, options);

            //curImageView.setImageBitmap(b);

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

        uploadProgress = 0;
        progressDialog.show();
        progressDialog.setCancelable(true);
        //sosApi.uploadProfilePic(this,imgStr);
        String serverFileName = sosApi.GSV(SOS_API.KEY_ACC_DATA_MOBILE_HASH) + ".jpg";
        String serverRelRootDir = SOS_API.SERVER_REL_ROOT_DIR_PATH_PROFILE_PICTURES;
        String scriptPath = SOSApplication.GSA() + SOS_API.API_URL + "act=" + SOS_API.ACTION_UPLOAD_IMAGE + "&" +
                ServerImageManager.KEY_REQ_SERVER_FILE_NAME + "=" + serverFileName + "&" +
                ServerImageManager.KEY_REQ_REL_ROOT_DIR+ "=" + serverRelRootDir;

        Log.e("UPDPH", "uploadProfilePic: -> " + scriptPath );

        /*
        $serverFileName = $_REQUEST['sfn'];
        $data = $_REQUEST['data'];
        $relRootDir = $_REQUEST['relRootDir'];
        $destination = $relRootDir . $serverFileName;*/


        ServerImageManager.UploadFileToServer(this, this, scriptPath,  localPath  );




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


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                fireCameraIntent();

            } else {

                String[] permissions = {Manifest.permission.CAMERA};
                requestPermissions(permissions, SOS_API.REQ_PERMISSION_SAVE_BITMAP);

            }
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

        progressDialog.hide();

        Log.e(TAG, "onUploadPPResult: result -> " + data.toString() );

        if(data.getString(SOS_API.JSON_KEY_RESULT).equals(SOS_API.JSON_RESULT_SUCCESS)){

            SOS_API.deletePP();

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



        File dir = SOS_API.GetSOSAchatCacheRootDir();
        if(dir == null) return;
        Log.e(TAG, "deleting cache path -> " + dir.toString() );


        if(dir.exists()) {

                BitmapCacheManager.EmptyDir(dir);
                double cacheSize = BitmapCacheManager.GetImagesCacheSize();
                updateBtnTextCacheSize(cacheSize);

                if(cacheSize == 0) {
                    Toast.makeText(this, getResources().getString(R.string.strCacheCleared), Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this, "Cache not cleared, size -> " + cacheSize + " Mb.", Toast.LENGTH_LONG).show();
                }

        }else{
            Log.e(TAG, "Folder is null. Path -> " + dir.toString() );
        }




    }
}
