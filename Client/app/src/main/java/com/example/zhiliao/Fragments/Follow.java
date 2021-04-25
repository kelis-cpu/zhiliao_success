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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.zhiliao.Adapter.FansAdapter;
import com.example.zhiliao.Entity.Dynamic;
import com.example.zhiliao.Entity.Fans;
import com.example.zhiliao.R;

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
import java.util.List;

public class Follow extends Fragment {
    private FragmentManager manager;
    private TabHost tab;
    private ImageView back;
    private List<Fans>list;
    private ListView listView1;
    private ListView listView2;
    private FansAdapter fansAdapter;
    private SharedPreferences sp;
    private final static String GETFOLLOWSURL="http://172.20.10.9:8091/get/follows?username=";
    private final static String GETFANSURL="http://172.20.10.9:8091/get/fans?username=";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.follow,null);
        TabHost tabHost=view.findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("关注",null).setContent(R.id.tab1));
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("粉丝",null).setContent(R.id.tab2));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        //list=new ArrayList<Fans>();
        manager=getFragmentManager();
        list=new ArrayList<Fans>();
        sp=getActivity().getSharedPreferences("user_info",0);
        tab=getActivity().findViewById(android.R.id.tabhost);
        listView1=getActivity().findViewById(R.id.list1);
        listView2=getActivity().findViewById(R.id.list2);
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
                        Toast.makeText(getActivity(),"Tab1加载成功",Toast.LENGTH_SHORT).show();
                        fansAdapter=new FansAdapter(getActivity(),0,list);
                        listView1.setAdapter(fansAdapter);
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"加载失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(),"Tab2加载成功",Toast.LENGTH_SHORT).show();
                        fansAdapter=new FansAdapter(getActivity(),1,list);
                        listView2.setAdapter(fansAdapter);
                        break;
                }
            }
        };
        URL url_Getfollow=null;
        try{
            url_Getfollow=new URL(GETFOLLOWSURL+sp.getString("user",""));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Get(handler,url_Getfollow,1);
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
               //list.clear();
                //Toast.makeText(getActivity(),tabId,Toast.LENGTH_SHORT).show();
                if (tabId.equals("tab1")){
                    URL url_Getfollow=null;
                    try{
                        url_Getfollow=new URL(GETFOLLOWSURL+sp.getString("user",""));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    Get(handler,url_Getfollow,1);
                }else{
                    URL url_Getfan=null;
                    try {
                        url_Getfan=new URL(GETFANSURL+sp.getString("user",""));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    Get(handler,url_Getfan,3);
                }
            }
        });
    }
    private void Get(android.os.Handler mh, URL url,int retn){
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
                   // Log.i("JSONString", jsonString);
                    //DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
                    //os.writeBytes(jsonString);
                    //os.flush();
                    //os.close();
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
                        int type=stringToJson(res.toString(),retn);//加载
                        Log.i("type",type+"");
                        switch (type) {
                            case 0:
                                mh.sendEmptyMessage(0);
                                break;
                            case 1:
                                mh.sendEmptyMessage(1);//Tab1加载成功
                                break;
                            case 2:
                                mh.sendEmptyMessage(2);//失败
                                break;
                            case 3:
                                mh.sendEmptyMessage(3);//Tab2加载成功
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
    private int stringToJson(String articles,int type) throws JSONException {
        list.clear();
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
                String avatar=js.getString("avatar");
                String username=js.getString("username");
                String sign=js.getString("sign");
                String follow_status=js.getString("follow_status");
                Fans tmp=new Fans(avatar,username,sign,follow_status);
                list.add(tmp);//添加到listView
            }
            return type;
        }
    }
}
