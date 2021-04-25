package com.example.zhiliao.Activity;

import android.Manifest;
import android.app.Dialog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.zhiliao.Entity.Subject;
import com.example.zhiliao.Fragments.TimePickerFragment;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.BottomPopupOption;
import com.example.zhiliao.Tools.UploadArticle;
import com.example.zhiliao.Tools.UploadPhoto;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;


public class OpenClass extends Fragment {
    //请求识别码
    private static final int CODE_CAMERA_REQUEST =1 ;
    private static final int CODE_GALLERY_REQUEST = 2;
    private static final int CODE_RESULT_REQUEST =3 ;
    private static final int RESULT_OK=4;

    private static  final Map<String,String> Sub;
    static {
        Sub=new HashMap<String,String>();
        Sub.put("语文","Chinese");
        Sub.put("数学","Math");
        Sub.put("英语","English");
        Sub.put("物理","Physics");
        Sub.put("政治","Politico");
        Sub.put("历史","History");
        Sub.put("生物","Biology");
        Sub.put("地理","Gro");
        Sub.put("化学","Chemistry");
    }
    private static  final Map<String,String> week;
    static {
        week=new HashMap<String,String>();
        week.put("周一","Monday");
        week.put("周二","Tuesday");
        week.put("周三","Wednesday");
        week.put("周四","Thursday");
        week.put("周五","Friday");
        week.put("周六","Saturday");
        week.put("周日","Sunday");
    }


    private int REQUEST_TAKE_PHOTO_PERMISSION =-1;
    private  String HeadPortrait_PATH;
    private String IMAGE_FILE_NAME;
    private TextView cancel;
    private Button open;
    private Spinner subjects;
    private Spinner weekday;
    private TextView begTime;
    private TextView endTime;
    private EditText classDes;
    private ImageView camera;
    private final static String OPENCLASSURL="http://172.20.10.9:8091/set/openclass";
    private static final String URLUPLOAD="http://172.20.10.9:8091/upload/photo";

    private Bitmap photo;
    private static  int output_X=150;
    private static int output_Y=150;
    private SharedPreferences sp;
    private int PHOTO=0;
    private int BACK=0;
    private int classId;
    private Handler handler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.openclass,null);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        init();//绑定控件
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager f=getFragmentManager();
                f.popBackStack();
            }
        });//取消开课
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String des=classDes.getText().toString();//描述
                String beg=begTime.getText().toString();//开始时间
                String end=endTime.getText().toString();//结束时间
                String sub=Sub.get(subjects.getSelectedItem().toString());//开课科目
                String wk=week.get(weekday.getSelectedItem().toString());//周几
                if (TextUtils.isEmpty(wk)||TextUtils.isEmpty(beg)||TextUtils.isEmpty(end)){Toast.makeText(getActivity(),"请选择开课时间",Toast.LENGTH_SHORT).show();}else if (TextUtils.isEmpty(des)){
                    Toast.makeText(getActivity(),"请添加课程描述",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(sub)){Toast.makeText(getActivity(),"请选择开课科目",Toast.LENGTH_LONG).show();}else if (!CheckTime(beg,end)){
                    Toast.makeText(getActivity(),"注意开课时间",Toast.LENGTH_SHORT).show();
                }else{
                    sp=getActivity().getSharedPreferences("user_info",0);
                    String []data={des,wk,beg,end,sub,sp.getString("user","")};
                    BACK=0;
                    handler =new Handler(){
                        @Override
                        public void handleMessage(@NonNull Message msg) {
                            switch (msg.what){
                                case 0:
                                    Toast.makeText(getActivity(),"服务器连接失败",Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    Toast.makeText(getActivity(),"封面上传成功",Toast.LENGTH_SHORT).show();
                                    BACK+=1;
                                    break;
                                case 2:
                                    Toast.makeText(getActivity(),"封面上传失败",Toast.LENGTH_SHORT).show();
                                    break;
                                case 3:
                                    Toast.makeText(getActivity(),"开课信息上传成功",Toast.LENGTH_SHORT).show();
                                    classId=msg.arg1;
                                    UploadPhoto uploadPhoto=new UploadPhoto();
                                    URL url_photo=null;
                                    try {
                                        url_photo=new URL(URLUPLOAD);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }
                                    uploadPhoto.uploadPhoto(handler,url_photo,HeadPortrait_PATH,classId,1);//上传图片
                                    BACK+=1;
                                    break;
                                case 4:
                                    Toast.makeText(getActivity(),"连接超时",Toast.LENGTH_SHORT).show();
                                    break;
                                case 5:
                                    Toast.makeText(getActivity(),"开课信息上传失败",Toast.LENGTH_SHORT).show();
                                    break;
                            }
                            if (BACK==2){
                                BACK=0;
                                getFragmentManager().popBackStack();
                            }
                        }
                    };
                    UploadArticle upload2=new UploadArticle();
                    URL url2=null;
                    try {
                        url2=new URL(OPENCLASSURL);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    upload2.sendArticle(handler,upload2.stringTojson2(data),url2);//上传开课信息
                }
            }
        });//开课
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomPopupOption bottomPopupOption = new BottomPopupOption(getActivity());
                bottomPopupOption.setItemText("拍照","相册");
                bottomPopupOption.showPopupWindow();
                bottomPopupOption.setItemClickListener(new BottomPopupOption.onPopupWindowItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        bottomPopupOption.dismiss();
                        switch(position){
                            case 0:
                                ImageFromCameraCapture();
                                Toast.makeText(getActivity(),"拍照",Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                ImageFromGallery();
                                Toast.makeText(getActivity(),"相册",Toast.LENGTH_SHORT).show();
                        }
                        PHOTO=1;
                    }
                });
            }
        });//选择封面
        begTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePicker=new TimePickerFragment(begTime);
                timePicker.show(getFragmentManager(), "timePicker");
            }
        });//设置开课时间
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment timePicker=new TimePickerFragment(endTime);
                timePicker.show(getFragmentManager(), "timePicker");
            }
        });//设置开课时间
        IMAGE_FILE_NAME=getPhotoFileName();
        HeadPortrait_PATH=Environment.getExternalStorageDirectory()+"/test/"+IMAGE_FILE_NAME;
        }
    public void init(){
        cancel = getActivity().findViewById(R.id.cancel1);
        open = getActivity().findViewById(R.id.open);
        subjects = getActivity().findViewById(R.id.subject);
        weekday = getActivity().findViewById(R.id.weekday);
        begTime = getActivity().findViewById(R.id.begTime);
        endTime = getActivity().findViewById(R.id.endTime);
        classDes = getActivity().findViewById(R.id.classDes);
        camera = getActivity().findViewById(R.id.takecover);
    }
    //启动手机相机拍摄获取照片
    private void ImageFromCameraCapture(){
        //6.0以上动态获取权限
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            //申请权限，REQUEST_TAKE_PHOTO_PERMISSION是自定义的常量
            ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.CAMERA},REQUEST_TAKE_PHOTO_PERMISSION);
        }
        else{
            Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (hasSdcard()){
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                        Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME
                )));
            }
            startActivityForResult(intent,CODE_CAMERA_REQUEST);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==RESULT_CANCELED || data==null){
            return;//用户没有进行有效的设置操作，返回
        }
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
                    Toast.makeText(getActivity(),"无sd卡",Toast.LENGTH_SHORT).show();
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

    //从本地相册选取图片
    private void ImageFromGallery(){
        Intent intent=new Intent();
        //设置文件类型
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent,CODE_GALLERY_REQUEST);
    }
    //检查设备是否存在SDcard
    public static  boolean hasSdcard(){
        String state=Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
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
    private void setImage(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            photo = extras.getParcelable("data");
//            photo = intent.getParcelableExtra("data");
            camera.setImageBitmap(photo);
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
                Toast.makeText(getActivity(),"文件不存在",Toast.LENGTH_SHORT).show();
            }
        }
    }
    //用当前时间给取得照片命名
    private String getPhotoFileName(){
        Date date=new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat=new SimpleDateFormat("'IMG'_yyyy-MM-dd_HH:mm:ss");
        return dateFormat.format(date)+".jpg";
    }
    //判断开课时间
    private boolean CheckTime(String beg,String end){
        String []t1=beg.split(":");
        String []t2=end.split(":");
        float begTime=Integer.parseInt(t1[0])+Integer.parseInt(t1[1])/100;
        float endTime=Integer.parseInt(t2[0])+Integer.parseInt(t2[1])/100;
        if (begTime>=endTime){
            return false;
        }else {
            return true;
        }
    }
}
