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
import com.koziengineering.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.R;
import com.koziengineering.rhyfdocta.sosachat.app.SOSApplication;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rhyfdocta on 11/9/17.
 */

public class AdapterProducts extends ArrayAdapter<Product> {


    Context context;
    List<Product> objects;
    //public static String prodsPicPath;
    //private String catPixPath;
    private CallBacks callBacks;
    private SOS_API sosApi;


    public AdapterProducts(Context context, int resource, List<Product> objects, CallBacks callBacks) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.callBacks = callBacks;
        this.sosApi = SOSApplication.getInstance().getSosApi();

    }


    static class ViewHolderProduct{
        TextView tvProdName;
        TextView tvProdPrive;
        ImageView ivProd;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        View view = convertView;
        ViewHolderProduct viewHolderProduct;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_prod, null);
            viewHolderProduct = new ViewHolderProduct();
            viewHolderProduct.tvProdName = view.findViewById(R.id.tvProdName);
            viewHolderProduct.tvProdPrive = view.findViewById(R.id.tvProdPrice);
            viewHolderProduct.ivProd = view.findViewById(R.id.ivProd);
            view.setTag(viewHolderProduct);



        }else{
            viewHolderProduct = (ViewHolderProduct) view.getTag();
        }

        final Product product = objects.get(position);
        viewHolderProduct.tvProdName.setText(product.getPdName());

        String price = product.getPdPrice() + " " + product.getPdCur();
        viewHolderProduct.tvProdPrive.setText(price);

        final Uri picUri = Uri.parse(sosApi.GSA() + SOS_API.DIR_PATH_PRODUCTS_PIX + product.getPdImg() + ".jpg");
        Log.e("ADP", "getView: it url -> " + picUri.toString() );
        //view.setUniqueName(1,picUri);
        Log.e("PROD PIC ERR", "\nurl: " + picUri );

        Picasso.with(context).load(picUri).error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation).centerCrop().resize(1000,563).into(viewHolderProduct.ivProd, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.e("PICASSO PROD PIC E", "onError: " + picUri );
            }
        });




        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.onItemClicked(product, picUri);
                // Log.e("PD ADAPT ", "onClick: " + picUri );
                //Toast.makeText(context, "ITEM CLICKE", Toast.LENGTH_SHORT).show();
            }
        });

        /*
        final Product product = objects.get(position);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE);
        View view;


        if(convertView == null) {

        view = inflater.inflate(R.layout.list_item_prod, null);

        }else{
            view = convertView;
        }






        TextView tvProdName = (TextView) view.findViewById(R.id.tvProdName);
        tvProdName.setText(product.getPdName());//product.getPdName());

        TextView tvProdPrive = (TextView) view.findViewById(R.id.tvProdPrice);
        String price = product.getPdPrice() + " " + product.getPdCur();

        tvProdPrive.setText(price);


        ImageView ivProd = (ImageView) view.findViewById(R.id.ivProd);
        final Uri picUri = Uri.parse(MainActivity.DIR_PATH_PRODUCTS_PIX.concat(product.getPdImg()));
        //view.setUniqueName(1,picUri);
        Log.e("PROD PIC ERR", "\nurl: " + picUri );

        Picasso.with(context).load(picUri).centerCrop().resize(1000,563).into(ivProd, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.e("PICASSO PROD PIC E", "onError: " + picUri );
            }
        });




        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBacks.onItemClicked(product, picUri);
               // Log.e("PD ADAPT ", "onClick: " + picUri );
                //Toast.makeText(context, "ITEM CLICKE", Toast.LENGTH_SHORT).show();
            }
        });

*/
        return view;

    }

    public interface CallBacks{
        void onItemClicked(Product product, Uri picUri);
    }





}
