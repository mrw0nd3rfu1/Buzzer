package com.example.abhinav.buzzer;

/**
 * Created by Abhinav on 23-Jan-17.
 */

public class Home {

    public int With_image=1;

    private String event, post, image, username,post_id,uid;
    public Home() {

    }

    public Home(String event, String post, String image, String username,int with_image) {
        this.event = event;
        this.post = post;
        this.image = image;
        this.username = username;
        this.With_image=with_image;
    }

    public String getEvent() {
        return event;
    }


    public String getPost() {
        return post;
    }
    public void setUid(String uid)
    {
        this.uid=uid;
    }

public  void setHas_image(){this.With_image=1;}

    public String getImage() {
        return image;
    }
public int getHas_image(){return this.With_image;}

    public String getUsername() {
        return username;
    }
    public String getPost_id(){return post_id;}




}
