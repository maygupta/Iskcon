package com.iskcon.pb.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadMedia extends AsyncTask<String, Integer, String> {

    @Override
    protected String doInBackground(String... mediaData) {
        int count;
        try {
            URL mediaUrl = new URL(mediaData[0]);
            URLConnection conn = mediaUrl.openConnection();
            conn.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = conn.getContentLength();

            // download the file
            InputStream input = new BufferedInputStream(mediaUrl.openStream());

            OutputStream output = new FileOutputStream(mediaData[1]);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress((int) (total * 100 / lenghtOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.d("VALUES", String.valueOf(values[0]));
    }
}