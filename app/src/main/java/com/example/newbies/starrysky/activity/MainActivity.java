package com.example.newbies.starrysky.activity;


import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.example.newbies.starrysky.MessagePool;
import com.example.newbies.starrysky.R;
import com.example.newbies.starrysky.StaticDataPool;
import com.example.newbies.starrysky.adapter.ExpandableListAdapter;
import com.example.newbies.starrysky.adapter.SampleViewPagerAdapter;
import com.example.newbies.starrysky.entity.FriendSampleInfo;
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

    /**
     * 搜索栏
     */
    private SearchView searchBar;
    /**
     * 添加新朋友
     */
    private Button newFriend;
    /**
     * 群聊
     */
    private Button groupChart;
    /**
     * 讨论组
     */
    private Button discussionGroup;
    /**
     * 好友分组
     */
    private ExpandableListView friends;
    private ExpandableListAdapter expandableListAdapter;
    private List<String> groupNames;
    private List<List<FriendSampleInfo>> info;

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
        groupNames = new ArrayList<>();
        info = new ArrayList<>();
        groupNames.add("test1");
        groupNames.add("test2");
        groupNames.add("test3");
        groupNames.add("test4");
        List<FriendSampleInfo> group1 = new ArrayList<>();
        group1.add(new FriendSampleInfo(null,"一"));
        group1.add(new FriendSampleInfo(null,"二"));
        group1.add(new FriendSampleInfo(null,"三"));
        List<FriendSampleInfo> group2 = new ArrayList<>();
        group2.add(new FriendSampleInfo(null,"一"));
        group2.add(new FriendSampleInfo(null,"二"));
        group2.add(new FriendSampleInfo(null,"三"));
        List<FriendSampleInfo> group3 = new ArrayList<>();
        group3.add(new FriendSampleInfo(null,"一"));
        group3.add(new FriendSampleInfo(null,"二"));
        group3.add(new FriendSampleInfo(null,"三"));
        List<FriendSampleInfo> group4 = new ArrayList<>();
        group4.add(new FriendSampleInfo(null,"一"));
        group4.add(new FriendSampleInfo(null,"二"));
        group4.add(new FriendSampleInfo(null,"三"));
        info.add(group1);
        info.add(group2);
        info.add(group3);
        info.add(group4);
        expandableListAdapter = new ExpandableListAdapter(this,groupNames,info);
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

        searchBar = friendPager.findViewById(R.id.searchBar);
        newFriend = friendPager.findViewById(R.id.newFriend);
        groupChart = friendPager.findViewById(R.id.groupChart);
        discussionGroup = friendPager.findViewById(R.id.discussionGroup);
        friends = friendPager.findViewById(R.id.friends);

        friends.setAdapter(expandableListAdapter);
        friends.setDivider(null);
    }

    @Override
    public void initListener() {
        bottomBarLayout.setViewPager(viewPager);
        bottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int position) {
                bottomBarItem.setStatus(true);
                StaticDataPool.client.sendMessage(MessagePool.login("1","2"));
            }
        });
    }

    @Override
    public void onClick(View v) {

    }


}
