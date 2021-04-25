package com.example.zhiliao;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.zhiliao.Activity.GetClass;
import com.example.zhiliao.Entity.Subject;
import com.example.zhiliao.Tools.SearchView.*;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yalantis.jellytoolbar.listener.JellyListener;
import com.yalantis.jellytoolbar.widget.JellyToolbar;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Subjects extends Fragment implements View.OnClickListener{
    private JellyToolbar toolbar;
    private AppCompatEditText editText;
    private ConstraintLayout constraintLayout;//约束布局管理器
    ActionBar actionBar;
    private boolean tmp=true;
    private ImageView chinese;
    private ImageView math;
    private ImageView english;
    private ImageView physics;
    private ImageView chemistry;
    private ImageView biolgy;
    private ImageView political;
    private ImageView history;
    private ImageView gro;
    private SearchView searchView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.subjects,null);
        ImageView imageView=view.findViewById(R.id.search_view).findViewById(R.id.search_back);
        imageView.setVisibility(View.INVISIBLE);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        init();
        //string= 搜索框输入的内容
        searchView.setOnClickSearch(new ICallBack() {
            @Override
            public void SearchAction(String string) {//string为搜索内容
                Intent intent=new Intent(getActivity(),GetClass.class);
                intent.putExtra("subject",string);
                startActivity(intent);
            }
        });

    }

    /**private JellyListener jellyListener = new JellyListener() {
        @Override
        public void onCancelIconClicked() {//果冻搜索
            if (TextUtils.isEmpty(editText.getText())) {
                toolbar.collapse();
            } else {
                editText.getText().clear();
            }
        }
    };**/

    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getActivity(), GetClass.class);
        switch (v.getId()){
            case R.id.math:
                intent.putExtra("subject","math");
                break;
            case R.id.chinese:
                intent.putExtra("subject","chinese");
                break;
            case R.id.english:
                intent.putExtra("subject","english");
                break;
            case R.id.physics:
                intent.putExtra("subject","physics");
                break;
            case R.id.chemistry:
                intent.putExtra("subject","chemistry");
                break;
            case R.id.biolgy:
                intent.putExtra("subject","biolgy");
                break;
            case R.id.gro:
                intent.putExtra("subject","gro");
                break;
            case R.id.history:
                intent.putExtra("subject","history");
                break;
            case R.id.political:
                intent.putExtra("subject","political");
                break;
        }
        startActivity(intent);
    }
    private void init(){
        chinese=getActivity().findViewById(R.id.chinese);
        math=getActivity().findViewById(R.id.math);
        english=getActivity().findViewById(R.id.english);
        physics=getActivity().findViewById(R.id.physics);
        chemistry=getActivity().findViewById(R.id.chemistry);
        biolgy=getActivity().findViewById(R.id.biolgy);
        political=getActivity().findViewById(R.id.political);
        history=getActivity().findViewById(R.id.history);
        gro=getActivity().findViewById(R.id.gro);
        searchView=getActivity().findViewById(R.id.search_view);
        math.setOnClickListener(this);
        chinese.setOnClickListener(this);
        english.setOnClickListener(this);
        physics.setOnClickListener(this);
        chemistry.setOnClickListener(this);
        biolgy.setOnClickListener(this);
        political.setOnClickListener(this);
        history.setOnClickListener(this);
        gro.setOnClickListener(this);
    }//绑定控件
}
