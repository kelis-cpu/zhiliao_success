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
@RequestMapping("/alter")
public class AlterPwdController extends HttpServlet {
    @Autowired
    private UserRepository userRepository;

    @Override
    @RequestMapping("/pwd")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName="";
        String oldpwd="";
        String pwd1="";
        String pwd2="";
        try{
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(req.getInputStream(),"utf-8"));
            StringBuilder sb=new StringBuilder();
            String line=null;
            while((line=bufferedReader.readLine())!=null){
                sb.append(line);
            }
            JSONObject js=new JSONObject(sb.toString());
            userName=js.getString("username");
            System.out.println(userName);
            oldpwd=js.getString("oldpwd");
            pwd1=js.getString("pwd1");
            pwd2=js.getString("pwd2");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        int type=0;
        if (!pwd1.equals(pwd2)){//新密码不一致
            type=0;
        }else {
            UserDAO userDAO=userRepository.findByUsername(userName);
            System.out.println(userDAO);
            //UserDAO userDAO=userDAOS.get(0);
            System.out.println("oldpwd"+oldpwd+","+"pwd"+userDAO.getUserpwd());
            if (userDAO.getUserpwd().equals(oldpwd)){//旧密码相等
                userDAO.setUserpwd(pwd1);
                userRepository.save(userDAO);
                type=1;
            }else{
                type=2;//旧密码不等
            }
        }
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        PrintWriter out=resp.getWriter();
        out.write(toJson(type));
        out.flush();
        out.close();
    }
    private String toJson(int type){
        JSONObject js=new JSONObject();
        try{
            js.put("type",type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return js.toString();
    }
}
