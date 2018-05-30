package com.example.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
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

import java.io.File;
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
            tvName = (TextView) view.findViewById(R.id.tvWliName);
            tvPriceNQual = (TextView) view.findViewById(R.id.tvWliPriceNQual);
            tvDate = (TextView) view.findViewById(R.id.tvWliDate);
            iv = (ImageView) view.findViewById(R.id.ivWli);
            ivAddToFavorite = (ImageView)view.findViewById(R.id.ivAddToFavorite);

        }
    }

    private List<ProductMyProducts> list;
    private ListenerAllProducts listener;
    private SOS_API sosApi;

    public AdapterAP(Context context, List<ProductMyProducts> list, ListenerAllProducts listener){
        this.list = list;
        this.listener = listener;
        sosApi = new SOS_API(context);
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

        String quality = qualities[new Integer(pd.getPdQual()).intValue()];
        String price = HM.GIPB(context, R.string.priceToDiscuss, pd.getPdPrice(),pd.getPdCur());

        String priceNQual = price + " / " + quality;

        holder.tvName.setText(pd.getPdName());
        holder.tvPriceNQual.setText(priceNQual);
        holder.tvDate.setText(pd.getDataBundle().getString(Product.KEY_PD_DATE_ADDED));

        //////////////////////////////////////////////////

        final String pixPath = SOS_API.DIR_PATH_PRODUCTS_PIX + pd.getPdImg();
        Uri uri = Uri.parse(pixPath);


        String cachePath = BitmapCacheManager.GetImageCachePath(BitmapCacheManager.PIC_CACHE_PATH_TYPE_RECENT_ITEMS, pd.getPdImg());
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
                .load(picUri)
                .centerCrop()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation).resize(400, 400).into(holder.iv, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.e("SOSAchat", "onError: ");
            }
        });*/

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
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        //callbacks.onBitmapShouldBeSaved(resource, homeCategoryItem.getImageUrl());
                        sosApi.getBitmapCacheManager().saveBitmapToCache(resource, pixPath, SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS );


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

        ImageView ivAddToFavorite = (ImageView) view.findViewById(R.id.ivAddToFavorite);
        ivAddToFavorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                listener.onItemFavorite(pd, picUri);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    

    public interface ListenerAllProducts {
        void onItemClicked(ProductMyProducts pd, Uri picUri);
        void onItemFavorite(ProductMyProducts pd, Uri picUri);


    }


}