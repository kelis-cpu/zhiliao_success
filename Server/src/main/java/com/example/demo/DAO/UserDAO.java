package com.example.demo.DAO;

import javax.persistence.*;

@Entity
@Table(name="user")
public class UserDAO {
@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer userid;
@Column(name = "username")
    private String username;
@Column(name = "userpwd")
    private String userpwd;
@Column(name = "avatar")
    private String avatar;
@Column(name = "fans")
    private String fans;
@Column(name = "follows")
    private String follows;
@Column(name = "sign")
    private String sign;
@Column(name = "sex")
    private String sex;
@Column(name = "place")
    private String place;
@Column(name = "birth")
    private String birth;
@Column(name="introduce")
    private String introduce;
@Column(name="collect_article")
    private String collect_article;
@Column(name = "collect_subject")
    private String collect_subject;
@Column(name = "visit_article")
    private String visit_article;
@Column(name = "visit_subject")
    private String visit_subject;

    public String getIntroduce() {
        return introduce;
    }

    public String getBirth() {
        return birth;
    }
    public String getPlace() {
        return place;
    }
    public String getSex() {
        return sex;
    }
    public Integer getUser_id() {
        return userid;
    }
    public String getUser_name() {
        return username;
    }
    public String getUserpwd() {
        return userpwd;
    }
    public String getAvatar() {
        return avatar;
    }
    public String getFollows() {
        return follows;
    }
    public String getFans() {
        return fans;
    }
    public String getSign() {
        return sign;
    }
    public String getCollect_article(){return collect_article;}
    public String getCollect_subject(){return collect_subject;}

    public String getVisit_article() {
        return visit_article;
    }

    public String getVisit_subject() {
        return visit_subject;
    }

    public void setUser_id(Integer user_id) {
        this.userid = user_id;
    }
    public void setUser_name(String user_name) {
        this.username = user_name;
    }
    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    public void setFans(String fans) {
        this.fans = fans;
    }
    public void setFollows(String follows) {
        this.follows = follows;
    }
    public void setSign(String sign) {
        this.sign = sign;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setCollect_article(String collect_article) {
        this.collect_article = collect_article;
    }

    public void setCollect_subject(String collect_subject) {
        this.collect_subject = collect_subject;
    }

    public void setVisit_article(String visit_article) {
        this.visit_article = visit_article;
    }

    public void setVisit_subject(String visit_subject) {
        this.visit_subject = visit_subject;
    }
}
