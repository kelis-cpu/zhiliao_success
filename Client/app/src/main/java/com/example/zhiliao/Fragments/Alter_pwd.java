package com.example.zhiliao.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.zhiliao.My;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.HttpConnection;
import com.example.zhiliao.Tools.Md5;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class Alter_pwd extends Fragment {
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    private ImageView back;
    private EditText oldpwd;
    private EditText pwd1;
    private EditText pwd2;
    private Button alter;
    private int temp=0;
    private FragmentManager manager;
    private final static String ALTERURL="http://172.20.10.9:8091/alter/pwd";
    private URL url;
    private Md5 md5;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.alter_pwd,null);
        TextView textView=view.findViewById(R.id.head);
        Button btn=view.findViewById(R.id.alter);
        btn.setPressed(false);
        btn.setEnabled(false);
        textView.setText("修改密码");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
        sp=getActivity().getSharedPreferences("user_info",0);
        Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch(msg.what){
                    case 0:
                        Toast.makeText(getActivity(),"新旧密码不一致",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(getActivity(),"修改成功",Toast.LENGTH_SHORT).show();
                        ed=sp.edit();
                        ed.clear();
                        ed.commit();//删除原密码
                        Intent intent=new Intent(getActivity(), My.class);
                        startActivity(intent);
                        getActivity().finish();
                        break;
                    case 2:
                        Toast.makeText(getActivity(),"密码错误",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        manager=getFragmentManager();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.popBackStack();
            }
        });
        oldpwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(oldpwd.getText().toString())&&!TextUtils.isEmpty(pwd1.getText().toString())&&!TextUtils.isEmpty(pwd2.getText().toString())){
                    alter.setEnabled(true);
                    alter.setPressed(true);
                }
                else{
                    alter.setEnabled(false);
                    alter.setEnabled(false);
                }
            }
        });
        pwd1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(oldpwd.getText().toString())&&!TextUtils.isEmpty(pwd1.getText().toString())&&!TextUtils.isEmpty(pwd2.getText().toString())){
                    alter.setEnabled(true);
                    alter.setPressed(true);
                }
                else{
                    alter.setEnabled(false);
                    alter.setEnabled(false);
                }
            }
        });
        pwd2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(oldpwd.getText().toString())&&!TextUtils.isEmpty(pwd1.getText().toString())&&!TextUtils.isEmpty(pwd2.getText().toString())){
                    alter.setEnabled(true);
                    alter.setPressed(true);
                }
                else{
                    alter.setEnabled(false);
                    alter.setEnabled(false);
                }
            }
        });
        alter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!pwd1.getText().toString().equals(pwd2.getText().toString())){
                    Toast.makeText(getActivity(),"前后两次密码不一致",Toast.LENGTH_SHORT).show();
                }else{
                    HttpConnection httpConnection=new HttpConnection();
                    try{
                        url=new URL(ALTERURL);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    md5=new Md5();
                    String []data={sp.getString("user",""),md5.md5(oldpwd.getText().toString()),md5.md5(pwd1.getText().toString()),md5.md5(pwd2.getText().toString())};
                    new Thread(){
                        @Override
                        public void run() {
                            httpConnection.httpConnnection(url,handler,stringToJson(data));
                        }
                    }.start();

                }
            }
        });
    }

    private void init(){
        back=getActivity().findViewById(R.id.back);
        oldpwd=getActivity().findViewById(R.id.old_pwd);
        pwd1=getActivity().findViewById(R.id.pwd1);
        pwd2=getActivity().findViewById(R.id.pwd2);
        alter=getActivity().findViewById(R.id.alter);
    }
    private String stringToJson(String []data){
        JSONObject js=new JSONObject();
        try {
            js.put("username",data[0]);
            js.put("oldpwd",data[1]);
            js.put("pwd1",data[2]);
            js.put("pwd2",data[3]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return js.toString();
    }
}
