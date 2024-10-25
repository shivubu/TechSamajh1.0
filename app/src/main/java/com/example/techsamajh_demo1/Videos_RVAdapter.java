package com.example.techsamajh_demo1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Videos_RVAdapter extends RecyclerView.Adapter<Videos_RVAdapter.MyViewHolder> {
    VideoInterface videoInterface;
    Context context;
    ArrayList<VideoModel> videos;
    public Videos_RVAdapter(Context context, ArrayList<VideoModel> videos,VideoInterface videoInterface)
    {
        this.context=context;
        this.videos=videos;
        this.videoInterface=videoInterface;
    }

    @NonNull
    @Override
    public Videos_RVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recyclerview_videorow,parent,false);
        return new Videos_RVAdapter.MyViewHolder(view,videoInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Videos_RVAdapter.MyViewHolder holder, int position) {
        holder.name.setText(videos.get(position).getTitle());
        holder.link.setText(videos.get(position).getId());

    }

    @Override
    public int getItemCount() {
        return videos.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,link;

        public MyViewHolder(@NonNull View itemView,VideoInterface videoInterface) {
            super(itemView);
            name=itemView.findViewById(R.id.vid_name);
            link=itemView.findViewById(R.id.video_link);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(videoInterface!=null)
                    {
                        int pos=getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION)
                        {
                            videoInterface.onItemClick(link.getText().toString());
                        }
                    }
                }
            });

        }
    }
}
