package com.example.demo.DAO;

import javax.persistence.*;

@Entity
@Table(name = "comments")
public class CommentDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(name = "articleID")
    private Integer articleID;
    @Column(name = "username")
    private String username;
    @Column(name = "comment")
    private String comment;

    public CommentDAO(Integer articleID,String username,String comment){
        this.comment=comment;
        this.username=username;
        this.articleID=articleID;
    }
    public CommentDAO(){}
    public Integer getId() {
        return Id;
    }

    public Integer getArticleID() {
        return articleID;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public void setArticleID(Integer articleID) {
        this.articleID = articleID;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
