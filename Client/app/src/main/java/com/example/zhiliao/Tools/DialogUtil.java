package com.example.zhiliao.Tools;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhiliao.R;

public class DialogUtil {

       private Button positiveButton;
        private static AlertDialog dialog;

        /**
         * @param ctx                    Context
         * @param title                       提示标题
         * @param msg                         提示内容
         * @param positiveText                确认
         * @param negativeText                取消
         * @param cancelableTouchOut          点击外部是否隐藏提示框
         * @param alertDialogBtnClickListener 点击监听
         */
        public  void showAlertDialog(Context ctx, String title, String msg,
                                           String positiveText, String negativeText, boolean
                                                   cancelableTouchOut, final AlertDialogBtnClickListener
                                                   alertDialogBtnClickListener) {
            View view = LayoutInflater.from(ctx).inflate(R.layout.custom_dialog_layout, null);
            //ImageView mIcon = view.findViewById(R.id.icon);
            TextView mTitle = view.findViewById(R.id.title);
            TextView mMessage = view.findViewById(R.id.message);
            positiveButton = view.findViewById(R.id.positiveButton);
            Button negativeButton = view.findViewById(R.id.negativeButton);
           // mIcon.setImageResource(iconRes);
            mTitle.setText(title);
            mMessage.setText(msg);
            TimeCount timeCount=new TimeCount(30000,1000);
            timeCount.start();
            //positiveButton.setText(positiveText);
            negativeButton.setText(negativeText);
            positiveButton.setOnClickListener(v -> {
                alertDialogBtnClickListener.clickPositive();
                dialog.dismiss();
            });
            negativeButton.setOnClickListener(v -> {
                alertDialogBtnClickListener.clickNegative();
                dialog.dismiss();
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
            builder.setView(view);

            builder.setCancelable(true);   //返回键dismiss
            //创建对话框
            dialog = builder.create();
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);//去掉圆角背景背后的棱角
            dialog.setCanceledOnTouchOutside(cancelableTouchOut);   //失去焦点dismiss
            dialog.show();
        }

        public interface AlertDialogBtnClickListener {
            void clickPositive();

            void clickNegative();
        }
        class TimeCount extends CountDownTimer{
            public TimeCount(long totalTime,long internal){
                super(totalTime,internal);
            }
            @Override
            public void onTick(long millisUntilFinished) {
                positiveButton.setClickable(false);
                positiveButton.setBackgroundColor(Color.parseColor("#B6B6D8"));
                positiveButton.setText("确定("+millisUntilFinished/1000+")");
            }

            @Override
            public void onFinish() {
                positiveButton.setClickable(true);
                positiveButton.setText("确定");
            }
        }

}
