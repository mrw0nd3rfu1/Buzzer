package com.example.abhinav.buzzer.College;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CollegeName {
    private String collegeID;
    private String collegeName;

    public CollegeName(){
       }


    public CollegeName(String collegeID , String collegeName,String LastPost,String FirstPost) {
        this.collegeID = collegeID;
        this.collegeName = collegeName;
       }
//    public Integer getLastPost(){return Integer.parseInt(LastPost);}

    String getCollegeID() {
        return collegeID;
    }

    String getCollegeName() {
        return collegeName;
    }

//    public Integer getFirstPost(){return Integer.parseInt(FirstPost);}



}