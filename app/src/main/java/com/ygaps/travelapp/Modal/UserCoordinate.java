package com.ygaps.travelapp.Modal;

public class UserCoordinate {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    String lat;
    String longitute;
    public String getLongitute() {
        return longitute;
    }

    public void setLongitute(String longitute) {
        this.longitute = longitute;
    }


    public UserCoordinate(String id, String lat, String longitute) {
        this.id = id;
        this.lat = lat;
        this.longitute = longitute;
    }
}
