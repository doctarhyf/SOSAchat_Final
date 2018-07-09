package com.koziengineering.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.R;
import com.koziengineering.rhyfdocta.sosachat.app.SOSApplication;

import java.io.File;
import java.util.List;

import static com.koziengineering.rhyfdocta.sosachat.API.SOS_API.TAG;

/**
 * Created by rhyfdocta on 11/9/17.
 */

public class AdapterTypesItem extends ArrayAdapter<TypesItem> {


    Context context;
    List<TypesItem> objects;
    private String catPixPath;
    private CallBacks callBacks;
    private SOS_API sosApi;


    public AdapterTypesItem(Context context, int resource, List<TypesItem> objects, CallBacks callBacks) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.callBacks = callBacks;
        this.sosApi = SOSApplication.getInstance().getSosApi();
    }

    static class ViewHolderCatItem{

        TextView tv;
        ImageView iv;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolderCatItem viewHolderCatItem;

        if(view == null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_item_type, null);
            viewHolderCatItem = new ViewHolderCatItem();
            viewHolderCatItem.tv = view.findViewById(R.id.tvCatName);
            viewHolderCatItem.iv = view.findViewById(R.id.ivCatItem);
            view.setTag(viewHolderCatItem);


        }else{
            viewHolderCatItem = (ViewHolderCatItem) view.getTag();
        }

        final TypesItem item = objects.get(position);
        viewHolderCatItem.tv.setText(item.getTypeName());



        final Uri picUri = Uri.parse(item.getTypeImgPath());

        Log.e("ADPT_TYPES", "type url ->  " + picUri.toString() );

        /*
        Picasso.with(context).load(picUri)
        .error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation).centerInside().resize(400,400).into(viewHolderCatItem.iv, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.e("PICASSO", "onError: " );
            }
        });*/

        Uri uri = picUri;
        final String  url = item.getTypeImgPath();


        String picName = url.split("/")[url.split("/").length-1];
        String cachePath = BitmapCacheManager.GetImageCachePath(BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_ITEMS_TYPES_IN_CATEGORIES, picName);
        if(BitmapCacheManager.FileExists(cachePath)){
            uri = Uri.fromFile(new File(cachePath));


            Log.e(TAG, "PIC_PATH : -> " + uri.toString() );

            //Toast.makeText(context, "Loade from cache", Toast.LENGTH_SHORT).show();

        }else{
            Log.e(TAG, "NO_CACHE -> " + uri.toString() );
            //Toast.makeText(context, "Loade from network", Toast.LENGTH_SHORT).show();
        }

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
                    public void onLoadStarted(Drawable placeholder) {
                        super.onLoadStarted(placeholder);

                        viewHolderCatItem.iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                        viewHolderCatItem.iv.setImageResource(R.drawable.progress_animation);
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);

                        viewHolderCatItem.iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        viewHolderCatItem.iv.setImageResource(R.drawable.no_image);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation)  {


                        //callbacks.onBitmapShouldBeSaved(resource, homeCategoryItem.getImageUrl());
                        sosApi.getBitmapCacheManager().saveBitmapToCache(resource, url, SOS_API.DIR_NAME_PIX_CACHE_HOME_CAT_TYPES );


                        viewHolderCatItem.iv.setImageBitmap(resource);

                        //callbacks.onItemClicked(homeCategoryItem);
                    }
                });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                callBacks.onItemClicked(item, picUri);
            }
        });





        return view;



    }




    public String getCatPixPath() {
        return catPixPath;
    }

    public interface CallBacks{
        void onItemClicked(TypesItem typesItem, Uri picUri);
    }




    public void setCatPixPath(String catPixPath) {
        this.catPixPath = catPixPath;
    }
}
