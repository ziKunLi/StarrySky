package com.example.newbies.starrysky.activity;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.newbies.starrysky.R;
import com.example.newbies.starrysky.adapter.SampleViewPagerAdapter;
import com.example.newbies.starrysky.view.BottomBarItem;
import com.example.newbies.starrysky.view.BottomBarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author NewBies
 */
public class MainActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /**
     * 页卡
     */
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    /**
     * 底部导航
     */
    @BindView(R.id.bottomBarLayout)
    BottomBarLayout bottomBarLayout;
    /**
     * 主页
     */
    @BindView(R.id.home)
    BottomBarItem home;
    /**
     * 聊天
     */
    @BindView(R.id.chart)
    BottomBarItem chart;
    /**
     * 联系人
     */
    @BindView(R.id.friend)
    BottomBarItem friend;
    /**
     * 页卡适配器
     */
    private SampleViewPagerAdapter adapter;
    private List<View> views;
    private View homePager;
    private View chartPager;
    private View friendPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定组件
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void initData() {
        views = new ArrayList<>();
    }

    @Override
    public void initView() {
        homePager = getLayoutInflater().inflate(R.layout.home_pager,null);
        chartPager = getLayoutInflater().inflate(R.layout.chart_pager,null);
        friendPager = getLayoutInflater().inflate(R.layout.friend_pager,null);
        views.add(chartPager);
        views.add(homePager);
        views.add(friendPager);
        adapter = new SampleViewPagerAdapter(views);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public void initListener() {
        bottomBarLayout.setViewPager(viewPager);
        bottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int position) {
                bottomBarItem.setStatus(true);
                System.err.println("position : " + position);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

}
