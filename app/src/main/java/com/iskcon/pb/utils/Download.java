package com.iskcon.pb.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.iskcon.pb.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Download {
    // Media Player Object
    private MediaPlayer mPlayer;

    private Context mContext;

    public void setProgress(ProgressDialog progress) {
        this.progress = progress;
    }

    private ProgressDialog progress;

    public void setCurrentView(View currentView) {
        this.currentView = currentView;
    }

    public View getCurrentView() {
        return currentView;
    }

    private View currentView;

    public Download(){}
    public Download(Context context) {
        mContext = context;
    }

    public boolean isPlaying() {
        boolean ret = false;
        if ( mPlayer != null ) {
            ret = mPlayer.isPlaying();
        }
        return ret;
    }

    public void stopCurrentSong() {
        if (mPlayer != null && currentView != null) {
            (currentView.findViewById(R.id.play_icon)).setVisibility(View.VISIBLE);
            (currentView.findViewById(R.id.pause_icon)).setVisibility(View.GONE);
            mPlayer.stop();
        }
    }

    public void pause() {
        if ( mPlayer != null ) {
            (currentView.findViewById(R.id.play_icon)).setVisibility(View.VISIBLE);
            (currentView.findViewById(R.id.pause_icon)).setVisibility(View.GONE);
            mPlayer.pause();
        }
    }

    public void resume(File file) {
        if ( mPlayer != null ) {
            (currentView.findViewById(R.id.play_icon)).setVisibility(View.GONE);
            (currentView.findViewById(R.id.pause_icon)).setVisibility(View.VISIBLE);
            mPlayer.seekTo(mPlayer.getCurrentPosition());
            mPlayer.start();
        } else {
            playMusic(file);
        }
    }

    // Play Music
    public void playMusic(File file){
        // Read Mp3 file present under SD card
        Uri myUri1 = Uri.fromFile(file);
        mPlayer  = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        (currentView.findViewById(R.id.download_icon)).setVisibility(View.GONE);
        (currentView.findViewById(R.id.play_icon)).setVisibility(View.GONE);
        (currentView.findViewById(R.id.pause_icon)).setVisibility(View.VISIBLE);
        try {
            mPlayer.setDataSource(mContext, myUri1);
            mPlayer.prepare();
            // Start playing the Music file
            mPlayer.start();
            mPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mp) {
                }
            });
        } catch (IllegalArgumentException e) {
            Toast.makeText(mContext, "You might not set the URI correctly!",	Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(mContext,	"URI cannot be accessed, permission needed",	Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(mContext,	"Media Player is not in correct state",	Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(mContext,	"IO Error occured",	Toast.LENGTH_LONG).show();
        }
    }

    public void markDownloadComplete(File file) {
        (currentView.findViewById(R.id.download_icon)).setVisibility(View.GONE);
        (currentView.findViewById(R.id.play_icon)).setVisibility(View.VISIBLE);
        (currentView.findViewById(R.id.pause_icon)).setVisibility(View.GONE);
    }

    public static class MemoryCache {
        private Map<String, SoftReference<Bitmap>> cache= Collections.synchronizedMap(new HashMap<String, SoftReference<Bitmap>>());

        public Bitmap get(String id){
            if(!cache.containsKey(id))
                return null;
            SoftReference<Bitmap> ref=cache.get(id);
            return ref.get();
        }

        public void put(String id, Bitmap bitmap){
            cache.put(id, new SoftReference<Bitmap>(bitmap));
        }

        public void clear() {
            cache.clear();
        }
    }

    public static class Utils {
        public static void CopyStream(InputStream is, OutputStream os)
        {
            final int buffer_size=1024;
            try
            {
                byte[] bytes=new byte[buffer_size];
                for(;;)
                {
                    int count=is.read(bytes, 0, buffer_size);
                    if(count==-1)
                        break;
                    os.write(bytes, 0, count);
                }
            }
            catch(Exception ex){}
        }
    }

    /**
     * Created by maygupta on 8/18/15.
     */
    public static class JsonParser {

        static InputStream is = null;
        static JSONObject jObj = null;
        static String json = "";

        public JsonParser (){

        }
        public String getJSONFromUrl(String url )
        {
            try
            {
                DefaultHttpClient httpClient= new DefaultHttpClient();
                HttpGet httpPost = new HttpGet(url);
                //  httpPost.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }catch(Exception e)
            {
                String kirtans = "[{ \"name\": \"HH Loknath Swami Guru Puja\", \"url\": \"http://lokanathswamikirtans.com/kirtans/2015/April/Day_1_Mauritius_kirtan_mela_10_4_15_HH_LNS.MP3\"},\n" +
                        "{ \"name\": \"Mangal Arti\", \"url\": \"http://lokanathswamikirtans.com/kirtans/2015/12_feb_mayapur_eve.mp3\"},\n" +
                        "{ \"name\": \"Gaur Arti\",\"url\": \"http://lokanathswamikirtans.com/kirtans/Mukund_das/Bhaja_gauranga.mp3\"},\n" +
                        "{ \"name\": \"Narsimha Arti\",\"url\": \"http://lokanathswamikirtans.com/kirtans/2014/Oct/Damodarastakam%20by%20Guru%20maharaj.mp3\"},\n" +
                        "{ \"name\": \"HH BBGS yashomati Nandan\",\"url\": \"http://www.mayapur.com/download/Kirtans/2008-07-07_rathayatra.festival.kirtan_kgp.mp3\"},\n" +
                        "{ \"name\": \"HH Bhakti Charu Swami\",\"url\": \"http://harekrishnasongs.com/wp-content/audio/Bhakti_Charu_Swami/Hare_Krishna_Kirtans/BCS_Bhajans_-_Harinaam_Sankirtan_-_2008-10-28_Mayapur.mp3\"},\n" +
                        "{ \"name\": \"Ohe Vaishanva Thakur\",\"url\": \"http://www.mayapur.com/download/Kirtans/2008-07-17_ohe!.vaisnava.thakura_jgmp.mp3\"},\n" +
                        "{ \"name\": \"SP Hare Krishna\",\"url\": \"http://www.iskconct.org/mp3/sp/Hare%20Krishna%20Kirtan.mp3\"},\n" +
                        "{ \"name\": \"HH Bhakti Charu Swami\",\"url\": \"http://harekrishnasongs.com/wp-content/audio/Bhakti_Charu_Swami/Hare_Krishna_Kirtans/14_Bhajans_-_Hare_Krishna_Kirtan_-_Bhakti_Charu_Sw_Punjabi_Baugh.mp3\"},\n" +
                        "{ \"name\": \"HH BBGS Hare Krishna\", \"url\": \"http://harekrishnasongs.com/wp-content/audio/Bhakti_Bringa_Govinda_Swami/Hare_Krishna_Kirtans/07_Bhajans_-_Hare_Krishna_Kirtan_-_Bhakti_Bringa_Govinda_Sw_Punjabi_Baugh.mp3\"},\n" +
                        "{ \"name\": \"HH BBGS Hare Krishna fav tune\",\"url\": \"http://harekrishnasongs.com/wp-content/audio/Bhakti_Bringa_Govinda_Swami/Hare_Krishna_Kirtans/16_Bhajans_-_Hare_Krishna_Kirtan_-_Bhakti_Bringa_Govinda_Sw_Punjabi_Baugh.mp3\"},\n" +
                        "{ \"name\": \"HH BBGS SVD Kirtana\", \"url\": \"http://harekrishnasongs.com/wp-content/audio/Bhakti_Bringa_Govinda_Swami/Hare_Krishna_Kirtans/BBGS_Bhajans_-_Hare_Krishna_Kirtan_-_2011-12-16_Vrindavan.MP3\"}]";

                String lectures = "[{ \"name\": \"HG Rukmini Krishna Prabhu 7.14.23\", \"url\": \"http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krshna%20P%204July%28S.B.7-14-23%29.mp3\"},\n" +
                        "{ \"name\": \"HG Rukmini Krishna Prabhu 7.14.08\",\"url\": \"http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krsna%20P%2020June%20%28S.B.7_14-8%29.mp3\"},\n" +
                        "{ \"name\": \"HG Rukmini Krishna Prabhu 7.13.09\",\"url\": \"http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P_25th%20APril2015_SB%207.13.19.mp3\"},\n" +
                        "{ \"name\": \"HG Rukmini Krishna pr Gaur Purnima\",\"url\": \"http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P_5th%20Feb2015_Gaur%20Purnima.mp3\"},\n" +
                        "{ \"name\": \"HG Rukmini Krishna Pr 7.11.29\",\"url\": \"http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P%2028%20feb%20%28S.B.%207-11.29%29.mp3\"},\n" +
                        "{ \"name\": \"Varaha Dwadashi\",\"url\": \"http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukimini%20Krishna%20P_31stJan2015_Varaha%20Dwadashi_SB%203.18.6.mp3\"},\n" +
                        "{ \"name\": \"HG Rukmini Krishna Prabhu 7.10.23\",\"url\": \"http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P_SB%207.10.23_3rd%20Jan2015.mp3\"},\n" +
                        "{ \"name\": \"HG Rukmini Krishna Prabhu 7.10.18\",\"url\": \"http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P_SB%207.10.18_27th%20Dec2014.mp3\"},\n" +
                        "{ \"name\": \"HG Rukmini Krishna Prabhu 7.10.09\",\"url\": \"http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P_SB%207.10.9_13th%20Dec%202014.mp3\"},\n" +
                        "{ \"name\": \"HG Rukmini Krishna Prabhu 7.10.01\",\"url\": \"http://www.iskconpunjabibagh.com/lectures/?download&file_name=HG%20Rukmini%20Krishna%20Prabhu%2FHG%20Rukmini%20Krishna%20P%20_29Nov_2014%20SB%207.10.1.mp3\"}]";

                if(url.contains("lectures")) {
                    json = lectures;
                } else {
                    json = kirtans;
                }
                return json;
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                json = sb.toString();
                Log.e("JSON", json);
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            return json;
        }
    }

    public static class ImageLoader {

        MemoryCache memoryCache=new MemoryCache();
        FileCache fileCache;
        private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
        ExecutorService executorService;

        public ImageLoader(Context context){
            fileCache=new FileCache(context);
            executorService= Executors.newFixedThreadPool(5);
        }

        int stub_id = R.drawable.download;

        public void DisplayImage(String url, int loader, ImageView imageView)
        {
            stub_id = loader;
            imageViews.put(imageView, url);
            Bitmap bitmap=memoryCache.get(url);
            if(bitmap!=null)
                imageView.setImageBitmap(bitmap);
            else
            {
                queuePhoto(url, imageView);
                imageView.setImageResource(loader);
            }
        }

        private void queuePhoto(String url, ImageView imageView)
        {
            PhotoToLoad p=new PhotoToLoad(url, imageView);
            executorService.submit(new PhotosLoader(p));
        }

        private Bitmap getBitmap(String url)
        {
            File f=fileCache.getFile(url);

            //from SD cache
            Bitmap b = decodeFile(f);
            if(b!=null)
                return b;

            //from web
            try {
                Bitmap bitmap=null;
                URL imageUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
                conn.setConnectTimeout(30000);
                conn.setReadTimeout(30000);
                conn.setInstanceFollowRedirects(true);
                InputStream is=conn.getInputStream();
                OutputStream os = new FileOutputStream(f);
                Utils.CopyStream(is, os);
                os.close();
                bitmap = decodeFile(f);
                return bitmap;
            } catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }

        //decodes image and scales it to reduce memory consumption
        private Bitmap decodeFile(File f){
            try {
                //decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(f),null,o);

                //Find the correct scale value. It should be the power of 2.
                final int REQUIRED_SIZE=570;
                int width_tmp=o.outWidth, height_tmp=o.outHeight;
                int scale=1;
                while(true){
                    if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                        break;
                    width_tmp/=2;
                    height_tmp/=2;
                    scale*=2;
                }

                //decode with inSampleSize
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize=scale;
                return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            } catch (FileNotFoundException e) {}
            return null;
        }

        //Task for the queue
        private class PhotoToLoad
        {
            public String url;
            public ImageView imageView;
            public PhotoToLoad(String u, ImageView i){
                url=u;
                imageView=i;
            }
        }

        class PhotosLoader implements Runnable {
            PhotoToLoad photoToLoad;
            PhotosLoader(PhotoToLoad photoToLoad){
                this.photoToLoad=photoToLoad;
            }

            @Override
            public void run() {
                if(imageViewReused(photoToLoad))
                    return;
                Bitmap bmp=getBitmap(photoToLoad.url);
                memoryCache.put(photoToLoad.url, bmp);
                if(imageViewReused(photoToLoad))
                    return;
                BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
                Activity a=(Activity)photoToLoad.imageView.getContext();
                a.runOnUiThread(bd);
            }
        }

        boolean imageViewReused(PhotoToLoad photoToLoad){
            String tag=imageViews.get(photoToLoad.imageView);
            if(tag==null || !tag.equals(photoToLoad.url))
                return true;
            return false;
        }

        //Used to display bitmap in the UI thread
        class BitmapDisplayer implements Runnable
        {
            Bitmap bitmap;
            PhotoToLoad photoToLoad;
            public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
            public void run()
            {
                if(imageViewReused(photoToLoad))
                    return;
                if(bitmap!=null)
                    photoToLoad.imageView.setImageBitmap(bitmap);
                else
                    photoToLoad.imageView.setImageResource(stub_id);
            }
        }

        public void clearCache() {
            memoryCache.clear();
            fileCache.clear();
        }

    }
}
