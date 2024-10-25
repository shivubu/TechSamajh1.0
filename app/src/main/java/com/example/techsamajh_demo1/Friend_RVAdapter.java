package com.example.techsamajh_demo1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Friend_RVAdapter extends RecyclerView.Adapter<Friend_RVAdapter.MyViewHolder>{
    private final Friendface_Interface friendfaceInterface;
    Context context;
    ArrayList<String> friends;
    public Friend_RVAdapter(Context context, ArrayList<String> friends,Friendface_Interface friendfaceInterface)
    {
        this.friendfaceInterface = friendfaceInterface;
        this.context=context;
        this.friends=friends;
    }
    @NonNull
    @Override
    public Friend_RVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.recyclerview_row2,parent,false);
        return new Friend_RVAdapter.MyViewHolder(view,friendfaceInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Friend_RVAdapter.MyViewHolder holder, int position) {
        holder.friendname.setText(friends.get(position));

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView friendname;

        public MyViewHolder(@NonNull View itemView,Friendface_Interface friendfaceInterface) {
            super(itemView);
            friendname=itemView.findViewById(R.id.fr_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(friendfaceInterface!=null)
                    {
                        int pos=getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION)
                        {
                            friendfaceInterface.onItemClick(friendname.getText().toString());
                        }
                    }
                }
            });


        }
    }
}
