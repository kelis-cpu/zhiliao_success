package com.example.zhiliao.Fragments;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class NotificationsUtils extends Fragment {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static final int CLOSE=0;
    private static final int OPEN=1;

    private boolean temp;
    private Button set_status;//设置消息按钮
    private TextView status;//消息推送状态
    private ImageView back;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.msgput_layout,null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();
        if (isNotificationEnabled(getActivity())){
            status.setText("已开启");
            set_status.setText("关闭消息通知");
            temp=true;
        }else {
            status.setText("已关闭");
            set_status.setText("开启消息通知");
            temp=false;
        }
        set_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (temp){//已开启，现在点击关闭
                    requestPermission(CLOSE);
                }else {
                    requestPermission(OPEN);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager f=getFragmentManager();
                f.popBackStack();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==OPEN||requestCode==CLOSE){
            if (isNotificationEnabled(getActivity())){
                status.setText("已开启");
                set_status.setText("关闭消息通知");
                temp=true;
            }
            else{
                status.setText("已关闭");
                set_status.setText("开启消息通知");
                temp=false;
            }
        }
    }

    //判断消息推送是否打开
    public static  boolean isNotificationEnabled(Context context){
        AppOpsManager opsManager= (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
        ApplicationInfo applicationInfo=context.getApplicationInfo();
        String pkg=context.getApplicationContext().getPackageName();
        int uid=applicationInfo.uid;
        Class appOpsClass=null;
        try {
            appOpsClass=Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod=appOpsClass.getMethod(CHECK_OP_NO_THROW,Integer.TYPE,Integer.TYPE,String.class);
            Field opPostNotificationValue = appOpsClass.getDeclaredField(OP_POST_NOTIFICATION);

            int value = (Integer) opPostNotificationValue.get(Integer.class);
            return ((Integer) checkOpNoThrowMethod.invoke(opsManager, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return false;
    }
    //进入系统设置界面
    protected  void requestPermission(int requestCode){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.BASE){
            //进入系统设置界面
            Intent intent=new Intent(Settings.ACTION_SETTINGS);
            startActivityForResult(intent,requestCode);
            return;
        }else  if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            //在5.x环境使用
            Intent intent=new Intent(Settings.ACTION_SETTINGS);
            startActivityForResult(intent,requestCode);
            return;
        }
        return;
    }
    private void init(){
        status=getActivity().findViewById(R.id.msg_status);
        set_status=getActivity().findViewById(R.id.set_msg);
        back=getActivity().findViewById(R.id.back);
    }
}
