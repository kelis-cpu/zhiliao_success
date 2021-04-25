package com.example.demo.controller;

import com.example.demo.DAO.ArticleDAO;
import com.example.demo.DAO.CommentDAO;
import com.example.demo.Repository.ArticleRepository;
import com.example.demo.Repository.CommentRepository;
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
public class GetArticleController extends HttpServlet {
    @Autowired
    private ArticleRepository articleRepository;
    //@Autowired
    //private CommentRepository commentRepository;
    private List<ArticleDAO> articles=new ArrayList<ArticleDAO>();

    @Override
    @RequestMapping("/articles")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        articles=articleRepository.find();//返回文章简要信息
        //System.out.println(articles.get(0).getArticleID());
       // System.out.println(articles);
        //JSONArray.fromObject(articles).toString();
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        PrintWriter out=resp.getWriter();
        try {
            out.print(ToJsonString(articles));
            System.out.println(ToJsonString(articles));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }

    @Override
    @RequestMapping(value = "/create",params = {"username"})//返回我的创作
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username=req.getParameter("username");
        System.out.println("username"+username);
        articles=articleRepository.findByUsrname(username);
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        PrintWriter out=resp.getWriter();
        try {
            out.print(ToJsonString(articles));
            System.out.println(ToJsonString(articles));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }
    @RequestMapping(value = "/search",params = {"query"})
    public void search_Article(HttpServletRequest req,HttpServletResponse resp) throws IOException, JSONException {
        String query=req.getParameter("query");
        List<ArticleDAO> articles=articleRepository.search(query);
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        PrintWriter out= resp.getWriter();
        out.print(ToJsonString(articles));
        out.flush();
        out.close();
    }

    public String ToJsonString(List<ArticleDAO> ad) throws JSONException {
        int len=ad.size();
        JSONArray ja=new JSONArray();
        System.out.println(len);
        if (len==0)
        {
            ja.put("fail");
            return ja.toString();
        }
        ArticleDAO tmp;
        for (int i=0;i<len;i++){
            tmp=ad.get(i);
            System.out.println(tmp.getHead());
            JSONObject js=new JSONObject();
            //System.out.println(tmp.getTime());
            //js.put("test","test");
            Integer articleID=tmp.getArticleID();
            js.put("articleID",articleID);
            js.put("time",tmp.getTime());
            js.put("head",tmp.getHead());
            js.put("content",tmp.getContent());
            js.put("usrname",tmp.getUsrname());
            js.put("img1Url",tmp.getImg1Url());
            js.put("img2Url",tmp.getImg2Url());
            js.put("img3Url",tmp.getImg3Url());
            //js.put("commentList",getComments(articleID));
            ja.put(js);

        }
        ja.put("success");
        return ja.toString();
    }

}
