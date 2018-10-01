package com.koziengineering.rhyfdocta.sosachat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.koziengineering.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.BuildConfig;
import com.koziengineering.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.koziengineering.rhyfdocta.sosachat.Helpers.SpinnerReselect;
import com.koziengineering.rhyfdocta.sosachat.Helpers.HM;
import com.koziengineering.rhyfdocta.sosachat.Helpers.HelperMethods;
import com.example.rhyfdocta.sosachat.R;
import com.koziengineering.rhyfdocta.sosachat.ServerImageManagement.ServerImage;
import com.koziengineering.rhyfdocta.sosachat.ServerImageManagement.ServerImageManager;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.Product;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.koziengineering.rhyfdocta.sosachat.app.SOSApplication;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ActivityPostItem extends AppCompatActivity implements
        SOS_API.SOSApiListener,
        //SOS_API.CallbacksImageFileUpload,
        ServerImageManager.Callbacks {


    private static final int RESULT_LOAD_IMAGE = 1200;
    private static final String KEY_TAG_PICTURE_LOADED = "picLoaded";

    private static final int REQ_PERMISSION_GALLERY = 1300;
    private static final int REQ_PERMISSION_CAMERA = 1301;
    public static final int DELAY_TO_ITEM_DET_TRANSITION = 1500;

    private ImageView ivMainItemPic;
    private ImageView curImageView;
    private String TAG = "API";

    EditText etItemName, etItemPrice, etItemDesc;

    SpinnerReselect spCat;
    Spinner spQual, spCur;
    ImageView ivPic1, ivPic2, ivPic3;
    Bundle data;
    ScrollView svActivityExposeitem;

    CheckBox cbAcceptTerms;

    AlertDialog.Builder builder;
    AlertDialog alertDialog;
    private int REQ_CAMERA = 1;
    //StringRequest requestItemDetails;
    //private RequestQueue queue;
    int ivMainDefaultSrcID, ivSmallPicDefID;
    SOS_API sosApi;
    String itemCategoryId;
    boolean itemModeEditing = false;
    Bundle editingData;
    private Spinner spType;
    String subtitle;
    String pdUniqueName = null;

    private String typeToSelect = "";
    private String catToSelect = "";
    private Button btnExposeItem;

    //private ProgressDialog progressDialog;
    private DialogUploadItem dialogUploadItem;
    /*private int totalUploadProgress = 0;
    private int progMain = 0;
    private int prog1 = 0;
    private int prog2 = 0;
    private int prog3 = 0;*/
    ServerImageManager serverImageManager;

    //List<ImageView> ITEM_IMAGEVIEWS_IDS_ARRAY = new ArrayList<>();
    //private String picType = "null";
    //private Boolean[] imagesUploaded = {false, false, false, false};
    private String curPicPath;
    //Boolean[] imagesLoaded = {false,false,false,false};  //Main pic, Pic 1, Pic 2, Pic 3

    AlertDialog alertDialogPictureSource;

    String fileName;
    private int numImgUploaded = 0;
    JSONArray itemsCategoriesJSONArray;
    String catsArray[];
    String typesArray[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);
        sosApi = SOSApplication.getInstance().getSosApi();//new SOS_API(this);


        Intent intent = getIntent();
        editingData = intent.getExtras();
        initGUI();
        setupPictureImageManager();
        subtitle = HM.getStringResource(this, R.string.subtitlePostItem);

        if(editingData != null) {

            itemModeEditing = editingData.getBoolean(SOS_API.KEY_ITEM_MODE_EDITING);
            subtitle = HM.getStringResource(this, R.string.subtitleEditing);
            cbAcceptTerms.setChecked(true);


            //pdUniqueName = editingData.getString(Product.KEY_PD_IMG);//.split(".")[0];
            pdUniqueName = editingData.getString(Product.KEY_PD_IMG).replace("_main.jpg","");
            sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, pdUniqueName);
            loadEditingData();


        }



        /*progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(HM.RGS(this, R.string.pbMsgPostingNewItem));
        progressDialog.setCancelable(false);*/

        dialogUploadItem = new DialogUploadItem(this);
        dialogUploadItem.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                Toast.makeText(ActivityPostItem.this, "Upload cancelled", Toast.LENGTH_SHORT).show();

                dialogUploadItem.init();
                serverImageManager.cancelAllUploads();
                btnExposeItem.setEnabled(true);
            }
        });

        dialogUploadItem.setMessage(HM.RGS(this, R.string.pbMsgPostingNewItem));
        dialogUploadItem.setCancelable(false);





        svActivityExposeitem = findViewById(R.id.svActivityExposeitem);

        getSupportActionBar().setTitle(subtitle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivMainDefaultSrcID = R.drawable.product_expose_main_pic;

        alertDialog = HelperMethods.getAlertDialogProcessingWithMessage(this, HelperMethods.getStringResource(this,R.string.pbMsgProcessing),false);
        sosApi.loadItemsCatsAndTypes(this);

        //prepareDataBundle();



        //toggleImageViews(SOS_API.isOnline(this));

        Log.e("XXX", "onCreatePI(),  UN : " + pdUniqueName );
    }

    private void setupPictureImageManager() {
        int[] ids = new int[]{ivMainItemPic.getId(), ivPic1.getId(), ivPic2.getId(), ivPic3.getId()};
        String[] postfixes = new String[]{SOS_API.KEY_NEW_ITEM_IMG_TYPE_MAIN,SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC1,SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC2, SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC3};
        String PRODUCTS_IMAGES_SERVER_ROOT_PATH = sosApi.GSA() + SOS_API.ROOT_URL + SOS_API.DIR_NAME_PIX_ROOT + "/" + SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS+ "/";
        serverImageManager = new ServerImageManager(this, ids, postfixes,4, PRODUCTS_IMAGES_SERVER_ROOT_PATH);
        if(itemModeEditing) serverImageManager.setUploadToken(pdUniqueName);

    }

    private void toggleImageViews(boolean enabled) {

        ivMainItemPic.setEnabled(enabled);
        ivPic1.setEnabled(enabled);
        ivPic2.setEnabled(enabled);
        ivPic3.setEnabled(enabled);

        if(enabled){
            ivMainItemPic.setAlpha(SOS_API.IMAGEVIEW_ALPHA_ENABLED);
            ivPic1.setAlpha(SOS_API.IMAGEVIEW_ALPHA_ENABLED);
            ivPic2.setAlpha(SOS_API.IMAGEVIEW_ALPHA_ENABLED);
            ivPic3.setAlpha(SOS_API.IMAGEVIEW_ALPHA_ENABLED);
        }else {
            ivMainItemPic.setAlpha(SOS_API.IMAGEVIEW_ALPHA_DISABLED);
            ivPic1.setAlpha(SOS_API.IMAGEVIEW_ALPHA_DISABLED);
            ivPic2.setAlpha(SOS_API.IMAGEVIEW_ALPHA_DISABLED);
            ivPic3.setAlpha(SOS_API.IMAGEVIEW_ALPHA_DISABLED);
        }

    }

    private void loadEditingData() {

        sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, editingData.getString(Product.KEY_PD_UNIQUE_NAME));
        pdUniqueName = sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID);
        serverImageManager.setUploadToken(pdUniqueName);

        Log.e("LED", "loadEditingData: un -> " + pdUniqueName );

        serverImageManager.loadImagesForEdit(this);

        Bundle b = new Bundle();
        b.putInt(ServerImageManager.BUNDLE_KEY, 0);
        ServerImage pi = serverImageManager.getServerImageByKey(ServerImageManager.KEY_GET_IMAGE_BY_IMAGEGALLERY_PRIORITY_ID, b);

        Uri uri = pi.getRemoteOrCacheURI();
        //loadItemEditingOldPic(uri, 0);
        /*
        loadItemEditingOldPic(pdUniqueName, SOS_API.KEY_POSTFIX_PIC_1, 1);
        loadItemEditingOldPic(pdUniqueName, SOS_API.KEY_POSTFIX_PIC_2, 2);
        loadItemEditingOldPic(pdUniqueName, SOS_API.KEY_POSTFIX_PIC_3, 3);
        */
        etItemName.setText(editingData.getString(Product.KEY_PD_NAME));
        etItemDesc.setText(editingData.getString(Product.KEY_PD_DESC));

        String cur = editingData.getString(Product.KEY_PD_CUR);
        String price = editingData.getString(Product.KEY_PD_PRICE);
        String qualId = editingData.getString(Product.KEY_PD_QUAL);
        String qual = HM.RGSA(this, R.array.newItemQuality)[Integer.parseInt(qualId)];

        etItemPrice.setText(price);
        HM.SSIWN(spCur, cur);
        HM.SSIWN(spQual, qual);

        btnExposeItem.setText(HM.RGS(this, R.string.btnUpdateItem));


    }


    private void initGUI() {

        btnExposeItem = findViewById(R.id.btnExposeItem);



        ivMainItemPic = findViewById(R.id.ivNewItemMainPic);




        ivPic1 = findViewById(R.id.ivNewItemPic1);
        ivPic2 = findViewById(R.id.ivNewItemPic2);
        ivPic3 = findViewById(R.id.ivNewItemPic3);





        etItemDesc = findViewById(R.id.etNewItemDesc);
        etItemName = findViewById(R.id.etNewItemName);
        etItemPrice = findViewById(R.id.etNewItemPrice);

        spCur = findViewById(R.id.spCur);

        spQual = findViewById(R.id.spNewItemQuality);
        spCat = findViewById(R.id.spNewItemCat);
        spType = findViewById(R.id.spNewItemType);

        cbAcceptTerms = findViewById(R.id.cbAcceptTerms);

        //setSpinnerListeners();

        itemCategoryId = "" + spCat.getSelectedItemId();

        /*
        ITEM_IMAGEVIEWS_IDS_ARRAY.add(ivMainItemPic);
        ITEM_IMAGEVIEWS_IDS_ARRAY.add(ivPic1);
        ITEM_IMAGEVIEWS_IDS_ARRAY.add(ivPic2);
        ITEM_IMAGEVIEWS_IDS_ARRAY.add(ivPic3);*/



        if(!SOS_API.isOnline(this)){
            btnExposeItem.setEnabled(false);

            sosApi.toggleAlertDialogResponseWithMessage(ActivityPostItem.this,true, HM.RGS(this, R.string.msgErrorInternetConnection));
        }


    }

    private void setSpinnerListeners() {

        spCur.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String cur = spCur.getSelectedItem().toString();
                //Toast.makeText(getApplicationContext(), cur, Toast.LENGTH_SHORT).show();

                boolean curIsToDiscuss = cur == getResources().getString(R.string.currencyToDiscuss);
                etItemPrice.setEnabled(!curIsToDiscuss);

                if(curIsToDiscuss){
                    etItemPrice.setVisibility(View.GONE);
                }else{
                    etItemPrice.setVisibility(View.VISIBLE);
                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {


                int realid = -1;
                try {
                    realid = new Integer(itemsCategoriesJSONArray.getJSONObject(i).getString(SOS_API.KEY_ITEM_CATEGORY_ID)).intValue();
                   //Log.e(TAG, "Cat selected id -> " + i + ", text -> " + spCat.getSelectedItem().toString() +", realid -> " + realid);

                        //sosApi.loadItemsTypes(ActivityPostItem.this, realid);

                    itemCategoryId = "" + realid;

                    sosApi.loadItemsTypesFromCatId(ActivityPostItem.this, realid);

                    //Log.e(TAG, "cur id -> " + spCat.getSelectedItemId() );

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private boolean checkFieldsValidity() {

        Resources res = getResources();

        //boolean noError = true;
        // TODO: 11/10/17 Check fields validity

        //NotEmpty = NE

        boolean isMainImageLoaded = false;
        if(itemModeEditing){
            isMainImageLoaded = true;//serverImageManager.isImageEdited(ivMainItemPic.getId());
        }else{
            isMainImageLoaded = serverImageManager.isImageLoaded(ivMainItemPic.getId());
        }
        boolean itemDescNE = !etItemDesc.getText().toString().isEmpty();
        boolean itemNameNE = !etItemName.getText().toString().isEmpty();

        boolean curIsToDiscuss = spCur.getSelectedItem().toString() == res.getString(R.string.currencyToDiscuss);
        boolean priceIsEmpty = etItemPrice.getText().toString().isEmpty();

        boolean itemPriceNE = curIsToDiscuss || (!curIsToDiscuss && !priceIsEmpty);

        //noError = ! (itemDescNE && itemNameNE);// && itemPriceNE);

        Log.d("BOOLCHECK", "desc name price iv " + itemDescNE + " " + itemNameNE + " " + itemPriceNE + " " + isMainImageLoaded);


        return itemDescNE && itemNameNE && itemPriceNE && isMainImageLoaded ;
    }


    @Override
    protected void onPause() {
        super.onPause();
        alertDialogPictureSource = null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        alertDialogPictureSource = null;
    }


    public void loadPic(View view){

        Log.e(TAG, "loadPic: " );

        curImageView = (ImageView)view;
        String[] sources = {getResources().getString(R.string.sourceGallery), getResources().getString(R.string.sourceCamera)};

        View viewDialog = getLayoutInflater().inflate(R.layout.alert_dialog_choose_pic_source, null);



        ImageView ivlarge = viewDialog.findViewById(R.id.ivPPLarge);

        ivlarge.setVisibility(View.GONE);


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

    private void startCameraApp() {


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                fireCameraIntent();

            } else {

                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
                requestPermissions(permissions, REQ_PERMISSION_CAMERA);

            }
        }else{
            fireCameraIntent();
        }

    }

    private void startGallery() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                fireGalleryIntent();

            } else {

                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permissions, REQ_PERMISSION_GALLERY);

            }
        }else{
            fireGalleryIntent();
        }


    }

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

    private void fireGalleryIntent() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    private void fireCameraIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = getCameraPictureFile();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N){
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));

        }else{
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(ActivityPostItem.this,
                    BuildConfig.APPLICATION_ID + ".provider", file));
        }


        startActivityForResult(intent, REQ_CAMERA);


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

    // TODO: 12/19/2017 SETUP REAL REQ SIZE FROM IMAGE VIEWS

    int[] reqSize = {300,300};
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //setCurrentIvType(curImageView);
        ServerImage curPI = null;
        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn,null,null,null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            curPicPath = picturePath;


            Bundle bundle = new Bundle();
            bundle.putInt(ServerImageManager.BUNDLE_KEY, curImageView.getId());
            ServerImage serverImage = serverImageManager.getServerImageByKey(ServerImageManager.KEY_GET_IMAGE_BY_IMAGEVIEWS_IDS,
                    bundle);
            curPI = serverImage;

            Log.e(TAG, "PICTYPE : " + serverImage.getImagePostfix() + ", IMAGE PATH GALLERY -> " + picturePath );
            cursor.close();


            BitmapCacheManager.LoadBitmapFilePathIntoImageView(curImageView, picturePath);


            if(itemModeEditing) {
                serverImageManager.setImageEdited(curImageView.getId(), picturePath);
            }else{
                serverImageManager.setImageLoaded(curImageView.getId(), picturePath);
            }

            Log.e("CPI",  curPI.toString());
            Log.e("CPI", serverImageManager.toString() );
        }



        if(requestCode == REQ_CAMERA && resultCode == RESULT_OK){

            String picturePath = SOS_API.GSAIPCP() + "/" + fileName;

            //curPicPath = path;

            //curImageView.setImageDrawable(Drawable.createFromPath(path));

            Bundle bundle = new Bundle();
            bundle.putInt(ServerImageManager.BUNDLE_KEY, curImageView.getId());
            ServerImage serverImage = serverImageManager.getServerImageByKey(ServerImageManager.KEY_GET_IMAGE_BY_IMAGEVIEWS_IDS,
                    bundle);
            curPI = serverImage;

            //Log.e(TAG, "PICTYPE : " + serverImage.getImagePostfix() + ", IMAGE PATH CAMERA -> " + picturePath );

            BitmapCacheManager.LoadBitmapFilePathIntoImageView(curImageView, picturePath);

            if(itemModeEditing) {
                serverImageManager.setImageEdited(curImageView.getId(), picturePath);
            }else{
                serverImageManager.setImageLoaded(curImageView.getId(), picturePath);
            }

            Log.e("CPI",  curPI.toString());
            Log.e("CPI", serverImageManager.toString() );

        }




        //NEW_ITEM_IMAGES_TYPES_AND_URLS.put(picType, curPicPath);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()){
            case android.R.id.home:
                resetProductUniqueID();
                finish();
                break;

            case R.id.menuResetExposeForm:
                resetExposeForm();
                break;


        }



        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_post_item, menu);
        return true;
    }

    private void resetExposeForm() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("Reset form?")
                .setMessage("Are you sure you wanna reset the form?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        clearAllFields();
                        Toast.makeText(ActivityPostItem.this, getResources().getString(R.string.toastMsgFormReset), Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setCancelable(false);

        builder.show();


        deleteCameraPictureFiles();






    }

    private void clearAllFields() {
        etItemName.setText("");
        etItemDesc.setText("");
        etItemPrice.setText("");
        etItemDesc.setText("");
        spCur.setSelection(0);
        cbAcceptTerms.setChecked(false);

        ivMainItemPic.setImageResource(R.drawable.product_expose_main_pic);
        ColorDrawable cd =new ColorDrawable(getResources().getColor(R.color.sosBlue));
        ivPic1.setImageDrawable(cd);
        ivPic2.setImageDrawable(cd);
        ivPic3.setImageDrawable(cd);
    }

    private void deleteCameraPictureFiles() {
        // TODO: 11/15/17 SHOULD CLEAN UP ALL CAMERA FILES IN sdcard/SOSAchat DIRECTORY
    }

    public void showMessageDialog(String title,String message)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
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

    public void toggleProgressDialog(boolean show)
    {


        if(show) {
            alertDialog.show();
        }else{
            alertDialog.hide();
        }
    }

    public void exposeItem(View v){
       //Toast.makeText(this, "ITEM WILL BE POSTED", Toast.LENGTH_SHORT).show();


        btnExposeItem.setEnabled(false);
        if(!cbAcceptTerms.isChecked()){

            //showMessage( getResources().getString(R.string.titleTermsAndCondition),
                   //getResources().getString(R.string.msgTermAndCondDialog));


            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.titleTermsAndCondition))
                    .setMessage(  getResources().getString(R.string.msgTermAndCondDialog))
                    .setPositiveButton(getResources().getString(R.string.btnReadTermsNconds), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(ActivityPostItem.this, ActivityTermsAndCond.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.btnOk), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            btnExposeItem.setEnabled(true);

                        }
                    });


            builder.show();



        }




        if(!checkFieldsValidity()) {


            showMessageDialog(getResources().getString(R.string.msgEmptyFields),
                    getResources().getString(R.string.msgPostItemFieldsIncomplete));

            btnExposeItem.setEnabled(true);
            return;
        }




        if(!SOS_API.isOnline(this)){
            sosApi.gotoNoNetworkActivity();
        }else if(itemModeEditing){
            sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, pdUniqueName);
            Log.e("XXX", "exposeItem(), UN -> " + pdUniqueName );

            uploadImageToServer();
        }
        else{

            String unid = sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID);

            if(unid.equals(SOS_API.KEY_SESSION_DATA_EMPTY)){
                sosApi.getNewItemUniqueId(new SOS_API.CallbacksUniqueID() {
                    @Override
                    public void onUniqueIDLoaded(String un) {
                        sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, un);
                        serverImageManager.setUploadToken(un);

                        Log.e(TAG, "onUniqueIDLoaded: -> " + un);
                        uploadImageToServer();

                    }

                    @Override
                    public void onError(String error) {

                        sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, SOS_API.KEY_SESSION_DATA_EMPTY);
                        serverImageManager.setUploadToken(null);
                        Toast.makeText(ActivityPostItem.this,
                                getResources().getString(R.string.msgNetEr), Toast.LENGTH_LONG).show();

                        Log.e(TAG, "Error getting UID : -> " + error );
                        btnExposeItem.setEnabled(true);
                    }
                });
            }else{
                Log.e(TAG, "onUniqueID FROM SESSION -> " + unid);
                serverImageManager.setUploadToken(unid);
                uploadImageToServer();
            }

            Log.e(TAG, "PIM STATS : \n\n" + serverImageManager.toString());
            Log.e(TAG, "ALL PD IMAGES : \n\n" + serverImageManager.toStringAllProducImages() );


        }

        /*
        if(SOS_API.isOnline(this)) {
            sosApi.getNewItemUniqueId(new SOS_API.CallbacksUniqueID() {
                @Override
                public void onUniqueIDLoaded(String un) {
                    Log.e(TAG, "onUniqueIDLoaded: -> " + un);



                    if(sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID).equals(SOS_API.KEY_SESSION_DATA_EMPTY)){
                        sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, un);


                    }

                    pdUniqueName = sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID);

                    uploadImageToServer();

                }

                @Override
                public void onError(String error) {

                    Toast.makeText(ActivityPostItem.this,"Error getting UID : " + error, Toast.LENGTH_LONG).show();
                }
            });
        }else{

        }*/
    }

    /*
    private boolean allImagesUploaded() {
        return imagesUploaded[0] && imagesUploaded[1] && imagesUploaded[2] && imagesUploaded[3];
    }*/



    @Override
    public void onProducImageManagerProgress(ServerImage serverImage, int progress, int totalProgress) {
        Log.e(TAG, "total : " + totalProgress + " %" );
        //progressDialog.setProgress(totalProgress);
        dialogUploadItem.setProgress(totalProgress);
    }



    @Override
    public void onProducImageManagerPostExecute(ServerImage pi) {
        Log.e(TAG, "onProducImageManagerPostExecute: -> " + pi.getImagePostfix() );
    }

    @Override
    public void onProducImageManagerPreExecute(ServerImage pi) {
        Log.e(TAG, "onProducImageManagerPreExecute: -> " + pi.getImagePostfix() );
    }

    @Override
    public void onProducImageAllImagesUploadedComplete() {
        Log.e("BAAM", "onProducImageAllImagesUploadedComplete: " );


        pdUniqueName = sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID);
        data.putString(Product.KEY_PD_UNIQUE_NAME, pdUniqueName);
        data.putString(Product.KEY_PD_UNIQUE_ID, pdUniqueName);
        sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, SOS_API.KEY_SESSION_DATA_EMPTY);

        //numImgUploaded = 0;
        sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, SOS_API.KEY_SESSION_DATA_EMPTY);
        //progressDialog.dismiss();
        dialogUploadItem.dismiss();
        btnExposeItem.setEnabled(false);
        toggleImageViews(false);



        sosApi.exposeItem(ActivityPostItem.this, data);

    }

    @Override
    public void onProducImageTotalDataSize(double totalDataSize) {
        dialogUploadItem.setDataSize(totalDataSize);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        resetProductUniqueID();
    }

    private void resetProductUniqueID() {

        Log.e("XXX", "resetProductUniqueID: " );
        sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, SOS_API.KEY_SESSION_DATA_EMPTY);

    }

    private void uploadImageToServer() {

        prepareDataBundle();

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = layoutInflater.inflate(R.layout.layout_dialog_input_password, null);
        final EditText etpwd = v.findViewById(R.id.etDgPassword);


        new AlertDialog.Builder(ActivityPostItem.this)
                //.setTitle(HM.RGS(ActivityAccountSettings.this, R.string.dgTitleInputPassword))
                .setView(v)
                .setPositiveButton("POST", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(etpwd.getText().toString().equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_PASSWORD))) {

                            //progressDialog.show();
                            dialogUploadItem.show();

                            //Message msg = new Message();
                            //progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", msg);

                            serverImageManager.setUploadToken(sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID));
                            serverImageManager.uploadAllImagesToServer(ActivityPostItem.this, itemModeEditing);
                            btnExposeItem.setEnabled(false);

                        }else{
                            Toast.makeText(ActivityPostItem.this, HM.RGS(ActivityPostItem.this, R.string.tmsgPwdNotCorrect), Toast.LENGTH_LONG).show();
                            btnExposeItem.setEnabled(true);
                        }
                    }
                })
                .setCancelable(false)
                .setNegativeButton("CANCEL", null).show();

            //to remove
            //progressDialog.setCancelable(true);




    }


    private void prepareDataBundle() {

        String currency = spCur.getSelectedItem().toString();

        String[] curs = HelperMethods.RGSA(this, R.array.currencies);

        String priceToDiscuss = currency.equalsIgnoreCase(curs[2]) ? "1" : "0";
        if(priceToDiscuss.equalsIgnoreCase("1")){
            currency = HM.getStringResource(this, R.string.strNA);
        }





        // TODO: 12/15/2017 PREPARE DATA BUNDLE TO EXPOSE ITEM
        data = new Bundle();

        data.putString(SOS_API.KEY_ITEM_NAME, etItemName.getText().toString());
        data.putString(SOS_API.KEY_ITEM_PRICE, etItemPrice.getText().toString());
        data.putString(SOS_API.KEY_ITEM_CURRENCY, currency);
        data.putString(SOS_API.KEY_ITEM_PRICE_TO_DISCUSS, priceToDiscuss);
        data.putString(SOS_API.KEY_ITEM_DESCRIPTION, etItemDesc.getText().toString());
        data.putString(SOS_API.KEY_ITEM_CATEGORY, spCat.getSelectedItem().toString());

        data.putString(SOS_API.KEY_ITEM_TYPE, spType.getSelectedItem().toString());
        data.putString(Product.KEY_PD_CAT, spCat.getSelectedItem().toString());
        data.putString(Product.KEY_PD_OWNER_ID, sosApi.getSessionVar(SOS_API.KEY_ACC_DATA_USER_ID));

        // TODO: 5/24/2018 Upload Pic with progress

        data.putString(SOS_API.KEY_ITEM_MAIN_PIC, SOS_API.TRUE);//getBase64Pic(SOS_API.KEY_ITEM_MAIN_PIC));
        data.putString(SOS_API.KEY_ITEM_PIC_1,SOS_API.KEY_ITEM_NO_PIC);
        data.putString(SOS_API.KEY_ITEM_PIC_2,SOS_API.KEY_ITEM_NO_PIC);
        data.putString(SOS_API.KEY_ITEM_PIC_3,SOS_API.KEY_ITEM_NO_PIC);




        /*
        if(imagesLoaded[1] == true) {
            data.putString(SOS_API.KEY_ITEM_PIC_1, SOS_API.TRUE);//getBase64Pic(SOS_API.KEY_ITEM_PIC_1));

        }

        if(imagesLoaded[2] == true) {
            data.putString(SOS_API.KEY_ITEM_PIC_2, SOS_API.TRUE);//getBase64Pic(SOS_API.KEY_ITEM_PIC_2));
        }

        if(imagesLoaded[3] == true) {
            data.putString(SOS_API.KEY_ITEM_PIC_3, SOS_API.TRUE);//getBase64Pic(SOS_API.KEY_ITEM_PIC_3));
        }*/

        data.putString(SOS_API.KEY_ITEM_QUALITY, spQual.getSelectedItem().toString());

        if(itemModeEditing) {

            String itemId = editingData.getString(SOS_API.KEY_ITEM_ID);
            data.putString(SOS_API.KEY_ITEM_ID, itemId);
            data.putString(Product.KEY_PD_UNIQUE_NAME, sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID));
        }




    }

    private String getBase64Pic(String pic) {
        String imgString = "no_pic";

        if(pic == SOS_API.KEY_ITEM_MAIN_PIC){

            byte[] imgBytes = HelperMethods.getByteArrayFromImageView(ivMainItemPic);
            imgString =  Base64.encodeToString(imgBytes, Base64.DEFAULT);
        }

        if(pic == SOS_API.KEY_ITEM_PIC_1 ){

            byte[] imgBytes = HelperMethods.getByteArrayFromImageView(ivPic1);
            imgString =  Base64.encodeToString(imgBytes, Base64.DEFAULT);
        }

        if(pic == SOS_API.KEY_ITEM_PIC_2 ){

            byte[] imgBytes = HelperMethods.getByteArrayFromImageView(ivPic2);
            imgString =  Base64.encodeToString(imgBytes, Base64.DEFAULT);
        }

        if(pic == SOS_API.KEY_ITEM_PIC_3 ){

            byte[] imgBytes = HelperMethods.getByteArrayFromImageView(ivPic3);
            imgString =  Base64.encodeToString(imgBytes, Base64.DEFAULT);
        }


        return imgString;

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
    public void onExposeItemResult(final Bundle data) {
        Log.e(TAG, "onExposeItemResult: data -> " + data.toString() );
        toggleProgressDialog(false);
        btnExposeItem.setEnabled(true);

        boolean itemUpdated = data.getBoolean(SOS_API.KEY_ITEM_UPDATED);

        if(data.getString(SOS_API.JSON_KEY_RESULT).equals(SOS_API.JSON_RESULT_SUCCESS)){

            Log.e(TAG, "onExposeItemResult: -> " + data.getString(SOS_API.JSON_KEY_RESULT) );




            String congratMsg;

            if(itemUpdated){
                congratMsg = HelperMethods.getStringResource(this, R.string.msgUpdateItemSuccess);
            }else{
                congratMsg = HelperMethods.getStringResource(this, R.string.msgExposeItemSuccess);
            }

            Toast.makeText(this, congratMsg, Toast.LENGTH_SHORT).show();

            clearAllFields();


            Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                gotoMyProducts(data);
                                btnExposeItem.setEnabled(true);
                            }
                        }, Toast.LENGTH_SHORT + DELAY_TO_ITEM_DET_TRANSITION);



        }else{
            Toast.makeText(this, HelperMethods.getStringResource(this, R.string.msgExposeItemError), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLoadAllMyProductsResult(List<ProductMyProducts> myProducts, boolean networkError) {

    }

    private void gotoMyProducts(Bundle data) {
        Intent intent = new Intent(this, ActivityMyProducts.class);
        intent.putExtras(data);
        startActivity(intent);

    }



    @Override
    public void onLoadItemsCats(JSONArray catsAndTypes) {

        itemsCategoriesJSONArray = catsAndTypes;
        JSONObject curCat = null;
        JSONObject curType = null;

        try {
            if (catsAndTypes.length() > 0) {

                catsArray = new String[catsAndTypes.length()];

                for (int i = 0; i < catsAndTypes.length(); i++) {

                    JSONObject cat = catsAndTypes.getJSONObject(i);
                   //Log.e(TAG, "onLoadItemsCats: cat item -> " + jo.toString() );
                    String curCatName = cat.getString(SOS_API.KEY_ITEM_CATEGORY_NAME);
                    String curCatId = cat.getString(SOS_API.KEY_ITEM_CATEGORY_ID);
                    catsArray[i] = curCatName;
                    if(itemModeEditing) {
                        if (curCatId.equalsIgnoreCase(editingData.getString(Product.KEY_PD_CAT))) {
                            curCat = catsAndTypes.getJSONObject(i);
                            catToSelect = curCat.getString(SOS_API.KEY_ITEM_CATEGORY_NAME);

                            JSONArray types = curCat.getJSONArray("cat_types");

                            if(types.length() > 0){

                                typesArray = new String[types.length()];

                                for(int j = 0; j < types.length(); j++){

                                    JSONObject type = types.getJSONObject(j);
                                    typesArray[j] = type.getString(SOS_API.KEY_ITEM_TYPE_NAME);

                                    //Log.e(TAG, "FRIGIN TYPES " + typesArr );

                                    if(type.getString(SOS_API.KEY_ITEM_TYPE_ID).equalsIgnoreCase(editingData.getString(Product.KEY_PD_TYPE))){
                                        SpinnerAdapter adp = spType.getAdapter();
                                        Log.e(TAG, "DA TYPE" + type.toString() );
                                        curType = type;
                                        typeToSelect = curType.getString(SOS_API.KEY_ITEM_TYPE_NAME);
                                        //HM.SSIWN(spType, editingData.getString(SOS_API.KEY_ITEM_TYPE_NAME));

                                    }

                                }

                                HM.LSP(spType, typesArray, new HelperMethods.SpinnerLoaderListener() {
                                    @Override
                                    public void onLoadComplete() {


                                        if(itemModeEditing){
                                        HM.SSIWN(spType, typeToSelect);

                                        }
                                    }
                                });
                                //HM.SSIWN(spType, typeToSelect);

                            }else{
                                //Log.e(TAG, "TYPES IS ZERO" );
                                HM.LSP(spType, new String[]{HM.getStringResource(ActivityPostItem.this, R.string.strNA)}, null);
                            }

                        }
                    }

                }

                HM.LSP(spCat, catsArray, new HelperMethods.SpinnerLoaderListener() {
                    @Override
                    public void onLoadComplete() {
                        if(itemModeEditing) {

                            HM.SSIWN(spCat, catToSelect);
                        }
                    }
                });




                setSpinnerListeners();



            }
        }catch (JSONException e){
            e.printStackTrace();
           //Log.e(TAG, "onLoadItemsCats: exception -> " + e.getMessage() );
        }




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

    }

    @Override
    public void onLoadItemsTypesResultError() {
       //Log.e(TAG, "onLoadItemsTypesResultError: -> "+ data.toString() );

        String[] array = new String[0];
        HM.LSP(spType, array, null);


    }



    @Override
    public void onLoadItemsTypesResult(String[] types) {

        HM.LSP(spType, types,null);

        if(itemModeEditing){
            String typeId = editingData.getString(SOS_API.KEY_ITEM_TYPE_ID);
            String catId = editingData.getString(SOS_API.KEY_ITEM_CATEGORY_ID);
            sosApi.loadCatTypeNames(this, catId, typeId);

        }
    }



    @Override
    public void onLoadCatsTypesNames(String cn, String tn) {



        //HM.SSIWN(spCat, cn);
        HM.SSIWN(spType, tn);




    }

    @Override
    public void onCategoryTypesLoaded(List<TypesItem> types, boolean errorLoading) {

    }



}
