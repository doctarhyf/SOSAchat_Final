package com.example.rhyfdocta.sosachat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.rhyfdocta.sosachat.ObjectsModels.Inquiry;
import com.example.rhyfdocta.sosachat.R;

import java.util.ArrayList;

public class AdapterInquiry extends ArrayAdapter<Inquiry> {

    private final Context context;
    private final CallBacks callBacks;
    private ArrayList<Inquiry> inquiries;

    public AdapterInquiry(Context context, ArrayList<Inquiry> inquiries, CallBacks callBacks){
        super(context, 0, inquiries);
        this.context = context;
        this.inquiries = inquiries;
        this.callBacks = callBacks;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Inquiry inquiry = getItem(position);



        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_item_inquiry, parent, false);
        }

        TextView tvTitle = convertView.findViewById(R.id.tvListInquiryTitle);
        TextView tvPostedBy = convertView.findViewById(R.id.tvInqPostedBy);
        TextView tvDate = convertView.findViewById(R.id.tvInqDate);

        tvTitle.setText(inquiry.getTitle());
        tvPostedBy.setText(inquiry.getPosterName());
        tvDate.setText(inquiry.getDateTime());

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
        void onInquiriesLoaded(ArrayList<Inquiry> inquiries);

        void onInquiriesLoadError(boolean isNetworkError, String message);

        void onInquiriesEmpty();
    }
}
