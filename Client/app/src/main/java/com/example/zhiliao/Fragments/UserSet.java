package com.example.zhiliao.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zhiliao.R;

public class UserSet extends Fragment implements View.OnClickListener{
    private RelativeLayout msg;//消息通知区域
    private SwitchCompat model;//夜间模式设置
    private RelativeLayout privacy;//隐私设置
    private RelativeLayout security;//账号安全
    private RelativeLayout service;//用户服务条款
    private RelativeLayout privacyPro;//隐私协议
    private RelativeLayout about_us;//关于我们
    private ImageView back;//回退键

    private FragmentManager manager;
    private FragmentTransaction ft;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
        manager=getFragmentManager();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.popBackStack();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.set_layout,null);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.msg:
                NotificationsUtils notificationsUtils=new NotificationsUtils();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,notificationsUtils);
                break;
            case R.id.privacy:
                Privacy_Set privacy_set=new Privacy_Set();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,privacy_set);
                break;
            case R.id.security:
                AccountSec accountSec=new AccountSec();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,accountSec);
                break;
            case R.id.service:
                UserService userService=new UserService();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,userService);
                break;
            case R.id.pro:
                PrivacyProtocol privacyProtocol=new PrivacyProtocol();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,privacyProtocol);
                break;
            case R.id.about_us:
                About_Us about_us=new About_Us();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,about_us);
                break;
        }
        ft.addToBackStack(null);
        ft.commit();
    }
    private void init(){
        msg=getActivity().findViewById(R.id.msg);
        model=getActivity().findViewById(R.id.model);
        privacy=getActivity().findViewById(R.id.privacy);
        security=getActivity().findViewById(R.id.security);
        service=getActivity().findViewById(R.id.service);
        privacyPro=getActivity().findViewById(R.id.pro);
        about_us=getActivity().findViewById(R.id.about_us);
        back=getActivity().findViewById(R.id.back);
        msg.setOnClickListener(this);
        model.setOnClickListener(this);
        privacy.setOnClickListener(this);
        security.setOnClickListener(this);
        service.setOnClickListener(this);
        privacyPro.setOnClickListener(this);
        about_us.setOnClickListener(this);
        back.setOnClickListener(this);
    }
}
