package com.victoria.vshow.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.victoria.vshow.R;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Victor Corleone
 * 视频对象类
 */
public class VideoViewHolder extends RecyclerView.ViewHolder {
    // 图片布局
    public ImageView img_thumb;
    // 视频播放布局
    public VideoView videoView;
    public ImageView img_play;
    public RelativeLayout rootView;

    public VideoViewHolder(View itemView) {
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