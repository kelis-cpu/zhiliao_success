package com.example.demo.controller;

import com.example.demo.DAO.ClassDAO;
import com.example.demo.Repository.ClassRepository;
import org.json.JSONArray;
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
import java.util.List;

@RestController
@RequestMapping("/getclass")
public class GetClassController extends HttpServlet {
    @Autowired
    ClassRepository classRepository;
    private List<ClassDAO> classDAOS;
    private int type=2;

    @RequestMapping("/subjects")//对应学科
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String subject="";//查询科目
        try{
            //获取输入流
            BufferedReader streamReader=new BufferedReader(new InputStreamReader(req.getInputStream(),"utf-8"));
            //数据写入到stringbuilder
            StringBuilder sb=new StringBuilder();
            String line=null;
            while((line=streamReader.readLine())!=null)
            {
                sb.append(line);
            }
            JSONObject js=new JSONObject(sb.toString());
            subject=js.getString("subject");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        classDAOS=classRepository.findByToSubject(subject);
        JSONArray jsonArray=new JSONArray();
        if (classDAOS.size()==0){
            type=0;
        }
        else{
            type=1;
        for (int i=0;i<classDAOS.size();i++){
            JSONObject js=new JSONObject();
            try {
                js.put("classID",classDAOS.get(i).getClassID());
                js.put("cover",classDAOS.get(i).getCover());
                js.put("numPeople",classDAOS.get(i).getNumPeople());
                js.put("openTime",classDAOS.get(i).getOpenTime());
                js.put("teacherName",classDAOS.get(i).getTeacherName());
                js.put("status",classDAOS.get(i).getStatus());
                js.put("classDescription",classDAOS.get(i).getClassDescription());
                jsonArray.put(js);
                System.out.print(classDAOS.get(i).getToSubject());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }}
        jsonArray.put(type);
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        PrintWriter out=resp.getWriter();
        out.print(jsonArray.toString());
        out.flush();
        out.close();
    }

}
