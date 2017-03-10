package com.example.abhinav.buzzer;

import android.app.Application;

/**
 * Created by Abhishek on 10-03-2017.
 */

public  class GlobalClass  {
    public String CollegeName,CollegeId;
    public void setCollegeName(String collegeName)
    {
        this.CollegeName=collegeName;
    }
    public  void setCollegeId(String collegeId)
    {
        this.CollegeId=collegeId;

    }
    public String getCollegeName(){return CollegeName;}
    public String getCollegeId(){
        return CollegeId;
    }
    private static final GlobalClass holder=new GlobalClass();
    public static GlobalClass getInstance(){return holder;}

}
