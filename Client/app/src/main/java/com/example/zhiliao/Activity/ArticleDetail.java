package com.example.zhiliao.Activity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhiliao.Adapter.CommentAdapter;
import com.example.zhiliao.Entity.Comment;
import com.example.zhiliao.Entity.Dynamic;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.HttpConnection;
import com.example.zhiliao.Tools.ImageLoad;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ArticleDetail extends AppCompatActivity {
    private final static  String GetCOMMENTURL="http:/172.20.10.9:8091/get/comments?articleID=";
    private final static String SendCOMMENTURL="http:172.20.10.9:8091/set/comment";
    private SharedPreferences sp;

    private List<Comment> list=new ArrayList<Comment>();//评论内容
    private ListView commentList;//评论列表
    private CommentAdapter commentAdapter;
    private TextView isShowMore;
    private ImageView back;
    private TextView thumbsInfo;
    private ImageView thumb;//点赞按钮
    private ImageView to_comment;//评论按钮
    private boolean isLike=false;//是否点赞
    private EditText comment_content;//评论框
    private boolean loadMore =false;
    private String commentNum="查看全部评论";
    private Integer thumbsNum=0;
    private Integer articleID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Dynamic dynamic= (Dynamic) getIntent().getSerializableExtra("articleInfo");//获取文章信息
        setContentView(R.layout.article_detail);
        @SuppressLint("ResourceType") View view=findViewById(R.id.article_detail);
        isShowMore=findViewById(R.id.loadMore);
        sp=getSharedPreferences("user_info",0);
        init(view,dynamic);//初始化视图
        Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch(msg.what){
                    case 0:
                        Toast.makeText(ArticleDetail.this,"连接超时",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(ArticleDetail.this,"加载成功",Toast.LENGTH_SHORT).show();
                        commentNum=commentNum+"("+list.size()+")";
                        isShowMore.setText(commentNum);
                        commentAdapter=new CommentAdapter(ArticleDetail.this,0,list);
                        commentList.setAdapter(commentAdapter);
                        break;
                    case 2:
                        Toast.makeText(ArticleDetail.this,"加载失败",Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(ArticleDetail.this,"评论为0",Toast.LENGTH_SHORT).show();
                        commentNum="暂无评论";
                        isShowMore.setText(commentNum);
                        commentAdapter=new CommentAdapter(ArticleDetail.this,0,list);
                        commentList.setAdapter(commentAdapter);
                        break;
                }
                thumbsInfo.setText(thumbsNum==0?"快给他点个赞吧!":thumbsNum+"人觉得很赞!");
                thumb.setImageResource(isLike?R.drawable.islike:R.drawable.notlike);
            }
        };
        new Thread(){
            @Override
            public void run() {//加载评论
                try {
                    URL url=new URL(GetCOMMENTURL+Integer.valueOf(dynamic.getArticleID())+"&username="+sp.getString("user",""));
                    HttpConnection httpConnection=new HttpConnection();
                    HttpURLConnection httpURLConnection=httpConnection.request(url,"");
                    if (httpURLConnection.getResponseCode()==httpURLConnection.HTTP_OK){
                        String comments=httpConnection.response(httpURLConnection);//数据类型为JSONArray
                        ToListView(comments,handler);
                    }else{
                        handler.sendEmptyMessage(0);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        isShowMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loadMore && list.size()!=0){
                    commentAdapter.setShow(true);
                    isShowMore.setText("点击收起");
                }else{
                    commentAdapter.setShow(false);
                    isShowMore.setText(commentNum);
                }
                loadMore=!loadMore;
                commentAdapter.notifyDataSetChanged();
            }
        });//评论加载点击事件
        comment_content.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId==EditorInfo.IME_ACTION_DONE){
                    if (TextUtils.isEmpty(comment_content.getText().toString())){
                        Toast.makeText(ArticleDetail.this,"内容不能为空",Toast.LENGTH_SHORT).show();
                    }else{
                    String comment=comment_content.getText().toString();
                    comment_content.setText("");
                    comment_content.clearFocus();
                    String username=sp.getString("user","");
                    list.add(new Comment(username,comment));
                    commentAdapter.setShow(true);
                    commentAdapter.notifyDataSetChanged();
                    String []data={username,comment,String.valueOf(articleID)};
                    //发送评论到服务器
                        new Thread(){
                            @Override
                            public void run() {
                                try {
                                    sendComment(data);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                }
                return false;//隐藏软键盘
            }
        });//评论事件
       thumb.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               thumb.setImageResource(isLike?R.drawable.notlike:R.drawable.islike);
           }
       });//点赞事件
    }
    private void init(View view,Dynamic dynamic){
        @SuppressLint("ResourceType") View view1=view.findViewById(R.id.article_item);//文章内容布局页
        @SuppressLint("ResourceType") View view2=view.findViewById(R.id.public_head);//返回布局
        back=view2.findViewById(R.id.back);
        commentList=view.findViewById(R.id.commentList);
        thumbsInfo=view.findViewById(R.id.thumb);
        thumb=view1.findViewById(R.id.like);
        to_comment=view1.findViewById(R.id.to_comment);
        comment_content=view.findViewById(R.id.comment_content);
        TextView textView=view2.findViewById(R.id.head);
        ImageView avatar=view1.findViewById(R.id.avatar);//头像
        TextView username=view1.findViewById(R.id.name);//昵称
        TextView time=view1.findViewById(R.id.time);//发表时间
        ImageView img_content=view1.findViewById(R.id.imgcontent);//文章图片
        TextView content=view1.findViewById(R.id.content);//文章正文
        if (dynamic.get_head().equals("-700063")){
            avatar.setImageResource(R.drawable.math);
        }else {
            ImageLoad imageLoad=new ImageLoad(dynamic.get_head());
            //Log.i("usr:url",dynamic.get_name()+":"+dynamic.getIvfilepath1());
            imageLoad.loadImage(new ImageLoad.ImageCallBack() {
                @Override
                public void getDrawable(Drawable drawable) {
                    avatar.setImageDrawable(drawable);
                }
            });
        }
        ImageLoad imageLoad=new ImageLoad(dynamic.getIvfilepath1());
       // Log.i("usr:url",dynamic.get_name()+":"+dynamic.getIvfilepath1());
        imageLoad.loadImage(new ImageLoad.ImageCallBack() {
            @Override
            public void getDrawable(Drawable drawable) {
                img_content.setImageDrawable(drawable);
            }
        });
        username.setText(dynamic.get_name());
        time.setText(dynamic.get_time());
        content.setText(dynamic.get_content());
        textView.setText("文章详情");
        articleID=dynamic.getArticleID();
    }
    private void ToListView(String comments,Handler mh) throws JSONException {
        list.clear();
        JSONArray ja=new JSONArray(comments);
        int len=ja.length();
        thumbsNum=ja.getInt(len-2);//点赞人数
        isLike=ja.getBoolean(len-3);//是否点赞
        if (ja.getInt(len-1)==0){
            mh.sendEmptyMessage(2);
        }else if (len==3){//是否点赞,点赞人数,传输成功
            mh.sendEmptyMessage(3);
        }else{
            for(int i=0;i<len-2;i++){
            JSONObject js=new JSONObject();
            js=ja.getJSONObject(i);
            Comment comment=new Comment(js.getString("username"),js.getString("comment"));
            list.add(comment);
            }
            mh.sendEmptyMessage(1);
        }
    }
    private boolean sendComment(String []data) throws IOException, JSONException {
        HttpConnection httpConnection=new HttpConnection();
        URL url=new URL(SendCOMMENTURL);
        //HttpURLConnection httpURLConnection;
        HttpURLConnection httpURLConnection=httpConnection.request(url,ToJson(data));
        if (httpURLConnection.getResponseCode()==httpURLConnection.HTTP_OK){
            String res=httpConnection.response(httpURLConnection);
            JSONObject js=new JSONObject(res);
            if (js.getInt("type")==1)
                return true;
            else
                return false;
        }
        else
            return false;
    }
    private String ToJson(String []data) throws JSONException {
        JSONObject js=new JSONObject();
        js.put("username",data[0]);
        js.put("comment",data[1]);
        js.put("articleID",data[2]);
        return js.toString();
    }
}
