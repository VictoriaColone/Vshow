package com.victoria.vshow.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 用于展示带闪光的button
 * @author Victoria
 */
public class LightningView extends AppCompatTextView {

    /** 闪光颜色 */
    private Shader mGradient;
    /** 闪光倾斜度 */
    private Matrix mGradientMatrix;
    /** 闪光绘笔 */
    private Paint mPaint;
    /** 闪光宽高 */
    private int mViewWidth = 0, mViewHeight = 0;
    /** 闪光移动距离 */
    private float mTranslateX = 0, mTranslateY = 0;
    /** 自动展示 */
    private boolean mAnimating = false;
    /** 矩形对象 */
    private Rect rect;
    /** 属性动画 */
    private ValueAnimator valueAnimator;

    /** 是否自动运行动画 */
    private boolean autoRun = true;
    public LightningView(Context context) {
        super(context);
        init();
    }

    public LightningView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LightningView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        rect = new Rect();
        mPaint = new Paint();
        initGradientAnimator();
    }

    public void setAutoRun(boolean autoRun) {
        this.autoRun = autoRun;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        rect.set(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0) {
            mViewWidth = getWidth();
            mViewHeight = getHeight();
            if (mViewWidth > 0) {
                //亮光样式
                mGradient = new LinearGradient(0, 0, mViewWidth / 2, mViewHeight,
                        new int[]{0x00ffffff, 0x70ffffff, 0x00ffffff},
                        new float[]{0, 0.5f, 1},
                        Shader.TileMode.CLAMP);
                mPaint.setShader(mGradient);
                mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN));
                mGradientMatrix = new Matrix();
                mGradientMatrix.setTranslate(0, mViewHeight);
                mGradient.setLocalMatrix(mGradientMatrix);
                rect.set(0, 0, w, h);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mAnimating && mGradientMatrix != null) {
            canvas.drawRect(rect, mPaint);
        }
    }

    private void initGradientAnimator() {
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        // 设置闪光间隔时间
        valueAnimator.setDuration(2500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (Float) animation.getAnimatedValue();
                // 改变每次动画的平移x值，范围是[-0.5mViewWidth, 0.5mViewWidth]
                mTranslateX = 2 * mViewWidth * v - mViewWidth /2;
                // 平移matrix, 设置平移量
                if (mGradientMatrix != null) {
                    mGradientMatrix.setTranslate(mTranslateX, 0);
                }
                // 设置线性变化的matrix
                if (mGradient != null) {
                    mGradient.setLocalMatrix(mGradientMatrix);
                }
                // 重绘
                invalidate();
            }
        });
        if (autoRun) {
            valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    mAnimating = true;
                    if (valueAnimator != null) {
                        valueAnimator.start();
                    }
                }
            });
        }
    }

    // 停止动画
    public void stopAnimation() {
        if (mAnimating && valueAnimator != null) {
            mAnimating = false;
            valueAnimator.cancel();
            invalidate();
        }
    }

    // 开始动画
    public void startAnimation() {
        if (!mAnimating && valueAnimator != null) {
            mAnimating = true;
            valueAnimator.start();
        }
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
}
