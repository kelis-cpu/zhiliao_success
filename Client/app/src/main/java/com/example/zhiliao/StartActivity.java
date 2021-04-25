package com.example.zhiliao;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {
    private SharedPreferences sp;
    private  Handler handler=new Handler()
    {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch(msg.what)
            {
                case 0://去登录页
                    Intent intent1=new Intent(StartActivity.this,My.class);
                    startActivity(intent1);
                    finish();
                    break;
                case 1://去主页
                    Intent intent2=new Intent(StartActivity.this,MainActivity.class);
                    startActivity(intent2);
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);
        sp=getSharedPreferences("user_info",0);
        if(sp.getString("user","").isEmpty()||sp.getString("password","").isEmpty())
        {
            handler.sendEmptyMessageDelayed(0,3000);
        }
        else
        {
            handler.sendEmptyMessageDelayed(1,3000);
        }
    }
}
