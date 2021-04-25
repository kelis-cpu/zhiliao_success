package com.example.zhiliao.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class getHttpBitmap {
    public Bitmap getHttpmap(String url){
        URL myFileurl=null;
        Bitmap bitmap=null;
        try{
            myFileurl=new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try{
            HttpURLConnection conn= (HttpURLConnection) myFileurl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            InputStream is=conn.getInputStream();
            bitmap= BitmapFactory.decodeStream(is);
            is.close();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }//网络加载图片
}
