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
                 String type) {
        this.name = name;
        this.url = url;
        this.author = author;
        this.type = type;
        put("name", name);
        put("url", url);
        put("author", author);
        put("type", type);
        pinInBackground();
    }

    public Media(){}

    public String getImageUrl() {
        String author = getAuthor();
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
