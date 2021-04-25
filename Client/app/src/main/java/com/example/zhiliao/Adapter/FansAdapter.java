package com.example.zhiliao.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zhiliao.Entity.Fans;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.ImageLoad;

import java.util.List;

public class FansAdapter extends ArrayAdapter<Fans> {
    private final static String[] follow_status={"已关注","关注"};
    private int resource=0;
    private LayoutInflater layoutInflater;
    private List<Fans> fans;
    private Context ctx;
    public FansAdapter(@NonNull Context context, int resource, List<Fans> fans) {
        super(context, resource, fans);
        ctx=context;
        this.fans=fans;
        this.resource=resource;
        layoutInflater=LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return fans.size();
    }
    @Nullable
    @Override
    public Fans getItem(int position) {
        return fans.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.follow_item,null);
            viewHolder.avatar=convertView.findViewById(R.id.avatar);
            viewHolder.username=convertView.findViewById(R.id.username);
            viewHolder.sign=convertView.findViewById(R.id.sign);
            viewHolder.follow_status=convertView.findViewById(R.id.follow_status);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Fans fan=fans.get(position);
        if (fan!=null){
            if (fan.getAvatar().equals("local")){
                viewHolder.avatar.setImageResource(R.drawable.tx);
            }else{
                ImageLoad img=new ImageLoad(fan.getAvatar());
                img.loadImage(new ImageLoad.ImageCallBack() {
                    @Override
                    public void getDrawable(Drawable drawable) {
                        viewHolder.avatar.setImageDrawable(drawable);
                    }
                });
            }//设置头像
            viewHolder.username.setText(fan.getUsername());
            viewHolder.sign.setText(fan.getSign());
            viewHolder.follow_status.setText(fan.getFollow_status());
        }
        viewHolder.follow_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resource=(resource+1)%2;
                viewHolder.follow_status.setText(follow_status[resource]);//模拟关注
            }
        });
        return convertView;
    }
    class ViewHolder{
        public ImageView avatar;
        public TextView username;
        public TextView sign;
        public  TextView follow_status;
    }
}
