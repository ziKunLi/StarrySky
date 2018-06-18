package com.example.newbies.starrysky.activity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.newbies.starrysky.MessagePool;
import com.example.newbies.starrysky.R;
import com.example.newbies.starrysky.StaticDataPool;
import com.example.newbies.starrysky.nio.Client;
import com.example.newbies.starrysky.nio.ClientHandler;
import com.example.newbies.starrysky.util.LogUtil;
import com.example.newbies.starrysky.view.MaterialTextField;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 *
 * @author NewBies
 * @date 2018/3/22
 */
public class LoginActivity extends BaseActivity {
    /**
     * 背景视频
     */
    @BindView(R.id.bgVideo)
    VideoView bgVideo;
    /**
     * 账号
     */
    @BindView(R.id.id)
    MaterialTextField id;
    /**
     * 密码
     */
    @BindView(R.id.password)
    MaterialTextField password;
    /**
     * 头像
     */
    @BindView(R.id.head)
    ImageView head;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(true);
        setContentView(R.layout.login_activity);
        //绑定组件
        ButterKnife.bind(this);
        initData();
        initView();
        initListener();
    }

    @Override
    public void onResume(){
        super.onResume();
        //设置播放资源路径
        bgVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.a1e4779f91_495328_fhd));
        //开始播放
        bgVideo.start();
        //设置视频播放完后的事件监听，我这里是当播放完后再次播放
        bgVideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                bgVideo.start();
            }
        });
    }

    @Override
    public void initData() {
        List<String> receivers = new ArrayList<>();
        receivers.add("1");
        receivers.add("2");
        receivers.add("3");
        LogUtil.v(MessagePool.generalMessage("111",receivers,"你好"));
    }

    @Override
    public void initView() {


    }

    @Override
    public void initListener() {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 登录按钮
     */
    @OnClick(R.id.login)
    public void login(){
        showToast("登录");
        final String idString = id.getEditText().getText().toString();
        final String passwordString = password.getEditText().getText().toString();
        if(idString == null||idString.equals("")||passwordString == null||passwordString.equals("")){
            showToast("请输入账号或密码");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                StaticDataPool.client.connect("192.168.23.1", 6666);
                //上线后，将会一直死循环监听服务器发来的消息，所以需要开启线程
                StaticDataPool.client.online(new ClientHandler(),idString,passwordString);
            }
        }).start();
        startActivity(MainActivity.class);
        finish();
    }

    /**
     * 忘记密码
     */
    @OnClick(R.id.forget)
    public void forget(){
        showToast("忘记密码");
    }
    /**
     * 注册
     */
    @OnClick(R.id.registered)
    public void registered(){
        showToast("注册");
    }
}
