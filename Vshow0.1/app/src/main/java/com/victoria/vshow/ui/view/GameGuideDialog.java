package com.victoria.vshow.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Outline;
import android.net.Uri;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import com.facebook.drawee.view.SimpleDraweeView;
import com.victoria.vshow.R;
import org.jetbrains.annotations.NotNull;

/**
 * @author yutao12
 * @since 2020-10-19
 */
public class GameGuideDialog extends Dialog {

    /** 图片 */
    private SimpleDraweeView mImage;
    /** 视频控件 */
    private VideoView mVideoView;
    /** 立即游玩按钮 */
    private Button mPlayButton;
    /** 倒计时数字textview */
    private TextView mCountDownNum;
    /** 倒计时器 */
    private CountDownTimer mCountDownTimer;
    /** 关闭退出按钮 */
    private ImageView mCloseButton;
    /** 标记位——是否需要倒计时 */
    private Boolean needCountDown = true;
    /** 视频地址 */
    private static final String s1 = "https://b.bdstatic.com/searchbox/icms/searchbox/img/351089f1-bffd-4cd7-8e08-c047beeaf940.mp4";

    /**
     * 构造方法
     *
     * @param context
     */
    public GameGuideDialog(@NonNull Context context) {
        super(context, R.style.NoTitleDialog);
        init();
    }

    /**
     * 初始化控件
     */
    private void init() {
        // 设置可以被取消
        setCanceledOnTouchOutside(false);
        // 设置主视图
        setContentView(R.layout.game_minigame_guide_dialog);
        mVideoView = findViewById(R.id.game_guide_video_top);
//        mVideoView.setVisibility(View.GONE);
        mVideoView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 39);
            }
        });
        mVideoView.setClipToOutline(true);
        mVideoView.setVideoURI(Uri.parse(s1));
        mImage = findViewById(R.id.game_guide_image_top);
        mImage.setVisibility(View.GONE);
        mPlayButton = findViewById(R.id.game_guide_play_game);
        mCountDownNum = findViewById(R.id.game_guide_countdown_num);
        mCloseButton = findViewById(R.id.game_guide_dialog_exit);
        mImage.setImageURI("https://b.bdstatic.com/searchbox/icms/searchbox/img/dadacf68-fb93-4778-bf5d-4f39d6efdd21.gif");
        initListener();

    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        mCloseButton.setOnTouchListener(alphaChange());
        mCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCountDownTimer != null) {
                    mCountDownTimer.cancel();
                }
                dismiss();
            }
        });
        mPlayButton.setOnTouchListener(alphaChange());
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 设置按下后透明度改变
     * @return
     */
    @NotNull
    private View.OnTouchListener alphaChange() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 设置按压态 0.2透明度
                        v.setAlpha(0.2f);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // 取消按压态
                        v.setAlpha(1.0f);
                        break;
                    default:
                        break;
                }
                return false;
            }
        };
    }

    @Override
    public void show() {
        super.show();
        // 开始倒计时
        if (needCountDown) {
            // 倒计时
            mCountDownTimer = new CountDownTimer(10000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mCountDownNum.setText((millisUntilFinished / 1000 + 1) + "秒后自动打开游戏");
                }

                @Override
                public void onFinish() {
                    // 打开scheme,后端空时默认去游戏中心
                    try {
                        if(isShowing()) {
                            dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            mCountDownTimer.start();
        }
        if (mVideoView != null) {
            mVideoView.start();
        }


    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }
}
