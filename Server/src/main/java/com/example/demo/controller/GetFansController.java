package com.example.demo.controller;

import com.example.demo.DAO.UserDAO;
import com.example.demo.Repository.UserRepository;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/get")
public class GetFansController extends HttpServlet {
    @Autowired
    private UserRepository userRepository;
    private List<UserDAO> list;
    @Override
    @RequestMapping(value = "/fans",params = {"username"})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        list=new ArrayList<UserDAO>();
        String username=req.getParameter("username");
        UserDAO userDAO= userRepository.findByUsername(username);
        System.out.println(userDAO.getFans());
        String []follows=userDAO.getFans().split(",");//获取粉丝
        System.out.println(follows.length);
        JSONArray ja=new JSONArray();
        if (follows.length==0){
            ja.put("success");
        }else{
            for (String user:follows) {
                UserDAO user1=userRepository.findByUsername(user);
                System.out.println(user1.getSign());
                list.add(user1);//获取粉丝的基本信息
            }
            for (UserDAO user:list
            ) {
                JSONObject js=new JSONObject();
                try {
                    js.put("avatar",user.getAvatar());
                    js.put("username",user.getUser_name());
                    js.put("sign",user.getSign());
                    js.put("birth",user.getBirth());
                    js.put("place",user.getPlace());
                    js.put("sex",user.getSex());
                    js.put("introduce",user.getIntroduce());
                    js.put("follow_status","关注");
                    ja.put(js);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ja.put("success");
        }
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out=resp.getWriter();
        out.write(ja.toString());
        out.flush();
        out.close();
    }

}
