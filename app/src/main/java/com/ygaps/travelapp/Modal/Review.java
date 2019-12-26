package com.ygaps.travelapp.Modal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Review {
    String name;
    String review;
    String avatar;
    String createdOn;



    public Review(String name, String review,String createOn) {
        this.name = name;
        this.review = review;
        //this.avatar = avatar;
        this.createdOn = createOn;
    }
    public String getCreatedOn() {
        String temp;
        long miliStartDate=Long.parseLong(createdOn);
        Date created=new Date(miliStartDate);
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        temp=dateFormat.format(created);
        return temp;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "Review{" +
                "name='" + name + '\'' +
                ", review='" + review + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createdOn='" + createdOn + '\'' +
                '}';
    }
}
