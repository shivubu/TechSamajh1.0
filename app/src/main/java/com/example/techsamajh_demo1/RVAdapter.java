package com.example.techsamajh_demo1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.MyViewHolder>{
    private final RecyclerView_interface recyclerViewInterface;
    Context context;
    ArrayList<UserModel> users;
    public RVAdapter(Context context, ArrayList<UserModel> users,
                     RecyclerView_interface recyclerViewInterface) {
        this.context= context;
        this.users=users;
        this.recyclerViewInterface=recyclerViewInterface;
    }

    @NonNull
    @Override
    public RVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflator = LayoutInflater.from(context);
        View view=inflator.inflate(R.layout.recyclerview_row,parent,false);
        return new RVAdapter.MyViewHolder(view,recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull RVAdapter.MyViewHolder holder, int position) {
        holder.Name.setText(users.get(position).getName());
        holder.Age.setText(users.get(position).getAge());
        holder.City.setText(users.get(position).getCity());
        try{
            Glide.with(context).load(users.get(position).getUrl()).into(holder.imageView);
        }catch(NullPointerException exe)
        {
            holder.imageView.setImageResource(R.drawable.profile_icon);
        }
        if(users.get(position).getUrl()==null)
        {
            holder.imageView.setImageResource(R.drawable.profile_icon);
        }


    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView Name;
        TextView Age;
        TextView City;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView,RecyclerView_interface recyclerViewInterface) {
            super(itemView);
            Name=itemView.findViewById(R.id.textView32);
            Age=itemView.findViewById(R.id.eventloc);
            City=itemView.findViewById(R.id.textView30);
            imageView=itemView.findViewById(R.id.imageView4);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerViewInterface!=null)
                    {
                        int pos=getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION)
                        {
                            recyclerViewInterface.onItemClick(Name.getText().toString(),Age.getText().toString(),City.getText().toString());
                        }
                    }
                }
            });

        }
    }
}
