package com.koziengineering.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.koziengineering.rhyfdocta.sosachat.API.SOS_API;
import com.koziengineering.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.HomeCategoryItem;
import com.example.rhyfdocta.sosachat.R;
import com.koziengineering.rhyfdocta.sosachat.app.SOSApplication;

import java.io.File;
import java.util.List;

import static com.koziengineering.rhyfdocta.sosachat.API.SOS_API.TAG;


/**
 * Created by Franvanna on 12/22/2017.
 */

public class AdapterHomeCategories extends RecyclerView.Adapter<AdapterHomeCategories.ViewHolder> {


    private Context context;

    public static  class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvCatTitle;
        public ImageView ivCatBg;
        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            tvCatTitle = layout.findViewById(R.id.tvCatTitle);
            ivCatBg = layout.findViewById(R.id.ivCatBg);

        }
    }

    private List<HomeCategoryItem> list;
    private Callbacks callbacks;
    private SOS_API sosApi;

    public AdapterHomeCategories(Context context, List<HomeCategoryItem> list, Callbacks callbacks){

        this.context = context;
        this.list = list;
        this.callbacks = callbacks;
        this.sosApi = SOSApplication.getInstance().getSosApi();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_home_cat_item, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final HomeCategoryItem homeCategoryItem = list.get(position);
        holder.tvCatTitle.setText(homeCategoryItem.getTitle());


       String url = homeCategoryItem.getImageUrl();

        //final String pixPath = SOS_API.DIR_PATH_PRODUCTS_PIX + pd.getPdImg();
        Uri uri = Uri.parse(url);


        String picName = url.split("/")[url.split("/").length-1];
        String cachePath = BitmapCacheManager.GetImageCachePath(BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_ITEMS_CATEGORIES, picName);
        if(BitmapCacheManager.FileExists(cachePath)){
            uri = Uri.fromFile(new File(cachePath));


            Log.e(TAG, "PIC_PATH : -> " + uri.toString() );

            //Toast.makeText(context, "Loade from cache", Toast.LENGTH_SHORT).show();

        }else{
            Log.e(TAG, "NO_CACHE -> " + uri.toString() );
            //Toast.makeText(context, "Loade from network", Toast.LENGTH_SHORT).show();
        }

        Glide.with(holder.ivCatBg.getContext())
                .load(uri)
                .asBitmap()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>(300,300) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        //callbacks.onBitmapShouldBeSaved(resource, homeCategoryItem.getImageUrl());
                        sosApi.getBitmapCacheManager().saveBitmapToCache(resource, homeCategoryItem.getImageUrl(), SOS_API.DIR_NAME_PIX_CACHE_HOME_CATS );


                        holder.ivCatBg.setImageBitmap(resource);

                        //callbacks.onItemClicked(homeCategoryItem);
                    }
                });

        //callbacks.onItemClicked(homeCategoryItem);

        holder.ivCatBg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if(event.getAction() == 0){
                    holder.ivCatBg.setAlpha(0.5f);

                }

                if(event.getAction() == 1 || event.getAction() == 3) {
                    holder.ivCatBg.setAlpha(1f);


                }

                if(event.getAction() == 1){
                    callbacks.onItemClicked(homeCategoryItem);
                }




                return true;





            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  interface Callbacks {
        void onItemClicked(HomeCategoryItem homeCategoryItem);

    }


}
