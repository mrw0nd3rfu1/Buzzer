package com.example.abhinav.buzzer;

import android.support.v4.app.Fragment;

/**
 * Created by Abhinav on 23-Jan-17.
 */

public class Home{

    private String event,post,image, username;


    public Home(){

    }

    public Home(String event, String post, String image , String username) {
        this.event = event;
        this.post = post;
        this.image = image;
        this.username = username;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
