package com.example.abhinav.buzzer.Utility;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Created by Abhinav on 23-Jan-17.
 */
@IgnoreExtraProperties

public class Home {

    public int With_image;

    private String event, post, image,profile_pic, username,post_id,uid,post_key;
    private long messageTime;

    public Home() {

    }

    public Home(String event, String post, String image,String profile_pic, String username,String post_key,int with_image) {
        this.event = event;
        this.post = post;
        this.image = image;
        this.profile_pic=profile_pic;
        this.username = username;
        this.post_key=post_key;
        this.With_image=with_image;

        messageTime = new Date().getTime();

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

    String getPost_key(){return post_key;}

    public String getUsername() {
        return username;
    }
    public String getPost_id(){return post_id;}


    public String getProfile_pic() {
        return profile_pic;
    }


    public long getMessageTime() {
        return messageTime;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
