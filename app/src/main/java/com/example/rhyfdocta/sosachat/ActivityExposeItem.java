package com.example.rhyfdocta.sosachat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
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
import com.example.rhyfdocta.sosachat.HelperObjects.SpinnerReselect;
import com.example.rhyfdocta.sosachat.HelperObjects.HM;
import com.example.rhyfdocta.sosachat.HelperObjects.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.datatype.Duration;

public class ActivityExposeItem extends AppCompatActivity implements SOS_API.SOSApiListener {


    private static final int RESULT_LOAD_IMAGE = 1200;
    private static final String KEY_TAG_PICTURE_LOADED = "picLoaded";

    private static final int REQ_PERMISSION_GALLERY = 1300;
    private static final int REQ_PERMISSION_CAMERA = 1301;
    public static final int DELAY_TO_ITEM_DET_TRANSITION = 1500;

    private ImageView ivMainItemPic;
    private ImageView curImageView;
    private String TAG = "TAG";

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
    String pdUniqueName;
    List<ImageView> ivsIds = new ArrayList<>();
    private String typeToSelect = "";
    private String catToSelect = "";
    private Button btnExposeItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expose_item);
        sosApi = new SOS_API(this);
        Intent intent = getIntent();
        editingData = intent.getExtras();
        initGUI();
        subtitle = HM.getStringResource(this, R.string.subtitleExposeItem);

        if(editingData != null) {

            itemModeEditing = editingData.getBoolean(SOS_API.KEY_ITEM_MODE_EDITING);
            subtitle = HM.getStringResource(this, R.string.subtitleEditing);
            cbAcceptTerms.setChecked(true);


            //pdUniqueName = editingData.getString(Product.KEY_PD_IMG);//.split(".")[0];
            pdUniqueName = editingData.getString(Product.KEY_PD_IMG).replace("_main.jpg","");

            loadEdintingData();


        }



        svActivityExposeitem = (ScrollView) findViewById(R.id.svActivityExposeitem);

        getSupportActionBar().setSubtitle(subtitle);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ivMainDefaultSrcID = R.drawable.product_expose_main_pic;

        alertDialog = HelperMethods.getAlertDialogProcessingWithMessage(this, HelperMethods.getStringResource(this,R.string.pbMsgProcessing),false);
        sosApi.loadItemsCatsAndTypes(this);
    }

    private void loadEdintingData() {
        loadItemEditingOldPic(pdUniqueName + SOS_API.KEY_POSTFIX_PIC_MAIN, 0);
        loadItemEditingOldPic(pdUniqueName + SOS_API.KEY_POSTFIX_PIC_1, 1);
        loadItemEditingOldPic(pdUniqueName + SOS_API.KEY_POSTFIX_PIC_2, 2);
        loadItemEditingOldPic(pdUniqueName + SOS_API.KEY_POSTFIX_PIC_3, 3);

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

    private void loadItemEditingOldPic(String picName, final int idx) {


        ivPixLoaded = new Boolean[]{true, true, true, true};


        //String picMain = editingData.getString(SOS_API.KEY_PD_MAIN_PIC_URI);

        String url = SOS_API.DIR_PATH_PRODUCTS_PIX + picName;


        Picasso.Builder builder = new Picasso.Builder(this);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
                //Toast.makeText(ActivityExposeItem.this, "", Toast.LENGTH_SHORT).show();
                ivPixLoaded[idx] = false;
               //Log.e(TAG, "onImageLoadFailed: img id -> " + idx );

            }
        });



        builder.build().load(url).into(ivsIds.get(idx));

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

        ivsIds.add(ivMainItemPic);
        ivsIds.add(ivPic1);
        ivsIds.add(ivPic2);
        ivsIds.add(ivPic3);


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

                        //sosApi.loadItemsTypes(ActivityExposeItem.this, realid);

                    itemCategoryId = "" + realid;

                    sosApi.loadItemsTypesFromCatId(ActivityExposeItem.this, realid);

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

        boolean itemDescNE = !etItemDesc.getText().toString().isEmpty();
        boolean itemNameNE = !etItemName.getText().toString().isEmpty();

        boolean curIsToDiscuss = spCur.getSelectedItem().toString() == res.getString(R.string.currencyToDiscuss);
        boolean priceIsEmpty = etItemPrice.getText().toString().isEmpty();

        boolean itemPriceNE = curIsToDiscuss || (!curIsToDiscuss && !priceIsEmpty);

        //noError = ! (itemDescNE && itemNameNE);// && itemPriceNE);

        Log.d("BOOLCHECK", "desc name price iv " + itemDescNE + " " + itemNameNE + " " + itemPriceNE + " " + ivPixLoaded[0]);


        return itemDescNE && itemNameNE && itemPriceNE && ivPixLoaded[0] ;
    }
    Boolean[] ivPixLoaded = {false,false,false,false};  //Main pic, Pic 1, Pic 2, Pic 3

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
        data.putString(SOS_API.KEY_ITEM_MAIN_PIC,getBase64Pic(SOS_API.KEY_ITEM_MAIN_PIC));

        data.putString(SOS_API.KEY_ITEM_PIC_1,SOS_API.KEY_ITEM_NO_PIC);
        data.putString(SOS_API.KEY_ITEM_PIC_2,SOS_API.KEY_ITEM_NO_PIC);
        data.putString(SOS_API.KEY_ITEM_PIC_3,SOS_API.KEY_ITEM_NO_PIC);

        if(ivPixLoaded[1] == true) {
            data.putString(SOS_API.KEY_ITEM_PIC_1, getBase64Pic(SOS_API.KEY_ITEM_PIC_1));
        }

        if(ivPixLoaded[2] == true) {
            data.putString(SOS_API.KEY_ITEM_PIC_2, getBase64Pic(SOS_API.KEY_ITEM_PIC_2));
        }

        if(ivPixLoaded[3] == true) {
            data.putString(SOS_API.KEY_ITEM_PIC_3, getBase64Pic(SOS_API.KEY_ITEM_PIC_3));
        }


        data.putString(SOS_API.KEY_ITEM_QUALITY, spQual.getSelectedItem().toString());





        if(itemModeEditing == true) {

            String itemId = editingData.getString(SOS_API.KEY_ITEM_ID);
            data.putString(SOS_API.KEY_ITEM_ID, itemId);
            data.putString(Product.KEY_PD_UNIQUE_NAME, pdUniqueName);
        }



    }

    AlertDialog alertDialogPictureSource;

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

    private void setItemPicAdded(int id) {

        if(id == ivMainItemPic.getId()){
            ivPixLoaded[0] = true;
        }

        if(id == ivPic1.getId()){
            ivPixLoaded[1] = true;
        }

        if(id == ivPic2.getId()){
            ivPixLoaded[2] = true;
        }

        if(id == ivPic3.getId()){
            ivPixLoaded[3] = true;
        }

       //Log.e(TAG, "setItemPicAdded: -> " + ivPixLoaded.toString() );


    }

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

    String fileName;

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

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data){
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn,null,null,null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
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


            setItemPicAdded(curImageView.getId());
        }



        if(requestCode == REQ_CAMERA && resultCode == RESULT_OK){

            String path = SOS_API.GSAIPCP() + "/" + fileName;
            //curImageView.setImageDrawable(Drawable.createFromPath(path));


            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize=4;      // 1/8 of original image
            Bitmap b = BitmapFactory.decodeFile(path, options);

            //reqSize = {300,300};


            //Bitmap b = (Bitmap) data.getExtras().get("data");
            //Bitmap b = HM.DSBFF(path, reqSize[0], reqSize[1]);

            curImageView.setImageBitmap(b);




            setItemPicAdded(curImageView.getId());


        }

        Log.e(TAG, "onActivityResult: " );
    }

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
        getMenuInflater().inflate(R.menu.menu_expose_item, menu);
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
                        Toast.makeText(ActivityExposeItem.this, getResources().getString(R.string.toastMsgFormReset), Toast.LENGTH_SHORT).show();

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
                            Intent intent = new Intent(ActivityExposeItem.this, ActivityTermsAndCond.class);
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




        if(checkFieldsValidity() == false) {


            showMessageDialog(getResources().getString(R.string.msgEmptyFields),
                    getResources().getString(R.string.msgPostItemFieldsIncomplete));
            return;
        }




        toggleProgressDialog(true);


        prepareDataBundle();
        sosApi.exposeItem(this, data);





    }

    private String getBase64Pic(String keyItemMainPic) {
        String imgString = "no_pic";

        if(keyItemMainPic == SOS_API.KEY_ITEM_MAIN_PIC){

            byte[] imgBytes = HelperMethods.getByteArrayFromImageView(ivMainItemPic);
            imgString =  Base64.encodeToString(imgBytes, Base64.DEFAULT);
        }

        if(keyItemMainPic == SOS_API.KEY_ITEM_PIC_1 ){

            byte[] imgBytes = HelperMethods.getByteArrayFromImageView(ivPic1);
            imgString =  Base64.encodeToString(imgBytes, Base64.DEFAULT);
        }

        if(keyItemMainPic == SOS_API.KEY_ITEM_PIC_2 ){

            byte[] imgBytes = HelperMethods.getByteArrayFromImageView(ivPic2);
            imgString =  Base64.encodeToString(imgBytes, Base64.DEFAULT);
        }

        if(keyItemMainPic == SOS_API.KEY_ITEM_PIC_3 ){

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
        //Log.e(TAG, "onExposeItemResult: data -> " + data.toString() );
        toggleProgressDialog(false);
        boolean itemUpdated = data.getBoolean(SOS_API.KEY_ITEM_UPDATED);

        if(data.getString(SOS_API.JSON_KEY_RESULT).equals(SOS_API.JSON_RESULT_SUCCESS)){

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


    JSONArray itemsCategoriesJSONArray;
    String catsArray[];
    String typesArray[];
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
                                HM.LSP(spType, new String[]{HM.getStringResource(ActivityExposeItem.this, R.string.strNA)}, null);
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
