package com.iskcon.pb.models;

/**
 * Created by maygupta on 7/4/15.
 */
public class KirtanData {

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

}
