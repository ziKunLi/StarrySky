package com.example.newbies.starrysky.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v4.view.ViewPropertyAnimatorUpdateListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newbies.starrysky.LogUtil;
import com.example.newbies.starrysky.R;

/**
 *
 * @author NewBies
 * @date 2018/3/21
 */

public class MaterialTextField extends FrameLayout {
    protected InputMethodManager inputMethodManager;

    protected TextView label;
    protected View card;
    protected ImageView image;
    protected EditText editText;
    protected ViewGroup editTextLayout;

    protected int labelTopMargin = -1;
    protected boolean expanded = false;

    protected int ANIMATION_DURATION = -1;
    protected boolean OPEN_KEYBOARD_ON_FOCUS = true;
    /**
     * 文字颜色
     */
    protected int labelColor = -1;
    protected int imageDrawableId = -1;
    protected int cardCollapsedHeight = -1;
    protected boolean hasFocus = false;
    protected int backgroundColor = -1;

    protected float reducedScale = 0.2f;

    public MaterialTextField(Context context) {
        super(context);
        init();
    }

    public MaterialTextField(Context context, AttributeSet attrs) {
        super(context, attrs);
        handleAttributes(context, attrs);
        init();
    }

    public MaterialTextField(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        handleAttributes(context, attrs);
        init();
    }

    protected void init() {
        inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void toggle() {
        if (expanded) {
            reduce();
        } else {
            expand();
        }
    }

    public void reduce() {
        if (expanded) {
            final int heightInitial = getContext().getResources().getDimensionPixelOffset(R.dimen.mtf_cardHeight_final);

            ViewCompat.animate(label)
                    .alpha(1)
                    .scaleX(1)
                    .scaleY(1)
                    .translationY(0)
                    .setDuration(ANIMATION_DURATION);

            ViewCompat.animate(image)
                    .alpha(0)
                    .scaleX(0.4f)
                    .scaleY(0.4f)
                    .setDuration(ANIMATION_DURATION);

            //添加动画只是为了响应事件，设置可见性的，如果不可见就不能聚焦
            ViewCompat.animate(editText)
                    .alpha(1f)
                    //在运行属性动画的底层Animator中设置更新事件的侦听器。
                    .setUpdateListener(new ViewPropertyAnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(View view) {
                            //percentage
                            float value = ViewCompat.getAlpha(view);
                            LogUtil.v(" == " + value + " == " + (view == editText));
                            //当输入框透明度变化时，整个输入框的高度随之变化，然而并没有，因为value没变过，这个只是单纯的设置高度
                            card.getLayoutParams().height = (int) (value * (heightInitial - cardCollapsedHeight) + cardCollapsedHeight);
                            card.requestLayout();
                        }
                    })
                    .setDuration(ANIMATION_DURATION)
                    .setListener(new ViewPropertyAnimatorListener() {
                        @Override
                        public void onAnimationStart(View view) {
                            if (expanded) {
                                LogUtil.v("ttttt");
                                editText.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onAnimationEnd(View view) {
                            if (!expanded) {
                                LogUtil.v("ssss");
                                editText.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onAnimationCancel(View view) { }
                    });

            ViewCompat.animate(card)
                    .scaleY(reducedScale)
                    .setDuration(ANIMATION_DURATION);

            //关闭软键盘，取消聚焦
            if (editText.hasFocus()) {
                inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                editText.clearFocus();
            }

            expanded = false;
        }
    }

    public void expand() {
        if (!expanded) {
            ViewCompat.animate(editText)
                    .alpha(1f)
                    .setDuration(ANIMATION_DURATION);

            ViewCompat.animate(card)
                    .scaleY(1f)
                    .setDuration(ANIMATION_DURATION);

            //标签变为半透明，大小变小，然后向上移动到顶部
            ViewCompat.animate(label)
                    .alpha(0.4f)
                    .scaleX(0.7f)
                    .scaleY(0.7f)
                    .translationY(-labelTopMargin)
                    .setDuration(ANIMATION_DURATION);

            ViewCompat.animate(image)
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(ANIMATION_DURATION);

            //聚焦
            if (editText != null) {
                editText.requestFocus();
            }

            //打开软键盘
            if (OPEN_KEYBOARD_ON_FOCUS) {
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            }

            expanded = true;
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public View getCard() {
        return card;
    }

    public TextView getLabel() {
        return label;
    }

    public ImageView getImage() {
        return image;
    }

    public EditText getEditText() {
        return editText;
    }

    public ViewGroup getEditTextLayout() {
        return editTextLayout;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setHasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;

        if (hasFocus) {
            expand();
            editText.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //聚焦？经测试，加上这句代码，就能防止软键盘弹出时遮挡输入框了（科学）
                    editText.requestFocusFromTouch();
                    //唤起软键盘，只有当界面绘制完毕后，调用该方法才会有效果，所以这里做一个延时操作
                    inputMethodManager.showSoftInput(editText, 0);
                }
            }, 300);
        } else {
            reduce();
        }
    }

    protected void handleAttributes(Context context, AttributeSet attrs) {
        try {
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaterialTextField);

            ANIMATION_DURATION = styledAttrs.getInteger(R.styleable.MaterialTextField_mtf_animationDuration, 400);

            OPEN_KEYBOARD_ON_FOCUS = styledAttrs.getBoolean(R.styleable.MaterialTextField_mtf_openKeyboardOnFocus, false);

            labelColor = styledAttrs.getColor(R.styleable.MaterialTextField_mtf_labelColor, -1);

            imageDrawableId = styledAttrs.getResourceId(R.styleable.MaterialTextField_mtf_image, -1);

            cardCollapsedHeight = styledAttrs.getDimensionPixelOffset(R.styleable.MaterialTextField_mtf_cardCollapsedHeight, context.getResources().getDimensionPixelOffset(R.dimen.mtf_cardHeight_initial));

            hasFocus = styledAttrs.getBoolean(R.styleable.MaterialTextField_mtf_hasFocus, false);

            backgroundColor = styledAttrs.getColor(R.styleable.MaterialTextField_mtf_backgroundColor, -1);

            styledAttrs.recycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected EditText findEditTextChild() {
        if (getChildCount() > 0 && getChildAt(0) instanceof EditText) {
            return (EditText) getChildAt(0);
        }
        return null;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //获取输入框
        editText = findEditTextChild();
        if (editText == null) {
            return;
        }
        //添加布局文件
        addView(LayoutInflater.from(getContext()).inflate(R.layout.mtf_layout, this, false));

        editTextLayout = (ViewGroup) findViewById(R.id.mtf_editTextLayout);
        //将输入框从该ViewGroup中删除
        removeView(editText);
        //然后将其添加到布局文件相应的位置上
        editTextLayout.addView(editText);

        label = (TextView) findViewById(R.id.mtf_label);
        ViewCompat.setPivotX(label, 0);
        ViewCompat.setPivotY(label, 0);

        //设置输入框未展开时文本标签显示的文字
        if (editText.getHint() != null) {
            label.setText(editText.getHint());
            editText.setHint("");
        }

        card = findViewById(R.id.mtf_card);

        //设置组件的背景颜色
        if (backgroundColor != -1) {
            card.setBackgroundColor(backgroundColor);
        }

        final int expandedHeight = getContext().getResources().getDimensionPixelOffset(R.dimen.mtf_cardHeight_final);
        final int reducedHeight = cardCollapsedHeight;

        reducedScale = (float) (reducedHeight * 1.0 / expandedHeight);
        ViewCompat.setScaleY(card, reducedScale);
        ViewCompat.setPivotY(card, expandedHeight);

        //初始化icon相关属性
        image = (ImageView) findViewById(R.id.mtf_image);
        ViewCompat.setAlpha(image, 0);
        ViewCompat.setScaleX(image, 0.4f);
        ViewCompat.setScaleY(image, 0.4f);

        ViewCompat.setAlpha(editText, 0f);
        editText.setBackgroundColor(Color.TRANSPARENT);

        labelTopMargin = FrameLayout.LayoutParams.class.cast(label.getLayoutParams()).topMargin;

        //初始化文本标签颜色和icon
        customizeFromAttributes();

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        setHasFocus(hasFocus);
    }

    protected void customizeFromAttributes() {
        if (labelColor != -1) {
            this.label.setTextColor(labelColor);
        }

        if (imageDrawableId != -1) {
            this.image.setImageDrawable(ContextCompat.getDrawable(getContext(), imageDrawableId));
        }
    }

}

