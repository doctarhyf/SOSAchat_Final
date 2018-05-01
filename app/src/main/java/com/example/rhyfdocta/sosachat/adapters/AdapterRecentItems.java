package com.example.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.HelperObjects.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.HelperObjects.HM;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.R;

import java.io.File;
import java.util.List;

/**
 * Created by Franvanna on 1/10/2018.
 */

public class AdapterRecentItems extends RecyclerView.Adapter<AdapterRecentItems.ViewHolder> {


    private static final String TAG = "ADP_REC_IT";

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivItemPic;
        TextView tvItemName;
        TextView tvItemPrice;
        TextView tvItemDate;
        View layout;

        public ViewHolder(View view) {
            super(view);
            layout = view;
            ivItemPic = (ImageView) layout.findViewById(R.id.ivFetProd);
            tvItemName = (TextView) layout.findViewById(R.id.tvFeatProdName);
            tvItemPrice = (TextView) layout.findViewById(R.id.tvFeatProdPrice);
            tvItemDate = (TextView) layout.findViewById(R.id.tvFeatProdDate);
        }
    }

    private Context context;
    private List<Product> products;
    private CallbacksAdapterRecentItems callbacksAdapterRecentItems;
    private SOS_API sosApi;

    public AdapterRecentItems(Context context, List<Product> products, CallbacksAdapterRecentItems callbacksAdapterRecentItems){

        this.context = context;
        this.products = products;
        this.callbacksAdapterRecentItems = callbacksAdapterRecentItems;
        sosApi = new SOS_API(context);


    }

    @Override
    public AdapterRecentItems.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_recent_prod, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final AdapterRecentItems.ViewHolder holder, int position) {

        final Product pd = products.get(position);

        final Bundle data = pd.getData();

        holder.tvItemName.setText(pd.getPdName());
        holder.tvItemPrice.setText(HM.GIPB(context, R.string.priceToDiscuss, pd.getPdPrice(), pd.getPdCur()));
        holder.tvItemDate.setText(data.getString(SOS_API.KEY_ITEM_DATE_ADDED));



        final String pixPath = SOS_API.DIR_PATH_PRODUCTS_PIX + pd.getPdUniqueName() + "_main.jpg";
        Uri picUri = Uri.parse(pixPath);



        /*Picasso.with(context).load(picUri).centerInside().resize(450,450).into(holder.ivItemPic, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.e("PICASSO  ", "onError: PICASSO ITEM DETAILS ERROR \nLink : " + picUri.toString() );
            }
        });*/

        // TODO: 1/26/2018 FUCK BITCHES
        String cachePath = BitmapCacheManager.GET_PIC_CACHE_PATH(BitmapCacheManager.PIC_CACHE_PATH_TYPE_RECENT_ITEMS, pd.getPdUniqueName() + "_main.jpg");
        if(BitmapCacheManager.FILE_EXISTS(cachePath)){
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
                .fitCenter()
                .into(new SimpleTarget<Bitmap>(450,450) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        callbacksAdapterRecentItems.onBitmapShouldBeSaved(resource, pixPath);

                        holder.ivItemPic.setImageBitmap(resource);
                    }
                });


        Log.e(TAG, "LINK_REC_ITEM ->  " + pixPath );

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                callbacksAdapterRecentItems.onItemClicked(pd);

            }
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public interface CallbacksAdapterRecentItems {
        void onItemClicked(Product pd);
        void onBitmapShouldBeSaved(Bitmap bitmap, String picUrl);
    }


}
