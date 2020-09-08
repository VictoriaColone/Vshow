package com.victoria.vshow.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 *
 * @author yutao12
 */
public class FloatBall extends SimpleDraweeView {

    /** 吸附动画时间300ms */
    public static final int ANIMATION_TIME = 300;

    /**
     * 构造器
     */
    public FloatBall(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    /**
     * 构造器
     *
     * @param context 上下文
     */
    public FloatBall(Context context) {
        super(context);
    }

    /**
     * 构造器
     *
     * @param context 上下文
     * @param attrs   属性
     */
    public FloatBall(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 构造器
     *
     * @param context 上下文
     * @param attrs   属性
     * @param defStyle 风格
     */
    public FloatBall(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 构造器
     *
     * @param context 上下文
     * @param attrs   属性
     */
    public FloatBall(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    /**
     * 悬浮球吸附
     */
    public void floatBallAdsorption() {
        animate().translationX(getWidth() / 2).setInterpolator
                (new AccelerateInterpolator()).setDuration(ANIMATION_TIME).start();
    }

    /**
     * 悬浮球展示
     */
    public void floatBallShow() {
        animate().translationX( - getWidth() / 2).setInterpolator
                (new AccelerateInterpolator()).setDuration(ANIMATION_TIME).start();
    }

    /**
     * 设置按压变色
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 设置按压态 0.2透明度
                this.setAlpha(0.2f);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 取消按压态
                this.setAlpha(1.0f);
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 更新悬浮球配置
     */
    public void update() {
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

}
