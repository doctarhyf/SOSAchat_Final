package com.koziengineering.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import com.koziengineering.rhyfdocta.sosachat.API.SOS_API;
import com.koziengineering.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.koziengineering.rhyfdocta.sosachat.Helpers.HM;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.Product;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.ProductWishList;
import com.example.rhyfdocta.sosachat.R;
import com.koziengineering.rhyfdocta.sosachat.app.SOSApplication;

import java.io.File;
import java.util.List;

/**
 * Created by rhyfdocta on 11/9/17.
 */

public class AdapterWishListItems extends ArrayAdapter<ProductWishList> {


    private static final String TAG = "ADP_WL";
    Context context;
    List<ProductWishList> objects;
    private CallBacks callBacks;
    private SOS_API sosApi;


    public AdapterWishListItems(Context context, int resource, List<ProductWishList> objects, CallBacks callBacks) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.callBacks = callBacks;
        this.sosApi = SOSApplication.getInstance().getSosApi();
    }

    static class ViewHolderWishListItem {

        TextView tvName, tvPriceNQual, tvDate;
        ImageView iv;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolderWishListItem viewHolderWishListItem;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_wishlist, null);
            viewHolderWishListItem = new ViewHolderWishListItem();
            viewHolderWishListItem.tvName = view.findViewById(R.id.tvWliName);
            viewHolderWishListItem.tvPriceNQual = view.findViewById(R.id.tvWliPriceNQual);
            viewHolderWishListItem.tvDate = view.findViewById(R.id.tvWliDate);
            viewHolderWishListItem.iv = view.findViewById(R.id.ivWli);
            view.setTag(viewHolderWishListItem);


        } else {
            viewHolderWishListItem = (ViewHolderWishListItem) view.getTag();
        }



        final ProductWishList pd = objects.get(position);
        Bundle d = pd.getData();

        String priceNQual;
        String price = pd.getPdPrice();
        String quality = HM.RGSA(context, R.array.newItemQuality)[Integer.valueOf(pd.getPdQual())];
        if(price.equals("")){
            priceNQual = pd.getPdCur();
        }else {
            priceNQual = pd.getPdPrice() + " " + pd.getPdCur() + " / " + quality;
        }

        viewHolderWishListItem.tvName.setText(pd.getPdName());
        viewHolderWishListItem.tvPriceNQual.setText(priceNQual);
        viewHolderWishListItem.tvDate.setText(d.getString(Product.KEY_PD_DATE_ADDED));

        /*
        final Uri picUri = Uri.parse(sosApi.GSA() + SOS_API.DIR_PATH_CATEGORIES + "products/" + d.getString(Product.KEY_PD_UNIQUE_NAME) + "_main.jpg");
        Picasso.with(context).load(picUri).error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation).centerCrop().resize(400, 400).into(viewHolderWishListItem.iv, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.e("PICASSO AdapterTypesItem", "onError: ");
            }
        });*/

        final String pixPath = sosApi.GSA() + SOS_API.DIR_PATH_CATEGORIES + "products/" + d.getString(Product.KEY_PD_UNIQUE_NAME) + "_main.jpg";
        Uri uri = Uri.parse(pixPath);


        String cachePath = BitmapCacheManager.GetImageCachePath(BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_RECENT_ITEMS, pd.getPdImg());
        if(BitmapCacheManager.FileExists(cachePath)){
            uri = Uri.fromFile(new File(cachePath));


            Log.e(TAG, "PIC_PATH : -> " + uri.toString() );

            //Toast.makeText(context, "Loade from cache", Toast.LENGTH_SHORT).show();

        }else{
            Log.e(TAG, "NO_CACHE -> " + uri.toString() );
            //Toast.makeText(context, "Loade from network", Toast.LENGTH_SHORT).show();
        }

        final Uri picUri = uri;


        Glide.with(context)
                .load(uri)
                .asBitmap()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>(400,400) {

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);

                        viewHolderWishListItem.iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        viewHolderWishListItem.iv.setImageResource(BitmapCacheManager.RES_ID_IMAGE_LOAD_ERROR);
                        viewHolderWishListItem.iv.setEnabled(false);
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        viewHolderWishListItem.iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        viewHolderWishListItem.iv.setImageResource(BitmapCacheManager.RES_ID_PROGRESS_ANIMATION);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        //callbacks.onBitmapShouldBeSaved(resource, homeCategoryItem.getImageUrl());
                        sosApi.getBitmapCacheManager().saveBitmapToCache(resource, pixPath, SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS );

                        viewHolderWishListItem.iv.setEnabled(true);
                        viewHolderWishListItem.iv.setImageBitmap(resource);

                        //callbacks.onItemClicked(homeCategoryItem);
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


        return view;


    }

    public interface CallBacks {
        void onItemClicked(ProductWishList pd, Uri picUri);
        void onItemRemoveClicked(ProductWishList pd, Uri picUri);
    }
}