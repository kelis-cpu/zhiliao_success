package com.example.zhiliao.Activity;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zhiliao.R;
import com.example.zhiliao.Tools.SearchView.SearchView;
import com.example.zhiliao.Tools.SearchView.bCallBack;

public class SearchArticle extends AppCompatActivity {
    private SearchView searchView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article);
        init();
        searchView.setOnClickBack(new bCallBack() {
            @Override
            public void BackAction() {
                finish();
            }
        });
    }
    private void init(){
        findViewById(R.id.search_view);
    }
}
