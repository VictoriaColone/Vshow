package com.victoria.vshow.ui.Activity;

import android.annotation.SuppressLint;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.navigation.NavigationView;
import com.victoria.vshow.Adapter.VideoAdapter;
import com.victoria.vshow.R;
import com.victoria.vshow.ui.view.CloseDialog;
import com.victoria.vshow.ui.view.GameGuideDialog;
import com.victoria.vshow.utils.TestUtils;
import com.victoria.vshow.viewpager.OnViewPagerListener;
import com.victoria.vshow.viewpager.ViewPagerLayoutManager;
import com.victoria.vshow.widget.FloatBall;
import com.victoria.vshow.widget.LightningView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @author Victoria Corleone
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private VideoAdapter mVideoAdapter;
    private ViewPagerLayoutManager mLayoutManager;
    // 下拉刷新
    private SwipeRefreshLayout swipeRefresh;
    private RadioGroup rg_main;
    // 悬浮球
    private FloatBall mFloatBall;
    // 帧动画地球仪
    private ImageView mEarthFrame;
    // 闪光按钮
    private LightningView lightningView;
    // 导航栏
    private NavigationView navigationView;



    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        // 设置颜色
        swipeRefresh.setColorSchemeColors(R.color.qmui_btn_blue_bg);
        rg_main = findViewById(R.id.rg_main);
        rg_main.check(R.id.main_for_show);
        mRecyclerView = findViewById(R.id.recycler);
        mFloatBall = findViewById(R.id.float_imgview);
        mEarthFrame = findViewById(R.id.earth_frame);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        lightningView = headerLayout.findViewById(R.id.lightiningButton);
        initView();
        initListener();
    }


    /**
     * 沉浸式处理
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    private void initView() {

        mFloatBall.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
         Toast.makeText(MainActivity.this, "点我干嘛", Toast.LENGTH_LONG).show();
        }
        });
        // 创建补间动画
        AnimationSet animation = setTweenAnimation();
        // 播放补间动画
        mFloatBall.startAnimation(animation);
        // 创建帧动画
        AnimationDrawable animationDrawable = setDrawableAnimatinon(mEarthFrame);
        // 启动帧动画
        animationDrawable.start();
        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        mVideoAdapter = new VideoAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mVideoAdapter);
        new GameGuideDialog(this).show();
    }

    /**
     * 设置监听
     */
    private void initListener() {

        // 响应视频上的操作
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {

            @Override
            public void onInitComplete() {
                Log.e(TAG, "onInitComplete");
                playVideo(0);
            }

            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e(TAG, "释放位置:" + position + " 下一页:" + isNext);
                int index = 0;

                if(position != 0 || isNext) {
                    if (isNext) {
                        index = 0;
                    } else {
                        index = 1;
                    }
                    releaseVideo(index);
                }
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                Log.e(TAG, "选中位置:" + position + "  是否是滑动到底部:" + isBottom);
                playVideo(0);
            }
        });

        // 下拉刷新监听
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        // 颠倒视频流
//                        mVideoAdapter.imgs = new int[]{R.mipmap.img_video_2, R.mipmap.img_video_1};
                        mVideoAdapter.videos = new int[]{R.raw.video_3, R.raw.video_2, R.raw.video_1};
                        // 更新视频流
                        mVideoAdapter.notifyDataSetChanged();
                    }
                },2000);
            }
        });

        // forYou forShow监听
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    // for Show
                    case R.id.main_for_show:
                        // 颠倒视频流
                        // mVideoAdapter.imgs = new int[]{R.mipmap.img_video_2, R.mipmap.img_video_1};
                        mVideoAdapter.videos = new int[]{R.raw.video_3, R.raw.video_2, R.raw.video_1};
                        // 更新视频流
                        mVideoAdapter.notifyDataSetChanged();
                        mFloatBall.floatBallShow();
                        break;
                    case R.id.main_for_you:
                        // 颠倒视频流
                        //  mVideoAdapter.imgs = new int[]{R.mipmap.img_video_1, R.mipmap.img_video_2};
                        mVideoAdapter.videos = new int[]{R.raw.video_1, R.raw.video_2, R.raw.video_3};
                        // 更新视频流
                        mVideoAdapter.notifyDataSetChanged();
                        mFloatBall.floatBallAdsorption();
                        break;
                }
            }
        });

        // 登录按钮，跳转半屏布局
        lightningView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "点我干嘛", Toast.LENGTH_LONG).show();
                TestUtils.printPackages(TestUtils.getInstalledPackages(MainActivity.this), "installed");
                TestUtils.printPackages(TestUtils.getAllLauncherIconPackages(MainActivity.this), "launcherIcon");
            }
        });

        // 导航页菜单栏"设置"按钮点击事件
        navigationView.getMenu().getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Toast.makeText(MainActivity.this, "点我干嘛", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }
    /**
     * 播放视频
     * @param position
     */
    //播放视频
    private void playVideo(int position) {
        View itemView = mRecyclerView.getChildAt(0);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final RelativeLayout rootView = itemView.findViewById(R.id.root_view);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
        videoView.start();

        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mediaPlayer[0] = mp;
                mp.setLooping(true);
                imgThumb.animate().alpha(0).setDuration(200).start();
                return false;
            }
        });

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {

            }
        });


        imgPlay.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = true;

            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    imgPlay.animate().alpha(1f).start();
                    videoView.pause();
                    isPlaying = false;
                } else {
                    imgPlay.animate().alpha(0f).start();
                    videoView.start();
                    isPlaying = true;
                }
            }
        });
    }

    /**
     * 释放播放
     * @param index
     */
    private void releaseVideo(int index) {
        View itemView = mRecyclerView.getChildAt(index);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        videoView.stopPlayback();
        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
    }

    /**
     * 补间动画创建
     */
    private AnimationSet setTweenAnimation() {
        // 引用xml实现组合动画
        // Animation animation = AnimationUtils.loadAnimation(this, R.anim.component);

        // 组合动画设置
        AnimationSet animation = new AnimationSet(true);
        // 步骤1:创建组合动画对象(设置为true)
        // 步骤2:设置组合动画的属性
        // 特别说明以下情况
        // 因为在下面的旋转动画设置了无限循环(RepeatCount = INFINITE)
        // 所以动画不会结束，而是无限循环
        // 所以组合动画的下面两行设置是无效的
        animation.setRepeatMode(Animation.REVERSE);
        // 设置了循环一次,但无效
        animation.setRepeatCount(1);
        // 步骤3:逐个创建子动画(方式同单个动画创建方式,此处不作过多描述)
        // 子动画1:旋转动画
        Animation rotate = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotate.setDuration(1000);
        rotate.setRepeatMode(Animation.REVERSE);
        rotate.setRepeatCount(Animation.INFINITE);
        // 子动画2:平移动画
        Animation translate = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT,-0.5f,
                TranslateAnimation.RELATIVE_TO_PARENT,0,
                TranslateAnimation.RELATIVE_TO_SELF,0
                ,TranslateAnimation.RELATIVE_TO_SELF,0);
        translate.setDuration(10000);
        // 子动画3:透明度动画
        Animation alpha = new AlphaAnimation(0,1);
        alpha.setDuration(3000);
        alpha.setStartOffset(7000);
        // 子动画4:缩放动画
        Animation scale1 = new ScaleAnimation(1,0.5f,1,0.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        scale1.setDuration(1000);
        scale1.setStartOffset(4000);
        // 步骤4:将创建的子动画添加到组合动画里
        animation.addAnimation(alpha);
        animation.addAnimation(rotate);
        animation.addAnimation(translate);
        animation.addAnimation(scale1);
        return animation;
    }

    /**
     * 帧动画创建
     * @param earthFrame
     */
    private AnimationDrawable setDrawableAnimatinon(ImageView earthFrame) {

        // xml设置帧动画
//        earthFrame.setImageResource(R.drawable.earth_animation);
        // java设置帧动画
        AnimationDrawable animationDrawable = new AnimationDrawable();
        for (int i = 1; i <= 44; i++) {
            int id = getResources().getIdentifier("earth" + i, "drawable", getPackageName());
            Drawable drawable = getResources().getDrawable(id);
            animationDrawable.addFrame(drawable, 100);
        }
        earthFrame.setImageDrawable(animationDrawable);

        return (AnimationDrawable) earthFrame.getDrawable();
    }

    @Override
    public void onBackPressed(){
         CloseDialog mDialog = new CloseDialog(this);
            mDialog.show();
    }

}

