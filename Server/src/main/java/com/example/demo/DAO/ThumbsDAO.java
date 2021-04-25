package com.example.demo.DAO;

import javax.persistence.*;

@Entity
@Table(name = "thumbs")
public class ThumbsDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "username")
    private String username;
    @Column(name = "articleID")
    private Integer articleID;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setArticleID(Integer articleID) {
        this.articleID = articleID;
    }

    public String getUsername() {
        return username;
    }

    public Integer getArticleID() {
        return articleID;
    }
}
