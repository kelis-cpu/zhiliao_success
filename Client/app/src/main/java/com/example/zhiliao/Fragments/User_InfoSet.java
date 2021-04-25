package com.example.zhiliao.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bigkoo.pickerview.TimePickerView;
import com.example.zhiliao.Activity.Set_UserName;
import com.example.zhiliao.Activity.Write;
import com.example.zhiliao.R;
import com.example.zhiliao.Tools.BottomPopupOption;
import com.example.zhiliao.Tools.HttpConnection;
import com.example.zhiliao.Tools.ImageLoad;
import com.lljjcoder.citypickerview.widget.CityPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class User_InfoSet extends AppCompatActivity {
    private final static int SET_USER_CODE=100;
    private final static int SET_SIGN_CODE=101;
    private final static String INFOURL="http://172.20.10.9:8091/getusrinfo?username=";
    private final static  String SET_USER_INFO_URL="http:172.20.10.9:8091/setuserinfo";
    private SharedPreferences sp;
    private FragmentManager manager;
    private ImageView back;
    private ImageView avatar;//头像
    private TextView username,sex,birth,place;
    private EditText introduce;//个性签名
    private TextView sign;
    private TimePickerView pvTime;//时间选择器
    private String ava;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(resultCode){
            case SET_USER_CODE:
                String user=data.getStringExtra("username");
                username.setText(user);
                break;
            case SET_SIGN_CODE:
                String sign_tv=data.getStringExtra("sign");
                sign.setText(sign_tv);
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_allinfo);
        TextView head=findViewById(R.id.head);
        head.setText("我的资料");
        init();//绑定控件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler=new Handler(){
                    @Override
                    public void handleMessage(@NonNull Message msg) {
                        switch (msg.what) {
                            case 1:
                                Toast.makeText(User_InfoSet.this, "用户名已存在", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Intent intent=new Intent();
                                intent.putExtra("birth",birth.getText().toString());
                                System.out.println(birth.getText().toString());
                                intent.putExtra("place",place.getText().toString());
                                intent.putExtra("sex",sex.getText().toString());
                                setResult(102,intent);
                                finish();
                                break;
                        }
                    }
                };
                save(handler);
            }
        });
        birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initTimePicker();
                pvTime.show();
            }
        });
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(User_InfoSet.this, Set_UserName.class);
                intent.putExtra("username",username.getText().toString());
                intent.putExtra("object",0);
                startActivityForResult(intent,SET_USER_CODE);
            }
        });
        sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomPopupOption bottomPopupOption = new BottomPopupOption(User_InfoSet.this);
                bottomPopupOption.setItemText("男","女");
                bottomPopupOption.showPopupWindow();
                bottomPopupOption.setItemClickListener(new BottomPopupOption.onPopupWindowItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        switch(position){
                            case 0:
                                sex.setText("男");
                                break;
                            case 1:
                                sex.setText("女");
                                break;
                        }
                        bottomPopupOption.dismiss();
                    }
                });
            }
        });
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectAddress();
            }
        });
        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(User_InfoSet.this,Set_UserName.class);
                intent.putExtra("sign",sign.getText().toString());
                intent.putExtra("object",1);
                startActivityForResult(intent,SET_SIGN_CODE);
            }
        });
        Handler handler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch(msg.what){
                    case 0:
                        if (!ava.equals("local")){
                            ImageLoad imageLoad=new ImageLoad(ava);
                            imageLoad.loadImage(new ImageLoad.ImageCallBack() {
                                @Override
                                public void getDrawable(Drawable drawable) {
                                    avatar.setImageDrawable(drawable);
                                }
                            });
                        }//设置头像
                        break;
                }
            }
        };
        new Thread(){
            @Override
            public void run() {
                sp=getSharedPreferences("user_info",0);
                String username=sp.getString("user","");
                URL url =null;
                JSONObject js;
                try {
                    url=new URL(INFOURL+username);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                HttpConnection httpConnection=new HttpConnection();
                HttpURLConnection httpURLConnection=httpConnection.request(url,"");
                try {
                    Log.i("respcode",httpURLConnection.getResponseCode()+"");
                    if (httpURLConnection.getResponseCode()==httpURLConnection.HTTP_OK){
                        ava=loadInfo(httpConnection.response(httpURLConnection));
                        handler.sendEmptyMessage(0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void init(){
        back=findViewById(R.id.back);
        avatar=findViewById(R.id.avatar);
        username=findViewById(R.id.username);
        birth=findViewById(R.id.birth);
        place=findViewById(R.id.local);
        sex=findViewById(R.id.sex);
        sign=findViewById(R.id.sign);
        introduce=findViewById(R.id.introduce);
    }

    private String getInfo() throws JSONException {
        sp=getSharedPreferences("user_info",0);
        String old_name=sp.getString("user","");
        JSONObject js=new JSONObject();
        js.put("old_username",old_name);
        js.put("new_username",username.getText().toString());
        //js.put("avatar",avatar.)
        js.put("birth",birth.getText().toString());
        js.put("introduce",introduce.getText().toString());
        js.put("place",place.getText().toString());
        js.put("sex",sex.getText().toString());
        js.put("sign",sign.getText().toString());
        return js.toString();
    }
    //初始化时间选择器
    private void initTimePicker() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 1, 1);//起始时间
        Calendar endDate = Calendar.getInstance();
        endDate.set(2099, 12, 31);//结束时间
        pvTime = new TimePickerView.Builder(User_InfoSet.this,
                new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        //选中事件回调
                        //mTvMyBirthday 这个组件就是个TextView用来显示日期 如2020-09-08
                        birth.setText(getTimes(date));
                    }
                })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "", "")
                .isCenterLabel(true)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setDecorView(null)
                .build();
    }
    //格式化时间
    private String getTimes(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
    //加载个人信息
    private String loadInfo(String info) throws JSONException {
        JSONObject js=new JSONObject(info);
        String avatar_url=js.getString("avatar");
        String user=js.getString("username");
        String sign_tx=js.getString("sign");
        String birth_tx=js.getString("birth");
        String place_tx=js.getString("place");
        String sex_tx=js.getString("sex");
        String introduce_tx=js.getString("introduce");
        if (!TextUtils.isEmpty(user)){
            username.setText(user);
        }
        if (!TextUtils.isEmpty(sign_tx)){
            sign.setText(sign_tx);
        }
        if (!TextUtils.isEmpty(birth_tx)){
            birth.setText(birth_tx);
        }
        if (!TextUtils.isEmpty(sex_tx)){
            sex.setText(sex_tx);
        }
        if (!TextUtils.isEmpty(place_tx)){
            place.setText(place_tx);
        }
        if (!TextUtils.isEmpty(introduce_tx)){
            introduce.setText(introduce_tx);
        }
        return avatar_url;
    }
    //选择地区
    private void selectAddress() {
        CityPicker cityPicker = new CityPicker.Builder(User_InfoSet.this)
                .textSize(14)
                .title("地址选择")
                .titleBackgroundColor("#FFFFFF")
                .confirTextColor("#696969")
                .cancelTextColor("#696969")
                .province("江苏省")
                .city("常州市")
                .district("天宁区")
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();
        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];
                //为TextView赋值
                place.setText(province.trim() + "-" + city.trim() + "-" + district.trim());
            }
        });
    }
    //保存修改后的个人信息
    private void save(Handler mh){
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url=new URL(SET_USER_INFO_URL);
                    HttpConnection httpConnection=new HttpConnection();
                    HttpURLConnection httpURLConnection=httpConnection.request(url,getInfo());
                    if (httpURLConnection.getResponseCode()==httpURLConnection.HTTP_OK){
                        String json=httpConnection.response(httpURLConnection);
                        JSONObject js=new JSONObject(json);
                        if (js.getInt("type")==2){
                            mh.sendEmptyMessage(2);
                        }else{
                            mh.sendEmptyMessage(1);
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
