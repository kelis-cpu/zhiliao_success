package com.example.zhiliao;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.zhiliao.Fragments.UserFragment;

public class MainActivity extends AppCompatActivity {
    private FragmentManager manager;
    private ImageView img1,img2,img3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        manager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=manager.beginTransaction();
        Fragment beg=new UserFragment();
        fragmentTransaction.replace(R.id.frament,beg);
        fragmentTransaction.commit();//设置启动fragment
        init();

    }
    View.OnClickListener l =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentTransaction ft=manager.beginTransaction();
            Fragment f =null;
            switch (v.getId())
            {
                case R.id.subjects:
                f=new Subjects();
                break;
                case R.id.article:
                    f=new Article();
                    break;
                case R.id.user:
                    f=new UserFragment();
                    default:
                        break;
            }
            ft.replace(R.id.frament,f);
            ft.commit();
        }
    };
    private void init(){
        img1=findViewById(R.id.article);
        img2=findViewById(R.id.subjects);
        img3=findViewById(R.id.user);
        img1.setOnClickListener(l);
        img2.setOnClickListener(l);
        img3.setOnClickListener(l);
    }
}
