package com.iskcon.pb.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by maygupta on 10/21/15.
 */

public class Darshan {
    public String url;
    public String description;
    public Date createdAt;

    public Darshan(){}

    public String getUrl() {
        return this.url;
    }

    public String getDescription() {
        return this.description;
    }

    public Darshan(String url, String description) {
        this.url = url;
        this.description = description;
//        put("url", url);
//        put("description", description);
//        pinInBackground();
    }
}
