package com.example.demo.controller;

import com.example.demo.DAO.ArticleDAO;
import com.example.demo.DAO.CommentDAO;
import com.example.demo.DAO.ThumbsDAO;
import com.example.demo.Repository.CommentRepository;
import com.example.demo.Repository.ThumbsRepository;
import com.example.demo.service.Service;
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
public class GetArticleDetailController extends HttpServlet {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ThumbsRepository thumbsRepository;
    private int type=0;
    @Override
    @RequestMapping(value = "/comments",params = {"articleID","username"})
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String comments="";
        Integer articleID=Integer.valueOf(req.getParameter("articleID"));
        String username="";
        username=req.getParameter("username");
        System.out.println("articleID:"+articleID);
        try {
            comments=getComments(articleID,username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out=resp.getWriter();
        out.write(comments);
        out.flush();
        out.close();
    }

    private String getComments(int articleID,String username) throws JSONException {
        List<CommentDAO> comments=new ArrayList<CommentDAO>();
        List<ThumbsDAO> thumbs=new ArrayList<ThumbsDAO>();
        comments=commentRepository.findByArticleID(articleID);
        thumbs=thumbsRepository.findAllByArticleID(articleID);
        type=1;
        JSONArray ja=new JSONArray();
        if (comments.size()!=0){
            for (CommentDAO comment:comments
            ) {
                JSONObject js=new JSONObject();
                js.put("username",comment.getUsername());
                js.put("comment",comment.getComment());
                System.out.println("comment"+js);
                ja.put(js);
            }
        }
        ja.put(CheckThumb(thumbs,username));
        ja.put(thumbs.size());
        ja.put(type);
        return ja.toString();
    }//获取评论
    private boolean CheckThumb(List<ThumbsDAO> thumbs,String username){
        for (ThumbsDAO th:thumbs
             ) {
            if (th.getUsername().equals(username))
                return true;
        }
        return false;
    }
}
