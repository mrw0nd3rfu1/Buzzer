package com.example.abhinav.buzzer.Message;

import java.util.Date;

/**
 * Created by Abhinav on 02-Jun-17.
 */

public class MessagePosts {

    private String messageID;
    private String userName;
    private String comment;
    private long messageTime;

    public MessagePosts(String messageID , String userName, String comment) {
        this.messageID = messageID;
        this.userName = userName;
        this.comment = comment;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public MessagePosts(){

    }


    public String getMessageID() {
        return messageID;
    }

    public String getuserName(){
        return userName;
    }

    public String getComment(){ return comment; }



    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}