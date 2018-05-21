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
import com.example.rhyfdocta.sosachat.ObjectsModels.LookingFor;
import com.example.rhyfdocta.sosachat.adapters.AdapterLookingFor;

import java.util.ArrayList;

public class ActivityLookingFor extends AppCompatActivity implements
        AdapterLookingFor.CallBacks,
        AdapterView.OnItemClickListener{

    ListView lvInquiries;
    AdapterLookingFor adapterLookingFor;
    private ArrayList<LookingFor> inquiries;
    private SOS_API sosApi;
    private ProgressBar pb;
    private TextView tvError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_inquiries);

        pb = findViewById(R.id.pbInqList);
        tvError = findViewById(R.id.tvInqListError);

        getSupportActionBar().setTitle(getResources().getString(R.string.titleLookingFor));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvInquiries = findViewById(R.id.lvInquiries);

        sosApi = new SOS_API(this);
        sosApi.loadAllLookingFors(this);


        inquiries = new ArrayList<>();
        //inquiries.add(new LookingFor("Poster Name","null","My inquiry", "Them details"));

        adapterLookingFor = new AdapterLookingFor(this, inquiries, this);
        lvInquiries.setAdapter(adapterLookingFor);

        lvInquiries.setOnItemClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;

            case R.id.menuInqRefresh:
                sosApi.loadAllLookingFors(this);
                tvError.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                lvInquiries.setVisibility(View.GONE);
                break;

            case R.id.menuInqNew:
                Intent intent = new Intent(this, ActivityNewLookingFor.class);
                startActivity(intent);
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
    public void onLookingForsLoaded(ArrayList<LookingFor> inquiries) {
        this.inquiries = inquiries;
        adapterLookingFor = new AdapterLookingFor(this, inquiries, this);
        //adapterLookingFor.notifyAll();
        lvInquiries.setAdapter(adapterLookingFor);

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
    public void onLookingForsLoadError(boolean isNetworkError, String message) {

        String msg = "onLookingForsLoadError: netErr -> " + isNetworkError + ", msg : " + message;

        Log.e("SOSACHAT", msg );


        tvError.setText(getResources().getString(R.string.msgServerUnreachable));
        tvError.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
        lvInquiries.setVisibility(View.GONE);
    }

    @Override
    public void onLookingForsEmpty() {

        tvError.setText(getResources().getString(R.string.msgInqListEmpty));
        tvError.setVisibility(View.VISIBLE);

        pb.setVisibility(View.GONE);
        lvInquiries.setVisibility(View.GONE);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        LookingFor lookingFor = inquiries.get(position);
        Intent intent = new Intent(this, ActivityLookingForView.class);

        Bundle data = new Bundle();
        data.putAll(lookingFor.toBundle());
        intent.putExtras(data);
        startActivity(intent);
    }
}
