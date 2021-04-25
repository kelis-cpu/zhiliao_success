package com.example.zhiliao.Tools;

import android.graphics.drawable.Drawable;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageLoad {
    private String imgPath;
    public ImageLoad(String imgPath){this.imgPath=imgPath;}
    public interface ImageCallBack{
        public  void getDrawable(Drawable drawable);
    }
    public  void loadImage(ImageCallBack imageCallBack){
        final Handler handler=new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                Drawable drawable= (Drawable) msg.obj;
                imageCallBack.getDrawable(drawable);
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //System.out.println("imgPath"+imgPath);
                    Drawable drawable=Drawable.createFromStream(new URL(imgPath).openStream(),"");
                    Message message=Message.obtain();
                    message.obj=drawable;
                    handler.sendMessage(message);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
