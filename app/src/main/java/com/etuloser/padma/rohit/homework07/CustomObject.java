package com.etuloser.padma.rohit.homework07;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Rohit on 3/5/2017.
 */

public class CustomObject implements Serializable {

    String title,description,pubdate,imageurl,duration,audiourl;

    public String getTitle() {
        return title;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getAudiourl() {
        return audiourl;
    }

    public void setAudiourl(String audiourl) {
        this.audiourl = audiourl;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static Comparator<CustomObject> DescComparator = new Comparator<CustomObject>() {

        public int compare(CustomObject s1, CustomObject s2) {
            SimpleDateFormat parserDate = new SimpleDateFormat("EEE,d,MMM,yyyy");
            Date pub1 = null;
            Date  pub2=null;
            try {
                pub1 = parserDate.parse(s1.getPubdate());
                 pub2= parserDate.parse(s2.getPubdate());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return pub2.compareTo(pub1);
        }};

}
