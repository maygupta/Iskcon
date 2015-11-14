package com.iskcon.pb.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.io.Serializable;

/**
 * Created by maygupta on 7/4/15.
 */
@ParseClassName("Media")
public class Media extends ParseObject implements Serializable{

    private String name;
    private String url;
    private String author;
    private String type;
    private String authorImageUrl;

    public String getName() {
        return getString("name");
    }

    public String getUrl() {
        return getString("url");
    }

    public String getAuthor() {
        return getString("author");
    }

    public String getType() {
        return getString("type");
    }

    public Media(String name,
                 String author,
                 String url,
                 String type,
                 String imageUrl) {
        this.name = name;
        this.url = url;
        this.author = author;
        this.type = type;
        this.authorImageUrl = imageUrl;
        put("name", name);
        put("url", url);
        put("author", author);
        put("type", type);
        put("author_image_url", authorImageUrl);
        pinInBackground();
    }

    public Media(){}

    public String getImageUrl() {
        return getString("author_image_url");
    }

}
