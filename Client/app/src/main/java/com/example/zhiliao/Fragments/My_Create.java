package com.example.zhiliao.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.zhiliao.Adapter.ArticleAdapter;
import com.example.zhiliao.Article;
import com.example.zhiliao.Entity.Dynamic;
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

public class My_Create extends Fragment {
    private final static String GETURL="http://172.20.10.9:8091/get/create?username=";
    private final static String DELETEURL="http://172.20.10.9:8091/delete/article";
    private SharedPreferences sp;
    private FragmentManager manager;
    private SwipeMenuListView listView;
    private ImageView back;
    private ArticleAdapter my;
    private String json;
    private List<Dynamic>list;
    SwipeMenuCreator creator;
    private String status;
    private boolean tmp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.my_create,null);
        TextView textView=view.findViewById(R.id.head);
        textView.setText("我的创作");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        manager=getFragmentManager();
        list=new ArrayList<Dynamic>();
        sp=getActivity().getSharedPreferences("user_info",0);
        String user=sp.getString("user","");
        listView=getActivity().findViewById(R.id.listView);
        back=getActivity().findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.popBackStack();
            }
        });
        Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch(msg.what){
                    case 0:
                        Toast.makeText(getActivity(),"连接超时",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(),"加载成功",Toast.LENGTH_SHORT).show();
                        my=new ArticleAdapter(getActivity(),0,list);
                        listView.setAdapter(my);
                        createSwipeMenu();
                        listView.setMenuCreator(creator);
                        listView.setMenuCreator(creator);
                        onClick(listView);
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_SHORT).show();
                }
            }
        };
        HttpConnection httpConnection=new HttpConnection();
        new Thread(){
            @Override
            public void run() {
                URL url=null;
                try {
                    url=new URL(GETURL+user);
                    Log.i("url",user);
                    HttpURLConnection temp=httpConnection.request(url,"");
                    json=httpConnection.response(temp);
                    Log.i("data",json);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                    handler.sendEmptyMessage(2);
                }
                if (!json.isEmpty()){
                    try {
                        JSONArray ja=new JSONArray(json);
                        if (ja.get(ja.length()-1).equals("fail")){
                            handler.sendEmptyMessage(2);
                        }else{
                            list.clear();
                            for (int i=0;i<ja.length()-1;i++) {
                                JSONObject js = ja.getJSONObject(i);
                                //Toast.makeText(getActivity(),"here is my mistake",Toast.LENGTH_SHORT).show();
                                //int ID=js.getInt("articleID");
                                String head = js.getString("head");
                                String time = js.getString("time");
                                String usrname = js.getString("usrname");
                                String content = js.getString("content");
                                String img1Url = js.getString("img1Url");
                                String img2Url = js.getString("img2Url");
                                String img3Url = js.getString("img3Url");
                                Dynamic tmp = new Dynamic(head, img1Url, img2Url, img3Url, usrname, time, content, 0, false);
                                tmp.setArticleID(js.getInt("articleID"));//设置文章号
                                // Log.i("tmp",tmp.getIvfilepath1());
                                list.add(tmp);//添加到listView
                            }
                            handler.sendEmptyMessage(1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    handler.sendEmptyMessage(2);
                }
            }
        }.start();
    }
    private void createSwipeMenu(){
        creator=new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem=new SwipeMenuItem(getActivity());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,0x3F,0X25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.delete);
                menu.addMenuItem(deleteItem);
            }
        };
    }
    private void onClick(SwipeMenuListView lv){
        lv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Dynamic item=list.get(position);
                switch(index){
                    case 0:
                        list.remove(position);
                        my.notifyDataSetChanged();
                        delete(item);
                }
                return false;
            }
        });
    }
    private int dp2px(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,getResources().getDisplayMetrics());
    }
    private void delete(Dynamic item){
        try {
            HttpConnection httpConnection= new HttpConnection();
            URL url=new URL(DELETEURL);
            new Thread(){
                @Override
                public void run() {
                    HttpURLConnection temp=httpConnection.request(url,ToJson(item.getArticleID()));
                    try {
                        status=httpConnection.response(temp);
                        Log.i("status",status);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
            JSONObject js=new JSONObject(status);
            Log.i("status",js.getString("status"));
            if (js.getString("status").equals("success")){
                Toast.makeText(getActivity(),"delete success",Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getActivity(),"删除失败",Toast.LENGTH_LONG).show();
            }
            //发送到服务端删除
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String ToJson(int data){
        JSONObject js=new JSONObject();
        try {
            js.put("articleID",data);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return js.toString();
    }
}
