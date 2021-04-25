package com.example.zhiliao.Tools;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.example.zhiliao.Fragments.HomePage;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class RelativelayoutBehavior extends CoordinatorLayout.Behavior<RelativeLayout> {
    Context mContext;
    public  RelativelayoutBehavior(Context context, AttributeSet attrs){
        super(context,attrs);
        DisplayMetrics display=context.getResources().getDisplayMetrics();
        mContext=context;
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull RelativeLayout child, @NonNull View dependency) {
        float maxL = DensityUtil.dip2px(mContext, 150) + DensityUtil.getZhuangtai(mContext);

        Log.d("AAAAAAA", dependency.getY() + "拢共" + DensityUtil.dip2px(mContext, 356) + "toolsbar" + maxL);

        Message message = new Message();

        if (dependency.getY() > 0) {


            float a = ((dependency.getY() - maxL) / (DensityUtil.dip2px(mContext, 356) - maxL));

            Log.d("aaaa", "" + a);
            message.what = (int) (a * 100f);


            if (HomePage.mHandler != null)
                HomePage.mHandler.sendMessage(message);
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull RelativeLayout child, @NonNull View dependency) {
        Log.d("BBBBB",dependency.getY()+"");
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
