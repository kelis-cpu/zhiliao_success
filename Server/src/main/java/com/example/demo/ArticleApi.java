package com.example.demo;

public class ArticleApi {
    private String usrname;
    private String time;
    private String head;
    private String content;
    private String img1Url,img2Url,img3Url;
    private int articleID;

    public void setUsrname(String usrname) {
        this.usrname = usrname;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setImg3Url(String img3Url) {
        this.img3Url = img3Url;
    }

    public void setImg2Url(String img2Url) {
        this.img2Url = img2Url;
    }

    public void setImg1Url(String img1Url) {
        this.img1Url = img1Url;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsrname() {
        return usrname;
    }

    public String getImg3Url() {
        return img3Url;
    }

    public String getImg2Url() {
        return img2Url;
    }

    public String getHead() {
        return head;
    }

    public String getContent() {
        return content;
    }

    public int getArticleID() {
        return articleID;
    }

    public String getImg1Url() {
        return img1Url;
    }

    public String getTime() {
        return time;
    }

}
