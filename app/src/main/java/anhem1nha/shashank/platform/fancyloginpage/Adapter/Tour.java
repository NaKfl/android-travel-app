package anhem1nha.shashank.platform.fancyloginpage.Adapter;

public class Tour {
    public String tourId;
    public String avatar;
    public String destination;
    public String date;
    public String cash;
    public String people;

    public Tour(String tourId, String avatar, String destination, String date, String people, String cash){
        this.tourId=tourId;
        this.avatar=avatar;
        this.destination=destination;
        this.date=date;
        this.people=people;
        this.cash=cash;
    }

    public String getTour() {
        return tourId;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDatetodate() {
        return date;
    }

    public void setDatetodate(String date) {
        this.date = date;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String toString(){
        return avatar+" "+destination+" "+date+" "+cash+" "+people;
    }
}
