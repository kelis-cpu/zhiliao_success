package com.example.zhiliao.Tools;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

public class HttpConnection {
    private JSONArray ja;
    private JSONObject js;
    public HttpURLConnection request(URL url,String jsonString){
        HttpURLConnection httpURLConnection=null;
        try {
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
            Log.i("data",jsonString);
            if (jsonString!=null){
            PrintWriter out=new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(),"utf-8"));
            out.println(jsonString);
            out.flush();
            out.close();}
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpURLConnection;
    }
    public String response(HttpURLConnection httpURLConnection) throws IOException {
        if (httpURLConnection==null||httpURLConnection.getResponseCode()!=httpURLConnection.HTTP_OK)
            return "";
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
        StringBuilder stringBuilder=new StringBuilder();
        String temp=null;
        while((temp=bufferedReader.readLine())!=null){
            stringBuilder.append(temp);
        }
        return stringBuilder.toString();
    }
    public void httpConnnection(URL url,android.os.Handler mh,String jsonString) {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        int type=0;
        try {
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
            Log.i("JSONString", jsonString);//发送的数据
            //DataOutputStream os = new DataOutputStream(httpURLConnection.getOutputStream());
            //os.writeBytes(jsonString);
            //os.flush();
            //os.close();
            PrintWriter out=new PrintWriter(new OutputStreamWriter(httpURLConnection.getOutputStream(),"utf-8"));
            out.println(jsonString);
            out.flush();
            out.close();
            Log.i("状态码", "" + httpURLConnection.getResponseCode());
            if (httpURLConnection.getResponseCode() == httpURLConnection.HTTP_OK)//判断服务器是否连接成功
            {
                bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder res = new StringBuilder();
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    res.append(temp);//接收的数据
                    Log.i("Main", res.toString());
                }
                type = stringToJson(res.toString());//加载
                Log.i("type", type + "");
                //my=new ArticleAdapter(getActivity(),R.layout.article_item,list);
                //listView.setAdapter(my);
                switch (type) {
                    case 0:
                        mh.sendEmptyMessage(0);
                        break;
                    case 1:
                        mh.sendEmptyMessage(1);//成功
                        break;
                    case 2:
                        mh.sendEmptyMessage(2);//失败
                    default:
                }
            } else {
                mh.sendEmptyMessage(0);
            }
        } catch (
                ProtocolException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
            mh.sendEmptyMessage(2);
        } catch (JSONException e) {
            e.printStackTrace();
            mh.sendEmptyMessage(2);
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
    }
    public int stringToJson(String jsonString) throws JSONException {
        try{
            ja= new JSONArray(jsonString);
        } catch (JSONException e) {
            e.printStackTrace();
            js=new JSONObject(jsonString);
            return js.getInt("type");
        }
        return ja.getInt(ja.length()-1);//返回type
    }
    public JSONArray getJs(){return ja;}

}

