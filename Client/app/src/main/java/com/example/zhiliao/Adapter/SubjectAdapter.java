package com.example.zhiliao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zhiliao.Activity.ArticleDetail;
import com.example.zhiliao.Activity.ClassInfo;
import com.example.zhiliao.Entity.Dynamic;
import com.example.zhiliao.Entity.Subject;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.ImageLoad;
import com.example.zhiliao.Tools.getHttpBitmap;

import java.util.List;

public class SubjectAdapter extends ArrayAdapter<Subject> {
    private List<Subject> classes;
    private Context ctx;
    private LayoutInflater layoutInflater;
    public SubjectAdapter(@NonNull Context context, int resource, List<Subject> classes) {
        super(context, resource, classes);
        ctx=context;
        this.classes=classes;
        layoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder=null;
        if (convertView==null){
            viewHolder=new ViewHolder();
            LayoutInflater layoutInflater=LayoutInflater.from(ctx);
            convertView=layoutInflater.inflate(R.layout.subject_item,null);
            viewHolder.cover=convertView.findViewById(R.id.cover);
            viewHolder.numPeople=convertView.findViewById(R.id.num);
            viewHolder.status=convertView.findViewById(R.id.status);
            viewHolder.teacher=convertView.findViewById(R.id.teacher);
            viewHolder.description=convertView.findViewById(R.id.classDes);
            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) convertView.getTag();
        }
        Subject subject=classes.get(position);
        if (subject!=null){
            viewHolder.description.setText(subject.getClassDescription());
            if (subject.getStatus()==1){//0：未开讲，1：开讲
                viewHolder.status.setText("正在讲课");
                viewHolder.numPeople.setText("听讲人数:"+subject.getNumPeople());
                viewHolder.teacher.setText("上课老师:"+subject.getTeacher());
            }else{
                viewHolder.status.setText("未开讲");
                viewHolder.numPeople.setText("开课时间:"+subject.getOpenTime());
                viewHolder.status.setTextColor(0xFFFFFF);
                viewHolder.teacher.setText("");
            }
            if (subject.getCover().equals("test"))
                viewHolder.cover.setImageResource(R.drawable.sub_background);
            else{
                ImageLoad imageLoad=new ImageLoad(subject.getCover());
                Log.i("imgPATH",subject.getCover());
                ViewHolder finalViewHolder = viewHolder;
                imageLoad.loadImage(new ImageLoad.ImageCallBack() {
                    @Override
                    public void getDrawable(Drawable drawable) {
                        finalViewHolder.cover.setImageDrawable(drawable);
                    }
                });
                /*new Thread(){
                    @Override
                    public void run() {
                        getHttpBitmap getBitmap=new getHttpBitmap();
                        Bitmap bitmap=getBitmap.getHttpmap(subject.getCover());
                        finalViewHolder.cover.setImageBitmap(bitmap);
                    }
                }.start();*/
            }
        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(subject);
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
    public Subject getItem(int position) {
        return classes.get(position);
    }

    @Override
    public int getCount() {
        return classes.size();
    }

    static  class ViewHolder{
        ImageView cover;
        TextView status;
        TextView numPeople;
        TextView teacher;
        TextView description;
    }
    private void Click(Subject subject){
        Intent intent =new Intent(ctx, ClassInfo.class);
        String[]data={subject.getTeacher(),subject.getToSubject(),subject.getOpenTime(),subject.getClassDescription(),subject.getClassID()+""};
        intent.putExtra("data",data);
        ctx.startActivity(intent);
    }//点击事件
}
