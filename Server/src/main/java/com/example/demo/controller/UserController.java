package com.example.demo.controller;

import com.example.demo.ApiResult;
import com.example.demo.DAO.ArticleDAO;
import com.example.demo.DAO.ClassDAO;
import com.example.demo.DAO.UserDAO;
import com.example.demo.Repository.ArticleRepository;
import com.example.demo.Repository.ClassRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.service.Service;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;


@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ClassRepository classRepository;

    Service service=new Service();

@RequestMapping(value = "/getusrinfo",params = {"username"})
    public void getUserlist (HttpServletRequest request, HttpServletResponse resp) throws IOException {
    String name=request.getParameter("username");
    int code=0;
    String status="unknown error";
    UserDAO user= null;
    JSONObject js=null;
    try{
        user=userRepository.findByUsername(name);
        js=new JSONObject();
        try {
            js.put("avatar",user.getAvatar());
            js.put("username",user.getUser_name());
            js.put("sign",user.getSign());
            js.put("birth",user.getBirth());
            js.put("place",user.getPlace());
            js.put("sex",user.getSex());
            js.put("introduce",user.getIntroduce());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    catch (Exception e)
    {
        status=String.valueOf(e);
    }
    resp.setCharacterEncoding("utf-8");
    resp.setContentType("text/html");
    PrintWriter out= resp.getWriter();
    out.write(js.toString());
    out.flush();
    out.flush();
    }
    @RequestMapping(value = "/setuserinfo")
    public void set_user_info(HttpServletRequest req,HttpServletResponse resp) throws IOException, JSONException {
        Service service=new Service();
        JSONObject js=service.GetReq(req);
        String old_user_name=js.getString("old_username");
        String new_user_name="";
        try{
            new_user_name=js.getString("new_username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        UserDAO user1=userRepository.findByUsername(new_user_name);
        UserDAO user=userRepository.findByUsername(old_user_name);
        int type=0;
        if (user1!=null&& !new_user_name.equals(old_user_name)){
            type=1;
        }else {
            type=2;
            user.setUser_name(new_user_name);
            //user.setAvatar(js.getString("avatar"));
            user.setBirth(js.getString("birth"));
            user.setIntroduce(js.getString("introduce"));
            user.setPlace(js.getString("place"));
            user.setSex(js.getString("sex"));
            user.setSign(js.getString("sign"));
            userRepository.save(user);
        }
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out=resp.getWriter();
        out.write(Tojson(type));
        out.flush();
        out.close();
    }
    @RequestMapping(value = "/getArticle_collect",params = {"username"})
    public void getArticleCollect(HttpServletRequest req,HttpServletResponse resp) throws JSONException, IOException {
    String username=req.getParameter("username");
   // System.out.print("usr"+username);
    int type=0;
    UserDAO user=null;
    List<ArticleDAO> articles=new ArrayList<ArticleDAO>();
    JSONArray ja=new JSONArray();
    //List<ClassDAO> classes=new ArrayList<ClassDAO>();
    if(username.isEmpty()){
        ja.put("fail");//用户名参数不存在
    }else{
        user=userRepository.findByUsername(username);
        if (user==null){
            ja.put("fail");//用户名不存在
        }else{
            String []article=user.getCollect_article().split(",");
            //String []subject=user.getCollect_subject().split(",");
            for (String art_id:article
                 ) {
                ArticleDAO articleDAO=new ArticleDAO();
                articleDAO=articleRepository.findByArticleID(Integer.valueOf(art_id));
                if (articleDAO!=null)
                articles.add(articleDAO);
            }
            service.ArticlesToJsonString(articles,ja);
        }
    }
    resp.setContentType("text/html");
    resp.setCharacterEncoding("utf-8");
    PrintWriter out=resp.getWriter();
    out.print(ja.toString());
    out.flush();
    out.close();
    }
    @RequestMapping(value = "/getSubject_collect",params = {"username"})
    public void getSubjectCollect(HttpServletRequest req,HttpServletResponse resp) throws IOException {
    String username=req.getParameter("username");
    UserDAO user=null;
    List<ClassDAO> subjects=new ArrayList<ClassDAO>();
        JSONArray ja=new JSONArray();
        //List<ClassDAO> classes=new ArrayList<ClassDAO>();
        if(username.isEmpty()){
            ja.put("fail");//用户名参数不存在
        }else{
            user=userRepository.findByUsername(username);
            if (user==null){
                ja.put("fail");//用户名不存在
            }else{
                String []subject=user.getCollect_subject().split(",");
                for (String class_id:subject
                ) {
                    //ArticleDAO articleDAO=new ArticleDAO();
                    ClassDAO classDAO=new ClassDAO();
                    classDAO=classRepository.findByClassID(Integer.valueOf(class_id));
                    //articleDAO=articleRepository.findByArticleID(Integer.valueOf(art_id));
                    if (classDAO!=null)
                        subjects.add(classDAO);
                }
                service.SubjectsToJsonString(subjects,ja);
            }
        }
        resp.setContentType("text/html");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out=resp.getWriter();
        out.print(ja.toString());
        out.flush();
        out.close();
    }
    @RequestMapping(value = "get_visit_article",params = {"username"})
    public  void getVisitArticle(HttpServletRequest req,HttpServletResponse resp) throws JSONException, IOException {
    String username=req.getParameter("username");
    JSONArray ja=new JSONArray();
    List<ArticleDAO> articles=new ArrayList<ArticleDAO>();
    UserDAO user=null;
    if (username.isEmpty()){
        ja.put("fail");
    }else{
        user=userRepository.findByUsername(username);
        if (user==null){
            ja.put("fail");
        }
        else{
            String []visit_article=user.getVisit_article().split(",");
            for (String article_id:visit_article
                 ) {
                ArticleDAO articleDAO=new ArticleDAO();
                articleDAO=articleRepository.findByArticleID(Integer.valueOf(article_id));
                if (articleDAO!=null)
                    articles.add(articleDAO);
            }
            service.ArticlesToJsonString(articles,ja);
        }
    }
    resp.setCharacterEncoding("utf-8");
    resp.setContentType("text/html");
    PrintWriter out=resp.getWriter();
    out.write(ja.toString());
    out.flush();
    out.close();
    }
    @RequestMapping(value = "get_visit_subject",params = {"username"})
    public  void getVisitSubject(HttpServletRequest req,HttpServletResponse resp) throws JSONException, IOException {
        String username=req.getParameter("username");
        JSONArray ja=new JSONArray();
        List<ClassDAO> subjects=new ArrayList<ClassDAO>();
        UserDAO user=null;
        if (username.isEmpty()){
            ja.put("fail");
        }else{
            user=userRepository.findByUsername(username);
            if (user==null){
                ja.put("fail");
            }
            else{
                String []visit_subject=user.getVisit_subject().split(",");
                for (String subject_id:visit_subject
                ) {
                    ClassDAO classDAO=new ClassDAO();
                    classDAO=classRepository.findByClassID(Integer.valueOf(subject_id));
                    if (classDAO!=null)
                        subjects.add(classDAO);
                }
                service.SubjectsToJsonString(subjects,ja);
            }
        }
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        PrintWriter out=resp.getWriter();
        out.write(ja.toString());
        out.flush();
        out.close();
    }
    private String Tojson(int type) throws JSONException {
    JSONObject js=new JSONObject();
    js.put("type",type);
    return js.toString();
    }
}

