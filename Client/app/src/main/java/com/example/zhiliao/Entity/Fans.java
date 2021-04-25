package com.example.zhiliao.Entity;

public class Fans {
    private String avatar;//头像
    private String username;//昵称
    private String sign;//个性签名
    private String follow_status;//关注状态

    public Fans(String avatar,String username,String sign,String follow_status){
        this.avatar=avatar;
        this.username=username;
        this.sign=sign;
        this.follow_status=follow_status;
    };
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public void setFollow_status(String follow_status) {
        this.follow_status = follow_status;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAvatar() {
        return avatar;
    }
    public String getFollow_status() {
        return follow_status;
    }
    public String getSign() {
        return sign;
    }
    public String getUsername() {
        return username;
    }
}
