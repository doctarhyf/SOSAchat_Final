package com.example.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.rhyfdocta.sosachat.HelperObjects.HM;
import com.example.rhyfdocta.sosachat.HelperObjects.HelperMethods;
import com.example.rhyfdocta.sosachat.ObjectsModels.LookingFor;
import com.example.rhyfdocta.sosachat.R;

import java.util.ArrayList;

public class AdapterLookingFor extends ArrayAdapter<LookingFor> {

    private final Context context;
    private final CallBacks callBacks;
    private ArrayList<LookingFor> inquiries;

    public AdapterLookingFor(Context context, ArrayList<LookingFor> inquiries, CallBacks callBacks){
        super(context, 0, inquiries);
        this.context = context;
        this.inquiries = inquiries;
        this.callBacks = callBacks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final LookingFor lookingFor = getItem(position);



        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_inquiry, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvListInquiryTitle);
        TextView tvPostedBy = convertView.findViewById(R.id.tvInqPostedBy);
        TextView tvDate = convertView.findViewById(R.id.tvInqDate);

        tvTitle.setText(HelperMethods.UCFirst(lookingFor.getTitle()));
        tvPostedBy.setText(HM.UCF(lookingFor.getPosterName()));
        tvDate.setText(lookingFor.getDateTime());

        /*
        tvQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("CENI", "onClick: Question : " + question.getQuestion() );
                callBacks.onQuestionClicked(question);
            }
        });*/

        return convertView;
    }

    public interface CallBacks {
        void onLookingForsLoaded(ArrayList<LookingFor> inquiries);

        void onLookingForsLoadError(boolean isNetworkError, String message);

        void onLookingForsEmpty();
    }
}
