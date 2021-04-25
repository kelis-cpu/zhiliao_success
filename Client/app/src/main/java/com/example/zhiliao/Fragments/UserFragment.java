package com.example.zhiliao.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zhiliao.Activity.OpenClass;
import com.example.zhiliao.My;
import com.example.zhiliao.R;
import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;

public class UserFragment extends Fragment implements View.OnClickListener{
    private SharedPreferences sp;
    private SharedPreferences.Editor ed;
    // Fragment管理对象
    private FragmentManager manager;
    private FragmentTransaction ft;
    ImageButton logout;//退出按钮
    ImageView add;//开课按钮
    ImageView set;//设置按钮
    LinearLayout follow;//我的关注
    RelativeLayout home_page;//个人主页
    LinearLayout create;//我的创作
    LinearLayout collect;//我的收藏
    LinearLayout visit;//历史记录
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.user_layout,null);
        manager=getFragmentManager();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();//绑定控件
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp=getActivity().getSharedPreferences("user_info",0);
                ed=sp.edit();
                ed.clear();
                ed.commit();
                Intent intent=new Intent(getContext(),My.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
        home_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(),HomePage.class);
                startActivity(intent);
            }
        });
    }
    public void init(){
        logout=getActivity().findViewById(R.id.logout);
        add=getActivity().findViewById(R.id.add);
        set=getActivity().findViewById(R.id.set);
        follow=getActivity().findViewById(R.id.follow);
        home_page=getActivity().findViewById(R.id.home_page);
        create=getActivity().findViewById(R.id.create);
        collect=getActivity().findViewById(R.id.collect);
        visit=getActivity().findViewById(R.id.visit);
        visit.setOnClickListener(this);
        collect.setOnClickListener(this);
        create.setOnClickListener(this);
        home_page.setOnClickListener(this);
        logout.setOnClickListener(this);
        add.setOnClickListener(this);
        set.setOnClickListener(this);
        follow.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add:
                OpenClass openClass=new OpenClass();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,openClass);
                break;
            case R.id.set:
                UserSet userSet=new UserSet();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,userSet);
                break;
            case R.id.follow:
                Follow follow=new Follow();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,follow);
                break;
            case R.id.create:
                My_Create my_create=new My_Create();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,my_create);
                break;
            case R.id.collect:
                My_Collect my_collect=new My_Collect();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,my_collect);
                break;
            case R.id.visit:
                Visit visit=new Visit();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,visit);
                break;
        }
        ft.addToBackStack(null);
        ft.commit();
    }
}
