package com.example.zhiliao.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.zhiliao.R;
import com.example.zhiliao.Tools.DialogUtil;

public class Logoff extends Fragment {
    private FragmentManager manager;
    private ImageView back;
    private Button logoff;
    private DialogUtil dialogUtil=new DialogUtil();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.logoff,null);
        TextView textView=view.findViewById(R.id.head);
        textView.setText("注销");
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        back=getActivity().findViewById(R.id.back);
        logoff=getActivity().findViewById(R.id.logoff);
        manager=getFragmentManager();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.popBackStack();
            }
        });
        logoff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUtil.showAlertDialog(getActivity(), "注销提示", "确定要注销吗？", "确定", "取消", true, new DialogUtil.AlertDialogBtnClickListener() {
                    @Override
                    public void clickPositive() {
//注销完成的事件
                    }

                    @Override
                    public void clickNegative() {

                    }
                });
            }
        });
    }
}
