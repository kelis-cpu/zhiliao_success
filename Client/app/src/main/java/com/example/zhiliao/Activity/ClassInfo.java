package com.example.zhiliao.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhiliao.Fragments.Visit_Other;
import com.example.zhiliao.R;

public class ClassInfo extends AppCompatActivity {
    private String classID;
    private TextView teacher;
    private TextView subject;
    private TextView time;
    private TextView des;
    private ImageView back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classinfo);
        @SuppressLint("ResourceType") View view=findViewById(R.id.classinfo);
        TextView t=view.findViewById(R.id.head);
        t.setText("课程详情");
        init();//绑定控件
        Intent intent=getIntent();
        String []data=intent.getStringArrayExtra("data");
        System.out.println("length:"+data.length);
        teacher.setText(data[0]);
        subject.setText(data[1]);
        time.setText(data[2]);
        des.setText(data[3]);
        classID=data[4];
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        teacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ClassInfo.this, Visit_Other.class);
                intent.putExtra("classID",classID);
                startActivity(intent);
            }
        });
    }
    private void init(){
        teacher=findViewById(R.id.teacher);
        subject=findViewById(R.id.subjects);
        time=findViewById(R.id.time);
        des=findViewById(R.id.description);
        back=findViewById(R.id.back);
    }
}
