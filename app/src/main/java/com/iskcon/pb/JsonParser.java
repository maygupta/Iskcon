package com.iskcon.pb;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by maygupta on 8/18/15.
 */
public class JsonParser {

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
