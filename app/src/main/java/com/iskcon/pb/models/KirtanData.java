package com.iskcon.pb.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maygupta on 7/4/15.
 */
public class KirtanData implements Serializable{

    public String mName;
    public String mUrl;
    public String author;

    public KirtanData(String name,String url, String author) {
        this.mName = name;
        this.mUrl = url;
        this.author = author;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    @Override
    public String toString() {
        return mName;
    }

    public KirtanData (JSONObject jsonObject) {
        try {
            this.mName = jsonObject.getString("name");
            this.mUrl = jsonObject.getString("url");
            this.author = jsonObject.getString("author");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<KirtanData> fromJSONArray(JSONArray array) {
        ArrayList<KirtanData> data = new ArrayList<KirtanData>();
        try {
            for(int i = 0; i < array.length(); i++) {
                data.add(new KirtanData(array.getJSONObject(i)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    public String getImageUrl() {
        //return "http://www.iskconpunjabibagh.com/wp-content/uploads/2013/05/HG-Rukmini-Krishna-Prabhu.jpg";
        if ( author.equals("HG Rukmini Krishna Prabhu")) {
            return "http://i59.tinypic.com/65ydj6.png";
        }
        if ( author.equals("HH BM Govind Maharaj")) {
            return "http://iskconleaders.com/wp-content/uploads/2012/03/Bhakti-Madhurya-Govinda-Swami1.jpg";
        }
        if ( author.equals("HH Gopal Krishna Maharaj")) {
            return "http://iskconmiraroad.com/wp-content/uploads/2015/09/VyasaPujaceremonyofGopalKrishnaGoswamiMaharajcelebratedatISKCONDelhi57-140x140.jpg";
        }
        if ( author.equals("Srila Prabhupada")) {
            return "http://www.vina.cc/wp-content/uploads/2015/09/prabhupada-portrait.jpg";
        }
        if ( author.equals("HH BBGS")) {
            return "http://gbc.iskcon.org/wp-content/uploads/2011/12/bbgs_2009_may-300x300.jpg";
        }
        if ( author.equals("HH Loknath Swami")) {
            return "http://iskconleaders.com/wp-content/uploads/2012/02/Lokanath-Swami1.jpg";
        }
        if ( author.equals("HH Bhati Charu Swami")) {
            return "https://pbs.twimg.com/profile_images/440422377/GM_400x400.jpg";
        }

        return "http://i59.tinypic.com/65ydj6.png";
    }

}
