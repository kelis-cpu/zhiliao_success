package com.example.zhiliao.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.zhiliao.Activity.ArticleDetail;
import com.example.zhiliao.Entity.Comment;
import com.example.zhiliao.Entity.Dynamic;
import com.example.zhiliao.OperateData;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.HttpConnection;
import com.example.zhiliao.Tools.ImageLoad;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.os.Handler;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ArticleAdapter extends ArrayAdapter<Dynamic>{
    private final static String SETLIKEURL="http://172.20.10.9:8091/set/likes";
    private List<Dynamic> articles;
    private Context ctx;
    private LayoutInflater layoutInflater;
    private SharedPreferences sp;
    private List<Comment> commentList=new ArrayList<Comment>();
    //private FragmentManager manager;
    //private List<Comment>map;
    //private boolean islike;
    public ArticleAdapter(@NonNull Context context, int resource,List<Dynamic> articles) {
        super(context,resource,articles);
        ctx=context;
        this.articles=articles;
        layoutInflater=LayoutInflater.from(ctx);
    }

    @Override
    public int getCount() {//??????ListView??????????????????????????????
        return articles.size();
    }

    @Nullable
    @Override
    public Dynamic getItem(int position) {//??????????????????
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {//??????????????????id
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int id=0;
        //return super.getView(position, convertView, parent);
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            //LayoutInflater layoutInflater=LayoutInflater.from(ctx);
            convertView=layoutInflater.inflate(R.layout.article_item,null);
            viewHolder.head=convertView.findViewById(R.id.avatar);
            viewHolder.name=convertView.findViewById(R.id.name);
            viewHolder.time=convertView.findViewById(R.id.time);
            viewHolder.content=convertView.findViewById(R.id.content);
            //viewHolder.comment=convertView.findViewById(R.id.comment);
            viewHolder.like=convertView.findViewById(R.id.like);
            viewHolder.to_comment=convertView.findViewById(R.id.to_comment);
            //viewHolder.wholike=convertView.findViewById(R.id.wholike);
            viewHolder.imgContent=convertView.findViewById(R.id.imgcontent);
            //viewHolder.commentList=convertView.findViewById(R.id.commentList);//????????????
            //viewHolder.loadMore=convertView.findViewById(R.id.loadMore);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        //Dynamic dynamic=articles.get(position);
        Dynamic dynamic=articles.get(position);
       // commentList=dynamic.getCommentList();//????????????
        //CommentAdapter commentAdapter;
        //ap=dynamic.getCommentList();
        //System.out.println("mapSize:"+map.size()+",map[0]"+map.get(0).getComment()+",map[1]"+map.get(1).getComment());
        //commentAdapter=new CommentAdapter(ctx,0,map);
        //commentAdapter.setShow(false);
        //???????????????
        //viewHolder.commentList.setAdapter(commentAdapter);
        //setListViewHeightBasedOnChildren(viewHolder.commentList);
        if (dynamic!=null){
           // Log.i("head",String.valueOf(dynamic.get_head()));
            if (dynamic.get_head().equals("-700063"))//????????????????????????????????????
            viewHolder.head.setImageResource(R.drawable.tx);
            else
            {
                ImageLoad imageLoad=new ImageLoad(dynamic.get_head());
                imageLoad.loadImage(new ImageLoad.ImageCallBack() {
                    @Override
                    public void getDrawable(Drawable drawable) {
                        viewHolder.head.setImageDrawable(drawable);
                    }
                });
            }
            viewHolder.name.setText(dynamic.get_name());
            viewHolder.time.setText(dynamic.get_time());
            viewHolder.content.setText(dynamic.get_content());
            viewHolder.to_comment.setImageResource(R.drawable.comment);
            ImageLoad imageLoad=new ImageLoad(dynamic.getIvfilepath1());
            Log.i("usr:url",dynamic.get_name()+":"+dynamic.getIvfilepath1());
            imageLoad.loadImage(new ImageLoad.ImageCallBack() {
                @Override
                public void getDrawable(Drawable drawable) {
                    viewHolder.imgContent.setImageDrawable(drawable);
                }
            });
            boolean islike=dynamic.get_like();
            if(islike)
            viewHolder.like.setImageResource(R.drawable.islike);
            else
                viewHolder.like.setImageResource(R.drawable.notlike);
            id=dynamic.getArticleID();//??????ID
            //viewHolder.loadMore.setText("????????????");
        }
        sp=ctx.getSharedPreferences("user_info",0);
        //??????????????????
        boolean finalIslike = dynamic.get_like();//????????????
        int ID=id;//??????ID
        String usr=sp.getString("user","");//????????????
       /** viewHolder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler mh=new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        switch(msg.what){
                        case 0:
                                Toast.makeText(getContext(),"????????????",Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(getContext(),"??????????????????",Toast.LENGTH_SHORT).show();
                                break;
                            case  4:
                                Toast.makeText(getContext(),"????????????",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(getContext(),"??????",Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };
                URL url=null;
                try{
                    url=new URL(SETLIKEURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    mh.sendEmptyMessage(4);
                }
                OperateData send=new OperateData();
                send.sendData(send.stringTojson2(ID,finalIslike,usr),mh,url);
            }
        });**/
        //??????EditView??????
        /**viewHolder.comment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_DONE){
                    sp=ctx.getSharedPreferences("user_info",0);
                    String []data={sp.getString("user",""),v.getText().toString(),String.valueOf(dynamic.getArticleID())};//???????????????????????????
                    try {
                        if (sendComment(data)){
                            //??????????????????
                        }else{

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return false;//false ??????????????????????????????
            }
        });**/
        /**
        //????????????TextView??????
        viewHolder.loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ctx, ArticleDetail.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("articleInfo",dynamic);
                intent.putExtras(bundle);
                ctx.startActivity(intent);
            }
        });//???????????????**/
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Click(dynamic);
            }
        });//???????????????
        return convertView;
    }

    @Override
    public void add(@Nullable Dynamic object) {
        articles.add(object);
    }
    class ViewHolder{
        public ImageView head;
        public TextView name;
        public TextView time;
        public TextView content;
        public ImageView like;
        //public EditText comment;
        public ImageView to_comment;
        //public TextView wholike;
        public ImageView imgContent;
        //public ListView commentList;
        //public TextView loadMore;
    }
    /**private String set_wholike(Dynamic dynamic){
        String tmp1=new String();
        String more="";
        List<String> usr_like=dynamic.getWholike();
        if (usr_like.size()==0)
            return "";
        if (usr_like.size()>5){
            usr_like=usr_like.subList(0,4);
            more+="??????";
        }
        tmp1=usr_like.toString();
        tmp1=tmp1.substring(1,tmp1.length()-1)+more+"????????????";
        return tmp1;
    }**/


    private void Click(Dynamic dynamic){
        Intent intent =new Intent(ctx,ArticleDetail.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("articleInfo",dynamic);
        intent.putExtras(bundle);
        ctx.startActivity(intent);
    }//????????????

}
