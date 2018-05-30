package com.example.rhyfdocta.sosachat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
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

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.Helpers.SpinnerReselect;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.Helpers.HelperMethods;
import com.example.rhyfdocta.sosachat.ImageManager.ProductImage;
import com.example.rhyfdocta.sosachat.ImageManager.ProductImageManager;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;

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
        ProductImageManager.Callbacks{


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

    private ProgressDialog progressDialog;
    /*private int totalUploadProgress = 0;
    private int progMain = 0;
    private int prog1 = 0;
    private int prog2 = 0;
    private int prog3 = 0;*/
    ProductImageManager productImageManager;

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
        sosApi = new SOS_API(this);


        Intent intent = getIntent();
        editingData = intent.getExtras();
        initGUI();
        subtitle = HM.getStringResource(this, R.string.subtitlePostItem);

        if(editingData != null) {

            itemModeEditing = editingData.getBoolean(SOS_API.KEY_ITEM_MODE_EDITING);
            subtitle = HM.getStringResource(this, R.string.subtitleEditing);
            cbAcceptTerms.setChecked(true);


            //pdUniqueName = editingData.getString(Product.KEY_PD_IMG);//.split(".")[0];
            pdUniqueName = editingData.getString(Product.KEY_PD_IMG).replace("_main.jpg","");

            loadEdintingData();


        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);




        svActivityExposeitem = (ScrollView) findViewById(R.id.svActivityExposeitem);

        getSupportActionBar().setTitle(subtitle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivMainDefaultSrcID = R.drawable.product_expose_main_pic;

        alertDialog = HelperMethods.getAlertDialogProcessingWithMessage(this, HelperMethods.getStringResource(this,R.string.pbMsgProcessing),false);
        sosApi.loadItemsCatsAndTypes(this);

        //prepareDataBundle();



        //toggleImageViews(SOS_API.isOnline(this));
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

    private void loadEdintingData() {

        sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, editingData.getString(Product.KEY_PD_UNIQUE_NAME));
        pdUniqueName = sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID);

        loadItemEditingOldPic(pdUniqueName, SOS_API.KEY_POSTFIX_PIC_MAIN, 0);
        loadItemEditingOldPic(pdUniqueName, SOS_API.KEY_POSTFIX_PIC_1, 1);
        loadItemEditingOldPic(pdUniqueName, SOS_API.KEY_POSTFIX_PIC_2, 2);
        loadItemEditingOldPic(pdUniqueName, SOS_API.KEY_POSTFIX_PIC_3, 3);

        etItemName.setText(editingData.getString(Product.KEY_PD_NAME));
        etItemDesc.setText(editingData.getString(Product.KEY_PD_DESC));

        String cur = editingData.getString(Product.KEY_PD_CUR);
        String price = editingData.getString(Product.KEY_PD_PRICE);
        String qualId = editingData.getString(Product.KEY_PD_QUAL);
        String qual = HM.RGSA(this, R.array.newItemQuality)[new Integer(qualId).intValue()];

        etItemPrice.setText(price);
        HM.SSIWN(spCur, cur);
        HM.SSIWN(spQual, qual);

        btnExposeItem.setText(HM.RGS(this, R.string.btnUpdateItem));

        //Log.e(TAG, "CUR, PRICE -> " + cur + ", " + price );
    }

    private void loadItemEditingOldPic(String un, String picType, final int idx) {


        //imagesLoaded = new Boolean[]{true, true, true, true};


        //String picMain = editingData.getString(SOS_API.KEY_PD_MAIN_PIC_URI);

        String picName = un + picType;
        final String url = SOS_API.DIR_PATH_PRODUCTS_PIX + picName ;
        Uri picUri = Uri.parse(url);
        //Log.e(TAG, "loadItemEditingOldPic: -> " + url + "\nUN : " + un );

        final String cachePath = BitmapCacheManager.GetImageCachePath(BitmapCacheManager.PIC_CACHE_PATH_TYPE_PRODUCTS, picName);
        if(BitmapCacheManager.FileExists(cachePath)){
            picUri = Uri.fromFile(new File(cachePath));


            Log.e(TAG, "PIC_PATH : -> " + picUri.toString() );

            //Toast.makeText(this, "Loade from cache", Toast.LENGTH_SHORT).show();

        }else{
            Log.e(TAG, "NO_CACHE -> " + picUri.toString() );
            //Toast.makeText(this, "Loade from network", Toast.LENGTH_SHORT).show();
        }

        /*
        Glide.with(this)
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


                        sosApi.getBitmapCacheManager().saveBitmapToCache(resource, url, SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS);

                        ITEM_IMAGEVIEWS_IDS_ARRAY.get(idx).setImageBitmap(resource);
                        imagesLoaded[idx] = true;
                        String tag = SOS_API.GetPicExtTagByIndex(idx);

                        NEW_ITEM_IMAGES_TYPES_AND_URLS.put(tag, cachePath);

                        Log.e(TAG, "onResourceReady: -> " + NEW_ITEM_IMAGES_TYPES_AND_URLS );

                    }
                });
        */

    }



    private void initGUI() {

        btnExposeItem = (Button) findViewById(R.id.btnExposeItem);



        ivMainItemPic = (ImageView) findViewById(R.id.ivNewItemMainPic);




        ivPic1 = (ImageView) findViewById(R.id.ivNewItemPic1);
        ivPic2 = (ImageView) findViewById(R.id.ivNewItemPic2);
        ivPic3 = (ImageView) findViewById(R.id.ivNewItemPic3);





        etItemDesc = (EditText) findViewById(R.id.etNewItemDesc);
        etItemName = (EditText) findViewById(R.id.etNewItemName);
        etItemPrice = (EditText) findViewById(R.id.etNewItemPrice);

        spCur = (Spinner) findViewById(R.id.spCur);

        spQual = (Spinner) findViewById(R.id.spNewItemQuality);
        spCat = (SpinnerReselect) findViewById(R.id.spNewItemCat);
        spType = (Spinner) findViewById(R.id.spNewItemType);

        cbAcceptTerms = (CheckBox) findViewById(R.id.cbAcceptTerms);

        //setSpinnerListeners();

        itemCategoryId = "" + spCat.getSelectedItemId();

        /*
        ITEM_IMAGEVIEWS_IDS_ARRAY.add(ivMainItemPic);
        ITEM_IMAGEVIEWS_IDS_ARRAY.add(ivPic1);
        ITEM_IMAGEVIEWS_IDS_ARRAY.add(ivPic2);
        ITEM_IMAGEVIEWS_IDS_ARRAY.add(ivPic3);*/

        int[] ids = new int[]{ivMainItemPic.getId(), ivPic1.getId(), ivPic2.getId(), ivPic3.getId()};
        String[] postfixes = new String[]{SOS_API.KEY_NEW_ITEM_IMG_TYPE_MAIN,SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC1,SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC2, SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC3};
        String PRODUCTS_IMAGES_SERVER_ROOT_PATH = SOS_API.ROOT_URL + SOS_API.DIR_NAME_PIX_ROOT + "/" + SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS+ "/";
        productImageManager = new ProductImageManager(this, ids, postfixes,4, PRODUCTS_IMAGES_SERVER_ROOT_PATH);


        if(!SOS_API.isOnline(this)){
            btnExposeItem.setEnabled(false);

            sosApi.toggleAlertDialogResponseWithMessage(true, HM.RGS(this, R.string.msgErrorInternetConnection));
        }


    }

    /*private List<Integer> ivReqSizes = new ArrayList<>();

    private void setIvReqSize(final int idx, final ImageView imageView){
        ViewTreeObserver viewTreeObserver = imageView.getViewTreeObserver();

        if(viewTreeObserver.isAlive()){
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                    imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    int w = imageView.getWidth();
                    int h = imageView.getHeight();

                    ivReqSizes.add(w);
                    ivReqSizes.add(h);

                    Log.e(TAG, "sizes : " + ivReqSizes.size() );
                }
            });
        }


    }*/


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

        boolean isMainImageLoaded = productImageManager.isImageLoaded(ivMainItemPic.getId());
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
               /* .setNegativeButton(getResources().getString(R.string.btnCancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

*/



       /* AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.msgChooseYourPictureSource))
                .setSingleChoiceItems(sources, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        onPictureSourceSelected(which);

                    }
                })
                .setNegativeButton(getResources().getString(R.string.btnCancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });*/

        alertDialogPictureSource = builder.create();
        alertDialogPictureSource.show();



    }

    // TODO: 5/30/2018 F2
    /*
    private void setItemPicAdded(int id) {

        if(id == ivMainItemPic.getId()){
            imagesLoaded[0] = true;
        }

        if(id == ivPic1.getId()){
            imagesLoaded[1] = true;
        }

        if(id == ivPic2.getId()){
            imagesLoaded[2] = true;
        }

        if(id == ivPic3.getId()){
            imagesLoaded[3] = true;
        }

       //Log.e(TAG, "setItemPicAdded: -> " + imagesLoaded.toString() );


    }
    */

    public void dialogOnRowClick(View view){
        Toast.makeText(this, "Dialog Row Clicked", Toast.LENGTH_SHORT).show();
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



        if(checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){

            fireCameraIntent();

        }else{

            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            requestPermissions(permissions, REQ_PERMISSION_CAMERA);

        }

    }

    private void startGallery() {


        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

            fireGalleryIntent();

        }else{

            String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions, REQ_PERMISSION_GALLERY);

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
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
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
        ProductImage curPI = null;
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
            bundle.putInt(ProductImageManager.BUNDLE_KEY, curImageView.getId());
            ProductImage productImage = productImageManager.getProductImageByKey(ProductImageManager.KEY_GET_IMAGE_BY_IMAGEVIEWS_IDS,
                    bundle);
            curPI = productImage;

            Log.e(TAG, "PICTYPE : " + productImage.getImagePostfix() + ", IMAGE PATH GALLERY -> " + picturePath );
            cursor.close();

            /*
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=4;      // 1/8 of original image
            Bitmap b = BitmapFactory.decodeFile(picturePath, options);*/

            //reqSize = {300,300};



            Bitmap b = HM.DSBFF(picturePath, reqSize[0], reqSize[1]);

            // TODO: 11/16/17 COMPRESS ON BYTES COUNT THRESHOLD

           //Log.e(TAG, "BYTES CT " + b.getByteCount());

            curImageView.setImageBitmap(b);
            productImageManager.setImageLoaded(curImageView.getId(), picturePath);



            //setItemPicAdded(curImageView.getId());
        }



        if(requestCode == REQ_CAMERA && resultCode == RESULT_OK){

            String path = SOS_API.GSAIPCP() + "/" + fileName;

            //curPicPath = path;

            //curImageView.setImageDrawable(Drawable.createFromPath(path));

            Bundle bundle = new Bundle();
            bundle.putInt(ProductImageManager.BUNDLE_KEY, curImageView.getId());
            ProductImage productImage = productImageManager.getProductImageByKey(ProductImageManager.KEY_GET_IMAGE_BY_IMAGEVIEWS_IDS,
                    bundle);
            curPI = productImage;

            Log.e(TAG, "PICTYPE : " + productImage.getImagePostfix() + ", IMAGE PATH CAMERA -> " + path );
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=4;      // 1/8 of original image
            Bitmap b = BitmapFactory.decodeFile(path, options);

            //reqSize = {300,300};


            //Bitmap b = (Bitmap) data.getExtras().get("data");
            //Bitmap b = HM.DSBFF(path, reqSize[0], reqSize[1]);

            curImageView.setImageBitmap(b);
            productImageManager.setImageLoaded(curImageView.getId(), path);



            //setItemPicAdded(curImageView.getId());


        }

        Log.e("CPI",  curPI.toString());
        Log.e("CPI", productImageManager.toString() );


        //NEW_ITEM_IMAGES_TYPES_AND_URLS.put(picType, curPicPath);
    }

    // TODO: 5/30/2018 F3
    //HashMap<String, String> NEW_ITEM_IMAGES_TYPES_AND_URLS = new HashMap<>();

    // TODO: 5/30/2018 F1
    /*
    private String setCurrentIvType(ImageView curImageView) {
        String iv = "null";


        switch (curImageView.getId()) {
            case R.id.ivNewItemMainPic:
                picType = iv = SOS_API.KEY_NEW_ITEM_IMG_TYPE_MAIN;
                //NEW_ITEM_IMAGES_TYPES_AND_URLS.put(SOS_API.KEY_NEW_ITEM_IMG_TYPE_MAIN, iv);
                break;

            case R.id.ivNewItemPic1:
                picType = iv = SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC1;
                //NEW_ITEM_IMAGES_TYPES_AND_URLS.put(SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC1, iv);
                break;

            case R.id.ivNewItemPic2:
                picType = iv = SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC2;
                //NEW_ITEM_IMAGES_TYPES_AND_URLS.put(SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC2, iv);
                break;

            case R.id.ivNewItemPic3:
                picType = iv = SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC3;
                //NEW_ITEM_IMAGES_TYPES_AND_URLS.put(SOS_API.KEY_NEW_ITEM_IMG_TYPE_PIC3, iv);
                break;
        }




        return iv;
    }
    */


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()){
            case android.R.id.home:
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

                        }
                    });


            builder.show();



        }




        if(!checkFieldsValidity()) {


            showMessageDialog(getResources().getString(R.string.msgEmptyFields),
                    getResources().getString(R.string.msgPostItemFieldsIncomplete));
            return;
        }


        prepareDataBundle();

        if(!SOS_API.isOnline(this)){
            sosApi.gotoNoNetworkActivity();
        }else{

            String unid = sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID);

            if(unid.equals(SOS_API.KEY_SESSION_DATA_EMPTY)){
                sosApi.getNewItemUniqueId(new SOS_API.CallbacksUniqueID() {
                    @Override
                    public void onUniqueIDLoaded(String un) {
                        sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, un);
                        productImageManager.setUploadToken(un);

                        Log.e(TAG, "onUniqueIDLoaded: -> " + un);
                        uploadImageToServer();

                    }

                    @Override
                    public void onError(String error) {

                        sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, SOS_API.KEY_SESSION_DATA_EMPTY);
                        productImageManager.setUploadToken(null);
                        Toast.makeText(ActivityPostItem.this,
                                "Error getting UID : " + error, Toast.LENGTH_LONG).show();
                    }
                });
            }else{
                Log.e(TAG, "onUniqueID FROM SESSION -> " + unid);
                productImageManager.setUploadToken(unid);
                uploadImageToServer();
            }

            Log.e(TAG, "PIM STATS : \n\n" + productImageManager.toString());
            Log.e(TAG, "ALL PD IMAGES : \n\n" + productImageManager.toStringAllProducImages() );


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
    public void onProducImageManagerProgress(ProductImage productImage, int progress, int totalProgress) {
        Log.e(TAG, "total : " + totalProgress + " %" );
        progressDialog.setProgress(totalProgress);
    }



    @Override
    public void onProducImageManagerPostExecute(ProductImage pi) {
        Log.e(TAG, "onProducImageManagerPostExecute: -> " + pi.getImagePostfix() );
    }

    @Override
    public void onProducImageManagerPreExecute(ProductImage pi) {
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
        progressDialog.dismiss();
        btnExposeItem.setEnabled(false);
        sosApi.exposeItem(this, data);
    }

    private void uploadImageToServer() {

            //to remove
            //progressDialog.setCancelable(true);
            progressDialog.show();

            productImageManager.setUploadToken(sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID));
            productImageManager.uploadAllImagesToServer(this);


            //totalUploadProgress = progMain = prog1 = prog2 = prog3 = 0;
            //data.getString(Product.KEY_PD_UNIQUE_NAME);
/*
            Bundle metaData = new Bundle();
            metaData.putString(SOS_API.KEY_NEW_ITEM_IMG_TYPE, SOS_API.KEY_NEW_ITEM_IMG_TYPE_MAIN);
            String tag = SOS_API.KEY_NEW_ITEM_IMG_TYPE_MAIN;
            String fileName = sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID) + SOS_API.KEY_ITEM_POST_FIX_MAIN_PIC;
            String dirPath = Html.escapeHtml(SOS_API.DIR_NAME_PIX_ROOT + "/" + SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS + "/");

            Log.e(TAG, "DPATH -> " + dirPath );*/

            /*
            curPicPath = NEW_ITEM_IMAGES_TYPES_AND_URLS.get(tag);
            sosApi.uploadPicFile(
                    this,
                    curPicPath,
                    fileName,
                    dirPath,
                    tag,
                    metaData
            );

            if(imagesLoaded[1]){

                tag = SOS_API.KEY_ITEM_POST_FIX_PIC_1;
                fileName = sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID) + tag;
                curPicPath = NEW_ITEM_IMAGES_TYPES_AND_URLS.get(tag);

                sosApi.uploadPicFile(
                        this,
                        curPicPath,
                        fileName,
                        dirPath,
                        tag,
                        metaData
                );
            }

        if(imagesLoaded[2]){

            tag = SOS_API.KEY_ITEM_POST_FIX_PIC_2;
            fileName = sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID) + tag;
            curPicPath = NEW_ITEM_IMAGES_TYPES_AND_URLS.get(tag);

            sosApi.uploadPicFile(
                    this,
                    curPicPath,
                    fileName,
                    dirPath,
                    tag,
                    metaData
            );
        }

        if(imagesLoaded[3]){

            tag = SOS_API.KEY_ITEM_POST_FIX_PIC_3;
            fileName = sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID) + tag;
            curPicPath = NEW_ITEM_IMAGES_TYPES_AND_URLS.get(tag);

            //REGISTER FILE IN DB
            sosApi.uploadPicFile(
                    this,
                    curPicPath,
                    fileName,
                    dirPath,
                    tag,
                    metaData
            );
        }*/


    }


    public void CBIFUonFileWillUpload(String tag) {

        Log.e(TAG, "TAG : " + tag + ", CBIFUonFileWillUpload: " );

    }


    public void CBIFUonUploadProgress(String tag, int progress) {

        /*
        if(tag.equals(SOS_API.KEY_ITEM_POST_FIX_MAIN_PIC)) progMain = progress;
        if(tag.equals(SOS_API.KEY_ITEM_POST_FIX_PIC_1)) prog1 = progress;
        if(tag.equals(SOS_API.KEY_ITEM_POST_FIX_PIC_2)) prog2 = progress;
        if(tag.equals(SOS_API.KEY_ITEM_POST_FIX_PIC_3)) prog3 = progress;

        int progressDivider = 1;


        if(imagesLoaded[1]) progressDivider = 2;
        if(imagesLoaded[2]) progressDivider = 3;
        if(imagesLoaded[3]) progressDivider = 4;

        totalUploadProgress = (progMain + prog1 + prog2 + prog3 ) / progressDivider;

        progressDialog.setProgress(totalUploadProgress);

        Log.e(TAG, "TAG : " + tag + ",CBIFUonUploadProgress: -> " + totalUploadProgress + "%" );*/


    }


    public void CBIFUdidUpload(String tag) {
        Log.e(TAG, "TAG : " + tag + ",CBIFUdidUpload: " );
    }

    public void CBIFUonUploadFailed(String tag, Bundle data) {
        Log.e(TAG, "TAG : " + tag + ",CBIFUonUploadFailed: " );
    }

    public void CBIFUonUploadSuccess(String tag, Bundle data) {
        Log.e(TAG, "TAG : " + tag + ",CBIFUonUploadSuccess: " );
    }

    public void CBIFUonPostExecute(String tag, String result) {
        Log.e(TAG, "CBIFUonPostExecute: -> " + result );
        //pdUniqueName = null;

        numImgUploaded ++;



        /*
        if(tag.equals(SOS_API.KEY_ITEM_POST_FIX_MAIN_PIC)) imagesUploaded[0] = true;
        if(tag.equals(SOS_API.KEY_ITEM_POST_FIX_PIC_1)) imagesUploaded[1] = true;
        if(tag.equals(SOS_API.KEY_ITEM_POST_FIX_PIC_2)) imagesUploaded[2] = true;
        if(tag.equals(SOS_API.KEY_ITEM_POST_FIX_PIC_3)) imagesUploaded[3] = true;

        if(numImgUploaded == countBoolArrayWithVal(imagesLoaded, true)) {
            //data.putString(SOS_API.KEY_ITEM_UNIQUE_NAME, sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID));

            String imageFullName = pdUniqueName + tag;
            String fileCache = BitmapCacheManager.GetImageCachePath(BitmapCacheManager.PIC_CACHE_PATH_TYPE_PRODUCTS, imageFullName);

            boolean deleted = BitmapCacheManager.RemoveFile(fileCache);

            Log.e(TAG, "CBIFUonPostExecute: deleted -> " + fileCache + " -> " + deleted );

            pdUniqueName = sosApi.GSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID);
            data.putString(Product.KEY_PD_UNIQUE_NAME, pdUniqueName);
            data.putString(Product.KEY_PD_UNIQUE_ID, pdUniqueName);
            sosApi.SSV(SOS_API.KEY_NEW_ITEM_UNIQUE_ID, SOS_API.KEY_SESSION_DATA_EMPTY);

            numImgUploaded = 0;
            progressDialog.dismiss();
            btnExposeItem.setEnabled(false);
            sosApi.exposeItem(this, data);
            imagesLoaded = new Boolean[]{false, false, false, false};
            imagesUploaded = new Boolean[]{false, false, false, false};
            Log.e(TAG, "CBIFUonPostExecute: " + data.toString() );

        }*/


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

        if(itemModeEditing == true) {

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
