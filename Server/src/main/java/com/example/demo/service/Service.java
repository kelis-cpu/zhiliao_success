package com.example.demo.service;

import com.example.demo.DAO.ArticleDAO;
import com.example.demo.DAO.ClassDAO;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class Service {
    /**
     登录
     username、password
     **/
    public boolean login(String username,String password)
    {
       return true;
    }
    public JSONObject GetReq(HttpServletRequest req) throws IOException, JSONException {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(req.getInputStream(),"utf-8"));
        StringBuilder stringBuilder=new StringBuilder();
        String temp=null;
        while((temp=bufferedReader.readLine())!=null){
            stringBuilder.append(temp);
        }
        JSONObject js=new JSONObject(stringBuilder.toString());
        return js;
    }
    public String Type(int type) throws JSONException {
        JSONObject js=new JSONObject();
        js.put("type",type);
        return js.toString();
    }
    public void ArticlesToJsonString(List<ArticleDAO> ad,JSONArray ja) throws JSONException {
        int len = ad.size();
        //JSONArray ja=new JSONArray();
        System.out.println(len);
        if (len == 0) {
            ja.put("fail");
            //return ja.toString();
        } else {
            ArticleDAO tmp;
            for (int i = 0; i < len; i++) {
                tmp = ad.get(i);
                System.out.println(tmp.getHead());
                JSONObject js = new JSONObject();
                //System.out.println(tmp.getTime());
                //js.put("test","test");
                Integer articleID = tmp.getArticleID();
                js.put("articleID", articleID);
                js.put("time", tmp.getTime());
                js.put("head", tmp.getHead());
                js.put("content", tmp.getContent());
                js.put("usrname", tmp.getUsrname());
                js.put("img1Url", tmp.getImg1Url());
                js.put("img2Url", tmp.getImg2Url());
                js.put("img3Url", tmp.getImg3Url());
                //js.put("commentList",getComments(articleID));
                ja.put(js);

            }
            ja.put("success");
            //return ja.toString();
        }
    }
    public void SubjectsToJsonString(List<ClassDAO> cd,JSONArray ja){
        int len=cd.size();
        System.out.println(len);
        if (len==0){
            ja.put("fail");
        }else{
            ClassDAO tmp;
            for (int i=0;i<len;i++){
                JSONObject js=new JSONObject();
                try {
                    js.put("classID",cd.get(i).getClassID());
                    js.put("cover",cd.get(i).getCover());
                    js.put("numPeople",cd.get(i).getNumPeople());
                    js.put("openTime",cd.get(i).getOpenTime());
                    js.put("teacher",cd.get(i).getTeacherName());
                    js.put("status",cd.get(i).getStatus());
                    js.put("classDes",cd.get(i).getClassDescription());
                    js.put("toSub",cd.get(i).getToSubject());
                    ja.put(js);
                    //System.out.print(classDAOS.get(i).getToSubject());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ja.put("success");
        }
    }
}
