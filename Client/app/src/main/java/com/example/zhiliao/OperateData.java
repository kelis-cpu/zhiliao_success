package com.example.zhiliao;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.IDN;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URL;

public class OperateData {
    public String stringTojson(String stringArray[])//string转json格式字符串
    {
        JSONObject js=null;
        if (stringArray==null)
        {
            return "";
        }
        js=new JSONObject();
        try{
            js.put("username",stringArray[0]);
            js.put("password",stringArray[1]);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        String jsonString=String.valueOf(js);
        return jsonString;
    }
    public String stringTojson2(int ID,boolean islike,String usrname)//设置点赞
    {
        JSONObject js=null;
        js=new JSONObject();
        try{
            js.put("articleID", ID);
            js.put("islike",islike);
            js.put("usrname",usrname);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return js.toString();
    }
    public int jsonToint(String jsonString)//获取jsonString中的具体数值，用于判断登录状态
    {
        int type=1;
        try{
            JSONObject js=new JSONObject(jsonString);
            type=js.getInt("type");
            Log.i("type",""+type);
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        return type;
    }
    /**
     功能：发送jsonString到服务器进行解析
     msg.what:
     0:服务器连接失败
     1：注册/登录成功 跳转页面
     2：用户已存在/登陆失败
     3：地址为空
     4：连接超时
     **/
    public void sendData(final String jsonString, final android.os.Handler mh, final URL url)
    {
        if (url==null)
        {
            mh.sendEmptyMessage(3);
        }
        else
        {
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
                            int type=jsonToint(res.toString());//判断状态
                            switch (type)
                            {
                                case 0:
                                    mh.sendEmptyMessage(1);
                                    break;
                                case 1:
                                    mh.sendEmptyMessage(2);
                                    break;
                                case 2:
                                    mh.sendEmptyMessage(5);
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

}
