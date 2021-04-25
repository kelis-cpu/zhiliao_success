package com.example.zhiliao.Tools;

import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.logging.Handler;

public class UploadPhoto {
    private String photoUrl;
    public void uploadPhoto(android.os.Handler mh, URL url, String FilePath,int articleID,int what){

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpURLConnection=null;
                BufferedReader bufferedReader=null;
                try{
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    // 设置连接超时时间
                    httpURLConnection.setConnectTimeout(5 * 1000);
                    //设置从主机读取数据超时
                    httpURLConnection.setReadTimeout(5 * 1000);
                    // Post请求必须设置允许输出 默认false
                    httpURLConnection.setDoOutput(true);
                    //设置请求允许输入 默认是true
                    httpURLConnection.setDoInput(true);
                    // Post请求不能使用缓存
                    httpURLConnection.setUseCaches(false);
                    // 设置为Post请求
                    httpURLConnection.setRequestMethod("POST");
                    //设置本次连接是否自动处理重定向
                    httpURLConnection.setInstanceFollowRedirects(true);
                    //一次tcp连接可以持续发送多份数据
                    httpURLConnection.setRequestProperty("Connection","Keep-Alive");
                    //设置编码
                    httpURLConnection.setRequestProperty("Charset","UTF-8");
                    // 配置请求Content-Type
                    httpURLConnection.setRequestProperty("Content-Type", "text/plain");
                    //开始连接
                    httpURLConnection.connect();
                    DataOutputStream os=new DataOutputStream(httpURLConnection.getOutputStream());
                    //取得文件流
                    FileInputStream fileInputStream=new FileInputStream(FilePath);
                    if (articleID!=0){
                    os.writeInt(articleID);
                    os.writeInt(what);
                    os.flush();
                    }
                    int buffersize=1024;//每次写入1024bytes
                    int length=-1;
                    byte[] buffer=new byte[buffersize];
                    //发送数据
                    while((length=fileInputStream.read(buffer))!=-1){
                     os.write(buffer,0,length);
                    }
                    os.flush();
                    fileInputStream.close();
                    os.close();
                    Log.i("状态码",""+httpURLConnection.getResponseCode());
                    if (httpURLConnection.getResponseCode()==httpURLConnection.HTTP_OK)//判断服务器是否连接成功
                    {
                        bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                        StringBuilder res = new StringBuilder();
                        String temp;
                        while ((temp=bufferedReader.readLine())!=null)
                        {
                            res.append(temp);
                            Log.i("Main",res.toString());
                        }
                        int type=jsonToint(res.toString());
                        switch (type)
                        {
                            case 0:
                                mh.sendEmptyMessage(0);
                                break;
                            case 1:
                                Message msg=new Message();
                                msg.what=1;
                                msg.obj=photoUrl;//图片服务器地址
                                mh.sendMessage(msg);//成功
                                break;
                            case 2:
                                mh.sendEmptyMessage(2);//失败
                            default:
                        }
                    }
                    else
                    {
                        mh.sendEmptyMessage(0);
                    }
                } catch (ProtocolException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                    mh.sendEmptyMessage(4);
                }finally {
                    if (bufferedReader!=null){
                        try{bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (httpURLConnection!=null)
                    {
                        httpURLConnection.disconnect();
                    }
                }
            }
        }).start();
    }

    public int jsonToint(String jsonString)//获取jsonString中的具体数值，用于判断登录状态
    {
        int type=0;
        try{
            JSONObject js=new JSONObject(jsonString);
            type=js.getInt("type");
            photoUrl=js.getString("photoUrl");
            Log.i("type",""+type);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        return type;
    }
}
