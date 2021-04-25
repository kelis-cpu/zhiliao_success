package com.example.demo.controller;

import com.example.demo.DAO.ClassDAO;
import com.example.demo.DAO.CommentDAO;
import com.example.demo.Repository.ClassRepository;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.service.Service;
import lombok.SneakyThrows;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

@RestController
@RequestMapping("/set")
public class SetClassController extends HttpServlet {
    @Autowired
    private ClassRepository classRepository;
    @Autowired
    CommentRepository commentRepository;
    private int classid;

    @Override
    @RequestMapping("/openclass")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String classDescription="";
        String week="";
        String begTime="";
        String endTime="";
        String subject="";
        String teacherName="";
        //int cover=0;
        try{
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(req.getInputStream(),"utf-8"));//获取信息
            StringBuilder sb=new StringBuilder();
            String line=null;
            while((line=bufferedReader.readLine())!=null){
                sb.append(line);
            }
            JSONObject js=new JSONObject(sb.toString());
            classDescription=js.getString("classDescription");
            week=js.getString("week");
            begTime=js.getString("begTime");
            endTime= js.getString("endTime");
            subject= js.getString("subject");
            teacherName=js.getString("teacherName");
            System.out.println(teacherName+subject+week);
            //cover=js.getInt("cover");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        ClassDAO classDAO=new ClassDAO();
        classDAO.setClassDescription(classDescription);
        classDAO.setOpenTime(week+" "+begTime+" "+endTime);
        classDAO.setTeacherName(teacherName);
        classDAO.setToSubject(subject);
        classDAO.setNumPeople(0);
        classDAO.setStatus(0);
        classDAO.setCover("test");
        //System.out.println(classDAO);
        int type;
        try{
            classid=classRepository.save(classDAO).getClassID();
            type=3;
        } catch (Exception e) {
            e.printStackTrace();
            type=5;
        }
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out= resp.getWriter();
        out.print(toJSON(type,classid));
        out.flush();
        out.close();
    }

    @SneakyThrows
    @Override
    @RequestMapping(value = "/comment")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int type=0;
        String username="";
        String comment="";
        Integer articleID=0;
        Service service=new Service();
        JSONObject js;
        try {
            js=service.GetReq(req);
            username=js.getString("username");
            comment=js.getString("comment");
            articleID=js.getInt("articleID");
            System.out.println("username:"+username+",arid"+articleID);
            CommentDAO commentDAO=new CommentDAO(articleID,username,comment);
            commentRepository.save(commentDAO);
            type=1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out=resp.getWriter();
        out.write(service.Type(type));
        out.flush();
        out.close();
    }

    private String toJSON(int type, int id){
        JSONObject js=new JSONObject();
        try{
            js.put("type",type);
            js.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return js.toString();
    }
}
