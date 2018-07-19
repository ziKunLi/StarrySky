package com.example.newbies.starrysky.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.newbies.starrysky.MessagePool;
import com.example.newbies.starrysky.R;
import com.example.newbies.starrysky.StaticDataPool;
import com.example.newbies.starrysky.adapter.ChartListAdapter;
import com.example.newbies.starrysky.adapter.ExpandableListAdapter;
import com.example.newbies.starrysky.adapter.RecyclerAdaptListener;
import com.example.newbies.starrysky.adapter.SampleViewPagerAdapter;
import com.example.newbies.starrysky.entity.FriendSampleInfo;
import com.example.newbies.starrysky.util.JsonUtil;
import com.example.newbies.starrysky.util.LogUtil;
import com.example.newbies.starrysky.util.UIUtils;
import com.example.newbies.starrysky.view.BottomBarItem;
import com.example.newbies.starrysky.view.BottomBarLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * @author NewBies
 */
public class MainActivity extends BaseActivity {

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
     * 添加好友或群
     */
    @BindView(R.id.more)
    TextView more;
    /**
     * 输入ID的输入框
     */
    @BindView(R.id.inputId)
    EditText inputId;
    /**
     * 确认添加按钮
     */
    @BindView(R.id.sureAdd)
    TextView sureAdd;
    @BindView(R.id.friendRequest)
    CardView friendRequest;
    @BindView(R.id.userName)
    TextView userName;
    /**
     * 拒绝好友请求按钮
     */
    @BindView(R.id.refuse)
    Button refuse;
    /**
     * 同意好友请求按钮
     */
    @BindView(R.id.agree)
    Button agree;
    /**
     * 页卡适配器
     */
    private SampleViewPagerAdapter adapter;
    private List<View> views;
    private View homePager;
    /**
     * 聊天界面
     */
    private View chartPager;
    private RecyclerView recyclerView;
    private ChartListAdapter chartListAdapter;
    private List<FriendSampleInfo> infos;

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

    /**
     * 用于添加好友或群
     */
    private PopupWindow morePop;
    /**
     * 添加好友按钮
     */
    private TextView addFriend;
    /**
     * 添加群按钮
     */
    private TextView addGroup;
    /**
     * 用于判断是添加好友还是添加群
     */
    private int status;

    private List<String> groupNames;

    private List<List<FriendSampleInfo>> info;

    /**
     * 接收者
     */
    private List<String> receivers = new ArrayList<>();
    /**
     * 跳转活动时携带数据
     */
    private Bundle bundle = new Bundle();

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            switch (action){
                //普通信息
                case StaticDataPool.GENERAL_MESSAGE:
                    showToast(intent.getStringExtra("sender") + "说：" + intent.getStringExtra("message"));
                    break;
                //好友请求信息
                case StaticDataPool.FRIEND_REQUEST_MESSAGE:
                    showToast("您有好友请求哦");
                    showFriendRequestLayout(intent.getStringExtra("userId"),intent.getStringExtra("nickName"));
                    break;
                //添加回复信息
                case StaticDataPool.ADD_REPLY_MESSAGE:
                    showReplyMessage(
                            intent.getStringExtra("addType"),
                            intent.getStringExtra("status"),
                            intent.getStringExtra("id"));
                    break;
                case StaticDataPool.ADD_FRIEND_REPLY_MESSAGE:
                    showFriendReplyMessage(intent.getStringExtra("userId"),intent.getStringExtra("nickName"),intent.getStringExtra("status"));
                    break;
                //初始化好友列表
                case StaticDataPool.INIT_FRIEND_INFO:
                    initFriendAndGroup(intent.getStringExtra("friends"));
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定组件
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
        initBroadcastReceiver();
        //发送请求数据的信息
        StaticDataPool.client.sendMessage(MessagePool.initMessage(StaticDataPool.userId));
        StaticDataPool.isLogin = true;
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
        group1.add(new FriendSampleInfo(null,"一",null));
        group1.add(new FriendSampleInfo(null,"二",null));
        group1.add(new FriendSampleInfo(null,"三",null));
        List<FriendSampleInfo> group2 = new ArrayList<>();
        group2.add(new FriendSampleInfo(null,"一",null));
        group2.add(new FriendSampleInfo(null,"二",null));
        group2.add(new FriendSampleInfo(null,"三",null));
        List<FriendSampleInfo> group3 = new ArrayList<>();
        group3.add(new FriendSampleInfo(null,"一",null));
        group3.add(new FriendSampleInfo(null,"二",null));
        group3.add(new FriendSampleInfo(null,"三",null));
        List<FriendSampleInfo> group4 = new ArrayList<>();
        group4.add(new FriendSampleInfo(null,"一",null));
        group4.add(new FriendSampleInfo(null,"二",null));
        group4.add(new FriendSampleInfo(null,"三",null));
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

        initChartPager();
    }

    /**
     * 初始化聊天界面
     */
    private void initChartPager(){
        recyclerView = chartPager.findViewById(R.id.recyclerView);
        infos = new ArrayList<>();
        chartListAdapter = new ChartListAdapter(infos,R.layout.chart_item);
        chartListAdapter.setItemListener(new RecyclerAdaptListener() {
            @Override
            public void onItemClick(View view, int positionX, int positionY) {
                bundle.clear();
                bundle.putString("name",infos.get(positionX).getName());
                bundle.putStringArrayList("ids", (ArrayList<String>) infos.get(positionX).getIds());
                startActivity(ChartActivity.class,bundle);
            }

            @Override
            public void onItemLongClick(View view, int positionX, int positionY) {

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerView.setAdapter(chartListAdapter);

    }

    @Override
    public void initListener() {
        bottomBarLayout.setViewPager(viewPager);
        bottomBarLayout.setOnItemSelectedListener(new BottomBarLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(BottomBarItem bottomBarItem, int position) {
                bottomBarItem.setStatus(true);
                receivers.clear();
                StaticDataPool.client.sendMessage(MessagePool.generalMessage(StaticDataPool.userId,receivers,"你好"));
            }
        });
        more.setOnClickListener(this);
        sureAdd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.more:
                ViewCompat.animate(more)
                        .rotation(270)
                        .setDuration(400)
                        .start();
                showMorePop();
                break;
            case R.id.sureAdd:
                //添加好友或群
                if(status == 0){
                    StaticDataPool.client.sendMessage(MessagePool.addFriendMessage(StaticDataPool.userId,inputId.getText().toString()));
                }
                else {
                    StaticDataPool.client.sendMessage(MessagePool.addGroupMessage(StaticDataPool.userId,inputId.getText().toString()));
                }
                more.setVisibility(View.VISIBLE);
                inputId.setVisibility(View.GONE);
                inputId.setText("");
                sureAdd.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    /**
     * 初始化广播接收器
     */
    private void initBroadcastReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StaticDataPool.GENERAL_MESSAGE);
        intentFilter.addAction(StaticDataPool.INIT_FRIEND_AND_GROUP_MESSAGE);
        intentFilter.addAction(StaticDataPool.FRIEND_REQUEST_MESSAGE);
        intentFilter.addAction(StaticDataPool.ADD_REPLY_MESSAGE);
        intentFilter.addAction(StaticDataPool.ADD_FRIEND_REPLY_MESSAGE);
        intentFilter.addAction(StaticDataPool.INIT_FRIEND_INFO);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(messageReceiver,intentFilter);
    }

    private void showMorePop(){
        if(morePop == null){
            View popView = getLayoutInflater().inflate(R.layout.main_more_pop,null);
            morePop = new PopupWindow(popView, WindowManager.LayoutParams.WRAP_CONTENT,WindowManager.LayoutParams.WRAP_CONTENT,true);
            morePop.setBackgroundDrawable(new ColorDrawable());
            morePop.setOutsideTouchable(true);

            addFriend = popView.findViewById(R.id.addFriend);
            addGroup = popView.findViewById(R.id.addGroup);

            addFriend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAdd();
                    status = 0;
                }
            });
            addGroup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAdd();
                    status = 1;
                }
            });
            morePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ViewCompat.animate(more)
                            .rotation(-270)
                            .setDuration(400)
                            .start();
                    setBackgroundAlpha(1);
                }
            });
        }
        setBackgroundAlpha(0.7f);
        morePop.showAsDropDown(more,0, 20);
    }

    /**
     * 展示添加界面
     */
    private void showAdd(){
        inputId.setAlpha(0);
        more.setVisibility(View.GONE);
        inputId.setVisibility(View.VISIBLE);
        sureAdd.setVisibility(View.VISIBLE);
        ViewCompat.animate(inputId)
                .alpha(1)
                .setDuration(700)
                .start();
        morePop.dismiss();
    }

    /**
     * 初始化好友列表
     */
    private void initFriendAndGroup(String friends){
        List<HashMap> list = JsonUtil.jsonToArray(friends,HashMap.class);
        for(int i = 0 ; i < list.size(); i++){
            List<String> ids = new ArrayList<>();
            ids.add((String) list.get(i).get("friendId"));
            infos.add(new FriendSampleInfo(null,(String)list.get(i).get("friendNode"),ids));
        }
    }

    /**
     * 好友请求界面弹出
     * @param friendId
     * @param friendName
     */
    private void showFriendRequestLayout(final String friendId, final String friendName){
        ViewCompat.animate(friendRequest)
                .translationY(UIUtils.dip2Px(MainActivity.this,-160))
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        friendRequest.setVisibility(View.VISIBLE);
                        bottomBarLayout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(View view) {

                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                })
                .setDuration(500)
                .start();
        userName.setText(friendName + "请求加你为好友，是否同意？");
        //拒绝添加好友
        refuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticDataPool.client.sendMessage(MessagePool.friendRequestReplyMessage(StaticDataPool.userId,friendId,StaticDataPool.nickName,2,friendName));
                hideFriendRequestLayout();
            }
        });
        //同意添加好友
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticDataPool.client.sendMessage(MessagePool.friendRequestReplyMessage(StaticDataPool.userId,friendId,StaticDataPool.nickName,1,friendName));
                List<String> ids = new ArrayList<>();
                ids.add(friendId);
                infos.add(new FriendSampleInfo(null,friendName,ids));
                chartListAdapter.notifyDataSetChanged();
                showToast("添加成功");
                hideFriendRequestLayout();
            }
        });
    }

    private void hideFriendRequestLayout(){

        ViewCompat.animate(friendRequest)
                .translationY(UIUtils.dip2Px(MainActivity.this,160))
                .setDuration(500)
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {
                        bottomBarLayout.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        friendRequest.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                })
                .start();
    }

    /**
     * 展示添加群或好友的回复信息
     * @param status
     * @param id 好友或群ID
     */
    private void showReplyMessage(String addType,String status, String id){
        if(addType.equals("0")){
            if(status.equals("0")){
                showToast(id + "不存在");
            }
            else if(status.equals("1")){
                showToast("您已成功加入群");
                //更改界面
                StaticDataPool.client.sendMessage(MessagePool.initMessage(StaticDataPool.userId));
            }
            else{
                showToast("服务器开小差咯!");
            }
        }
        else{
            if(status.equals("0")){
                showToast(id + "用户不存在");
            }

        }
    }

    /**
     * 添加好友的回复消息
     * @param friendId
     * @param friendName
     * @param status
     */
    private void showFriendReplyMessage(String friendId,String friendName,String status){
        if(status.equals("1")){
            showToast(friendName + "接受了您的好友申请，你们现在已经是好友了哦~");
            //更新UI
        }
        else if(status.equals("2")){
            showToast(friendName + "拒绝了您的好友申请");
        }
    }
}
