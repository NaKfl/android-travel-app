package com.ygaps.travelapp.Modal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StopPoint {
    String id;
    String serviceId;
    String address;
    String name;
    String arrivalAt;
    String leaveAt;
    String minCost;
    String maxCost;
    String serviceTypeId;
    String avatar;
    String lat;

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    String longitude;

    public StopPoint(String id, String serviceId, String address, String name, String arrivalAt, String leaveAt, String minCost, String maxCost, String serviceTypeId, String avatar, String lat,String longi) {
        this.id = id;
        this.serviceId = serviceId;
        this.address = address;
        this.name = name;
        this.arrivalAt = arrivalAt;
        this.leaveAt = leaveAt;
        this.minCost = minCost;
        this.maxCost = maxCost;
        this.serviceTypeId = serviceTypeId;
        this.avatar = avatar;
        this.lat = lat;
        this.longitude=longi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArrivalAt() {
        return arrivalAt;
    }

    public void setArrivalAt(String arrivalAt) {
        this.arrivalAt = arrivalAt;
    }

    public String getLeaveAt() {
        return leaveAt;
    }

    public void setLeaveAt(String leaveAt) {
        this.leaveAt = leaveAt;
    }

    public String getMinCost() {
        return minCost;
    }

    public void setMinCost(String minCost) {
        this.minCost = minCost;
    }

    public String getMaxCost() {
        return maxCost;
    }

    public void setMaxCost(String maxCost) {
        this.maxCost = maxCost;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String setTime(){
        String result = " "+change(getMinCost())+" - "+change(getMaxCost());
        return result;
    }
    @Override
    public String toString() {
        return "StopPoint{" +
                "id='" + id + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", arrivalAt='" + arrivalAt + '\'' +
                ", leaveAt='" + leaveAt + '\'' +
                ", minCost='" + minCost + '\'' +
                ", maxCost='" + maxCost + '\'' +
                ", serviceTypeId='" + serviceTypeId + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }

    public String change(String str){
        String temp;
        long miliStartDate=Long.parseLong(str);
        Date created=new Date(miliStartDate);
        DateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");
        temp=dateFormat.format(created);
        return temp;
    }
}
