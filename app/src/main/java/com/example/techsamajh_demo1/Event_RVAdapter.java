package com.example.techsamajh_demo1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Event_RVAdapter extends RecyclerView.Adapter<Event_RVAdapter.MyViewHolder>{
    Context context;
    ArrayList<EventModel> events;
    public Event_RVAdapter(Context context, ArrayList<EventModel> events) {
        this.events=events;
        this.context=context;
    }
    @NonNull
    @Override
    public Event_RVAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.rv_rowevents,parent,false);
        return new Event_RVAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Event_RVAdapter.MyViewHolder holder, int position) {
        holder.name.setText(events.get(position).getName());
        holder.date.setText(events.get(position).getDate());
        holder.loc.setText(events.get(position).getLoc());

    }

    @Override
    public int getItemCount() {
        return events.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name,date,loc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.eventname);
            date=itemView.findViewById(R.id.eventdate);
            loc=itemView.findViewById(R.id.eventloc);
        }
    }
}
