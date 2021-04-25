package com.example.demo.DAO;

import javax.persistence.*;

@Entity
@Table(name = "subjects")
public class ClassDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer classID;
    @Column(name = "teacher_name")
    private String teacherName;
    @Column(name = "open_time")
    private String openTime;
    @Column(name = "num_people")
    private Integer numPeople;
    @Column(name = "cover")
    private String cover;
    @Column(name = "status")
    private Integer status;
    @Column(name = "to_subject")
    private String toSubject;
    @Column(name = "class_description")
    private String classDescription;

    public Integer getClassID() {
        return classID;
    }

    public Integer getNumPeople() {
        return numPeople;
    }

    public Integer getStatus() {
        return status;
    }

    public String getCover() {
        return cover;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public String getToSubject() {
        return toSubject;
    }
    public String getClassDescription(){return classDescription;}

    public void setClassID(Integer classID) {
        this.classID = classID;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setNumPeople(Integer numPeople) {
        this.numPeople = numPeople;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setToSubject(String toSubject) {
        this.toSubject = toSubject;
    }

    public void setClassDescription(String classDescription){this.classDescription=classDescription;}
}
