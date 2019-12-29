package com.ygaps.travelapp.Modal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Noti {
    String title;
    String content;
    String date;

    public Noti(String title, String content, String date) {
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return formatDate(date);
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Noti{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String formatDate(String str){
        String temp;
        long miliStartDate=Long.parseLong(str);
        Date created=new Date(miliStartDate);
        DateFormat dateFormat=new SimpleDateFormat("dd/MM HH:mm");
        temp=dateFormat.format(created);
        return temp;
    }
}

