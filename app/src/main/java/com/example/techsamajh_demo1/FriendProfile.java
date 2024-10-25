package com.example.techsamajh_demo1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FriendProfile extends AppCompatActivity {
    String username;
    TextView name,age,city,int1,int2,int3;
    ImageView profilepic;
    Button accept,reject;
    DatabaseReference uref = FirebaseDatabase.getInstance().getReference("Users");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_friend_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        username=getIntent().getStringExtra("Name");
        name=findViewById(R.id.name);
        age=findViewById(R.id.age);
        city=findViewById(R.id.city);
        int1=findViewById(R.id.interest1);
        int2=findViewById(R.id.interest2);
        int3=findViewById(R.id.interest3);
        profilepic=findViewById(R.id.profilepicture);
        accept=findViewById(R.id.accept);
        reject=findViewById(R.id.reject);
        name.setText(username);
        uref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    if(ds.child("Name").getValue().toString().equals(username))
                    {
                        age.setText(ds.child("Age").getValue().toString());
                        city.setText(ds.child("City").getValue().toString());
                        int1.setText(ds.child("interest1").getValue().toString());
                        int2.setText(ds.child("interest2").getValue().toString());
                        int3.setText(ds.child("interest3").getValue().toString());
                        try {
                            Glide.with(getApplicationContext()).load(ds.child("ImageUrl").getValue().toString()).into(profilepic);
                        }
                        catch(NullPointerException e)
                        {
                            profilepic.setImageResource(R.drawable.profile_icon);
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren())
                        {
                            if(Objects.requireNonNull(ds.child("email").getValue()).toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                            {
                                String myname=ds.child("Name").getValue().toString();
                                ds.getRef().child("Friends").child(username).setValue(true);
                                ds.getRef().child("FriendRequest").child(username).removeValue();
                                for(DataSnapshot ds1:snapshot.getChildren())
                                {
                                    if(ds1.child("Name").getValue().toString().equals(username))
                                    {
                                        ds1.getRef().child("Friends").child(myname).setValue(true);
                                    }
                                }
                                accept.setText("Friend Request Accpeted");
                                accept.setClickable(false);
                                reject.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds:snapshot.getChildren())
                        {
                            if(Objects.requireNonNull(ds.child("email").getValue()).toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                            {
                                ds.getRef().child("FriendRequest").child(username).removeValue();
                                reject.setText("Friend Request Rejected");
                                reject.setClickable(false);
                                accept.setVisibility(View.INVISIBLE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(FriendProfile.this,FriendsRequest.class));
        finish();
    }
}