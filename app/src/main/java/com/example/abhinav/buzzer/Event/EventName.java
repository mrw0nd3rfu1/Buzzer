package com.example.abhinav.buzzer.Event;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class EventName {
    private String eventID;

 private String eventName;

    public EventName(){
       }


    public EventName(String eventID , String eventName) {
        this.eventID = eventID;
        this.eventName = eventName;
      }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }



    public String getEventID() {
        return eventID;
    }

    public String getEventName(){
        return eventName;
    }



}