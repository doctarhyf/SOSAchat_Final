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
import com.example.rhyfdocta.sosachat.HelperObjects.HM;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.ObjectsModels.ProductWishList;
import com.example.rhyfdocta.sosachat.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rhyfdocta on 11/9/17.
 */

public class AdapterMyProducts extends ArrayAdapter<ProductMyProducts> {


    private static final String TAG = "ADP_WL";
    Context context;
    List<ProductMyProducts> objects;
    private CallBacks callBacks;



    public AdapterMyProducts(Context context, int resource, List<ProductMyProducts> objects, CallBacks callBacks) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.callBacks = callBacks;
    }

    static class ViewHolderMyProduct {

        TextView tvName, tvPriceNQual, tvDate;
        ImageView ivRmPd, ivEditPd, ivSoldMyPd;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolderMyProduct viewHolderMyProduct;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_my_products, null);
            viewHolderMyProduct = new ViewHolderMyProduct();
            viewHolderMyProduct.tvName = (TextView) view.findViewById(R.id.tvWliName);
            viewHolderMyProduct.tvPriceNQual = (TextView) view.findViewById(R.id.tvWliPriceNQual);
            viewHolderMyProduct.tvDate = (TextView) view.findViewById(R.id.tvWliDate);
            viewHolderMyProduct.ivRmPd = (ImageView) view.findViewById(R.id.ivWli);
            viewHolderMyProduct.ivEditPd = (ImageView) view.findViewById(R.id.ivEditMyPd);
            viewHolderMyProduct.ivSoldMyPd = (ImageView) view.findViewById(R.id.ivSoldMyPd);
            view.setTag(viewHolderMyProduct);


        } else {
            viewHolderMyProduct = (ViewHolderMyProduct) view.getTag();
        }




        final ProductMyProducts pd = objects.get(position);


        //String[] currecnies = HelperMethods.RES_GET_SA(context, R.array.currencies);


        String quality = HM.GPQF(context,new Integer(pd.getPdQual()).intValue());
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
        viewHolderMyProduct.tvPriceNQual.setText(priceNQual);
        viewHolderMyProduct.tvDate.setText(d.getString(Product.KEY_PD_DATE_ADDED));



        final Uri picUri = Uri.parse(SOS_API.DIR_PATH_CATEGORIES + "products/" + d.getString(Product.KEY_PD_UNIQUE_NAME) + "_main.jpg");

        Picasso.with(context).load(picUri).error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE).centerCrop().resize(400, 400).into(viewHolderMyProduct.ivRmPd, new Callback() {
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

        ImageView ivEditPd = (ImageView) view.findViewById(R.id.ivEditMyPd);
        ivEditPd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callBacks.onItemEditClicked(pd, picUri);
            }
        });

        ImageView ivSoldMyPd = (ImageView) view.findViewById(R.id.ivSoldMyPd);
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