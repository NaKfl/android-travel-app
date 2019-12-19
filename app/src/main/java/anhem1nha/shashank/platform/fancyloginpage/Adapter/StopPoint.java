package anhem1nha.shashank.platform.fancyloginpage.Adapter;

import org.json.JSONObject;

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

    public StopPoint(String id, String serviceId, String address, String name, String arrivalAt, String leaveAt, String minCost, String maxCost, String serviceTypeId, String avatar) {
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
}
