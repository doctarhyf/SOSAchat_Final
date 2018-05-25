package com.example.rhyfdocta.sosachat.adapters;

import android.content.Context;
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

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductWishList;
import com.example.rhyfdocta.sosachat.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rhyfdocta on 11/9/17.
 */

public class AdapterWishListItems extends ArrayAdapter<ProductWishList> {


    private static final String TAG = "ADP_WL";
    Context context;
    List<ProductWishList> objects;
    private CallBacks callBacks;


    public AdapterWishListItems(Context context, int resource, List<ProductWishList> objects, CallBacks callBacks) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.callBacks = callBacks;
    }

    static class ViewHolderWishListItem {

        TextView tvName, tvPriceNQual, tvDate;
        ImageView iv;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolderWishListItem viewHolderWishListItem;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_wishlist, null);
            viewHolderWishListItem = new ViewHolderWishListItem();
            viewHolderWishListItem.tvName = (TextView) view.findViewById(R.id.tvWliName);
            viewHolderWishListItem.tvPriceNQual = (TextView) view.findViewById(R.id.tvWliPriceNQual);
            viewHolderWishListItem.tvDate = (TextView) view.findViewById(R.id.tvWliDate);
            viewHolderWishListItem.iv = (ImageView) view.findViewById(R.id.ivWli);
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

        final Uri picUri = Uri.parse(SOS_API.DIR_PATH_CATEGORIES + "products/" + d.getString(Product.KEY_PD_UNIQUE_NAME) + "_main.jpg");
        Picasso.with(context).load(picUri).error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation).centerCrop().resize(400, 400).into(viewHolderWishListItem.iv, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.e("PICASSO AdapterTypesItem", "onError: ");
            }
        });


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Toast.makeText(context, "Wish List Clicked", Toast.LENGTH_SHORT).show();
                callBacks.onItemClicked(pd, picUri);
            }
        });

        ImageView ivRmFromWli = (ImageView) view.findViewById(R.id.ivRmFromWli);
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