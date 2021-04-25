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
@RequestMapping("/login")
public class LoginController extends HttpServlet {
    @Autowired
    private UserRepository userRepository;
    private int type;

    @Override
    @RequestMapping("/json/data")
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username="";
        String password="";
        try{
            //获取输入流
            BufferedReader streamReader=new BufferedReader(new InputStreamReader(req.getInputStream(),"UTF-8"));
            //System.out.println(streamReader);
            //写入数据到Stringbuilder
            StringBuilder sb=new StringBuilder();
            String line=null;
            while((line=streamReader.readLine())!=null)
            {
                //System.out.println("2222");
                sb.append(line);
            }
            System.out.println("getJsonString"+sb.toString());
            JSONObject js=new JSONObject(sb.toString());
            username=js.getString("username");
            password=js.getString("password");
            System.out.println("username:"+username);
            System.out.println("password:"+password);
        } catch (JSONException e) {
            System.out.println("111");
            e.printStackTrace();
        }
        //数据库查询
        UserDAO data=userRepository.findByUsername(username);
        System.out.println(data);
        if (data==null)
        {
            System.out.println("用户名出错，登陆失败");
            type=1;
        }
        else if (!data.getUserpwd().equals(password))//校验密码是否正确------ ==：比较内存中的首地址，equals：比较字符串包含的内容是否相同
        {
            System.out.println("密码错误，登陆失败");
            type=2;
        }
        else
        {
            type=0;
            System.out.println("Login Successful!");
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
        this.doGet(req, resp);

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

