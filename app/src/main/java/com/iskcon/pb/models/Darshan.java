package com.iskcon.pb.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by maygupta on 10/21/15.
 */
@ParseClassName("Darshan")
public class Darshan extends ParseObject {
    public String url;
    public String description;

    public Darshan(){}

    public String getUrl() {
        return getString("url");
    }

    public String getDescription() {
        return getString("description");
    }

    public Darshan(String url, String description) {
        this.url = url;
        this.description = description;
        put("url", url);
        put("description", description);
        pinInBackground();
    }
}
