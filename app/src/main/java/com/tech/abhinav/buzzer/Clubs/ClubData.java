package com.tech.abhinav.buzzer.Clubs;

/**
 * Created by Abhishek Jaiswal on 6/10/17.
 */

public class ClubData {
    public String clubName;
    public String clubImage;
    public String adminUid;

    public ClubData()
    {
    }
    public ClubData(String clubName, String clubImage,String adminUid)
    {
        this.clubImage=clubImage;
        this.clubName=clubName;
        this.adminUid=adminUid;

    }

}
