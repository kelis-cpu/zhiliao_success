package com.example.demo.controller;

import com.example.demo.DAO.UserDAO;
import com.example.demo.Repository.UserRepository;
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
@RequestMapping("/register")
public class RegisterController extends HttpServlet {
    @Autowired
    private UserRepository userRepository;
    private UserDAO register_usr=new UserDAO();
    private int type;

    @Override
    @RequestMapping("/json/data")
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usrname="";
        String password="";
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
            System.out.println("getJsonString"+sb.toString());
            JSONObject js=new JSONObject(sb.toString());
            usrname=js.getString("username");
            password=js.getString("password");
            System.out.println("username:"+usrname);
            System.out.println("password:"+password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //数据库查询
        UserDAO data= userRepository.findByUsername(usrname);
        if (data==null)//无重复用户名
        {
            register_usr.setUser_name(usrname);
            register_usr.setUserpwd(password);
            try{
                userRepository.save(register_usr);
                System.out.println("Register Successful!!!");
                type=0;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("插入失败");
                type=1;
            }
        }
        else{
            type=1;//用户已存在
            System.out.println("该用户已存在");
        }
        //返回信息到客户端

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        PrintWriter out=resp.getWriter();
        //返回状态码
        out.print(toJson(type));
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req,resp);
    }

    public String toJson(int type)
    {
        JSONObject js=new JSONObject();
        String jsonString="";
        try
        {
            js.put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString=js.toString();
        return jsonString;
    }
}
