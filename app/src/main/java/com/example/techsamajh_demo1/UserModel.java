package com.example.techsamajh_demo1;

public class UserModel {
    String name,age,city,url;

    public UserModel(String name,String age,String city,String url) {
        this.name = name;
        this.age=age;
        this.city=city;
        this.url=url;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getCity() {
        return city;
    }
    public String getUrl(){ return url;}
}
