package com.example.abhinav.buzzer.Utility;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Created by Abhinav on 23-Jan-17.
 */
@IgnoreExtraProperties

public class Home {

    public int With_image;

    private String event;
    private String post;
    private String image;
    private String profile_pic;
    private String username;
    private String post_id;
    private String uid;
    private String post_key;
    private String post_time;


    private String thumb_profile_pic;
    private long numShares;
    private String deeplinks;

    public Home() {

    }


    public Home(String event, String post, String image, String profile_pic, String username, String post_id, String uid, String post_key, long numShares, String deeplinks,String thumb_profile_pic, String post_time) {
        this.event = event;
        this.post = post;
        this.image = image;
        this.profile_pic = profile_pic;
        this.username = username;
        this.post_id = post_id;
        this.uid = uid;
        this.post_key = post_key;
        this.numShares = numShares;
        this.deeplinks = deeplinks;
        this.post_time = post_time;
        this.thumb_profile_pic =thumb_profile_pic;
    }

    public long getNumShares() {
        return numShares;
    }

    public String getThumb_profile_pic() {
        return thumb_profile_pic;
    }

    public void setThumb_profile_pic(String thumb_profile_pic) {
        this.thumb_profile_pic = thumb_profile_pic;
    }


    public void setNumShares(long numShares) {
        this.numShares = numShares;
    }

    public String getDeeplinks() {
        return deeplinks;
    }

    public void setDeeplinks(String deeplinks) {
        this.deeplinks = deeplinks;
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

    public String getPost_time() {
        return post_time;
    }

    public void setPost_time(String post_time) {
        this.post_time = post_time;
    }


    public String getProfile_pic() {
        return profile_pic;
    }



    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }
}
