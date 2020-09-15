package com.victoria.vshow.Adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.victoria.vshow.BuildConfig;
import com.victoria.vshow.R;
import com.victoria.vshow.ViewHolder.VideoViewHolder;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Victor Corleone
 */
public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    // 图片，视频流资源
    public int[] imgs = {R.mipmap.img_video_2, R.mipmap.img_video_1};
    public int[] videos = {R.raw.video_2, R.raw.video_1};

    public VideoAdapter() {
    }

    //布局填充
    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager, parent, false);
        return new VideoViewHolder(view);
    }

    //解析图片与视频资源
    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.img_thumb.setImageResource(imgs[position % 2]);
        holder.videoView.setVideoURI(Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + videos[position % 2]));
    }

    //项目总数
    @Override
    public int getItemCount() {
        return 20;
    }

}