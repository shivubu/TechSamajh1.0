package com.example.techsamajh_demo1;

import android.content.Intent;
import android.os.Bundle;
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

public class FriendsRequest extends AppCompatActivity implements FriendRequest_Interface{
    DatabaseReference uref= FirebaseDatabase.getInstance().getReference("Users");
    String email= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
    ArrayList<String> requests=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_friendrequest);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        RecyclerView recyclerView=findViewById(R.id.requests_rv);
        uref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    if(Objects.requireNonNull(ds.child("email").getValue()).toString().equals(email))
                    {
                        try {
                            for (DataSnapshot ds1 : ds.child("FriendRequest").getChildren()) {
                                if (Objects.requireNonNull(ds1.getValue()).toString().equals("Pending")) {
                                    String name = ds1.getKey().toString();
                                    requests.add(name);
                                }
                            }
                        }
                        catch(NullPointerException ignored)
                        {
                            Toast.makeText(FriendsRequest.this, "You currently have no friend requests.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                FriendRequest_RVAdapter adapter=new FriendRequest_RVAdapter(FriendsRequest.this,requests, FriendsRequest.this);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(FriendsRequest.this));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FriendsRequest.this,Homepage.class));
        finish();
    }

    @Override
    public void onItemClick(String string) {
        Intent intent=new Intent(FriendsRequest.this,FriendProfile.class);
        intent.putExtra("Name",string);
        startActivity(intent);
        finish();
    }
}