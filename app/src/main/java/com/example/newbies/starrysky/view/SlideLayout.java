package com.example.newbies.starrysky.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * @author NewBies
 * @date 2018/4/15
 */
public class SlideLayout extends ViewGroup{

    private int childCount;
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private int childWidth;

    public SlideLayout(Context context) {
        super(context);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        childCount = getChildCount();
        for(int i = 0; i < childCount; i++){
            measureChild(getChildAt(i),widthMeasureSpec,heightMeasureSpec);
        }
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int offset = 0;
        View tempView;
        for(int i = 0; i < childCount; i++){
            tempView =  getChildAt(i);
            tempView.layout(offset,0,offset + tempView.getMeasuredWidth(),tempView.getMeasuredHeight());
            offset += tempView.getMeasuredWidth();
        }
        childWidth = offset - getChildAt(0).getMeasuredWidth();
    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event){
//
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        startX = (int)event.getX();
        startY = (int)event.getY();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                endX = startX;
                endY = startY;
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = endX - startX;
                int offsetY = endY - startY;
                scrollBy(offsetX,0);
                System.err.println(offsetX);
//                if(Math.abs(offsetX) > Math.abs(offsetY)){
//
//                }
//                else{
//                    return false;
//                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }

        return true;
    }

//    private int width;
//    private int height;
//
//    private Scroller scroller;
//    private int leftBorder;
//    private int rightBorder;
//    private int touchSlop;
//    private int slideSlop;
//    private int duration;
//    private float dX, dY;//TouchEvent_ACTION_DOWN坐标(dX,dY)
//    private float lastX;//TouchEvent最后一次坐标(lastX,lastY)
//    private boolean isMoveValid;
//    private boolean isOpen;
//    private OnStateChangeListener listener;
//
//    public SlideLayout(Context context) {
//        this(context, null);
//    }
//
//    public SlideLayout(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        initTypedArray(context, attrs);
//        init(context);
//    }
//
//    private void initTypedArray(Context context, AttributeSet attrs) {
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SlideLayout);
//        slideSlop = (int) typedArray.getDimension(R.styleable.SlideLayout_sl_slideSlop,
//                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 45, getResources().getDisplayMetrics()));
//        duration = typedArray.getInteger(R.styleable.SlideLayout_sl_duration, 250);
//        typedArray.recycle();
//    }
//
//    private void init(Context context) {
//        scroller = new Scroller(context);
//        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
//    }
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        width = MeasureSpec.getSize(widthMeasureSpec);
//        height = MeasureSpec.getSize(heightMeasureSpec);
//        int count = getChildCount();
//        for (int i = 0; i < count; i++) {
//            View child = getChildAt(i);
//            //为ViewGroup中的每一个子控件测量大小
//            measureChild(child, widthMeasureSpec, heightMeasureSpec);
//        }
//        setMeasuredDimension(width, height);
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        int count = getChildCount();
//        if (changed && count > 0) {
//            int left = 0, top = 0;
//            for (int i = 0; i < count; i++) {
//                View child = getChildAt(i);
//                //为ViewGroup中的每一个子控件在水平方向上进行布局
//                int childWidth = child.getMeasuredWidth();
//                child.layout(left, top, left + childWidth, child.getMeasuredHeight());
//                left += childWidth;
//            }
//            //初始化左右边界值
//            leftBorder = getChildAt(0).getLeft();
//            rightBorder = getChildAt(count - 1).getRight();
//        }
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            if (listener != null && listener.closeAll(this)) {
//                return false;
//            } else {
//                final float eX = ev.getRawX();
//                final float eY = ev.getRawY();
//                lastX = dX = eX;
//                dY = eY;
//                super.dispatchTouchEvent(ev);
//                return true;
//            }
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_MOVE) {
//            final float eX = ev.getRawX();
//            final float eY = ev.getRawY();
//            //当横向ACTION_MOVE值大于TouchSlop时，拦截子控件的事件
//            if (Math.abs(eX - dX) > touchSlop && Math.abs(eX - dX) > Math.abs(eY - dY)) {
//                return true;
//            }
//        }
//        return super.onInterceptTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        final float eX = event.getRawX();
//        final float eY = event.getRawY();
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                if (!isMoveValid && Math.abs(eX - dX) > touchSlop && Math.abs(eX - dX) > Math.abs(eY - dY)) {
//                    //禁止父控件拦截事件
//                    requestDisallowInterceptTouchEvent(true);
//                    isMoveValid = true;
//                }
//                if (isMoveValid) {
//                    int offset = (int) (lastX - eX);
//                    lastX = eX;
//                    if (getScrollX() + offset < 0) {
//                        toggle(false, false);
//                        dX = eX;//reset eX
//                    } else if (getScrollX() + offset > rightBorder - width) {
//                        toggle(true, false);
//                        dX = eX;//reset eX
//                    } else {
//                        scrollBy(offset, 0);
//                    }
//                    return true;
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                if (isMoveValid) {
//                    if (eX - dX < -slideSlop) {
//                        toggle(true, true);
//                    } else if (eX - dX > slideSlop) {
//                        toggle(false, true);
//                    } else {
//                        toggle(isOpen, true);
//                    }
//                    isMoveValid = false;
//                    return true;
//                }
//                break;
//        }
//        return super.onTouchEvent(event);
//    }
//
//    private void toggle(boolean open, boolean withAnim) {
//        if (isOpen != open && listener != null) {
//            listener.onChange(this, open);
//        }
//        isOpen = open;
//        if (isOpen) {
//            if (withAnim) {
//                smoothScrollTo(rightBorder - width, duration);
//            } else {
//                scrollTo(rightBorder - width, 0);
//            }
//        } else {
//            if (withAnim) {
//                smoothScrollTo(0, duration);
//            } else {
//                scrollTo(0, 0);
//            }
//        }
//    }
//
//    private void smoothScrollTo(int dstX, int duration) {
//        int offset = dstX - getScrollX();
//        scroller.startScroll(getScrollX(), 0, offset, 0, duration);
//        invalidate();
//    }
//
//    @Override
//    public void computeScroll() {
//        if (scroller.computeScrollOffset()) {
//            scrollTo(scroller.getCurrX(), scroller.getCurrY());
//            invalidate();
//        }
//    }
//
//    public boolean isOpen() {
//        return isOpen;
//    }
//
//    public void setOpen(boolean open, boolean withAnim) {
//        toggle(open, withAnim);
//    }
//
//    public void open() {
//        toggle(true, true);
//    }
//
//    public void close() {
//        toggle(false, true);
//    }
//
//    public interface OnStateChangeListener {
//        void onChange(SlideLayout layout, boolean isOpen);
//
//        /**
//         * 关闭所有未关闭的Slide
//         *
//         * @param layout;this
//         * @return true:存在未关闭Slide;false:不存在未关闭Slide
//         */
//        boolean closeAll(SlideLayout layout);
//    }
//
//    public void setOnStateChangeListener(OnStateChangeListener listener) {
//        this.listener = listener;
//    }
}
