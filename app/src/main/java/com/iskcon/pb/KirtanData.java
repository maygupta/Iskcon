package com.iskcon.pb;

/**
 * Created by maygupta on 7/4/15.
 */
public class KirtanData {

    private String mName;
    private String mUrl;

    KirtanData(String name,String url) {
        this.mName = name;
        this.mUrl = url;
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
