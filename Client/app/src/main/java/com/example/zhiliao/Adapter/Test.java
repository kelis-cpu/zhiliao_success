package com.example.zhiliao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zhiliao.Entity.Dynamic;
import com.example.zhiliao.Entity.test_class;
import com.example.zhiliao.R;

import java.util.List;

public class Test extends ArrayAdapter<test_class> {
    private List<test_class> tests;
    private Context ctx;
    private LayoutInflater layoutInflater;
    private boolean tmp=false;
    public Test(@NonNull Context context, int resource,  List<test_class> test) {
        super(context, resource, test);
        ctx=context;
        tests=test;
        layoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater layoutInflater=LayoutInflater.from(ctx);
            convertView=layoutInflater.inflate(R.layout.article_item,null);
            viewHolder.like=convertView.findViewById(R.id.like);
            viewHolder.content=convertView.findViewById(R.id.content);
            viewHolder.head=convertView.findViewById(R.id.head);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        test_class t=tests.get(position);
        if (t!=null){
            viewHolder.head.setImageResource(R.drawable.chemistry);
            viewHolder.content.setText(t.getContent());
            viewHolder.like.setImageResource(R.drawable.islike);
        }
        ViewHolder finalViewHolder = viewHolder;
        viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tmp){
                    finalViewHolder.like.setImageResource(R.drawable.notlike);
                    tmp=true;
                }
                else{
                    finalViewHolder.like.setImageResource(R.drawable.islike);
                    tmp=false;
                }
                Toast.makeText(ctx,"this is test!",Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public test_class getItem(int position) {
        return tests.get(position);
    }

    @Override
    public int getCount() {
        return tests.size();
    }
    static class ViewHolder{
        public TextView content;
        public ImageView head;
        public ImageView like;
    }
}
