package com.example.techsamajh_demo1;

public class EventModel {
    String name,date,loc;
    public EventModel(String name,String date,String loc) {
        this.name = name;
        this.date = date;
        this.loc = loc;
    }
    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
    public String getLoc() {
        return loc;
    }
}
