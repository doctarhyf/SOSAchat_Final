package com.example.rhyfdocta.sosachat.adapters;

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

import com.example.rhyfdocta.sosachat.ObjectsModels.TypesItem;
import com.example.rhyfdocta.sosachat.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by rhyfdocta on 11/9/17.
 */

public class AdapterTypesItem extends ArrayAdapter<TypesItem> {


    Context context;
    List<TypesItem> objects;
    private String catPixPath;
    private CallBacks callBacks;


    public AdapterTypesItem(Context context, int resource, List<TypesItem> objects, CallBacks callBacks) {
        super(context, resource, objects);
        this.context = context;
        this.objects = objects;
        this.callBacks = callBacks;
    }

    static class ViewHolderCatItem{

        TextView tv;
        ImageView iv;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolderCatItem viewHolderCatItem;

        if(view == null){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_item_type, null);
            viewHolderCatItem = new ViewHolderCatItem();
            viewHolderCatItem.tv = (TextView) view.findViewById(R.id.tvCatName);
            viewHolderCatItem.iv = (ImageView) view.findViewById(R.id.ivCatItem);
            view.setTag(viewHolderCatItem);


        }else{
            viewHolderCatItem = (ViewHolderCatItem) view.getTag();
        }

        final TypesItem item = objects.get(position);
        viewHolderCatItem.tv.setText(item.getTypeName());



        final Uri picUri = Uri.parse(item.getTypeImgPath());

        Log.e("ADPT_TYPES", "type url ->  " + picUri.toString() );

        Picasso.with(context).load(picUri).error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation).centerInside().resize(400,400).into(viewHolderCatItem.iv, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Log.e("PICASSO AdapterTypesItem", "onError: " );
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
