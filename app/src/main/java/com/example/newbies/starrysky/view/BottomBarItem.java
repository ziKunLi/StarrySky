package com.example.newbies.starrysky.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.newbies.starrysky.LogUtil;
import com.example.newbies.starrysky.R;

/**
 *
 * @author NewBies
 * @date 2018/4/6
 */
public class BottomBarItem extends LinearLayout{

    private Context context;
    /**
     * 图标
     */
    private int normalIcon;
    /**
     * 选中时的图标
     */
    private int selectedIcon;
    /**
     * 底部文字描述
     */
    private String text;
    /**
     * 文字大小
     */
    private int textSize;
    /**
     * 未选中时文字颜色
     */
    private int normalTextColor;
    /**
     * 选中时的文字颜色
     */
    private int selectedTextColor;
    /**
     * 是否启用触摸背景效果
     */
    private boolean useTouchBg;
    /**
     * 触摸背景
     */
    private Drawable touchDrawable;
    /**
     * 图标宽度
     */
    private int iconWidth;
    /**
     * 图标高度
     */
    private int iconHeight;
    /**
     * 未读数字体大小
     */
    private int unReadTextSize;
    /**
     * 提示信息字体大小
     */
    private int tipMessageTextSize;
    /**
     * 最大未读数
     */
    private int unReadMaxText;
    /**
     * 小红点的大小
     */
    private int pointSize;
    /**
     * 提示红点
     */
    private TextView point;
    /**
     * 提示信息
     */
    private TextView message;
    /**
     * 提示数字
     */
    private TextView tip;
    /**
     * 文字
     */
    private TextView textView;
    /**
     * 图标
     */
    private ImageView icon;

    public BottomBarItem(Context context) {
        super(context);
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        getParameter(context,attrs);
        LogUtil.v(normalIcon + "");
        chickValues();
        init();
    }

    public BottomBarItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getParameter(context,attrs);
        LogUtil.v(normalIcon + "");
        chickValues();
        init();
    }

    private void getParameter(Context context,AttributeSet attrs){
        this.context = context;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BottomBarItem);
        normalIcon = typedArray.getResourceId(R.styleable.BottomBarItem_normalIcon,-1);
        selectedIcon = typedArray.getResourceId(R.styleable.BottomBarItem_selectedIcon,-1);
        text = typedArray.getString(R.styleable.BottomBarItem_text);
        //默认为12sp
        textSize = typedArray.getDimensionPixelSize(R.styleable.BottomBarItem_textSize,UIUtils.sp2px(context,12));
        normalTextColor = typedArray.getColor(R.styleable.BottomBarItem_normalTextColor, Color.BLACK);
        selectedTextColor = typedArray.getColor(R.styleable.BottomBarItem_selectedTextColor,Color.RED);
        useTouchBg = typedArray.getBoolean(R.styleable.BottomBarItem_useTouchBg,false);
        touchDrawable = typedArray.getDrawable(R.styleable.BottomBarItem_touchDrawable);
        iconWidth = typedArray.getDimensionPixelSize(R.styleable.BottomBarItem_iconWidth,0);
        iconHeight = typedArray.getDimensionPixelSize(R.styleable.BottomBarItem_iconHeight,0);
        unReadTextSize = typedArray.getDimensionPixelSize(R.styleable.BottomBarItem_unReadTextSize,UIUtils.sp2px(context,10));
        tipMessageTextSize = typedArray.getDimensionPixelSize(R.styleable.BottomBarItem_tipMessageTextSize,UIUtils.sp2px(context,10));
        unReadMaxText = typedArray.getInt(R.styleable.BottomBarItem_unReadMaxText,99);
        pointSize = typedArray.getDimensionPixelSize(R.styleable.BottomBarItem_pointSize,0);
        typedArray.recycle();
    }
    /**
     * 检查设置的值是否正确
     */
    private void chickValues(){
        if(useTouchBg&&touchDrawable == null){
            throw new IllegalStateException("开启了触摸背景，但并没有设置触摸背景");
        }
        if(normalIcon == -1){
            throw new IllegalStateException("请设置默认图标");
        }
        if(selectedIcon == -1){
            throw new IllegalStateException("请设置选中后的图标");
        }
    }

    /**
     * 初始化
     */
    private void init(){
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        View view = View.inflate(context,R.layout.bottom_bar_item,null);
        point = view.findViewById(R.id.point);
        message = view.findViewById(R.id.message);
        tip = view.findViewById(R.id.tip);
        icon = view.findViewById(R.id.icon);
        textView = view.findViewById(R.id.text);

        icon.setImageResource(normalIcon);

        if(iconWidth != 0&&iconHeight != 0){
            //如果有设置图标的宽度和高度，则设置ImageView的宽高
            RelativeLayout.LayoutParams iconLayoutParams = (RelativeLayout.LayoutParams) icon.getLayoutParams();
            iconLayoutParams.width = iconWidth;
            iconLayoutParams.height = iconHeight;
            icon.setLayoutParams(iconLayoutParams);
        }

        textView.setText(text);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
        textView.setTextColor(normalTextColor);

        tip.setTextSize(TypedValue.COMPLEX_UNIT_PX,tipMessageTextSize);
        message.setTextSize(TypedValue.COMPLEX_UNIT_PX,tipMessageTextSize);

        RelativeLayout.LayoutParams pointLayoutParams = (RelativeLayout.LayoutParams) point.getLayoutParams();
        pointLayoutParams.width = pointSize;
        pointLayoutParams.height = pointSize;
        point.setLayoutParams(pointLayoutParams);

        if (useTouchBg) {
            //如果有开启触摸背景
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(touchDrawable);
            }
        }
        addView(view);
    }

    /**
     * 设置选中或者未选中的状态
     * @param isSelected
     */
    public void setStatus(boolean isSelected) {
        icon.setImageResource(isSelected ? selectedIcon : normalIcon);
        textView.setTextColor(isSelected ? selectedTextColor : normalTextColor);
    }

    /**
     * 设置提示数量
     * @param num
     */
    public void setUnReadNum(int num){
        if(tip.getVisibility() == GONE){
            tip.setVisibility(VISIBLE);
        }
        message.setVisibility(GONE);
        point.setVisibility(GONE);
        if(num < unReadMaxText){
            tip.setText(num + "");
        }
        else{
            tip.setText(unReadMaxText + "+");
        }
    }

    /**
     * 设置提示小红点
     */
    public void setPoint(){
        if(point.getVisibility() == GONE){
            point.setVisibility(VISIBLE);
        }
        message.setVisibility(GONE);
        tip.setVisibility(GONE);
    }

    /**
     * 设置提示信息
     * @param messageText
     */
    public void setMessageText(String messageText){
        if(message.getVisibility() == GONE){
            message.setVisibility(VISIBLE);
        }
        point.setVisibility(GONE);
        tip.setVisibility(GONE);
        message.setText(messageText);
    }

    /**
     * 设置不显示左上角信息
     */
    public void setNoon(){
        point.setVisibility(GONE);
        tip.setVisibility(GONE);
        message.setVisibility(GONE);
    }
}
