package com.victoria.vshow.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AccelerateInterpolator;
import com.facebook.drawee.view.SimpleDraweeView;

public class FloatBall extends SimpleDraweeView{

    /** 吸附动画时间300ms */
    public static final int ANIMATION_TIME = 300;
    private String s1 = "https://pic.cnblogs.com/avatar/1142647/20170416093225.png";

    public FloatBall(Context context, AttributeSet attrs) {
        super(context, attrs);
            init(s1);
    }


    /**
     * 悬浮球吸附
     */
    public void floatBallAdsorption() {

        float x = getWidth()/2;
        animate().translationXBy(x).setInterpolator(new AccelerateInterpolator()
        ).setDuration(ANIMATION_TIME).start();
    }

    /**
     * 悬浮球展示
     */
    public void floatBallShow() {
        float x = - getWidth()/2;
        animate().translationXBy(x).setInterpolator(new AccelerateInterpolator()
        ).setDuration(ANIMATION_TIME).start();
    }


        // 设置按压变色
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

    public void init(String imgUrl) {
        // 设置主视图
        setVisibility(VISIBLE);
        setImageURI(imgUrl);
    }

}