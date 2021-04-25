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

import com.example.zhiliao.Entity.Msg;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.ImageLoad;

import java.util.List;

public class MsgAdapter extends ArrayAdapter<Msg> {

    private List<Msg> messages;
    private Context ctx;
    private LayoutInflater layoutInflater;

    public MsgAdapter(@NonNull Context context, int resource, @NonNull List<Msg> objects) {
        super(context, resource, objects);
        ctx=context;
        messages=objects;
        layoutInflater=LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public Msg getItem(int position) {
        return messages.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=layoutInflater.inflate(R.layout.chat_item,null);
            viewHolder.avatar=convertView.findViewById(R.id.avatar);
            viewHolder.username=convertView.findViewById(R.id.username);
            viewHolder.content=convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        Msg msg=messages.get(position);
        if (msg!=null){
            viewHolder.username.setText(msg.getUsername());
            viewHolder.time.setText(msg.getTime());
            viewHolder.content.setText(msg.getContent());
            new Thread(){
                @Override
                public void run() {
                    ImageLoad imageLoad=new ImageLoad(msg.getAvatar());
                    imageLoad.loadImage(new ImageLoad.ImageCallBack() {
                        @Override
                        public void getDrawable(Drawable drawable) {
                            viewHolder.avatar.setImageDrawable(drawable);
                        }
                    });
                }
            };
        }
        return convertView;
    }
    class ViewHolder{
        ImageView avatar;
        TextView username;
        TextView content;
        TextView time;
    }
}
