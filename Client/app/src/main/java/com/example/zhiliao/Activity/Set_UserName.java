package com.example.zhiliao.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhiliao.R;
import com.example.zhiliao.Tools.HttpConnection;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Set_UserName extends AppCompatActivity {
    private int sub;
    private final static int SET_USER_NAME=100;
    private TextView cancle;
    private TextView end;
    private EditText user;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_user_name);
        user=findViewById(R.id.username);
        cancle=findViewById(R.id.cancel);
        end=findViewById(R.id.end);
        Intent intent=getIntent();
        switch (sub=intent.getIntExtra("object",0)){
            case 0:
                user.setText(intent.getStringExtra("username"));
                break;
            case 1:
                user.setText(intent.getStringExtra("sign"));
        }

        user.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_DONE){
                    Intent intent=new Intent();
                    switch (sub){
                        case 0:
                            intent.putExtra("username",user.getText().toString());
                            setResult(SET_USER_NAME,intent);
                            break;
                        case 1:
                            intent.putExtra("sign",user.getText().toString());
                            setResult(SET_USER_NAME+1,intent);
                    }
                    finish();
                }
                return false;//隐藏软键盘
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                switch (sub){
                    case 0:
                        intent.putExtra("username",user.getText().toString());
                        setResult(SET_USER_NAME,intent);
                    case 1:
                        intent.putExtra("sign",user.getText().toString());
                        setResult(SET_USER_NAME+1,intent);
                }
                finish();
            }
        });
    }
}
