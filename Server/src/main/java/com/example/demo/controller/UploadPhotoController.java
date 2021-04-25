package com.example.demo.controller;

import com.example.demo.DAO.ArticleDAO;
import com.example.demo.DAO.ClassDAO;
import com.example.demo.Repository.ArticleRepository;
import com.example.demo.Repository.ClassRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@RestController
@RequestMapping("/upload")
public class UploadPhotoController extends HttpServlet {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ClassRepository classRepository;
    private final  static String photo_path="D:\\phpStudy\\WWW\\upload\\";
    private int type;
    private int articleID;
    private int what;
    private String photoUrl="";
    @Override
    @RequestMapping("/photo")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DataInputStream dis=new DataInputStream(req.getInputStream());
        try{
            savePhoto(dis);
            type=1;//上传成功
        }catch (Exception e){
            e.printStackTrace();
            type=2;//上传失败
        }
        //返回信息到客户端
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html");
        PrintWriter out=resp.getWriter();
        //返回状态码
        out.print(toJson(type,photoUrl));
        out.flush();
        out.close();
    }
//保存图片
private void savePhoto(DataInputStream dis){
        String time=String.valueOf(System.currentTimeMillis());
    String photoUrl=photo_path+time+".jpg";
    File file=new File(photoUrl);
    if (!file.exists()){
        try{
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    FileOutputStream fps=null;
    try{
        fps=new FileOutputStream(file);
    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }
    int bufferSize=1024;
    byte[] buffer=new byte[bufferSize];
    int length=-1;
    try{
        articleID=dis.readInt();
        what=dis.readInt();
        System.out.println("articleID"+articleID);
        System.out.println("what:"+what);
        while((length=dis.read(buffer))!=-1)
        {
            fps.write(buffer,0,length);
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    try{
        fps.flush();
        fps.close();
        switch(what){
            case 0:
                ArticleDAO articleDAO=articleRepository.findByArticleID(articleID);
                photoUrl="http://172.20.10.9/upload/"+time+".jpg";
                articleDAO.setImg1Url(photoUrl);
                articleRepository.save(articleDAO);
                break;
            case 1:
                ClassDAO classDAO=classRepository.findByClassID(articleID);
                photoUrl="http://172.20.10.9/upload/"+time+".jpg";
                classDAO.setCover(photoUrl);
                classRepository.save(classDAO);
        }

    } catch (IOException e) {
        e.printStackTrace();
    }
}
    public String toJson(int type,String url)
    {
        JSONObject js=new JSONObject();
        String jsonString="";
        try
        {
            js.put("type",type);
            js.put("photoUrl",photoUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonString=js.toString();
        return jsonString;
    }
}
