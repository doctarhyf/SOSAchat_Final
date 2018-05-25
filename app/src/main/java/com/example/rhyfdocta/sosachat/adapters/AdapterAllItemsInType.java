package com.example.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.Helpers.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Franvanna on 1/5/2018.
 */

public class AdapterAllItemsInType extends RecyclerView.Adapter<AdapterAllItemsInType.ViewHolder> {


    private SOS_API sosApi;

    public static  class ViewHolder extends RecyclerView.ViewHolder {


        public TextView tvName, tvPriceNQual, tvDate;
        public ImageView iv, ivAddToWishlist;
        public View layout;

        public ViewHolder(View view){
            super(view);
            layout = view;
            tvName = layout.findViewById(R.id.tvItemName);
            tvPriceNQual = layout.findViewById(R.id.tvItemPriceNQual);
            tvDate = layout.findViewById(R.id.tvItemPostedOn);
            ivAddToWishlist = layout.findViewById(R.id.ivAddToWishlist);
            iv = layout.findViewById(R.id.ivItemInType);

        }
    }

    private List<Product> list;
    private ListenerItemsInType listener;

    public AdapterAllItemsInType(Context context, List<Product> list, ListenerItemsInType listener){
        this.list = list;
        this.listener = listener;
        sosApi = new SOS_API(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_item_in_type, parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        Context context = holder.iv.getContext();

        final Product pd = list.get(position);
        holder.tvName.setText(pd.getPdName());
        holder.tvDate.setText(pd.getData().getString(Product.KEY_PD_DATE_ADDED));

        String[] qualities = HelperMethods.RGSA(context, R.array.newItemQuality);
        String quality = qualities[new Integer(pd.getPdQual()).intValue()];
        String price = HM.GIPB(context, R.string.priceToDiscuss, pd.getPdPrice(),pd.getPdCur());

        String priceNQual = price + " / " + quality;

        holder.tvPriceNQual.setText(priceNQual);

        final Uri picUri = Uri.parse(SOS_API.DIR_PATH_CATEGORIES + "products/" + pd.getData().getString(SOS_API.KEY_ITEM_UNIQUE_NAME) + "_main.jpg");

        Picasso.with(context)
                .load(picUri)
                .error(R.drawable.ic_error)
                .placeholder(R.drawable.progress_animation)
                .centerInside()
                .resize(300,300)
                .into(holder.iv);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onItemClicked(pd);
            }
        });

        if(pd.getData().getString(SOS_API.KEY_ACC_DATA_USER_ID).equals(sosApi.GSV(SOS_API.KEY_ACC_DATA_USER_ID))){
            holder.ivAddToWishlist.setVisibility(View.INVISIBLE);
        }

        holder.ivAddToWishlist.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                if(event.getAction() == 0){
                    holder.ivAddToWishlist.setAlpha(0.5f);

                }

                if(event.getAction() == 1 || event.getAction() == 3) {
                    holder.ivAddToWishlist.setAlpha(1f);


                }

                if(event.getAction() == 1){
                    listener.onItemAddToWishlistClicked(pd);
                }




                return true;





            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static interface ListenerItemsInType{
        public void onItemClicked(Product pd);
        public void onItemAddToWishlistClicked(Product pd);
    }


}
