package com.tech.abhinav.buzzer.Event;

import android.support.annotation.Keep;

@Keep

public class EventName {
    public String eventID;

    public String eventName;


    public String eventDate;
    public EventName(){
       }


    public EventName(String eventID , String eventName, String eventDate) {
        this.eventID = eventID;
        this.eventName = eventName;
        this.eventDate = eventDate;

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

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getEventName(){
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }



}