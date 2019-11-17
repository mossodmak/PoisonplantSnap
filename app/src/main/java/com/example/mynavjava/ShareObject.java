package com.example.mynavjava;

import android.net.Uri;

public class ShareObject {
    private String imageURL, user, timestamp, plant, percent;
    private Long timestampLong;
    public  ShareObject(){

    }

    public ShareObject(String imageURL, String timestamp, String plant, String percent, Long timestampLong) {
        this.imageURL = imageURL;
        this.user = "Anonymous";
        this.timestamp = timestamp;
        this.plant = plant;
        this.percent = percent;
        this.timestampLong = timestampLong;
    }

    public ShareObject(String imageURL, String user, String timestamp, String plant, String percent, Long timestampLong) {
        this.imageURL = imageURL;
        this.user = user;
        this.timestamp = timestamp;
        this.plant = plant;
        this.percent = percent;
        this.timestampLong = timestampLong;
    }

    public String getImageURL() {
        return imageURL;
    }

    public Long getTimestampLong() {
        return timestampLong;
    }

    public void setTimestampLong(Long timestampLong) {
        this.timestampLong = timestampLong;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPlant() {
        return plant;
    }

    public void setPlant(String plant) {
        this.plant = plant;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
