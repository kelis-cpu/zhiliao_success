package com.example.zhiliao.Tools;

import android.os.Message;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class UploadArticle {
    private int articleID=-1;
    public String stringTojson(String[] Array){
        JSONObject js=null;
        if (Array==null){
            return "";
        }
        js=new JSONObject();
        try{
            js.put("author",Array[0]);
            js.put("time",Array[1]);
            js.put("art",Array[2]);
            js.put("head",-700063);
            js.put("img1Url","test1");
            js.put("img2Url","test2");
            js.put("img3Url","test3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return String.valueOf(js);
    }
    public  String stringTojson2(String[] Array){
        JSONObject js=null;
        if (Array==null){
            return "";
        }
        js=new JSONObject();
        try{
            js.put("classDescription",Array[0]);
            js.put("week",Array[1]);
            js.put("begTime",Array[2]);
            js.put("endTime",Array[3]);
            js.put("subject",Array[4]);
            js.put("teacherName",Array[5]);
            //js.put("cover",Array[6]);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return String.valueOf(js);
    }
    public int jsonToint(String jsonString)//获取jsonString中的具体数值，用于判断登录状态
    {
        int type=0;
        try{
            JSONObject js=new JSONObject(jsonString);
            type=js.getInt("type");
            articleID=js.getInt("id");
            Log.i("id",""+articleID);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        return type;
    }
    public void sendArticle(android.os.Handler mh, String jsonString, URL url){
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
                    // 配置请求Content-Type
                    httpURLConnection.setRequestProperty("Content-Type", "application/json");
                    //开始连接
                    httpURLConnection.connect();

                    //发送数据
                    Log.i("JSONString",jsonString);
                    DataOutputStream os=new DataOutputStream(httpURLConnection.getOutputStream());
                    os.writeBytes(jsonString);
                    os.flush();
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
                            case 3:
                                Message msg=new Message();
                                msg.what=3;
                                msg.arg1=articleID;
                                mh.sendMessage(msg);//成功
                                break;
                            case 5:
                                mh.sendEmptyMessage(5);//失败
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
}
