package com.example.zhiliao.Fragments;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.zhiliao.R;
import com.example.zhiliao.Tools.HttpConnection;
import com.example.zhiliao.Tools.ImageLoad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Visit_Other extends AppCompatActivity {

    private static  final String GET_USER_INFO="http://172.20.10.9:8091/getusrinfo?username=";
    private SharedPreferences sp;

    private String classID;
    private String user_info;

    private Button send_msg;
    private ImageView avatar;
    private TextView sex;
    private TextView age;
    private TextView place;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        classID=getIntent().getStringExtra("classID");

        init();
        send_msg.setText("私信");
        send_msg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
    private void init(){
        sp=getSharedPreferences("user_info",0);
        send_msg=findViewById(R.id.edit);
        avatar=findViewById(R.id.avatar);
        sex=findViewById(R.id.sex);
        age=findViewById(R.id.age);
        place=findViewById(R.id.local);
        Handler mh=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch(msg.what){
                    case 1:
                        ImageLoad imageLoad=new ImageLoad(msg.obj.toString());
                        imageLoad.loadImage(new ImageLoad.ImageCallBack() {
                            @Override
                            public void getDrawable(Drawable drawable) {
                                avatar.setImageDrawable(drawable);
                            }
                        });
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url=new URL(GET_USER_INFO+sp.getString("user",""));
                    HttpConnection httpConnection=new HttpConnection();
                    HttpURLConnection httpURLConnection=httpConnection.request(url,"");
                    if (httpURLConnection.getResponseCode()==httpURLConnection.HTTP_OK){
                        user_info=httpConnection.response(httpURLConnection);
                        JSONObject js=new JSONObject(user_info);
                        sex.setText(js.getString("sex"));
                        age.setText(js.getString("birth"));
                        place.setText(js.getString("place"));
                        Message msg=new Message();
                        msg.obj=js.getString("avatar");
                        msg.what=1;
                        mh.sendMessage(msg);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
