package com.app.fztn;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.VideoViewHolder> {

    private List<VideoItem> videoList;
    private Context context;

    public static class VideoViewHolder extends RecyclerView.ViewHolder {
        public TextView textBilgi;
        public TextView agriBolgesi;
        public LinearLayout linearLayout;

        public VideoViewHolder(View itemView) {
            super(itemView);
            textBilgi = itemView.findViewById(R.id.textBilgi);
            agriBolgesi = itemView.findViewById(R.id.agriBolgesi);
            linearLayout = itemView.findViewById(R.id.linearLayout);
        }
    }

    public VideoAdapter(List<VideoItem> videoList, Context context) {
        this.videoList = videoList;
        this.context = context;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        VideoItem videoItem = videoList.get(position);
        holder.textBilgi.setText(videoItem.getTextBilgi());
        holder.agriBolgesi.setText("Hastalığınızın ağrı bölgesi : "+videoItem.getAgriBolgesi());
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoItem.getVideoUrl()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }
}


