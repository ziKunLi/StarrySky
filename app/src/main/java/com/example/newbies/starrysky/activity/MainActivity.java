package com.example.newbies.starrysky.activity;


import android.os.Bundle;
import android.view.View;


import com.example.newbies.starrysky.R;

import butterknife.ButterKnife;
/**
 * @author NewBies
 */
public class MainActivity extends BaseActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar(true);
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

}
