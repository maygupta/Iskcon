package com.iskcon.pb;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        mProgress = new ProgressDialog(this);
        mDownload = new Download(this);
        mDownload.setProgress(mProgress);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        ArrayList<KirtanData> data = new ArrayList<KirtanData>();

        // Defined Array values to show in ListView
        // http://harekrishnasongs.com/tag/hare-krishna-tunes/
        String[][] values = {
                {"HH Loknath Swami Guru Puja", "http://lokanathswamikirtans.com/kirtans/2015/April/Day_1_Mauritius_kirtan_mela_10_4_15_HH_LNS.MP3"},
                {"Mangal Arti", "http://lokanathswamikirtans.com/kirtans/2015/12_feb_mayapur_eve.mp3"},
                {"Gaur Arti","http://lokanathswamikirtans.com/kirtans/Mukund_das/Bhaja_gauranga.mp3"},
                {"Narsimha Arti","http://lokanathswamikirtans.com/kirtans/2014/Oct/Damodarastakam%20by%20Guru%20maharaj.mp3"},
                {"HH BBGS yashomati Nandan","http://www.mayapur.com/download/Kirtans/2008-07-07_rathayatra.festival.kirtan_kgp.mp3"},
                {"HH Bhakti Charu Swami","http://harekrishnasongs.com/wp-content/audio/Bhakti_Charu_Swami/Hare_Krishna_Kirtans/BCS_Bhajans_-_Harinaam_Sankirtan_-_2008-10-28_Mayapur.mp3"},
                {"Ohe Vaishanva Thakur","http://www.mayapur.com/download/Kirtans/2008-07-17_ohe!.vaisnava.thakura_jgmp.mp3"},
                {"SP Hare Krishna","http://www.iskconct.org/mp3/sp/Hare%20Krishna%20Kirtan.mp3"},
                {"HH Bhakti Charu Swami","http://harekrishnasongs.com/wp-content/audio/Bhakti_Charu_Swami/Hare_Krishna_Kirtans/14_Bhajans_-_Hare_Krishna_Kirtan_-_Bhakti_Charu_Sw_Punjabi_Baugh.mp3"},
                {"HH BBGS Hare Krishna", "http://harekrishnasongs.com/wp-content/audio/Bhakti_Bringa_Govinda_Swami/Hare_Krishna_Kirtans/07_Bhajans_-_Hare_Krishna_Kirtan_-_Bhakti_Bringa_Govinda_Sw_Punjabi_Baugh.mp3"},
                {"HH BBGS Hare Krishna fav tune","http://harekrishnasongs.com/wp-content/audio/Bhakti_Bringa_Govinda_Swami/Hare_Krishna_Kirtans/16_Bhajans_-_Hare_Krishna_Kirtan_-_Bhakti_Bringa_Govinda_Sw_Punjabi_Baugh.mp3"},
                {"HH BBGS SVD Kirtana", "http://harekrishnasongs.com/wp-content/audio/Bhakti_Bringa_Govinda_Swami/Hare_Krishna_Kirtans/BBGS_Bhajans_-_Hare_Krishna_Kirtan_-_2011-12-16_Vrindavan.MP3"}
        };


        for (String[] val:values) {
            data.add(new KirtanData(val[0],val[1]));
        }

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
        mProgress.setMessage("Downloading...");
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
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

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
            File file = new File(Kirtans.this.getFilesDir()+"/iskcon/"+ currentKirtanData.getmName());
            mDownload.playMusic(file);
        }
    }

}