package com.victoria.vshow.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.victoria.vshow.Adapter.VideoAdapter;
import com.victoria.vshow.R;
import com.victoria.vshow.viewpager.OnViewPagerListener;
import com.victoria.vshow.viewpager.ViewPagerLayoutManager;

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
    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;
    private RadioGroup rg_main;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        //设置颜色
        swipeRefresh.setColorSchemeColors(R.color.qmui_btn_blue_bg);
        rg_main = findViewById(R.id.rg_main);
        rg_main.check(R.id.main_for_show);
        mRecyclerView = findViewById(R.id.recycler);
        initView();
        initListener();
    }

    //沉浸式处理
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

        mLayoutManager = new ViewPagerLayoutManager(this, OrientationHelper.VERTICAL);
        mVideoAdapter = new VideoAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mVideoAdapter);
    }

    private void initListener() {

        //响应视频上的操作
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

        //下拉刷新监听
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        //颠倒视频流
                        mVideoAdapter.imgs = new int[]{R.mipmap.img_video_2, R.mipmap.img_video_1};
                        mVideoAdapter.videos = new int[]{R.raw.video_2, R.raw.video_1};
                        //更新视频流
                        mVideoAdapter.notifyDataSetChanged();
                    }
                },2000);
            }
        });

        //forYou forShow监听
        rg_main.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    //for Show
                    case R.id.main_for_show:
                        //颠倒视频流
                        mVideoAdapter.imgs = new int[]{R.mipmap.img_video_2, R.mipmap.img_video_1};
                        mVideoAdapter.videos = new int[]{R.raw.video_2, R.raw.video_1};
                        //更新视频流
                        mVideoAdapter.notifyDataSetChanged();
                        break;
                    case R.id.main_for_you:
                        //颠倒视频流
                        mVideoAdapter.imgs = new int[]{R.mipmap.img_video_1, R.mipmap.img_video_2};
                        mVideoAdapter.videos = new int[]{R.raw.video_1, R.raw.video_2};
                        //更新视频流
                        mVideoAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }


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

    //释放播放
    private void releaseVideo(int index) {
        View itemView = mRecyclerView.getChildAt(index);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        videoView.stopPlayback();
        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
    }

}

