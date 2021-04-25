package com.example.zhiliao.Entity;

public class Comment {
    private String username;
    private String comment;

    public Comment(String username,String comment){
        this.username=username;
        this.comment=comment;
    }
    public Comment(){}

    public String getUsername() {
        return username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
