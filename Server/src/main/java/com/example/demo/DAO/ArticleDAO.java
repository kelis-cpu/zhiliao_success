package com.example.demo.DAO;

import javax.persistence.*;
import javax.print.DocFlavor;
import javax.xml.soap.Text;
import java.util.Date;

@Entity
@Table(name="articles")
public class ArticleDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer articleID;
    @Column(name = "usrname")
    private String usrname;
    @Column(name = "time")
    private String time;
    @Column(name = "head")
    private Integer head;
    @Column(name = "content")
    private String content;
    @Column(name = "img1Url")
    private String img1Url;
    @Column(name = "img2Url")
    private String img2Url;
    @Column(name = "img3Url")
    private String img3Url;

    public String getTime() {
        return time;
    }

    public Integer getArticleID() {
        return articleID;
    }

    public String getContent() {
        return content;
    }

    public Integer getHead() {
        return head;
    }

    public String getImg2Url() {
        return img2Url;
    }

    public String getImg3Url() {
        return img3Url;
    }

    public String getImg1Url() {
        return img1Url;
    }

    public String getUsrname() {
        return usrname;
    }

    public void setArticleID(Integer articleID) {
        this.articleID = articleID;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setHead(Integer head) {
        this.head = head;
    }

    public void setImg2Url(String img2Url) {
        this.img2Url = img2Url;
    }

    public void setImg3Url(String img3Url) {
        this.img3Url = img3Url;
    }

    public void setImg1Url(String img1Url) {
        this.img1Url = img1Url;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setUsrname(String usrname) {
        this.usrname = usrname;
    }
}
