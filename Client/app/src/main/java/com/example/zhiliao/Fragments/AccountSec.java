package com.example.zhiliao.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zhiliao.R;

public class AccountSec extends Fragment implements View.OnClickListener{
    private FragmentManager manager;
    private FragmentTransaction ft;
    private ImageView back;
    private RelativeLayout alter_pwd;
    private RelativeLayout alter_pho;
    private RelativeLayout logoff;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.accountsec,null);
        TextView head=view.findViewById(R.id.head);
        head.setText("账号与安全");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        manager=getFragmentManager();
        init();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.popBackStack();
            }
        });
    }
    private void init(){
        back=getActivity().findViewById(R.id.back);
        alter_pho=getActivity().findViewById(R.id.alter_pho);//等短信验证功能实现
        alter_pwd=getActivity().findViewById(R.id.alter_pwd);
        logoff=getActivity().findViewById(R.id.log_off);
        back.setOnClickListener(this);
        alter_pwd.setOnClickListener(this);
        logoff.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.alter_pho:
                break;
            case R.id.alter_pwd:
                Alter_pwd alter_pwd=new Alter_pwd();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,alter_pwd);
                break;
            case  R.id.log_off:
                Logoff logoff=new Logoff();
                ft=manager.beginTransaction();
                ft.replace(R.id.frament,logoff);
                break;
        }
        ft.addToBackStack(null);
        ft.commit();
    }
}
