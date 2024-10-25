package com.example.techsamajh_demo1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendRequest_RVAdapter extends RecyclerView.Adapter<FriendRequest_RVAdapter.MyViewHolder>{
    private final FriendRequest_Interface friendRequestInterface;
    Context context;
    ArrayList<String> users;
    public FriendRequest_RVAdapter(Context context, ArrayList<String> users,FriendRequest_Interface friendRequestInterface) {
        this.context=context;
        this.users=users;
        this.friendRequestInterface=friendRequestInterface;

    }
    @NonNull
    @Override
    public FriendRequest_RVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recyclerview_row1,parent,false);
        return new FriendRequest_RVAdapter.MyViewHolder(view,friendRequestInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequest_RVAdapter.MyViewHolder holder, int position) {
        holder.name.setText(users.get(position));

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public MyViewHolder(@NonNull View itemView,FriendRequest_Interface friendRequestInterface) {
            super(itemView);
            name=itemView.findViewById(R.id.frq_name);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(friendRequestInterface!=null)
                    {
                        int pos=getAdapterPosition();
                        if(pos!=RecyclerView.NO_POSITION)
                        {
                            friendRequestInterface.onItemClick(name.getText().toString());
                        }
                    }
                }
            });
        }

    }
}
