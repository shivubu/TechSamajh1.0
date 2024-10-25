package com.example.techsamajh_demo1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class Friends extends AppCompatActivity implements Friendface_Interface{
    DatabaseReference uref= FirebaseDatabase.getInstance().getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_friends);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        RecyclerView recyclerView=findViewById(R.id.recycler2);
        String email=FirebaseAuth.getInstance().getCurrentUser().getEmail();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        uref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> friends=new ArrayList<>();
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    if(Objects.requireNonNull(ds.child("email").getValue()).toString().equals(email))
                    {
                        try {
                            for (DataSnapshot ds1 : ds.child("Friends").getChildren()) {
                                friends.add(ds1.getKey());
                            }
                        }catch(NullPointerException ignored)
                        {
                            Toast.makeText(Friends.this, "You currently have no friends.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                Friend_RVAdapter adapter=new Friend_RVAdapter(Friends.this,friends,Friends.this);
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
        startActivity(new Intent(Friends.this, Homepage.class));
        finish();
    }

    @Override
    public void onItemClick(String string) {
        Intent intent=new Intent(Friends.this, FriendProfile2.class);
        intent.putExtra("Name",string);
        startActivity(intent);
    }
}