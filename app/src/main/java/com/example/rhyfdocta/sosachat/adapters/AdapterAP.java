package com.example.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.Helpers.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.R;
import com.example.rhyfdocta.sosachat.app.SOSApplication;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Franvanna on 1/6/2018.
 */

public class AdapterAP extends RecyclerView.Adapter<AdapterAP.ViewHolder> {


    private static final String TAG = "SOSAchat";

    public static  class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPriceNQual, tvDate;
        ImageView iv, ivAddToFavorite;
        View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            tvName = view.findViewById(R.id.tvWliName);
            tvPriceNQual = view.findViewById(R.id.tvWliPriceNQual);
            tvDate = view.findViewById(R.id.tvWliDate);
            iv = view.findViewById(R.id.ivWli);
            ivAddToFavorite = view.findViewById(R.id.ivAddToFavorite);

        }
    }

    private List<ProductMyProducts> list;
    private ListenerAllProducts listener;
    private SOS_API sosApi;

    public AdapterAP(Context context, List<ProductMyProducts> list, ListenerAllProducts listener){
        this.list = list;
        this.listener = listener;
        sosApi = SOSApplication.getInstance().getSosApi();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_all_products, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Context context = holder.iv.getContext();
        final ProductMyProducts pd = list.get(position);
        holder.tvName.setText(pd.getPdName());


        if(pd.getDataBundle().getString(SOS_API.KEY_ACC_DATA_USER_ID).equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_USER_ID))){
            holder.ivAddToFavorite.setVisibility(View.INVISIBLE);
        }



        String[] qualities = HelperMethods.RGSA(context, R.array.newItemQuality);
        //String[] currecnies = HelperMethods.RGSA(context, R.array.currencies);

        String quality = qualities[Integer.parseInt(pd.getPdQual())];
        String price = HM.GIPB(context, R.string.priceToDiscuss, pd.getPdPrice(),pd.getPdCur());

        String priceNQual = price + " / " + quality;

        holder.tvName.setText(pd.getPdName());
        holder.tvPriceNQual.setText(priceNQual);
        holder.tvDate.setText(pd.getDataBundle().getString(Product.KEY_PD_DATE_ADDED));

        //////////////////////////////////////////////////

        final String pixPath = sosApi.GSA() + SOS_API.DIR_PATH_PRODUCTS_PIX + pd.getPdImg();
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

                        holder.iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        holder.iv.setImageResource(BitmapCacheManager.RES_ID_IMAGE_LOAD_ERROR);
                        holder.iv.setEnabled(false);
                    }

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        holder.iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        holder.iv.setImageResource(BitmapCacheManager.RES_ID_PROGRESS_ANIMATION);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        //callbacks.onBitmapShouldBeSaved(resource, homeCategoryItem.getImageUrl());
                        sosApi.getBitmapCacheManager().saveBitmapToCache(resource, pixPath, SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS );

                        holder.iv.setEnabled(true);
                        holder.iv.setImageBitmap(resource);

                        //callbacks.onItemClicked(homeCategoryItem);
                    }
                });

        ///////////////////////////////////////////////////

        View view = holder.layout;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(context, "Wish List Clicked", Toast.LENGTH_SHORT).show();
                listener.onItemClicked(pd, picUri);
            }
        });

        ImageView ivAddToFavorite = view.findViewById(R.id.ivAddToFavorite);
        ivAddToFavorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onItemAddedToFavorite(pd, picUri);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setFilter(List<ProductMyProducts> filtered_list){

        list = new ArrayList<>();
        list.addAll(filtered_list);

        notifyDataSetChanged();

    }

    public interface ListenerAllProducts {
        void onItemClicked(ProductMyProducts pd, Uri picUri);
        void onItemAddedToFavorite(ProductMyProducts pd, Uri picUri);


    }


}