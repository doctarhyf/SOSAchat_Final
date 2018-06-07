package com.example.rhyfdocta.sosachat.Helpers;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.rhyfdocta.sosachat.API.SOS_API;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class UploadAsyncTask extends AsyncTask<Void, Integer, String> {

   // private String serverFormFileName;
    private HttpClient httpClient = new DefaultHttpClient();
    private Context context;
    private Exception exception;
    //private ProgressDialog progressDialog;
    private File file;
    private String SERVER_PATH;
    private Callbacks callbacks;
    //private String tag;

    public UploadAsyncTask(Context context, String filePath, String serverPath,  Callbacks callbacks) {
        this.context = context;
        //Log.e("DAFAK", "UploadAsyncTask: -> " + filePath );
        this.file = new File(filePath);
        this.SERVER_PATH = serverPath;
        //this.serverFormFileName = serverFormFileName;
        this.callbacks = callbacks;
    }

    public interface Callbacks{

        void onProgress(int progress);
        void onPostExecute(String result);
        void onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {



        HttpResponse httpResponse = null;
        HttpEntity httpEntity = null;
        String responseString = null;

        try {
            HttpPost httpPost = new HttpPost(SERVER_PATH);
            MultipartEntityBuilder multipartEntityBuilder = MultipartEntityBuilder.create();


            // Add the file to be uploaded
            multipartEntityBuilder.addPart(SOS_API.IMAGE_UPLOAD_FORM_NAME, new FileBody(file));

            // Progress listener - updates task's progress
            MyHttpEntity.ProgressListener progressListener =
                    new MyHttpEntity.ProgressListener() {
                        @Override
                        public void transferred(float progress) {
                            publishProgress((int) progress);
                        }
                    };

            // POST
            httpPost.setEntity(new MyHttpEntity(multipartEntityBuilder.build(),
                    progressListener));


            httpResponse = httpClient.execute(httpPost);
            httpEntity = httpResponse.getEntity();

            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(httpEntity);
            } else {
                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }
        } catch (UnsupportedEncodingException | ClientProtocolException e) {
            e.printStackTrace();
            Log.e("UPLOAD", e.getMessage());
            this.exception = e;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseString;
    }

    @Override
    protected void onPreExecute() {

        // Init and show dialog
        /*this.progressDialog = new ProgressDialog(this.context);
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        this.progressDialog.setCancelable(false);
        this.progressDialog.show();*/
        callbacks.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {

        // Close dialog
        //this.progressDialog.dismiss();
        Log.e("UAT", "onPostExecute: -> " + result );
        //showFileChooser();
        callbacks.onPostExecute(result);

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // Update process
        //this.progressDialog.setProgress((int) progress[0]);
        callbacks.onProgress(progress[0]);

    }
}
