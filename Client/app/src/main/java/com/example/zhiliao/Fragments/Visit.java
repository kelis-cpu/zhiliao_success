package com.example.zhiliao.Fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.zhiliao.Adapter.ArticleAdapter;
import com.example.zhiliao.Adapter.SubjectAdapter;
import com.example.zhiliao.Entity.Dynamic;
import com.example.zhiliao.Entity.Subject;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.HttpConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Visit extends Fragment {
    private TabHost tab;
    private ListView list1;
    private ListView list2;
    private ImageView back;

    private FragmentManager manager;
    private String Articles;
    private String Subjects;
    private List<Dynamic> articles;
    private List<Subject> subjects;
    private SubjectAdapter subjectAdapter;
    private ArticleAdapter articleAdapter;
    private URL url;
    private SharedPreferences sp;
    private String username;

    private static final String GET_VISIT_ARTICLE="http://172.20.10.9:8091/get_visit_article?username=";
    private static final String GET_VISIT_SUBJECT="http://172.20.10.9:8091/get_visit_subject?username=";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.visit_layout,null);
        TabHost tab=view.findViewById(android.R.id.tabhost);
        tab.setup();
        tab.addTab(tab.newTabSpec("tab1").setIndicator("文章").setContent(R.id.tab1));
        tab.addTab(tab.newTabSpec("tab2").setIndicator("课程").setContent(R.id.tab2));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               manager.popBackStack();
            }
        });
        articles=new ArrayList<Dynamic>();
        subjects=new ArrayList<Subject>();
        Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch(msg.what){
                    case 0:
                        break;
                    case 1:
                        articleAdapter=new ArticleAdapter(getActivity(),0,articles);
                        list1.setAdapter(articleAdapter);
                        break;
                    case 2:
                        subjectAdapter=new SubjectAdapter(getActivity(),0,subjects);
                        list2.setAdapter(subjectAdapter);
                        break;
                }
            }
        };
        try {
            Get_AS(handler,0);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId=="tab1"){
                    try {
                        Get_AS(handler,0);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }else{
                    try {
                        Get_AS(handler,1);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private void init(){
        manager=getFragmentManager();
        sp=getActivity().getSharedPreferences("user_info",0);
        username=sp.getString("user","");
        tab=getActivity().findViewById(android.R.id.tabhost);
        list1=getActivity().findViewById(R.id.list1);
        list2=getActivity().findViewById(R.id.list2);
        back=getActivity().findViewById(R.id.back);
    }
    private void thread(int type,Handler handler){
        new Thread(){
            @Override
            public void run() {
                try {
                    HttpConnection httpConnection=new HttpConnection();
                    HttpURLConnection httpURLConnection=httpConnection.request(url,"");
                    if (httpURLConnection.getResponseCode()==httpURLConnection.HTTP_OK){
                        switch (type){
                            case 0:
                                Articles=httpConnection.response(httpURLConnection);
                                handler.sendEmptyMessage(0);
                                break;
                            case 1:
                                Subjects=httpConnection.response(httpURLConnection);
                                handler.sendEmptyMessage(1);
                                break;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();//加载收藏的文章或课程
    }
    private void article(String articles_string,android.os.Handler mh) throws JSONException {
        JSONArray ja=new JSONArray(articles_string);
        int len=ja.length();
        Log.i("len",len+"");
        articles.clear();
        if (len==1 || ja.getString(len-1).equals("fail")){//返回失败
            mh.sendEmptyMessage(0);
        }else{
            for(int i=0;i<len-1;i++){
                JSONObject js=ja.getJSONObject(i);
                Dynamic dynamic=new Dynamic(js.getString("head"),js.getString("img1Url"),"","",js.getString("usrname"),js.getString("time"),js.getString("content"),0,false);
                dynamic.setArticleID(js.getInt("articleID"));
                articles.add(dynamic);
            }
            mh.sendEmptyMessage(1);
        }
    }
    private void subject(String subjects_string,android.os.Handler mh) throws JSONException {
        JSONArray ja=new JSONArray(subjects_string);
        int len=ja.length();
        Log.i("len",len+"");
        subjects.clear();
        if (len==1 || ja.getString(len-1).equals("fail")){//返回失败
            mh.sendEmptyMessage(0);
        }else{
            for(int i=0;i<len-1;i++){
                JSONObject js=ja.getJSONObject(i);
                Subject subject=new Subject(js.getInt("classID"),js.getString("cover"),js.getInt("status"),js.getInt("numPeople"),js.getString("teacher"),js.getString("openTime"),js.getString("classDes"),js.getString("toSub"));
                subjects.add(subject);
            }
            mh.sendEmptyMessage(2);
        }
    }
    private void Get_AS(android.os.Handler handler,int type) throws MalformedURLException {
        Handler mh=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch(msg.what){
                    case 0:
                        try {
                            article(Articles,handler);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(0);
                        }
                        break;
                    case 1:
                        try {
                            subject(Subjects,handler);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handler.sendEmptyMessage(0);
                        }
                        break;
                }
            }
        };
        switch(type){
            case 0:
                url=new URL(GET_VISIT_ARTICLE+username);
                break;
            case 1:
                url=new URL(GET_VISIT_SUBJECT+username);
                break;
        }
        thread(type,mh);
    }
}
