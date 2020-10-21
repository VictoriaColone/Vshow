package com.victoria.vshow.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.victoria.vshow.R;
import com.victoria.vshow.view.GameGuideDialog;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * @author Victor Corleone
 */
public class SplashActivity extends Activity {

    private static final String TAG = "SplashActivity";
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        //异步消息机制延时发送跳转页面信息
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //在主线程执行
//                startMainActivity();
            }
        },2000);
    }

    //设置点击后快速进去,为防止多次启动，要在manifest文件中设置为单例
    @Override
    public boolean onTouchEvent(MotionEvent event){
//        startMainActivity();
        new GameGuideDialog(this).show();
        return super.onTouchEvent(event);
    }

    //跳转主界面
    private void startMainActivity() {
        Log.d(TAG, " startMainActivity ");
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        //关闭启动页面
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //在活动销毁时移除handler消息，否则在进入appM(SplashActivity)立刻点击返回，还是会唤醒ainActivity
        handler.removeCallbacksAndMessages(null);
    }
}
