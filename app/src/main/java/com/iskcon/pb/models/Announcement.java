package com.iskcon.pb.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by maygupta on 12/21/15.
 */
@ParseClassName("Announcement")
public class Announcement extends ParseObject {
    public String url;
    public String message;
    public String date;

    public Announcement(){}

    public Announcement(String message, String date, String url) {
        this.message = message;
        this.date = date;
        this.url = url;
    }
}

