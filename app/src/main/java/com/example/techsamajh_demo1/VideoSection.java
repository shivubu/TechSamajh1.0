package com.example.techsamajh_demo1;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VideoSection extends AppCompatActivity implements VideoInterface{
    DatabaseReference vref= FirebaseDatabase.getInstance().getReference().child("Videos");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_video_section);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String type=getIntent().getStringExtra("Name");
        RecyclerView recyclerView=findViewById(R.id.videos_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<VideoModel> videos=new ArrayList<>();
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    if(ds.getKey().equals(type))
                    {
                        for(DataSnapshot ds1: ds.getChildren())
                        {
                            videos.add(new VideoModel(ds1.getKey().toString(),ds1.getValue().toString()));
                        }
                    }
                }
                Videos_RVAdapter adapter=new Videos_RVAdapter(VideoSection.this,videos,VideoSection.this);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(VideoSection.this, Knowledge.class));
        finish();
    }

    @Override
    public void onItemClick(String link) {
        Intent i=new Intent(VideoSection.this, Videoplay.class);
        i.putExtra("Link",link);
        startActivity(i);
        finish();


    }
}