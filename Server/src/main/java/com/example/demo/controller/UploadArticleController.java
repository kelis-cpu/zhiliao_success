package com.example.demo.controller;

import com.example.demo.DAO.ArticleDAO;
import com.example.demo.Repository.ArticleRepository;
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
@RequestMapping("/upload")
public class UploadArticleController extends HttpServlet {
    private int type;
    private int id;
    private JSONObject js;
    @Autowired
    private ArticleRepository articleRepository;

    @Override
    @RequestMapping("/article")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String author="";
        String content="";
        String time="";
        String img1Url="";
        String img2Url="";
        String img3Url="";
        int head=0;
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
            js=new JSONObject(sb.toString());
            author=js.getString("author");
           // content=js.getString("content");
            content=js.getString("art");
            time=js.getString("time");
            head=js.getInt("head");
            img1Url=js.getString("img1Url");
            img2Url=js.getString("img2Url");
            img3Url=js.getString("img3Url");
            System.out.println("author:"+author);
            System.out.println("article:"+content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArticleDAO article=new ArticleDAO();
        article.setContent(content);
        article.setTime(time);
        article.setUsrname(author);
        article.setHead(head);
        article.setImg1Url(img1Url);
        article.setImg2Url(img2Url);
        article.setImg3Url(img3Url);
        try{
            id=articleRepository.save(article).getArticleID();
            type=3;
        }catch (Exception e){
            e.printStackTrace();
            type=5;
        }
        //返回信息到客户端

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        PrintWriter out=resp.getWriter();
        //返回状态码
        out.print(toJson(type,id));
        out.flush();
        out.close();
    }

    public String toJson(int type,int id)
    {
        JSONObject js=new JSONObject();
        String jsonString="";
        try
        {
            js.put("type",type);
            js.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString=js.toString();
        return jsonString;
    }
}
