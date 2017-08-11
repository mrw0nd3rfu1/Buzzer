package com.tech.abhinav.buzzer.Comment;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class CommentPosts {
    private String commentID;
    private String userName;
    private String comment;


    public CommentPosts(){
       }

    public CommentPosts(String commentID , String userName, String comment) {
        this.commentID = commentID;
        this.userName = userName;
        this.comment = comment;

       }

    public String getCommentID() {
        return commentID;
    }

    public String getuserName(){
        return userName;
    }

    public String getComment(){ return comment; }



}