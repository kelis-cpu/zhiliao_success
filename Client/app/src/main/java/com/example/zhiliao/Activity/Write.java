package com.example.zhiliao.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.zhiliao.Adapter.ArticleAdapter;
import com.example.zhiliao.Entity.Dynamic;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.BottomPopupOption;
import com.example.zhiliao.Tools.UploadArticle;
import com.example.zhiliao.Tools.UploadPhoto;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class Write extends AppCompatActivity {
    //请求识别码
    private static final int CODE_CAMERA_REQUEST =1 ;
    private static final int CODE_GALLERY_REQUEST = 2;
    private static final int CODE_RESULT_REQUEST =3 ;
    private static final int RESULT_OK=4;

    private int REQUEST_TAKE_PHOTO_PERMISSION =-1;
    //裁剪后图片的宽高
    private static  int output_X=150;
    private static int output_Y=150;
    private String IMAGE_FILE_NAME;
    private  String HeadPortrait_PATH;
    private Bitmap photo;//发表的照片
    private TextView cancel;//取消按钮
    private Button send;//发表按钮
    private ImageView camera;//拍照或者上传照片
    private EditText article;//文章内容
    private ImageView img;//文章图片
    private ListView listView;
    private Handler handler;//用于处理发表状态
    private SharedPreferences sp;
    private static final String URLUPLOAD="http://172.20.10.9:8091/upload/photo";
    private static  final String UPLOADARTICLE="http://172.20.10.9:8091/upload/article";
    private int BACK;//用于判断图片和文章是否都上传成功
    private int articleID=-1;
    private String photoUrl;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write);
        init();//绑定控件
        send.setEnabled(false);
        send.setPressed(false);
        article.addTextChangedListener(new TextWatcher() {//监听文章内容变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")){
                    send.setEnabled(false);
                    send.setPressed(false);
                }else {
                    send.setPressed(true);
                    send.setEnabled(true);
                }
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomPopupOption bottomPopupOption = new BottomPopupOption(Write.this);
                bottomPopupOption.setItemText("拍照","相册");
                bottomPopupOption.showPopupWindow();
                bottomPopupOption.setItemClickListener(new BottomPopupOption.onPopupWindowItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        bottomPopupOption.dismiss();
                        switch(position){
                            case 0:
                                ImageFromCameraCapture();
                                Toast.makeText(Write.this,"拍照",Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                ImageFromGallery();
                                Toast.makeText(Write.this,"相册",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        IMAGE_FILE_NAME=getPhotoFileName();
        HeadPortrait_PATH=Environment.getExternalStorageDirectory()+"/test/"+IMAGE_FILE_NAME;
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data[]=null;
                String Art=article.getText().toString();//文章内容
                Date date=new Date(System.currentTimeMillis());
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String time=dateFormat.format(date);//文章发布时间
                sp=Write.this.getSharedPreferences("user_info",0);
                String author=sp.getString("user","");//文章作者
                data=new String[]{author,time,Art};
                BACK=0;
                handler=new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        switch (msg.what){
                            case 0:
                                Toast.makeText(Write.this,"服务器连接失败",Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                Toast.makeText(Write.this,"图片上传成功",Toast.LENGTH_SHORT).show();
                                photoUrl= (String) msg.obj;
                                System.out.println("photoURl"+photoUrl);
                                BACK+=1;
                                break;
                            case 2:
                                Toast.makeText(Write.this,"图片上传失败",Toast.LENGTH_SHORT).show();
                                break;
                            case 3:
                                Toast.makeText(Write.this,"文章上传成功",Toast.LENGTH_SHORT).show();
                                articleID=msg.arg1;
                                UploadPhoto upload=new UploadPhoto();//上传图片
                                URL url=null;
                                try {
                                    url=new URL(URLUPLOAD);
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                }
                                upload.uploadPhoto(handler,url,HeadPortrait_PATH,articleID,0);
                                Log.i("articleID",String.valueOf(articleID));
                                BACK+=1;
                                break;
                            case 4:
                                Toast.makeText(Write.this,"连接超时",Toast.LENGTH_SHORT).show();
                                break;
                            case 5:
                                Toast.makeText(Write.this,"文章上传失败",Toast.LENGTH_SHORT).show();
                                handler.sendEmptyMessage(6);
                                break;
                            case 6:
                                Toast.makeText(Write.this,"文章上传失败，无法上传图片",Toast.LENGTH_SHORT).show();
                                break;
                        }
                        if (BACK==2){
                            Intent intent=new Intent();
                            //存放数据
                            intent.putExtra("author",author);
                            intent.putExtra("CurrentTime",time);
                            intent.putExtra("editArticle",Art);
                            //intent.putExtra("photo",HeadPortrait_PATH);
                            intent.putExtra("photoUrl",photoUrl);
                            setResult(RESULT_OK,intent);
                            BACK=0;

                            finish();
                        }

                    }
                };
                UploadArticle upload2=new UploadArticle();//上传文章
                URL url2=null;
                try {
                    url2=new URL(UPLOADARTICLE);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                upload2.sendArticle(handler,upload2.stringTojson(data),url2);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {//取消发表
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //用当前时间给取得照片命名
    private String getPhotoFileName(){
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat=new SimpleDateFormat("'IMG'_yyyy-MM-dd_HH:mm:ss");
        return dateFormat.format(date)+".jpg";
    }
//动态申请权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==REQUEST_TAKE_PHOTO_PERMISSION){
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                //申请成功，可以拍照
                ImageFromCameraCapture();
            }else{
                Toast.makeText(Write.this,"你拒绝了该权限\n",Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //启动手机相机拍摄获取照片
    public void ImageFromCameraCapture(){
        //6.0以上动态获取权限
        if (ContextCompat.checkSelfPermission(Write.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},REQUEST_TAKE_PHOTO_PERMISSION);
        }
        else{
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (hasSdcard()){
                intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME
                )));
            }
            startActivityForResult(intent,CODE_CAMERA_REQUEST);
        }
    }
    //从本地相册选取图片
   public void ImageFromGallery(){
        Intent intent=new Intent();
        //设置文件类型
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent,CODE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==RESULT_CANCELED||data==null){
            send.setEnabled(false);
            send.setPressed(false);
            return;//用户没有进行有效的设置操作，返回
        }
        send.setPressed(true);
        send.setEnabled(true);
        switch (requestCode){
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(data.getData());
                break;
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()){
                    File tmpFile=new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tmpFile));
                }
                else{
                    Toast.makeText(Write.this,"无sd卡",Toast.LENGTH_SHORT).show();
                }
                break;
            case CODE_RESULT_REQUEST:
                if (data!=null){
                    setImage(data);
                    File file =new File(Environment.getExternalStorageState(),IMAGE_FILE_NAME);
                    if (file.exists()&&!file.isDirectory()){
                        file.delete();
                    }
                }
                break;
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
//剪裁原始图片
public void cropRawPhoto(Uri uri){
        Intent intent=new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri,"image/*");
        //设置剪裁
        intent.putExtra("crop",true);
        //aspectX,aspectY:宽高比例
    intent.putExtra("aspectX",1);
    intent.putExtra("aspectY",1);
    //outputX,outputY:裁剪图片宽高
    intent.putExtra("outputX",output_X);
    intent.putExtra("outputY",output_Y);
    intent.putExtra("return-data",true);
    startActivityForResult(intent,CODE_RESULT_REQUEST);
}
    private void init(){
        cancel=findViewById(R.id.cancel);
        send=findViewById(R.id.send);
        camera=findViewById(R.id.takephoto);
        article=findViewById(R.id.article_content);
        img=findViewById(R.id.img1);
        listView=findViewById(R.id.listview);

    }
    //检查设备是否存在SDcard
    public static  boolean hasSdcard(){
        String state=Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }
    //提取保存裁剪后的图片
    private void setImage(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
//            photo = intent.getParcelableExtra("data");
            img.setImageBitmap(photo);
            //circleImageView2.setImageBitmap(photo);
            File nf = new File(Environment.getExternalStorageDirectory()+"/test");
            nf.mkdir();
            //在根目录下面的ASk文件夹下 创建okkk.jpg文件
            File f = new File(Environment.getExternalStorageDirectory()+"/test", IMAGE_FILE_NAME);
            FileOutputStream out = null;
            try {      //打开输出流 将图片数据填入文件中
                out = new FileOutputStream(f);
                photo.compress(Bitmap.CompressFormat.PNG, 90, out);

                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            File file = new File(HeadPortrait_PATH);
            if (!file.exists()){
                Toast.makeText(Write.this,"文件不存在",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
