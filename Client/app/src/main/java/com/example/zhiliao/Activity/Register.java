package com.example.zhiliao.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import com.example.zhiliao.My;
import com.example.zhiliao.OperateData;
import com.example.zhiliao.R;
import com.example.zhiliao.StartActivity;
import com.example.zhiliao.Tools.Md5;
import com.mob.MobSDK;
import com.mob.tools.FakeActivity;
import com.mob.tools.utils.SharePrefrenceHelper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class Register extends AppCompatActivity implements View.OnClickListener{
    private Md5 md5;
    private TextView get_code;
    private TextView protocol;
    private AlertDialog.Builder builder;//用于显示用户协议
    private CheckBox checkBox;//判断用户是否同意
    private Button register;
    private EditText code;
    private EditText usrname;
    private EditText pwd1;
    private EditText pwd2;
    private EditText phone;
    private SharePrefrenceHelper helper;
    private final String KEY_START_TIME="start_time";
    private Handler handler;
    private EventHandler eventHandler;
    private static final int COUNTDOWN=60;
    private static final String[] DEFAULT_COUNTRY = new String[]{"中国", "42", "86"};
    private String currentId;
    private String currentPrefix;
    private static  final  int REQUEST_CODE_VERRITY=1001;
    private int currentSecond;
    private String[] str=null;//用于存储密码
    private final static String URLREGISTER="http://172.20.10.9:8091/register/json/data";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        init();//绑定控件
       // if(NavUtils.getParentActivityName(Register.this)!=null)//实现层级式导航
       // {
        //    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // }
        initListener();
        currentId = DEFAULT_COUNTRY[1];
        currentPrefix = DEFAULT_COUNTRY[2];
        get_code.setEnabled(false);//默认不可获取验证码
        helper=new SharePrefrenceHelper(this);
        helper.open("sms_sp");
        //注册
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=usrname.getText().toString();
                String password1=pwd1.getText().toString();
                String password2=pwd2.getText().toString();
                String phone_num=phone.getText().toString();
                if (TextUtils.isEmpty(user))
                {
                    Toast.makeText(Register.this,"请输入用户名",Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password1))
                {
                    Toast.makeText(Register.this,"请输入密码",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password2))
                {
                    Toast.makeText(Register.this,"请确认密码",Toast.LENGTH_SHORT).show();
                }else if (!password1.equals(password2))
                {
                    Toast.makeText(Register.this,"前后密码不一致",Toast.LENGTH_SHORT).show();
                }else{
                    //str=new String[]{user,password1};
                    md5=new Md5();
                    str=new String[]{user,md5.md5(password1)};
                    Handler handler=new Handler()
                    {
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            super.handleMessage(msg);
                            switch(msg.what)
                            {
                                case 0:
                                    Toast.makeText(Register.this,"服务器连接失败",Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(Register.this,"注册成功",Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(Register.this, My.class);
                                    startActivity(intent);
                                    Register.this.finish();
                                    break;
                                case 2:
                                    Toast.makeText(Register.this,"用户已存在",Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Log.i("input error","url为空");
                                    break;
                                case 4:
                                    Toast.makeText(Register.this,"连接超时",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    };
                    OperateData operateData=new OperateData();
                    String jsonString=operateData.stringTojson(str);
                    URL url=null;
                    try{
                        url=new URL(URLREGISTER);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    operateData.sendData(jsonString,handler,url);
                }
            }
        });
    }
    private void init()//绑定控件
    {
        get_code=findViewById(R.id.get_code);
        code=findViewById(R.id.code);
        register=findViewById(R.id.register);
        usrname=findViewById(R.id.usrname);
        pwd1=findViewById(R.id.pwd1);
        pwd2=findViewById(R.id.pwd2);
        phone=findViewById(R.id.phone);
        protocol=findViewById(R.id.protocol);
        checkBox=findViewById(R.id.checkbox);
    }
    private void initListener()//手机验证码校验
    {
        get_code.setOnClickListener(Register.this);
        protocol.setOnClickListener(Register.this);
        checkBox.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    register.setEnabled(true);
                }
                else
                    register.setEnabled(false);
            }
        });

        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String phone_number = phone.getText().toString();
                String telRegex = "[1][3456789]\\d{9}";
                if (phone_number.matches(telRegex))
                {
                    get_code.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        handler=new Handler()
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                //Toast.makeText(Register.this,"22222", Toast.LENGTH_SHORT).show();
                if (get_code!=null)
                {
                    if (currentSecond>0){
                        get_code.setText("获取验证码"+"("+currentSecond+"s)");
                        get_code.setEnabled(false);
                        currentSecond--;
                        handler.sendEmptyMessageDelayed(0,1000);
                    }else{
                        get_code.setText("获取验证码");
                        get_code.setEnabled(true);
                    }
                }
                else
                    Toast.makeText(Register.this,"get_code为Kong",Toast.LENGTH_SHORT).show();
            }
        };
        eventHandler=new EventHandler() {
            @Override
            public void afterEvent(int event, final int result, Object o) {
                //Toast.makeText(Register.this,"111", Toast.LENGTH_SHORT).show();
                if (event==SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //验证成功
                            if (result==SMSSDK.RESULT_COMPLETE){
                                Toast.makeText(Register.this,"SUCCESS",Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(Register.this,"Fail",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }else if (event==SMSSDK.EVENT_GET_VERIFICATION_CODE){
                    Toast.makeText(Register.this,"正在获取验证码", Toast.LENGTH_SHORT).show();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (result==SMSSDK.RESULT_COMPLETE){
                                currentSecond=60;
                                handler.sendEmptyMessage(0);
                                helper.putLong(KEY_START_TIME,System.currentTimeMillis());
                            }
                            else
                           {
                                Toast.makeText(Register.this,"获取失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            };
            SMSSDK.registerEventHandler(eventHandler);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_VERRITY)
        {
            code.setText("");
            phone.setText("");
            //重置"获取验证码"按钮
            get_code.setEnabled(true);
            if (handler!=null)
            {
                handler.removeCallbacksAndMessages(null);
            }
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler!=null)
        {
            handler.removeCallbacksAndMessages(null);
        }
        SMSSDK.unregisterEventHandler(eventHandler);
    }//防止内存泄漏

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.get_code:
                //获取验证码
                        // Toast.makeText(Register.this,"111",Toast.LENGTH_SHORT).show();
                        long startTime=helper.getLong(KEY_START_TIME);
                        if(System.currentTimeMillis()-startTime<60*1000)
                        {
                            Toast.makeText(Register.this,"操作频繁请稍等", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(Register.this,phone.getText().toString(),Toast.LENGTH_SHORT).show();
                        SMSSDK.getVerificationCode("86",phone.getText().toString());
                        break;
            case R.id.protocol:
                //获取用户协议
                showprotocol();
                break;
                        default:
                            break;
        }
    }//获取校验码
    public void showprotocol()//用户协议
    {
        builder=new AlertDialog.Builder(Register.this).setTitle("用户协议").setMessage(Html.fromHtml(getResources().getString(R.string.protocol)))
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkBox.setChecked(true);
                        register.setEnabled(true);
                    }
                }).setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkBox.setChecked(false);
                        register.setEnabled(false);
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }
}
