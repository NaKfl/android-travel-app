package com.ygaps.travelapp.Modal;

public class Member {
    String id ;
    String name ;
    String phone;
    String avatar;
    String isHost;

    public Member(String id, String name, String phone, String avatar, String isHost) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
        this.isHost = isHost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getIsHost() {
        return isHost;
    }

    public void setIsHost(String isHost) {
        this.isHost = isHost;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", avatar='" + avatar + '\'' +
                ", isHost='" + isHost + '\'' +
                '}';
    }
}
