package com.example.abhinav.buzzer;

/**
 * Created by Abhinav on 23-Jan-17.
 */

public class Home {

    private String event, post, image, username,post_id;


    public Home() {

    }

    public Home(String event, String post, String image, String username) {
        this.event = event;
        this.post = post;
        this.image = image;
        this.username = username;
    }

    public String getEvent() {
        return event;
    }


    public String getPost() {
        return post;
    }


    public String getImage() {
        return image;
    }


    public String getUsername() {
        return username;
    }
    public String getPost_id(){return post_id;}




}
