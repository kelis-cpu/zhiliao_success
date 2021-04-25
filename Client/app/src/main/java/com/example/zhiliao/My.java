package com.example.zhiliao;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.zhiliao.Activity.Register;
import com.example.zhiliao.Tools.Md5;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.prefs.Preferences;

public class My extends AppCompatActivity {
    private EditText user;
    private EditText password;
    private Button register;
    private Button login;
    private ImageView leftArm;
    private ImageView rightArm;
    private ImageView leftHand;
    private ImageView rightHand;
    private SharedPreferences pref;//用于记住密码
    private SharedPreferences.Editor eauto;//用于自动登录
    private static final String URLLOGIN="http://172.20.10.9:8091/login/json/data";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my);
        pref=getSharedPreferences("user_info",0);
        eauto= pref.edit();
        init();//获取控件
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] data=null;
                final String usrname=user.getText().toString();
                final String passwd=password.getText().toString();
                if (TextUtils.isEmpty(usrname))
                {
                    Toast.makeText(My.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                }
                else if (TextUtils.isEmpty(passwd))
                {
                    Toast.makeText(My.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Md5 md5=new Md5();
                    data=new String[]{usrname,md5.md5(passwd)};
                    @SuppressLint("HandlerLeak") Handler handler=new Handler()
                    {
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            switch (msg.what) {
                                case 0:
                                    Toast.makeText(My.this, "服务器连接失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(My.this, "登录成功", Toast.LENGTH_SHORT).show();
                                    eauto.putString("user",usrname);
                                    eauto.putString("password",md5.md5(passwd));
                                    eauto.commit();
                                    startActivity(new Intent(My.this, MainActivity.class));
                                    finish();
                                    break;
                                case 2:
                                    Toast.makeText(My.this, "登录失败", Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Log.e("input error", "url为空");
                                    break;
                                case 4:
                                    Toast.makeText(My.this, "连接超时", Toast.LENGTH_SHORT).show();
                                    break;
                                case 5:
                                    Toast.makeText(My.this,"密码错误，登陆失败",Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                            }
                        }
                    };
                    OperateData operateData=new OperateData();
                    String jsonString=operateData.stringTojson(data);
                    URL url=null;
                    try
                    {
                        url=new URL(URLLOGIN);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    operateData.sendData(jsonString,handler,url);
                }
            }
        });
        //注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(My.this, Register.class);
                startActivity(intent);
            }
        });
    }
    private void init()
    {
        login=findViewById(R.id.login);
        register=findViewById(R.id.register);
        user=findViewById(R.id.usrname);
        password=findViewById(R.id.pwd);
        leftArm=findViewById(R.id.iv_left_arm);
        rightArm=findViewById(R.id.iv_right_arm);
        leftHand=findViewById(R.id.iv_left_hand);
        rightHand=findViewById(R.id.iv_right_hand);

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus==true){
                    close();
                }
                else {
                    open();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
             inputMethodManager.hideSoftInputFromWindow(user.getWindowToken(),0);
             View focusView=getCurrentFocus();
             if (focusView!=null){
                 focusView.clearFocus();
             }
        }
        return true;
    }
    public void close(){
        //左边
        RotateAnimation lAnim = new RotateAnimation(0,170,leftArm.getWidth(),0f);
        lAnim.setDuration(500);
        lAnim.setFillAfter(true);

        leftArm.startAnimation(lAnim);

        RotateAnimation rAnim = new RotateAnimation(0, -170,0f,0f);
        rAnim.setDuration(500);
        rAnim.setFillAfter(true);

        rightArm.startAnimation(rAnim);

        TranslateAnimation down = (TranslateAnimation) AnimationUtils.loadAnimation(this,R.anim.hand_down_anim);
        leftHand.startAnimation(down);
        rightHand.startAnimation(down);
    }

    public void open(){
        //左边
        RotateAnimation lAnim = new RotateAnimation(170,0,leftArm.getWidth(),0f);
        lAnim.setDuration(500);
        lAnim.setFillAfter(true);

        leftArm.startAnimation(lAnim);

        RotateAnimation rAnim = new RotateAnimation(-170,0,0f,0f);
        rAnim.setDuration(500);
        rAnim.setFillAfter(true);

        rightArm.startAnimation(rAnim);

        TranslateAnimation up = (TranslateAnimation) AnimationUtils.loadAnimation(this,R.anim.hand_up_anim);
        leftHand.startAnimation(up);
        rightHand.startAnimation(up);
    }
}
