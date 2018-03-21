package com.example.newbies.starrysky.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.example.newbies.starrysky.LogUtil;
import com.example.newbies.starrysky.R;


/**
 * @author NewBies
 * @date 2018/1/10
 */
public abstract class BaseActivity extends AppCompatActivity implements View.OnClickListener,View.OnSystemUiVisibilityChangeListener {
    /**
     * 屏幕是竖屏还是横屏
     **/
    private ScreenRoateTyte screenRoateTyte = ScreenRoateTyte.PORTRAIT;
    /**
     * 日志输出标志
     **/
    protected final String TAG = "lifeCycle";

    /**
     * 唤醒屏幕
     */
    private PowerManager mPowerManager;
    private PowerManager.WakeLock mWakeLock;

    /**
     * 加载窗口
     */
    private PopupWindow loadingPop;

    /**
     * 是否设置沉浸状态栏
     */
    public void setStatusBar(boolean isSetStatusBar) {
        if(isSetStatusBar){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                // 透明状态栏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                // 透明导航栏
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
        }
    }

    /**
     * 初始化参数
     */
    public abstract void initData();

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 设置监听
     */
    public abstract void initListener();

    /**
     * 处理点击事件
     * @param v
     */
    @Override
    public abstract void onClick(View v);


    /**
     * 绑定控件
     * @param resId
     * @return
     */
    protected  <T extends View> T bindView(int resId) {
        return (T) super.findViewById(resId);
    }
    /**
     * 页面跳转
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this,clz));
    }

    /**
     * 携带数据的页面跳转
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * 含有Bundle通过Class打开编辑界面
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 注册监听电源键事件广播
     */
    public void wakeOnScreen() {
        final IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        registerReceiver(mBatInfoReceiver, filter);
    }

    /**
     * 利用监听电源键，唤醒屏幕
     */
    private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if(Intent.ACTION_SCREEN_OFF.equals(action)) {
                Log.d("dai", "电源键监听");
                // 点亮亮屏
                mWakeLock = mPowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, "Tag");
                mWakeLock.acquire();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //屏幕唤醒
        wakeOnScreen();
    }

    /**
     * 与onStop配对，表示Activity正在被启动，并且即将开始。
     * 但是这个时候要注意它与onResume的区别。两者都表示Activity可见，
     * 但是onStart时Activity还正在加载其他内容，正在向我们展示，用户还无法看到，即无法交互。
     */
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, this.getClass().getSimpleName() + " : onStart()");
    }

    /**
     * 该方法执行了，就说明现在这个活动已经在栈顶了
     * 与onPause配对，表示Activity已经创建完成，并且可以开始活动了，
     * 这个时候用户已经可以看到界面了，并且即将与用户交互（完成该周期之后便可以响应用户的交互事件了）。
     */
    @Override
    protected void onResume() {
        super.onResume();
        //将屏幕设置为竖屏
        if (screenRoateTyte.equals(ScreenRoateTyte.PORTRAIT)&&this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //将屏幕设置为横屏
        else if(screenRoateTyte.equals(ScreenRoateTyte.LANDSCAPE)&&this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
        LogUtil.v(TAG, this.getClass().getSimpleName() + " : onResume()");
    }

    /**
     * 与onResume配对，表示Activity正在暂停，正常情况下，onStop接着就会被调用。在特殊情况下，
     * 如果这个时候用户快速地再回到当前的Activity,那么onResume会被调用（极端情况）。
     * 一般来说，在这个生命周期状态下，可以做一些存储数据、停止动画的工作，但是不能太耗时，
     * 如果是由于启动新的Activity而唤醒的该状态，那会影响到新Activity的显示，原因是onPause必须执行完，新的Activity的onResume才会执行。
     */
    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.v(TAG, this.getClass().getSimpleName() + " : onPause()");
    }

    /**
     * 与onStart配对，表示Activity即将停止，可以做一些稍微重量级的回收工作，同样也不能太耗时（可以比onPause稍微好一点）。
     */
    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.v(TAG, this.getClass().getSimpleName() + " : onStop()");
    }

    /**
     * 表示Activity正在重新启动。一般情况下，在当前Activity从不可见重新变为可见的状态时onRestart就会被调用。
     * 这种情形一般是由于用户的行为所导致的，比如用户按下Home键切换到桌面或者打开了一个新的Activity
     * （这时当前Activity会暂停，也就是onPause和onStop被执行），接着用户有回到了这个Activity，就会出现这种情况。
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, this.getClass().getSimpleName() + " : onRestart()");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销电源监听广播
        unregisterReceiver(mBatInfoReceiver);
        LogUtil.v(TAG, this.getClass().getSimpleName() + " : onDestroy()");
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    /**
     * 简化Toast
     * @param msg
     */
    protected void showToast(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    /**
     * 将指定位置组件替换为碎片
     * @param resId 被替换的组件
     * @param fragment 碎片实例
     * @param useBackStack 是否启动返回栈
     */
    public void replaceFragment(int resId, Fragment fragment, boolean useBackStack){
        //获取FragmentManage
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(resId, fragment);
        //使用返回栈
        if(useBackStack){
            transaction.addToBackStack(null);
        }
        //提交事务
        transaction.commit();
    }

    /**
     * 是否允许屏幕旋转方向
     * @param screenRoateTyte
     */
    public void setScreenRoate(ScreenRoateTyte screenRoateTyte) {
        this.screenRoateTyte = screenRoateTyte;
    }

    /**
     * 隐藏虚拟按键和状态栏，并且全屏，一定要放在setContentView之前
     */
    public void setHideVirtualKey(Window window){
        //保持布局状态
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE|
                //布局位于状态栏下方
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION|
                //全屏
                View.SYSTEM_UI_FLAG_FULLSCREEN|
                //隐藏导航栏
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION|
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= 19) {
            uiOptions |= 0x00001000;
        } else {
            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
        }
        window.getDecorView().setSystemUiVisibility(uiOptions);
    }


    /**
     * 返回主页时若不是全屏，让其全屏
     * @param visibility
     */
    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        setHideVirtualKey(getWindow());
    }


    /**
     * 设置添加屏幕的背景透明度
     * @param bgAlpha
     * @auther 李自坤
     */
    public void setBackgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        //0.0-1.0
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
    }

    /**
     * 当在进行耗时操作，UI必须等待时，展示加载界面
     * @param contextView
     */
    public void showLoadingPop(View contextView){
        if(loadingPop == null){
            View popView = getLayoutInflater().inflate(R.layout.loading_pop,null);
            loadingPop = new PopupWindow(popView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT,false);
        }
        loadingPop.showAtLocation(contextView, Gravity.CENTER,0,0);
        setBackgroundAlpha(0.6f);
        loadingPop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                setBackgroundAlpha(1.0f);
            }
        });
    }

    /**
     * 耗时操作结束，UI不需等待
     */
    public void dismissLoadingPop(){
        loadingPop.dismiss();
    }

    /**
     * 得到屏幕宽度
     * @return
     */
    public int getScreenWidth(){
        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    /**
     * 得到屏幕的高
     * @return
     */
    public int getScreenHeight(){
        WindowManager windowManager = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }

    /**
     * 横屏或竖屏枚举
     */
    public enum ScreenRoateTyte{
        /**
         * 横屏
         */
        LANDSCAPE,
        /**
         * 竖屏
         */
        PORTRAIT
    }
}