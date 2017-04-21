package com.mrdheerajpurohit.MultipartImageUpload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


import com.omega.bigdollars.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;

/**
 * Dheeraj Purohit Image Upload Using MultipartImage
 */


public class UploadActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private String filePath = null;
    private TextView txtPercentage;
    private ImageView imgPreview;
    private VideoView vidPreview;
    private Button btnUpload;
    EditText editText;
    long totalSize = 0;
    HttpPost httppost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        txtPercentage = (TextView) findViewById(R.id.txtPercentage);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        vidPreview = (VideoView) findViewById(R.id.videoPreview);
        editText = (EditText) findViewById(R.id.editText);

        if (ImageSelectActivity.activityChecker==8){
            editText.setVisibility(View.GONE);
        }
        // Receiving the data from previous activity
        Intent i = getIntent();

        // image or video path that is captured in previous activity
        filePath = i.getStringExtra("filePath");

        // boolean flag to identify the media type, image or video
        boolean isImage = i.getBooleanExtra("isImage", true);

        if (filePath != null) {
            // Displaying the image or video on the screen
            previewMedia(isImage);
        } else {
            Toast.makeText(getApplicationContext(),
                    "Sorry, file path is missing!", Toast.LENGTH_LONG).show();
        }

        btnUpload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                editText.setVisibility(View.GONE);
                // uploading the file to server
                new UploadFileToServer().execute();
            }
        });

    }


    //    **
//            * Displaying captured image/video on the screen
//    * */
    private void previewMedia(boolean isImage) {
        // Checking whether captured media is image or video
        if (isImage) {
            imgPreview.setVisibility(View.VISIBLE);
            vidPreview.setVisibility(View.GONE);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // down sizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);

            imgPreview.setImageBitmap(bitmap);
        } else {
            imgPreview.setVisibility(View.GONE);
            vidPreview.setVisibility(View.VISIBLE);
            vidPreview.setVideoPath(filePath);
            // start playing
            vidPreview.start();
        }
    }

    /**
     * Uploading the file to server
     * */
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressBar.setProgress(0);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

//            HttpClient httpclient = new DefaultHttpClient();
            if (ImageSelectActivity.activityChecker==8) {

                Log.e("file path","");
                Log.e("checker"," "+ ImageSelectActivity.activityChecker);
                HttpClient httpclient = new DefaultHttpClient();
             //File upload url (replace the ip with your server address)
                httppost = new HttpPost(Config.FILE_UPLOAD_URL);


                try {
                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                            new AndroidMultiPartEntity.ProgressListener() {

                                @Override
                                public void transferred(long num) {
                                    publishProgress((int) ((num / (float) totalSize) * 100));
                                }
                            });

                    File sourceFile = new File(filePath);

                    // Adding file data to http body
                    entity.addPart("image", new FileBody(sourceFile));

                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);

                    // Making server call
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // Server response
                        responseString = EntityUtils.toString(r_entity);
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }

                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                } catch (IOException e) {
                    responseString = e.toString();
                }

//                return responseString;
            }
            else if (ImageSelectActivity.activityChecker==9){

                Log.e("checker"," "+ ImageSelectActivity.activityChecker);
                HttpClient httpclient = new DefaultHttpClient();
                //File upload url (replace the ip with your server address)
                httppost = new HttpPost(Config.UPLOAD_URL);

                try {
                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                            new AndroidMultiPartEntity.ProgressListener() {

                                @Override
                                public void transferred(long num) {
                                    publishProgress((int) ((num / (float) totalSize) * 100));
                                }
                            });

                    File sourceFile = new File(filePath);
                    //   String uploadText = new String(editText.getText().toString());



                    // Adding file data to http body
                    entity.addPart("image", new FileBody(sourceFile));
                    entity.addPart("uploadText", new StringBody(editText.getText().toString()));
                    // entity.addPart("uploadText", new StringBody(uploadText));


                    // Extra parameters if you want to pass to server

                    // if uploading image, send filetype = 1
                if (ImageSelectActivity.ImageVideoChecker==1) {
                    Log.e("filetype","filetype");


                    entity.addPart("filetype", new StringBody("1"));
                    Log.e("filetype sent","filetype sent");
                }
                // if uploading video, send file type = 2
                else if (ImageSelectActivity.ImageVideoChecker==2){
                    entity.addPart("filetype",new StringBody("2"));
                }

                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);

                    // Making server call
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();

                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // Server response
                        responseString = EntityUtils.toString(r_entity);
                        Log.e("status code"," 200");
                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                    }

                } catch (ClientProtocolException e) {
                    responseString = e.toString();
                } catch (IOException e) {
                    responseString = e.toString();
                }


            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e("response", "Response from server: " + result);

            super.onPostExecute(result);
            startActivity(new Intent(getApplicationContext(),ImageSelectActivity.class));
        }

    }


}
