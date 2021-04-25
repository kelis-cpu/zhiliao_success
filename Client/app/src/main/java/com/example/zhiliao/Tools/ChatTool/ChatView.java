package com.example.zhiliao.Tools.ChatTool;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.zhiliao.R;

public class ChatView extends LinearLayout {
    private Context context;

    private TextView head;
    private ImageView back;

    public ChatView(Context context){
        super(context);
        this.context=context;
    }
    public void init(){
        initView();
    }
    public void initView(){
        LayoutInflater.from(context).inflate(R.layout.chat_view,this);
        back=findViewById(R.id.back);
        head=findViewById(R.id.head);
        head.setText("私信");
    }
}
