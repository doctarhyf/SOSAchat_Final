package com.example.rhyfdocta.sosachat;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.rhyfdocta.sosachat.API.SOS_API;
import com.example.rhyfdocta.sosachat.ObjectsModels.LookingFor;
import com.example.rhyfdocta.sosachat.adapters.AdapterLookingFor;

import java.util.ArrayList;

public class ActivityLookingFor extends AppCompatActivity implements
        AdapterLookingFor.CallBacks,
        AdapterView.OnItemClickListener, SearchView.OnQueryTextListener {

    ListView lvInquiries;
    AdapterLookingFor adapterLookingFor;
    private ArrayList<LookingFor> looking4s;
    private SOS_API sosApi;
    private ProgressBar pb;
    private TextView tvError;
    private  boolean mine = false;
    private Switch swOnlyMyLookingfor;
    private Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookingfors);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        swOnlyMyLookingfor = findViewById(R.id.swOnlyMyLookingfor);

        swOnlyMyLookingfor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mine = isChecked;
                lvInquiries.setVisibility(View.GONE);
                pb.setVisibility(View.VISIBLE);
                sosApi.loadLookingFors(ActivityLookingFor.this, mine,  SOS_API.NO_LIMIT);
            }
        });

        pb = findViewById(R.id.pbInqList);
        tvError = findViewById(R.id.tvInqListError);

        getSupportActionBar().setTitle(getResources().getString(R.string.titleLookingFor));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lvInquiries = findViewById(R.id.lvInquiries);

        sosApi = new SOS_API(this);

        Bundle extras= getIntent().getExtras();

        if(extras!= null){
            mine = extras.getBoolean(LookingFor.KEY_IS_MINE, false);

        }

        swOnlyMyLookingfor.setChecked(mine);

        sosApi.loadLookingFors(this, mine,  SOS_API.NO_LIMIT);


        looking4s = new ArrayList<>();
        //looking4s.add(new LookingFor("Poster Name","null","My inquiry", "Them details"));

        adapterLookingFor = new AdapterLookingFor(this, looking4s, this);
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
                sosApi.loadLookingFors(this, mine,SOS_API.NO_LIMIT);
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

        getMenuInflater().inflate(R.menu.menu_looking4s, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        /*if(!q.equals("")) {
            searchView.setIconified(false);
            onQueryTextChange(q);
        }*/

        searchView.setOnQueryTextListener(this);

        return true;
    }

    @Override
    public void onLookingForsLoaded(ArrayList<LookingFor> inquiries) {
        this.looking4s = inquiries;
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

        LookingFor lookingFor = looking4s.get(position);
        Intent intent = new Intent(this, ActivityLookingForView.class);

        Bundle data = new Bundle();

        data.putAll(lookingFor.toBundle());
        data.putBoolean(LookingFor.KEY_IS_MINE, mine);
        intent.putExtras(data);
        startActivity(intent);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        filterAdapter(query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        filterAdapter(newText);

        return true;
    }

    private void filterAdapter(String newText) {
        newText = newText.toLowerCase();
        ArrayList<LookingFor> newList = new ArrayList<>();

        for(LookingFor lookingFor : looking4s){
            String title = (String) lookingFor.getProperty(LookingFor.KEY_TITLE);
            String msg = (String) lookingFor.getProperty(LookingFor.KEY_DESC);

            if(title.toLowerCase().contains(newText) || msg.toLowerCase().contains(newText)){
                newList.add(lookingFor);
            }
        }

        adapterLookingFor.setFilter(newList);
    }
}
