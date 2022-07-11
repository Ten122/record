package com.example.recordapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.recordapp.adapter.RecordPageAdapter;
import com.example.recordapp.fragrecord.InFragment;
import com.example.recordapp.fragrecord.BaseFragment;
import com.example.recordapp.fragrecord.OutFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        tabLayout = findViewById(R.id.record_tabs);
        viewPager = findViewById(R.id.record_iv);

        //设置viewPage加载页面
        initPage();
    }

    private void initPage() {
        //初始化页面集合
        List<Fragment>fragmentList = new ArrayList<>();
        OutFragment outFrag = new OutFragment();  //支出
        InFragment inFrag = new InFragment();  //收入
        //放置页面
        fragmentList.add(outFrag);
        fragmentList.add(inFrag);
        //创建适配器
        RecordPageAdapter recordPageAdapter = new RecordPageAdapter(getSupportFragmentManager(),fragmentList);
        //设置适配器
        viewPager.setAdapter(recordPageAdapter);

        //将TabLayout和viewPager关联
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.record_src:
                finish();
                break;
        }
    }
}