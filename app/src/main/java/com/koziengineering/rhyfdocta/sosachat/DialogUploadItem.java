package com.koziengineering.rhyfdocta.sosachat;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.rhyfdocta.sosachat.R;

import java.text.DecimalFormat;

public class DialogUploadItem extends AlertDialog {
    
    private double progress = 0;
    private double dataSize = 0;
    private double dataProgress = 0;
    private TextView tvProgress, tvData, tvTitle, tvMessage;
    private ProgressBar progressBar;
    private Button dbBtnCancel;
    
    protected DialogUploadItem(@NonNull Context context) {
        super(context);

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_upload_item, null);
        setView(view);
        
        tvProgress = view.findViewById(R.id.tvProgress);
        tvData = view.findViewById(R.id.tvData);
        tvTitle = view.findViewById(R.id.dgTvTitle);
        tvMessage = view.findViewById(R.id.dgTvMessage);
        progressBar = view.findViewById(R.id.pb);
        //progressBar.set(ProgressDialog.STYLE_HORIZONTAL);
        dbBtnCancel = view.findViewById(R.id.dbBtnCancel);
        progressBar.setIndeterminate(false);

        dbBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        if (tvTitle.getText().equals("")){
            tvTitle.setVisibility(View.GONE);
        }
    }

    

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
        
        dataProgress = (progress / 100.0) * dataSize ;
        updateDisplays();
    }



    private void updateDisplays() {
        String strProgress = progress + "%";
        tvProgress.setText(strProgress);
        //dataSize /= 1000000;
        //dataProgress /= 1000000;


        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        String dprog = decimalFormat.format(((progress / 100) * dataSize) / 1000000);
        String dtot = decimalFormat.format( dataSize / 1000000);



        String strProgressData = dprog + "/" + dtot + "MB";
        tvData.setText(strProgressData);

        progressBar.setProgress((int)progress);

        Log.e("DBP", "dataSize -> " + dtot );
        Log.e("DBP", "dataProgress -> " + dprog );


    }



    @Override
    public void setTitle(CharSequence title) {
        tvTitle.setText(title);
    }

    @Override
    public void setMessage(CharSequence message) {
        tvMessage.setText(message);
    }

    public double getDataSize() {
        return dataSize;
    }

    public void setDataSize(double dataSize) {
        this.dataSize = dataSize;
    }

    public double getDataProgress() {
        return dataProgress;
    }

    public void setDataProgress(double dataProgress) {
        this.dataProgress = dataProgress;

        progress = (dataSize / dataProgress) * 100.0;
        updateDisplays();
    }

    public void init() {

        setProgress(0);
        dataSize = 0;
        dataProgress = 0;
        updateDisplays();
    }
}
