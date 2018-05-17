package com.example.rhyfdocta.sosachat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.HelperObjects.HM;
import com.example.rhyfdocta.sosachat.ObjectsModels.Inquiry;
import com.example.rhyfdocta.sosachat.adapters.AdapterInquiry;

import java.util.ArrayList;

public class ActivityInquiriesList extends AppCompatActivity implements
        AdapterInquiry.CallBacks,
        AdapterView.OnItemClickListener{

    ListView lvInquiries;
    AdapterInquiry adapterInquiry;
    private ArrayList<Inquiry> inquiries;
    private SOS_API sosApi;
    private ProgressBar pb;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_inquiries);

        pb = findViewById(R.id.pbInqList);
        tvError = findViewById(R.id.tvInqListError);

        getSupportActionBar().setTitle(getResources().getString(R.string.titleInquiry));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvInquiries = findViewById(R.id.lvInquiries);

        sosApi = new SOS_API(this);
        sosApi.loadAllInquiries(this);


        inquiries = new ArrayList<>();
        //inquiries.add(new Inquiry("Poster Name","null","My inquiry", "Them details"));

        adapterInquiry = new AdapterInquiry(this, inquiries, this);
        lvInquiries.setAdapter(adapterInquiry);

        lvInquiries.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.menuInqRefresh:
                sosApi.loadAllInquiries(this);
                tvError.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                lvInquiries.setVisibility(View.GONE);
                break;
        }

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_inq_list, menu);

        return true;
    }

    @Override
    public void onInquiriesLoaded(ArrayList<Inquiry> inquiries) {
        this.inquiries = inquiries;
        adapterInquiry = new AdapterInquiry(this, inquiries, this);
        //adapterInquiry.notifyAll();
        lvInquiries.setAdapter(adapterInquiry);

        if(inquiries.size() == 0){
            tvError.setVisibility(View.VISIBLE);
            tvError.setText("The lis is empty");

            pb.setVisibility(View.GONE);
            lvInquiries.setVisibility(View.GONE);
        }else {
            pb.setVisibility(View.GONE);
            tvError.setVisibility(View.GONE);
            lvInquiries.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onInquiriesLoadError(boolean isNetworkError, String message) {

        String msg = "onInquiriesLoadError: netErr -> " + isNetworkError + ", msg : " + message;

        Log.e("SOSACHAT", msg );

        tvError.setVisibility(View.VISIBLE);
        tvError.setText(message);

        pb.setVisibility(View.GONE);
        lvInquiries.setVisibility(View.GONE);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Inquiry inquiry = inquiries.get(position);
        Intent intent = new Intent(this, ActivityInquiryView.class);

        Bundle data = new Bundle();
        data.putAll(inquiry.toBundle());
        intent.putExtras(data);
        startActivity(intent);
    }
}
