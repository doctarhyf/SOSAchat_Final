package com.example.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductWishList;
import com.example.rhyfdocta.sosachat.R;

import java.io.File;
import java.util.List;

/**
 * Created by rhyfdocta on 11/9/17.
 */

public class AdapterMyProducts extends ArrayAdapter<ProductMyProducts> {


    private static final String TAG = "ADP_WL";
    Context context;
    List<ProductMyProducts> objects;
    private CallBacks callBacks;
    private SOS_API sosApi;
    //private ActivityMyProducts.MyGlideBitmapLoaderCallbacks glideBitmapLoaderCallbacks;


    public AdapterMyProducts(Context context, int resource, List<ProductMyProducts> objects, CallBacks callBacks) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.callBacks = callBacks;
        this.sosApi = new SOS_API(context);
    }

    static class ViewHolderMyProduct {

        TextView tvName, tvPriceNQual, tvDate, tvItemQual, tvItemPrice;
        ImageView ivRmPd, ivEditPd, ivSoldMyPd;


    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolderMyProduct viewHolderMyProduct;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_my_products, null);
            viewHolderMyProduct = new ViewHolderMyProduct();
            viewHolderMyProduct.tvName = view.findViewById(R.id.tvWliName);
            viewHolderMyProduct.tvPriceNQual = view.findViewById(R.id.tvWliPriceNQual);
            viewHolderMyProduct.tvDate = view.findViewById(R.id.tvWliDate);
            viewHolderMyProduct.ivRmPd = view.findViewById(R.id.ivWli);
            viewHolderMyProduct.ivEditPd = view.findViewById(R.id.ivEditMyPd);
            viewHolderMyProduct.ivSoldMyPd = view.findViewById(R.id.ivSoldMyPd);
            viewHolderMyProduct.tvItemQual = view.findViewById(R.id.tvItemQual);
            viewHolderMyProduct.tvItemPrice = view.findViewById(R.id.tvItemPrice);
            view.setTag(viewHolderMyProduct);


        } else {
            viewHolderMyProduct = (ViewHolderMyProduct) view.getTag();
        }




        final ProductMyProducts pd = objects.get(position);


        //String[] currecnies = HelperMethods.RES_GET_SA(context, R.array.currencies);


        String quality = pd.getPdQual();HM.GPQF(context,new Integer(pd.getPdQual()).intValue());
        String price = pd.getPdPrice().equals("-1") ? "" : pd.getPdPrice();

        //String price = HM.GIPB(context, R.string.priceToDiscuss, pd.getPdPrice(),pd.getPdCur());


        String priceNQual;
        if(price.equals("")){
            priceNQual = pd.getPdCur();
        }else {
            priceNQual = pd.getPdPrice() + " " + pd.getPdCur() + " / " + quality;
        }

        Bundle d = pd.getDataBundle();

        viewHolderMyProduct.tvName.setText(pd.getPdName());
        //viewHolderMyProduct.tvPriceNQual.setText(priceNQual);

        viewHolderMyProduct.tvItemQual.setText(pd.getPdQual());
        viewHolderMyProduct.tvItemPrice.setText(pd.getPdPrice());


        viewHolderMyProduct.tvDate.setText(d.getString(Product.KEY_PD_DATE_ADDED));



        //final Uri picUri = Uri.parse(SOS_API.DIR_PATH_CATEGORIES + "products/" + d.getString(Product.KEY_PD_UNIQUE_NAME) + "_main.jpg");

        String pixPath = SOS_API.DIR_PATH_CATEGORIES + "products/" + d.getString(Product.KEY_PD_UNIQUE_NAME) + "_main.jpg";
        Uri uri = Uri.parse(pixPath);



        String cachePath = BitmapCacheManager.GetImageCachePath(BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_RECENT_ITEMS, d.getString(Product.KEY_PD_UNIQUE_NAME) + "_main.jpg");
        if(BitmapCacheManager.FileExists(cachePath)){
            uri = Uri.fromFile(new File(cachePath));


            Log.e(TAG, "PIC_PATH : -> " + uri.toString() );

            //Toast.makeText(context, "Loade from cache", Toast.LENGTH_SHORT).show();

        }else{
            Log.e(TAG, "NO_CACHE -> " + uri.toString() );
            //Toast.makeText(context, "Loade from network", Toast.LENGTH_SHORT).show();
        }

        final Uri picUri = uri;


        /*
        Picasso.with(context)
                .load(picUri).error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .centerCrop()
                .resize(400, 400)
                .into(viewHolderMyProduct.ivRmPd, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.e("PICASS_ERR", "onError: ");
            }
        });*/



        Glide.with(context)
                .load(picUri)
                .asBitmap()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .centerCrop()
                .into(new SimpleTarget<Bitmap>(400,400) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        sosApi.getBitmapCacheManager().saveBitmapToCache(resource, picUri.toString(), SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS);

                        viewHolderMyProduct.ivRmPd.setImageBitmap(resource);
                    }
                });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(context, "Wish List Clicked", Toast.LENGTH_SHORT).show();
                callBacks.onItemClicked(pd, picUri);
            }
        });

        ImageView ivRmFromWli = view.findViewById(R.id.ivRmFromWli);
        ivRmFromWli.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callBacks.onItemRemoveClicked(pd, picUri);
            }
        });

        ImageView ivEditPd = view.findViewById(R.id.ivEditMyPd);
        ivEditPd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callBacks.onItemEditClicked(pd, picUri);
            }
        });

        ImageView ivSoldMyPd = view.findViewById(R.id.ivSoldMyPd);
        ivSoldMyPd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callBacks.onItemSoldClicked(pd, picUri);
            }
        });



        return view;


    }

    public interface CallBacks {
        void onItemClicked(ProductMyProducts pd, Uri picUri);
        void onItemRemoveClicked(ProductWishList pd, Uri picUri);
        void onItemEditClicked(ProductMyProducts pd, Uri picUri);

        void onItemSoldClicked(ProductMyProducts pd, Uri picUri);
    }
}