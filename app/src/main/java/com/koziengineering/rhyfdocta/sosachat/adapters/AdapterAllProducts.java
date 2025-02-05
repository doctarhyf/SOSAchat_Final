package com.koziengineering.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koziengineering.rhyfdocta.sosachat.API.SOS_API;
import com.koziengineering.rhyfdocta.sosachat.Helpers.HM;
import com.koziengineering.rhyfdocta.sosachat.Helpers.HelperMethods;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.Product;
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.ProductMyProducts;
import com.example.rhyfdocta.sosachat.R;
import com.koziengineering.rhyfdocta.sosachat.app.SOSApplication;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rhyfdocta on 11/9/17.
 */

public class AdapterAllProducts extends ArrayAdapter<ProductMyProducts> {


    private static final String TAG = "ADP_WL";
    Context context;
    List<ProductMyProducts> objects;
    private CallBacks callBacks;
    private SOS_API sosApi;



    public AdapterAllProducts(Context context, int resource, List<ProductMyProducts> objects, CallBacks callBacks) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.callBacks = callBacks;
        sosApi = SOSApplication.getInstance().getSosApi();
    }

    static class ViewHolderWishListItem {

        TextView tvName, tvPriceNQual, tvDate;
        ImageView iv, ivAddToFavorite;


    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ProductMyProducts pd = objects.get(position);
        View view = convertView;
        ViewHolderWishListItem viewHolderWishListItem;

        if (view == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_all_products, null);
            viewHolderWishListItem = new ViewHolderWishListItem();
            viewHolderWishListItem.tvName = view.findViewById(R.id.tvWliName);
            viewHolderWishListItem.tvPriceNQual = view.findViewById(R.id.tvWliPriceNQual);
            viewHolderWishListItem.tvDate = view.findViewById(R.id.tvWliDate);
            viewHolderWishListItem.iv = view.findViewById(R.id.ivWli);
            viewHolderWishListItem.ivAddToFavorite = view.findViewById(R.id.ivAddToFavorite);
            view.setTag(viewHolderWishListItem);


        } else {
            viewHolderWishListItem = (ViewHolderWishListItem) view.getTag();
        }



        if(pd.getDataBundle().getString(SOS_API.KEY_ACC_DATA_USER_ID).equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_USER_ID))){
            viewHolderWishListItem.ivAddToFavorite.setVisibility(View.INVISIBLE);
        }



        String[] qualities = HelperMethods.RGSA(context, R.array.newItemQuality);
        //String[] currecnies = HelperMethods.RGSA(context, R.array.currencies);

        String quality = qualities[new Integer(pd.getPdQual()).intValue()];
        String price = HM.GIPB(context, R.string.priceToDiscuss, pd.getPdPrice(),pd.getPdCur());

        String priceNQual = price + " / " + quality;

        viewHolderWishListItem.tvName.setText(pd.getPdName());
        viewHolderWishListItem.tvPriceNQual.setText(priceNQual);
        viewHolderWishListItem.tvDate.setText(pd.getDataBundle().getString(Product.KEY_PD_DATE_ADDED));

        final Uri picUri = Uri.parse(sosApi.GSA() + SOS_API.DIR_PATH_CATEGORIES + "products/" + pd.getDataBundle().getString(SOS_API.KEY_ITEM_UNIQUE_NAME) + "_main.jpg");
        //Log.e(TAG, "FUCK " + picUri.toString());
        Picasso.with(context).load(picUri).centerCrop().error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation).resize(400, 400).into(viewHolderWishListItem.iv, new Callback() {
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

        ImageView ivAddToFavorite = view.findViewById(R.id.ivAddToFavorite);
        ivAddToFavorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                callBacks.onItemFavorite(pd, picUri);
            }
        });


        return view;


    }

    public interface CallBacks {
        void onItemClicked(ProductMyProducts pd, Uri picUri);
        void onItemFavorite(ProductMyProducts pd, Uri picUri);

    }
}