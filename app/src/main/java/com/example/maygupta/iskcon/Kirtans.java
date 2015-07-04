package com.example.maygupta.iskcon;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_android_example);

        mDownload = new Download(this);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);

        ArrayList<KirtanData> data = new ArrayList<KirtanData>();

        // Defined Array values to show in ListView
        String[] values = new String[] {
                "HH BBGS Hare Kirshna kirtan",
                "HH Loknath Swami Guru Puja",
                "Mangal Arti",
                "Gaur Arti",
                "Narsimha Arti",
                "HH BBGS yashomati Nandan",
                "HH Loknath Swami Hare Krishna",
                "HH BBGS Hare Krishna melody",
                "HH BBGS Damodarashtakam",
                "HH BBGS Guru puja",
                "HH BBGS Guru puja",
                "HH BBGS Guru puja",
        };

        for (String val:values) {
            data.add(new KirtanData(val,"http://audio.iskcondesiretree.com/06_-_More/10_-_Bhajans_and_Kirtans_-_Categories/Bhajans_by_A_C_Bhaktivedanta_Swami_Prabhupada/Boro_Krpa_Koile_Krsna/Boro_Krpa_Koile_Krsna_-_Sung_by_HG_Ganga_Narayana_Prabhu_IDT.mp3"));
        }

        ArrayAdapter<KirtanData> kirtanAdapter=new MyArrayAdapter(this,data);

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

                // Show Alert
                Toast.makeText(getApplicationContext(),
                        "Position :" + itemPosition + "  ListItem : " + currentKirtanData.getmName(), Toast.LENGTH_LONG)
                        .show();

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

        File file = new File(Kirtans.this.getFilesDir()+"/iskcon/"+ name);
        // Check if the Music file already exists
        if (file.exists()) {
            Toast.makeText(getApplicationContext(), "File already exist under SD card, playing Music", Toast.LENGTH_LONG).show();
            // Play Music
            mDownload.playMusic(file);
            // If the Music File doesn't exist in SD card (Not yet downloaded)
        } else {
            Toast.makeText(getApplicationContext(), "File doesn't exist under SD Card, downloading Mp3 from Internet", Toast.LENGTH_LONG).show();
            // Trigger Async Task (onPreExecute method)
            new DownloadMusicfromInternet().execute(name,url);
        }
    }

    class DownloadMusicfromInternet extends AsyncTask<String, String, String> {

        // Show Progress bar before downloading Music
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Shows Progress Bar Dialog and then call doInBackground method
//            showDialog(progress_bar_type);
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
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }

        // Once Music File is downloaded
        @Override
        protected void onPostExecute(String file_url) {
            Toast.makeText(Kirtans.this, "Download complete, playing Music", Toast.LENGTH_LONG).show();
            File file = new File(Kirtans.this.getFilesDir()+"/iskcon/"+ currentKirtanData.getmName());
            mDownload.playMusic(file);
        }
    }

}