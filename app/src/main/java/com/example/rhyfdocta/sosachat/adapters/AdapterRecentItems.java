package com.example.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.R;

import java.io.File;
import java.util.List;

/**
 * Created by Franvanna on 1/10/2018.
 */

public class AdapterRecentItems extends RecyclerView.Adapter<AdapterRecentItems.ViewHolder> {


    private static final String TAG = "ADP_REC_IT";
    private final Callbacks callbacks;

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivItemPic;
        TextView tvItemName;
        TextView tvItemPrice;
        TextView tvItemDate;
        TextView tvType;
        TextView tvCat;
        View layout;
        TextView tvItemDesc;

        public ViewHolder(View view) {
            super(view);
            layout = view;
            ivItemPic = layout.findViewById(R.id.ivFetProd);
            tvItemName = layout.findViewById(R.id.tvFeatProdName);
            tvItemPrice = layout.findViewById(R.id.tvFeatProdPrice);
            tvItemDate = layout.findViewById(R.id.tvFeatProdDate);
            tvItemDesc = layout.findViewById(R.id.tvFeatProdDesc);

        }
    }

    private Context context;
    private List<Product> products;
    //private CallbacksBitmapLoading glideBitmapLoaderCallbacks;
    private SOS_API sosApi;

    public AdapterRecentItems(Context context, List<Product> products, Callbacks callBacks){

        this.context = context;
        this.products = products;
        this.callbacks = callBacks;
        sosApi = new SOS_API(context);


    }

    public interface Callbacks {
        void  onItemClicked(Product pd);
    }

    @Override
    public AdapterRecentItems.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.list_item_recent_prod_horizontal, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterRecentItems.ViewHolder holder, int position) {

        final Product pd = products.get(position);

        final Bundle data = pd.getData();

        holder.tvItemName.setText(pd.getPdName());
        holder.tvItemPrice.setText(HM.GIPB(context, R.string.priceToDiscuss, pd.getPdPrice(), pd.getPdCur()));

        String itemDate = data.getString(SOS_API.KEY_ITEM_DATE_ADDED);

        holder.tvItemDate.setText(itemDate);
        holder.tvItemDesc.setText(pd.getPdDesc());


        final String pixPath = SOS_API.DIR_PATH_PRODUCTS_PIX + pd.getPdUniqueName() + "_main.jpg";
        Uri picUri = Uri.parse(pixPath);


        String cachePath = BitmapCacheManager.GetImageCachePath(BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_RECENT_ITEMS, pd.getPdUniqueName() + "_main.jpg");
        if(BitmapCacheManager.FileExists(cachePath)){
            picUri = Uri.fromFile(new File(cachePath));


             Log.e(TAG, "PIC_PATH : -> " + picUri.toString() );

            //Toast.makeText(context, "Loade from cache", Toast.LENGTH_SHORT).show();

        }else{
            Log.e(TAG, "NO_CACHE -> " + picUri.toString() );
            //Toast.makeText(context, "Loade from network", Toast.LENGTH_SHORT).show();
        }



        Glide.with(context)
                .load(picUri)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>(SOS_API.IMG_W_ADP_RECENT_ITEMS,SOS_API.IMG_H_ADP_RECENT_ITEMS) {

                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);

                        holder.ivItemPic.setScaleType(ImageView.ScaleType.CENTER);
                        holder.ivItemPic.setImageResource(BitmapCacheManager.RES_ID_PROGRESS_ANIMATION);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);

                        holder.ivItemPic.setEnabled(false);
                        holder.ivItemPic.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        holder.ivItemPic.setImageResource(BitmapCacheManager.RES_ID_IMAGE_LOAD_ERROR);

                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        sosApi.getBitmapCacheManager().saveBitmapToCache(resource, pixPath, SOS_API.DIR_NAME_PIX_CACHE_PRODUCTS);

                        holder.ivItemPic.setEnabled(true);
                        holder.ivItemPic.setImageBitmap(resource);
                    }
                });


        //Log.e(TAG, "LINK_REC_ITEM ->  " + pixPath );

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callbacks.onItemClicked(pd);

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }




}
