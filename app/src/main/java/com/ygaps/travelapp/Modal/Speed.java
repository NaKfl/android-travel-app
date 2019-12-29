package com.ygaps.travelapp.Modal;

public class Speed {
    String lat;
    String longitute;
    String speed;
    public Speed(String lat, String longitute, String speed) {
        this.lat = lat;
        this.longitute = longitute;
        this.speed = speed;
    }



    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }


}
