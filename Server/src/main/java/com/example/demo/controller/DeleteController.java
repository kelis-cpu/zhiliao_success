package com.example.demo.controller;

import com.example.demo.DAO.ArticleDAO;
import com.example.demo.Repository.ArticleRepository;
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
@RequestMapping("/delete")
public class DeleteController extends HttpServlet {
    @Autowired
    ArticleRepository articleRepository;
    Service service=new Service();

    @SneakyThrows
    @Override
    @RequestMapping("/article")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JSONObject js=service.GetReq(req);
        Integer articleID=js.getInt("articleID");
        System.out.println("id"+articleID);
        String status="";
        try{
        articleRepository.deleteById(articleID);
        status="success";
        } catch (Exception e) {
            status="fail";
            e.printStackTrace();
        }finally {
            resp.setCharacterEncoding("utf-8");
            resp.setContentType("text/html");
            PrintWriter out=resp.getWriter();
            out.write(ToJson(status));
            out.flush();
            out.close();
        }
    }
    private String ToJson(String status){
        JSONObject js=new JSONObject();
        try{
            js.put("status",status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return js.toString();
    }
}
