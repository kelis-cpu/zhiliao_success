package com.example.zhiliao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.zhiliao.Tools.SearchView.*;

import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhiliao.Adapter.SubjectAdapter;
import com.example.zhiliao.Entity.Subject;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.HttpConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GetClass extends AppCompatActivity {
    private final static String GETURL="http://172.20.10.9:8091/getclass/subjects";
    private String url;//请求地址
    private List<Subject> list=new ArrayList<>();
    private SubjectAdapter subjectAdapter;
    private GridView gridView;
    private SearchView searchView;
    private Handler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.subject_gridview);
        init();//绑定控件
        handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case 0:
                        Toast.makeText(GetClass.this,"连接超时---无该课程",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(GetClass.this,"加载成功",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        subjectAdapter=new SubjectAdapter(GetClass.this,R.layout.subject_item,list);
                        gridView.setAdapter(subjectAdapter);
                        break;
                }
            }
        };
        Intent intent=getIntent();
        get(intent.getStringExtra("subject"));
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAction(String string) {
                try {
                    getClasses(GETURL,handler,stringTojson(string));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAction() {
                finish();
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("here");
                Subject sub=list.get(position);
                String []data={sub.getTeacher(),sub.getToSubject(),sub.getOpenTime(),sub.getClassDescription(),String.valueOf(sub.getClassID())};
                System.out.println("ClassID:"+sub.getClassID());
                Intent intent1=new Intent(GetClass.this,ClassInfo.class);
                intent1.putExtra("data",data);
                startActivity(intent1);
            }
        });
    }
    private void get(String sub){
        try {
            getClasses(GETURL,handler,stringTojson(sub));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void init(){
        gridView=findViewById(R.id.gridView);
        searchView=findViewById(R.id.search_view2);
    }
    public void getClasses(String url,android.os.Handler mh,String data) throws JSONException {//加载课程信息
        HttpConnection conn=new HttpConnection();
        URL url1=null;
        try {
            url1=new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        URL url2=url1;
        new Thread(){
            @Override
            public void run() {
                conn.httpConnnection(url2,mh,data);
                list.clear();
                JSONArray ja =conn.getJs();//得到返回的jsArray数据
                int len=ja.length();
                for (int i=0;i<len-1;i++){
                    Subject sj=new Subject();
                    try {
                        sj.setClassID(ja.getJSONObject(i).getInt("classID"));
                        sj.setCover(ja.getJSONObject(i).getString("cover"));
                        sj.setNumPeople(ja.getJSONObject(i).getInt("numPeople"));
                        sj.setOpenTime(ja.getJSONObject(i).getString("openTime"));
                        sj.setTeacher(ja.getJSONObject(i).getString("teacherName"));
                        sj.setStatus(ja.getJSONObject(i).getInt("status"));
                        sj.setClassDescription(ja.getJSONObject(i).getString("classDescription"));
                        Log.i("cover",ja.getJSONObject(i).getString("cover"));
                        list.add(sj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                mh.sendEmptyMessage(3);
            }
        }.start();

    }
    public String stringTojson(String string){
        JSONObject js=null;
        if (string==null)
        {
            return "";
        }
        js=new JSONObject();
        try{
            js.put("subject",string);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        String jsonString=String.valueOf(js);
        return jsonString;
    }
}
