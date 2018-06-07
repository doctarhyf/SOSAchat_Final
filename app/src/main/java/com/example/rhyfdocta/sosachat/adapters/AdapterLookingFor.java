package com.example.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.Helpers.BitmapCacheManager;
import com.example.rhyfdocta.sosachat.Helpers.HM;
import com.example.rhyfdocta.sosachat.Helpers.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.LookingFor;
import com.example.rhyfdocta.sosachat.ObjectsModels.Product;
import com.example.rhyfdocta.sosachat.R;

import java.util.ArrayList;

public class AdapterLookingFor extends ArrayAdapter<LookingFor> {

    private final Context context;
    private final CallBacks callBacks;
    private ArrayList<LookingFor> inquiries;
    private SOS_API sosApi;

    public AdapterLookingFor(Context context, ArrayList<LookingFor> inquiries, CallBacks callBacks){
        super(context, 0, inquiries);
        this.context = context;
        this.inquiries = inquiries;
        this.callBacks = callBacks;
        this.sosApi = new SOS_API(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LookingFor lookingFor = getItem(position);



        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_lookingfor, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvTitleLookingfor);
        TextView tvPostedBy = convertView.findViewById(R.id.tvInqPostedBy);
        TextView tvDate = convertView.findViewById(R.id.tvInqDate);
        TextView tvPreview = convertView.findViewById(R.id.tvPreviewLookingfor);
        ImageView iv = convertView.findViewById(R.id.ivLookingFor);

        tvTitle.setText(HelperMethods.UCFirst(lookingFor.getTitle()));
        tvPostedBy.setText(HM.UCF(lookingFor.getPosterName()));
        tvDate.setText(lookingFor.getDateTime());
        tvPreview.setText(lookingFor.getMessage());

        final String pathPP = (String) lookingFor.getValue(LookingFor.KEY_PATH_PP);
        String mtime = (String) lookingFor.getValue(LookingFor.KEY_MTIME_PP);
        long mtimel = mtime == null || mtime.isEmpty() ? 0 : Long.parseLong(mtime);
        String fileName = (String) lookingFor.getProperty(SOS_API.KEY_ACC_DATA_MOBILE_HASH) + ".jpg";


        BitmapCacheManager.GlideUniversalLoaderLoadPathIntoImageView(
                context,
                pathPP,
                mtimel,
                fileName,
                BitmapCacheManager.PIC_CACHE_ROOT_PATH_ID_PROFILE_PIC,
                SOS_API.DIR_NAME_PIX_CACHE_PROFILCE_PIC,
                iv,
                new BitmapCacheManager.CallbacksBitmapLoading() {
                    @Override
                    public void onItemClicked(Product pd) {

                    }

                    @Override
                    public void saveBitmapToLocalCache(Bitmap bitmap, String picUrl, String dirName) {
                        sosApi.getBitmapCacheManager().saveBitmapToCache(bitmap,pathPP,dirName);
                    }
                }

        );

        return convertView;
    }

    public interface CallBacks {
        void onLookingForsLoaded(ArrayList<LookingFor> inquiries);

        void onLookingForsLoadError(boolean isNetworkError, String message);

        void onLookingForsEmpty();
    }
}
