package com.example.rhyfdocta.sosachat.adapters;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.rhyfdocta.sosachat.ObjectsModels.HomeCategoryItem;
import com.example.rhyfdocta.sosachat.R;
import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * Created by Franvanna on 12/22/2017.
 */

public class AdapterHomeCategories extends RecyclerView.Adapter<AdapterHomeCategories.ViewHolder> {


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
    private HomeCategoryItemListener listener;

    public AdapterHomeCategories(List<HomeCategoryItem> list, HomeCategoryItemListener listener){
        this.list = list;
        this.listener = listener;
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


        /*Picasso.with(holder.ivCatBg.getContext())
                .load(homeCategoryItem.getImageUrl())
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation)
                .centerInside()
                .resize(300,300)
                .into(holder.ivCatBg);*/

        Glide.with(holder.ivCatBg.getContext())
                .load(homeCategoryItem.getImageUrl())
                .asBitmap()
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation)
                .fitCenter()
                .into(new SimpleTarget<Bitmap>(300,300) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        listener.onBitmapShouldBeSaved(resource, homeCategoryItem.getImageUrl());

                        holder.ivCatBg.setImageBitmap(resource);
                    }
                });

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
                    listener.onItemClicked(homeCategoryItem);
                }




                return true;





            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static interface HomeCategoryItemListener{
        void onItemClicked(HomeCategoryItem homeCategoryItem);
        void onBitmapShouldBeSaved(Bitmap bitmap,String picUrl);
    }


}
