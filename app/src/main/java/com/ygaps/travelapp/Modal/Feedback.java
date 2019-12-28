package com.ygaps.travelapp.Modal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Feedback {
    private String name;
    private String avatar;
    private String createOn;
    private String point;
    private String feedBack;

    public Feedback(String name, String createOn, String point,String feedBack) {
        this.name = name;
        this.createOn = createOn;
        this.feedBack=feedBack;
        this.point = point;
    }

    public String getCreatedOn(){
        String temp;
        long miliStartDate=Long.parseLong(createOn);
        Date created=new Date(miliStartDate);
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        temp=dateFormat.format(created);
        return temp;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateOn() {
        return createOn;
    }

    public void setCreateOn(String createOn) {
        this.createOn = createOn;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getFeedBack() {
        return feedBack;
    }

    public void setFeedBack(String feedBack) {
        this.feedBack = feedBack;
    }
}
