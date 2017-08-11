package com.tech.abhinav.buzzer.Event;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EventName {
    private String eventID;

 private String eventName;


    private String eventDate;
    public EventName(){
       }


    public EventName(String eventID , String eventName, String eventDate) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDate = eventDate;

      }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }



    public String getEventID() {
        return eventID;
    }

    public String getEventName(){
        return eventName;
    }



}