package com.example.zhiliao;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.zhiliao.Activity.SearchArticle;
import com.example.zhiliao.Activity.Write;
import com.example.zhiliao.Adapter.ArticleAdapter;
import com.example.zhiliao.Adapter.Test;
import com.example.zhiliao.Entity.Comment;
import com.example.zhiliao.Entity.Dynamic;
import com.example.zhiliao.Entity.test_class;
import com.example.zhiliao.Tools.SearchView.ICallBack;
import com.example.zhiliao.Tools.SearchView.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Article extends Fragment {
    private SearchView searchView;//搜索框
    private ImageView write;//发表按钮
    private static final int RESULT_OK=4;
    private static final int REQUEST=100;
    public List<Dynamic> list;
    private ListView listView;
    private ArticleAdapter my;
    private static  final String URLGETARTICLES="http://172.20.10.9:8091/get/articles";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.article,null);
        ImageView imageView=view.findViewById(R.id.search_view).findViewById(R.id.search_back);
        imageView.setVisibility(View.INVISIBLE);
        return view;
    }
    //
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==REQUEST)
        switch (resultCode){
            case RESULT_OK:
                String name=data.getStringExtra("author");
                String stringArticle=data.getStringExtra("editArticle");
                //图片路径
                String imagepath=data.getStringExtra("photoUrl");
                System.out.println("imagePath"+imagepath);
                //时间
                String stringTime=data.getStringExtra("CurrentTime");
                Dynamic C=new Dynamic("-700063",imagepath,null,null,name,stringTime,stringArticle,R.drawable.politcal,false);
                list.add(C);
                listView.setAdapter(my);
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();//绑定控件
        list=new ArrayList<Dynamic>();
        Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {//加载文章
                switch (msg.what){
                    case 0:
                        Toast.makeText(getActivity(),"连接超时",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_SHORT).show();
                       // Log.i("size",String.valueOf(list.size()));
                        my=new ArticleAdapter(getContext(),R.layout.article_item,list);
                        listView.setAdapter(my);
                        //setListViewHeightBasedOnChildren(listView);
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        URL url=null;
        try {
            url=new URL(URLGETARTICLES);
        } catch (MalformedURLException e) {//转换获取文章地址
            e.printStackTrace();
        }
        GetArticles(handler,url,"");//获取文章
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAction(String string) {
                Intent intent=new Intent(getActivity(), SearchArticle.class);
                intent.putExtra("query",string);
                startActivity(intent);
            }
        });
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"fabiao",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), Write.class);
                startActivityForResult(intent,REQUEST);
            }
        });//发表新文章
    }
    private void GetArticles(android.os.Handler mh, URL url,String jsonString){
        list.clear();
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection = null;
                BufferedReader bufferedReader = null;
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    // 设置连接超时时间
                    httpURLConnection.setConnectTimeout(5 * 1000);
                    //设置从主机读取数据超时
                    httpURLConnection.setReadTimeout(5 * 1000);
                    // Post请求必须设置允许输出 默认false
                    httpURLConnection.setDoOutput(true);
                    //设置请求允许输入 默认是true
                    httpURLConnection.setDoInput(true);
                    // Post请求不能使用缓存
                    httpURLConnection.setUseCaches(false);
                    // 设置为Post请求
                    httpURLConnection.setRequestMethod("POST");
                    //设置本次连接是否自动处理重定向
                    httpURLConnection.setInstanceFollowRedirects(true);
                    // 配置请求Content-Type
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    //开始连接
                    httpURLConnection.connect();

                    //发送数据
                    Log.i("JSONString", jsonString);
                    DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
                    os.writeBytes(jsonString);
                    os.flush();
                    os.close();
                    Log.i("状态码", "" + httpURLConnection.getResponseCode());
                    if (httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK)//判断服务器是否连接成功
                    {
                        bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        StringBuilder res = new StringBuilder();
                        String temp;
                        while ((temp = bufferedReader.readLine()) != null) {
                            res.append(temp);
                            Log.i("Main", res.toString());
                        }
                        int type=stringToJson(res.toString());//加载文章
                        Log.i("type",type+"");
                        switch (type) {
                            case 0:
                                mh.sendEmptyMessage(0);
                                break;
                            case 1:
                                mh.sendEmptyMessage(1);//成功
                                break;
                            case 2:
                                mh.sendEmptyMessage(2);//失败
                            default:
                        }
                    } else {
                        mh.sendEmptyMessage(0);
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    mh.sendEmptyMessage(2);
                } catch (JSONException e) {
                    e.printStackTrace();
                    mh.sendEmptyMessage(2);
                } finally {
                    if (bufferedReader != null) {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }
    private void init()//控件绑定
    {
        searchView=getActivity().findViewById(R.id.search_view);
        write=getActivity().findViewById(R.id.Write);
        listView=getActivity().findViewById(R.id.listview);
    }
    private int stringToJson(String articles) throws JSONException {
        JSONArray ja=null;
        //JSONObject js=new JSONObject();
        int len=0;
        try {
            ja=new JSONArray(articles);
            Log.i("ja",ja.getJSONObject(0).toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return 2;
        }

        len=ja.length();
        if (ja.get(len-1)=="fail"){
            return 2;
        }
        else{
            for (int i=0;i<len-1;i++){
                JSONObject js=ja.getJSONObject(i);
                //Toast.makeText(getActivity(),"here is my mistake",Toast.LENGTH_SHORT).show();
                //int ID=js.getInt("articleID");
                String head=js.getString("head");
                String time=js.getString("time");
                String usrname=js.getString("usrname");
                String content=js.getString("content");
                String img1Url=js.getString("img1Url");
                String img2Url=js.getString("img2Url");
                String img3Url=js.getString("img3Url");
                //List<Comment> commentList=new ArrayList<Comment>();
                //commentList=stringToMap(js.getString("commentList"));
                Dynamic tmp=new Dynamic(head,img1Url,img2Url,img3Url,usrname,time,content,0,false);
                tmp.setArticleID(js.getInt("articleID"));//设置文章号
               // tmp.setCommentList(commentList);
               // Log.i("tmp",tmp.getIvfilepath1());
                list.add(tmp);//添加到listView
            }
        return 1;
        }
    }//文章json字符串转化
    private List<Comment>stringToMap(String data) throws JSONException {
        List<Comment>map=new ArrayList<Comment>();
        JSONArray ja=new JSONArray(data);
        int len=ja.length();
        if (len>0){
            for (int i=0;i<len;i++){
                Comment comment=new Comment();
                comment.setUsername(ja.getJSONObject(i).getString("username"));
                comment.setComment(ja.getJSONObject(i).getString("comment"));
                map.add(comment);
            }
        }
        return map;
    }
}
