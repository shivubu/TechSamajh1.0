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

public class Events extends AppCompatActivity {
    DatabaseReference uref= FirebaseDatabase.getInstance().getReference().child("Users");
    DatabaseReference eref= FirebaseDatabase.getInstance().getReference().child("Events");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_events);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String email= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        RecyclerView rvevents=findViewById(R.id.events_rv);
        rvevents.setLayoutManager(new LinearLayoutManager(this));
        uref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    if(Objects.requireNonNull(ds.child("email").getValue()).toString().equals(email))
                    {
                        String loc=ds.child("City").getValue().toString();
                        eref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                ArrayList<EventModel> events=new ArrayList<>();
                                for(DataSnapshot ds:snapshot.getChildren())
                                {
                                    if(Objects.requireNonNull(ds.child("loc").getValue()).toString().equals(loc))
                                    {
                                        Toast.makeText(Events.this, "Event found", Toast.LENGTH_SHORT).show();
                                        events.add(new EventModel(ds.child("name").getValue().toString(),ds.child("date").getValue().toString(),ds.child("loc").getValue().toString()));
                                    }
                                }
                                Event_RVAdapter adapter=new Event_RVAdapter(Events.this,events);
                                rvevents.setAdapter(adapter);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Events.this,Homepage.class));
        finish();
    }
}