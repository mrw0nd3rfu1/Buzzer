package com.example.abhinav.buzzer;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CollegeName {
    private String collegeID;
    private String collegeName;

    public CollegeName(){
       }

    public CollegeName(String collegeID , String collegeName) {
        this.collegeID = collegeID;
        this.collegeName = collegeName;
       }

    public String getCollegeID() {
        return collegeID;
    }

    public String getCollegeName(){
        return collegeName;
    }


}