package com.example.newbies.starrysky.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.newbies.starrysky.MessagePool;
import com.example.newbies.starrysky.R;
import com.example.newbies.starrysky.StaticDataPool;
import com.example.newbies.starrysky.adapter.MessageListAdapter;
import com.example.newbies.starrysky.entity.Message;
import com.example.newbies.starrysky.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author NewBies
 * @date 2018/6/18
 */

public class ChartActivity extends BaseActivity{

    @BindView(R.id.messages)
    RecyclerView messages;
    @BindView(R.id.inputMessage)
    EditText inputMessage;
    @BindView(R.id.sendMessage)
    Button sendMessage;
    /**
     * 返回到主界面
     */
    @BindView(R.id.returnMain)
    TextView returnMain;
    @BindView(R.id.friendName)
    TextView friendName;

    private List<String> ids;
    /**
     * 展示信息的adapter
     */
    private MessageListAdapter messageListAdapter;
    /**
     * 保存发送的消息
     */
    private List<Message> messageList;

    private BroadcastReceiver messageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(StaticDataPool.GENERAL_MESSAGE)){
                Message message = new Message();
                message.setHeadImage(null);
                message.setMessage(intent.getStringExtra("message"));
                message.setSenderId(intent.getStringExtra("senderId"));
                message.setSenderName(intent.getStringExtra("senderName"));
                messageList.add(message);
                messageListAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(true);
        //防止软键盘弹出遮挡输入框
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.chart_activity);
        //绑定组件
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
        initBroadcastReceiver();
    }

    @Override
    public void initData() {
        ids = getIntent().getStringArrayListExtra("ids");
        messageList = new ArrayList<>();
        messageListAdapter = new MessageListAdapter(messageList,R.layout.message_item);
    }

    @Override
    public void initView() {
        friendName.setText(getIntent().getStringExtra("name"));
        messages.setLayoutManager(new LinearLayoutManager(ChartActivity.this));
        messages.setAdapter(messageListAdapter);
    }

    @Override
    public void initListener() {
        returnMain.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.returnMain:
                startActivity(MainActivity.class);
                break;
            case R.id.sendMessage:
                if(!StringUtil.isNull(inputMessage.getText().toString())){
                    Message message = new Message();
                    message.setSenderName(StaticDataPool.nickName);
                    message.setSenderId(StaticDataPool.userId);
                    message.setMessage(inputMessage.getText().toString());
                    messageList.add(message);
                    //通知界面改变
                    messageListAdapter.notifyDataSetChanged();
                    //发送普通消息
                    StaticDataPool.client.sendMessage(MessagePool.generalMessage(StaticDataPool.userId,ids,inputMessage.getText().toString()));
                }
                break;
            default:
                break;
        }
    }

    private void initBroadcastReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StaticDataPool.GENERAL_MESSAGE);
        LocalBroadcastManager.getInstance(ChartActivity.this).registerReceiver(messageReceiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(ChartActivity.this).unregisterReceiver(messageReceiver);
    }
}
