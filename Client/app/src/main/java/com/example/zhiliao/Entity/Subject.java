package com.example.zhiliao.Entity;

import java.io.Serializable;

public class Subject implements Serializable {
    private String cover;//封面
    private int status;//开课状态
    private int numPeople;//上课人数
    private String teacher;//上课教师昵称
    private int classID;//课程号
    private String openTime;//开课时间
    private String classDescription;//课程描述
    private String toSubject;//课程所属科目

    public Subject(){};
    public Subject(int classID,String cover,int status,int numPeople,String teacher,String openTime,String classDescription,String toSubject){
        this.classID=classID;
        this.cover=cover;
        this.status=status;
        this.numPeople=numPeople;
        this.teacher=teacher;
        this.openTime=openTime;
        this.classDescription=classDescription;
        this.toSubject=toSubject;
    };

    public int getClassID() {
        return classID;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public int getStatus() {
        return status;
    }

    public String getCover() {
        return cover;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getOpenTime(){return openTime;}

    public String getClassDescription(){return classDescription;}

    public String getToSubject(){return toSubject;}

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setOpenTime(String openTime){this.openTime=openTime;}

    public void setClassDescription(String classDescription){this.classDescription=classDescription;}

    public void setToSubject(String toSubject){this.toSubject=toSubject;}
}
