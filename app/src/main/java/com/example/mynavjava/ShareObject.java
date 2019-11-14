package com.example.mynavjava;

public class ShareObject {
    private String imageURL, user, timestamp, plant, percent;

    public  ShareObject(){

    }

    public ShareObject(String imageURL, String timestamp, String plant, String percent) {
        user = "Anonymous";
        this.imageURL = imageURL;
        this.timestamp = timestamp;
        this.plant = plant;
        this.percent = percent;
    }
    public ShareObject(String imageURL, String user, String timestamp, String plant, String percent) {
        this.imageURL = imageURL;
        this.user = user;
        this.timestamp = timestamp;
        this.plant = plant;
        this.percent = percent;
    }

    public String getImageURL() {
        return imageURL;
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
