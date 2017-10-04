package com.tech.abhinav.buzzer.Chat;

/**
 * Created by Abhinav on 24-Jun-17.
 */

public class Users {
    public String name;
    public String image;
    public String status;
    public String thumb_image;
    public String profile_pic;

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }


    public Users(){}

    public Users(String name, String image, String status, String thumb_image,String profile_pic) {
        this.name = name;
        this.image = image;
        this.status = status;
        this.profile_pic = profile_pic;
        this.thumb_image = thumb_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
