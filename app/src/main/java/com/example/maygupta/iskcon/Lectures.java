package com.example.maygupta.iskcon;

import android.app.Activity;
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


public class Lectures extends Activity {

    ListView listView;
    Download mDownload;
    KirtanData currentKirtanData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_android_example);

        mDownload = new Download(this);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        ArrayList<KirtanData> data = new ArrayList<KirtanData>();

        // Defined Array values to show in ListView
        String[][] values = {
                {"HG Rukmini Krishna Prabhu 7.14.23", "http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krshna%20P%204July%28S.B.7-14-23%29.mp3"},
                {"HG Rukmini Krishna Prabhu 7.14.08","http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krsna%20P%2020June%20%28S.B.7_14-8%29.mp3"},
                {"HG Rukmini Krishna Prabhu 7.13.09","http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P_25th%20APril2015_SB%207.13.19.mp3"},
                {"HG Rukmini Krishna pr Gaur Purnima","http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P_5th%20Feb2015_Gaur%20Purnima.mp3"},
                {"Narsimha Arti","http://lokanathswamikirtans.com/kirtans/2014/Oct/Damodarastakam%20by%20Guru%20maharaj.mp3"},
                {"HG Rukmini Krishna Pr 7.11.29","http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P%2028%20feb%20%28S.B.%207-11.29%29.mp3"},
                {"Varaha Dwadashi","http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukimini%20Krishna%20P_31stJan2015_Varaha%20Dwadashi_SB%203.18.6.mp3"},
                {"HG Rukmini Krishna Prabhu 7.10.23","http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P_SB%207.10.23_3rd%20Jan2015.mp3"},
                {"HG Rukmini Krishna Prabhu 7.10.18","http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P_SB%207.10.18_27th%20Dec2014.mp3"},
                {"HG Rukmini Krishna Prabhu 7.10.09","http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P_SB%207.10.9_13th%20Dec%202014.mp3"},
                {"HG Rukmini Krishna Prabhu 7.10.01","http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P%20_29Nov_2014%20SB%207.10.1.mp3"}
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

    private void play(String name, String url) {

        File file = new File(Lectures.this.getFilesDir()+"/iskcon/"+ name);
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
                File directory = new File(Lectures.this.getFilesDir()+dirName);

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
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            File file = new File(Lectures.this.getFilesDir()+"/iskcon/"+ currentKirtanData.getmName());
            mDownload.playMusic(file);
        }
    }

}