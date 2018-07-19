package com.example.newbies.starrysky.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.newbies.starrysky.MessagePool;
import com.example.newbies.starrysky.R;
import com.example.newbies.starrysky.StaticDataPool;
import com.example.newbies.starrysky.nio.CharsetHelper;
import com.example.newbies.starrysky.nio.ClientHandler;
import com.example.newbies.starrysky.util.LogUtil;
import com.example.newbies.starrysky.util.StringUtil;
import com.example.newbies.starrysky.view.MaterialTextField;

import java.nio.CharBuffer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * @author NewBies
 * @date 2018/6/21
 */
public class RegistActivity extends  BaseActivity {

    @BindView(R.id.id)
    MaterialTextField id;
    @BindView(R.id.nickName)
    MaterialTextField nickName;
    @BindView(R.id.password)
    MaterialTextField password;
    @BindView(R.id.regist)
    Button regist;
    @BindView(R.id.returnLogin)
    TextView returnLogin;

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(StaticDataPool.REGIST_REPLY_MESSAGE)){
                int status = intent.getIntExtra("status",0);
                if(status == 0){
                    showToast("注册失败！");
                }
                else {
                    showToast("注册成功！");
                    //得到昵称
                    StaticDataPool.nickName = nickName.getEditText().getText().toString();
                    startActivity(MainActivity.class);

                }
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.regist_activity);
        //绑定组件
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
        initBroadcastReceiver();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initListener() {
        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userId = id.getEditText().getText().toString();
                final String nickNameString = nickName.getEditText().getText().toString();
                final String passwordString = password.getEditText().getText().toString();
                if(!StringUtil.isRightId(userId)){
                    showToast("请输入正确的手机号");
                }
                else if(StringUtil.isNull(nickNameString)){
                    showToast("请输入昵称");
                }
                else if(StringUtil.isNull(passwordString)){
                    showToast("请输入密码");
                }
                else {
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           while (!StaticDataPool.isLogin) {
                               StaticDataPool.client.connect("192.168.23.1", 6666);
                               StaticDataPool.userId = userId;
                               //上线后，将会一直死循环监听服务器发来的消息，所以需要开启线程
                               StaticDataPool.client.online(new ClientHandler(), MessagePool.registMessage(userId, nickNameString, passwordString));
                           }
                       }
                   }).start();
                }
            }
        });

        returnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(LoginActivity.class);
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    private void initBroadcastReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(StaticDataPool.REGIST_REPLY_MESSAGE);
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(RegistActivity.this);
        manager.registerReceiver(receiver,intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager manager = LocalBroadcastManager.getInstance(RegistActivity.this);
        manager.unregisterReceiver(receiver);
    }
}
