package com.example.zhiliao.Entity;

public class Msg {
    private String avatar;
    private String username;
    private String Time;
    private String content;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getContent() {
        return content;
    }

    public String getTime() {
        return Time;
    }
}
