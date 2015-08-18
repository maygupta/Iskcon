package com.iskcon.pb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class Kirtans extends Activity {

    ListView listView;
    Download mDownload;
    KirtanData currentKirtanData;
    ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_android_example);

        Intent intent = getIntent();
        String jsonStr = intent.getStringExtra("kirtans");

        ArrayList<KirtanData> data = new ArrayList<KirtanData>();

        try {
            JSONArray json = new JSONArray(jsonStr);
            for(int i = 0; i < json.length(); i++) {
                JSONObject row = json.getJSONObject(i);
                data.add(new KirtanData(row.getString("name"), row.getString("url")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Downloading Kirtan !!");
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        mDownload = new Download(this);
        mDownload.setProgress(mProgress);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        ArrayAdapter<KirtanData> kirtanAdapter=new MyArrayAdapter(this,data, this.getFilesDir());

        // Assign adapter to ListView
        listView.setAdapter(kirtanAdapter);

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                currentKirtanData    = (KirtanData) listView.getItemAtPosition(position);

                String myTag=(String)view.getTag();

                if (view == mDownload.getCurrentView()){
                    if ( mDownload.isPlaying() ) {
                        mDownload.pause();
                    } else {
                        mDownload.resume();
                    }
                } else {
                    mDownload.stopCurrentSong();
                    mDownload.setCurrentView(view);
                    play(currentKirtanData.getmName(), (String) view.getTag());
                }

            }

        });
    }

    protected void showProgress() {
        mProgress.show();
    }

    protected void hideProgress() {
        mProgress.dismiss();
    }

    private void play(String name, String url) {

        File file = new File(Kirtans.this.getFilesDir()+"/iskcon/"+ name);
        // Check if the Music file already exists
        if (file.exists()) {
            // Play Music
            mDownload.playMusic(file);
            // If the Music File doesn't exist in SD card (Not yet downloaded)
        } else {
            // Trigger Async Task (onPreExecute method)
            new DownloadMusicfromInternet().execute(name,url);
        }
    }

    class DownloadMusicfromInternet extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            showProgress();
            super.onPreExecute();
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {

                URL url = new URL(f_url[1]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // Get Music file length
                int lenghtOfFile = conection.getContentLength();
                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(),10*1024);

                String dirName = "/iskcon/";
                File directory = new File(Kirtans.this.getFilesDir()+dirName);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Output stream to write file in SD card
                File outFile = new File(directory,f_url[0]);

                OutputStream output = new FileOutputStream(outFile);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // Publish the progress which triggers onProgressUpdate method
                    int progressPercentage = (int) ((total * 100) / lenghtOfFile);
                    mProgress.setProgress(progressPercentage);

                    // Write data to file
                    output.write(data, 0, count);
                }
                // Flush output
                output.flush();
                // Close streams
                output.close();
                input.close();
                hideProgress();
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            mProgress.setProgress(Integer.parseInt(progress[0]));
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            hideProgress();
        }
    }

}