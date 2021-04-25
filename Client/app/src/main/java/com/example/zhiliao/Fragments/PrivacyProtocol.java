package com.example.zhiliao.Fragments;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.zhiliao.R;

public class PrivacyProtocol extends Fragment {
    private ImageView back;
    private FragmentManager manager;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.privacy_protocol,null);
        TextView textView=view.findViewById(R.id.head);
        textView.setText("ZhiLiao");
        TextView textView1=view.findViewById(R.id.privacy_pro);
        textView1.setText(Html.fromHtml(getResources().getString(R.string.privacy)));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        back=getActivity().findViewById(R.id.back);
        manager=getFragmentManager();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.popBackStack();
            }
        });
    }
}
