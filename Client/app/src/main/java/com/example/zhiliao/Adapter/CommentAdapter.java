package com.example.zhiliao.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.zhiliao.Entity.Comment;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.HttpConnection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class CommentAdapter extends ArrayAdapter<Comment> {


    private List<Comment> list;
    private Context ctx;
    private LayoutInflater layoutInflater;
    private boolean isShow=false;

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isShow() {
        return isShow;
    }

    public CommentAdapter(@NonNull Context context, int resource, @NonNull List<Comment> objects) {
        super(context, resource, objects);
        ctx = context;
        list = objects;
        layoutInflater = LayoutInflater.from(ctx);
    }


    @Override
    public int getCount() {
        //int size=list.size();
        if (isShow)
        {
            System.out.println("加载数量"+list.size());
            return list.size();}
        else
            return 0;
    }

    @Nullable
    @Override
    public Comment getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.comment_item, null);
            viewHolder.username = convertView.findViewById(R.id.username);
            viewHolder.comment = convertView.findViewById(R.id.comment);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Comment comment = list.get(position);
        if (comment != null) {
            viewHolder.username.setText(comment.getUsername());
            viewHolder.comment.setText(comment.getComment());
        }
        return convertView;
    }

    class ViewHolder {
        public TextView username;
        public TextView comment;
    }
}
