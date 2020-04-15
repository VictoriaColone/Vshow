package com.victoria.vshow.Activity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.victoria.vshow.R;
import com.victoria.vshow.viewpager.OnViewPagerListener;
import com.victoria.vshow.viewpager.ViewPagerLayoutManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * @author Victoria Colone
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ViewPagerLayoutManager mLayoutManager;
    //下拉刷新
    private SwipeRefreshLayout swipeRefresh;

    // 图片，视频流资源
    private int[] imgs = {R.mipmap.img_video_1, R.mipmap.img_video_2};
    private int[] videos = {R.raw.video_1, R.raw.video_2};

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
        mAdapter = new MyAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
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
                //开启异步线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        imgs = new int[]{R.mipmap.img_video_2, R.mipmap.img_video_1};
                        videos = new int[]{R.raw.video_2, R.raw.video_1};
                        //run中异步更新UI线程
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                initView();
                            }
                        });
                    }
                }).start();

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


   class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


        public MyAdapter() {
        }

       //视频对象类
       public class ViewHolder extends RecyclerView.ViewHolder {
           ImageView img_thumb;
           VideoView videoView;
           ImageView img_play;
           RelativeLayout rootView;

           public ViewHolder(View itemView) {
               super(itemView);
               //视频布局
               img_thumb = itemView.findViewById(R.id.img_thumb);
               //视频播放布局
               videoView = itemView.findViewById(R.id.video_view);
               //视频播放暂停按钮
               img_play = itemView.findViewById(R.id.img_play);
               //RecyclerView根布局
               rootView = itemView.findViewById(R.id.root_view);
           }
       }

        //布局填充
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager, parent, false);
            return new ViewHolder(view);
        }

        //解析图片与视频资源
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.img_thumb.setImageResource(imgs[position % 2]);
            holder.videoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + videos[position % 2]));
        }

        //项目总数
        @Override
        public int getItemCount() {
            return 20;
        }

    }
}

