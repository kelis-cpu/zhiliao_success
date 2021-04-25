package com.example.zhiliao.Fragments;

import androidx.appcompat.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zhiliao.Activity.Write;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.BottomPopupOption;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomePage extends AppCompatActivity {
    private final static  int REQUEST_CODE=102;
    private CircleImageView avatar;
    private TextView age;
    private TextView sex;
    private TextView place;
    public static Handler mHandler;
    private RelativeLayout overRe;
    private Button edit;
    private CollapsingToolbarLayout collapseToolbar;
    private FragmentTransaction fm;
    private FragmentManager manager;
    private Write take_avatar=new Write();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
        init();
        Toolbar toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        mHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                float alpha=((float)msg.what)/100f;
                overRe.setAlpha(alpha);
            }
        };
        collapseToolbar.setBackgroundResource(R.drawable.background);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(HomePage.this,User_InfoSet.class);
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomPopupOption bottomPopupOption = new BottomPopupOption(HomePage.this);
                bottomPopupOption.setItemText("拍照","相册");
                bottomPopupOption.showPopupWindow();
                bottomPopupOption.setItemClickListener(new BottomPopupOption.onPopupWindowItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        bottomPopupOption.dismiss();
                        switch(position){
                            case 0:
                                take_avatar.ImageFromCameraCapture();
                                Toast.makeText(HomePage.this,"拍照",Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                take_avatar.ImageFromGallery();
                                Toast.makeText(HomePage.this,"相册",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                Toast.makeText(HomePage.this,"点击",Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==REQUEST_CODE){
            place.setText(data.getStringExtra("place"));
            sex.setText(data.getStringExtra("sex"));
            age.setText(data.getStringExtra("age"));
        }
    }
    private void init(){
        manager=getSupportFragmentManager();
        overRe=findViewById(R.id.over_re);
        collapseToolbar= findViewById(R.id.collapseToolbar);
        edit=findViewById(R.id.edit);
        age=findViewById(R.id.age);
        sex=findViewById(R.id.sex);
        place=findViewById(R.id.local);
        avatar=findViewById(R.id.avatar);
    }
}
