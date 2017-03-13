package com.example.abhinav.buzzer;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CollegeName {
    private String collegeID;
    private String collegeName;
    private String LastPost,FirstPost;

    public CollegeName(){
       }


    public CollegeName(String collegeID , String collegeName,String LastPost,String FirstPost) {
        this.collegeID = collegeID;
        this.collegeName = collegeName;
        this.LastPost=LastPost;
        this.FirstPost=FirstPost;
       }
    public Integer getLastPost(){return Integer.parseInt(LastPost);}

    public String getCollegeID() {
        return collegeID;
    }

    public String getCollegeName(){
        return collegeName;
    }

    public Integer getFirstPost(){return Integer.parseInt(FirstPost);}



}