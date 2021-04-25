package com.example.zhiliao.Entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dynamic implements Serializable {//分享文章实体类
    private int articleID;
    private String head;//头像
    private int imag_num;//默认显示照片
    private String name;//昵称
    private String time;//发表时间
    private String content;//正文内容
    private String comment;//评论
    private String ivfilepath1;//图片地址
    private String ivfilepath2;
    private String ivfilepath3;
    private List<String>wholike=new ArrayList<String>();//点赞的人
    private boolean like;//是否点赞
   // private List<Comment> commentList;

    public Dynamic(String head,String ivfilepath1,String ivfilepath2,String ivfilepath3,String name,String time,String content,int imag_num,boolean like){
        this.head=head;
        this.ivfilepath1=ivfilepath1;
        this.ivfilepath2=ivfilepath2;
        this.ivfilepath3=ivfilepath3;
        this.imag_num=imag_num;
        this.name=name;
        this.time=time;
        this.content=content;
        this.like=like;
    }
    public void setArticleID(int id){this.articleID=id;}
    public  void setLike(boolean like){this.like=like;}
    public void setWholike(String wholike){this.wholike.add(wholike);}
    public void setTime(String time){this.time=time;}
    public void setComment(String comment){this.comment=comment;}
    public void removeWholike(String username){
        int index=wholike.indexOf(username);
        wholike.remove(index);
    }

  //  public void setCommentList(List<Comment>commentList) {
      //  this.commentList = commentList;
    //}

    public  List<String> getWholike(){return wholike;}
    public int getImag_num(){return imag_num;}
    public String get_head(){return head;}
    public String getIvfilepath1(){return ivfilepath1;}
    public String getIvfilepath2(){return ivfilepath2;}
    public String getIvfilepath3(){return ivfilepath3;}
    public String get_name(){return name;}
    public String get_time(){return time;}
    public String get_content(){return content;}
    public String get_comment(){return comment;}
    public boolean get_like(){return like;}
    public int getArticleID() {
        return articleID;
    }
   // public List<Comment> getCommentList() {
     //   return commentList;
   // }
}
