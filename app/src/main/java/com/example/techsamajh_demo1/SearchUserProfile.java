package com.example.techsamajh_demo1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
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

public class SearchUserProfile extends AppCompatActivity {
    ImageView profilepic;
    TextView username,userage,usercity,intrest1,interest2,interest3;
    DatabaseReference uref= FirebaseDatabase.getInstance().getReference("Users");
    Button request;
    static String myname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_user_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        String name=getIntent().getStringExtra("name");
        String age=getIntent().getStringExtra("age");
        String city=getIntent().getStringExtra("city");
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
        username=findViewById(R.id.username1);
        userage=findViewById(R.id.age1);
        usercity=findViewById(R.id.city1);
        intrest1=findViewById(R.id.int1);
        interest2=findViewById(R.id.int2);
        interest3=findViewById(R.id.int3);
        profilepic=findViewById(R.id.imageView5);
        request=findViewById(R.id.sendreq);
        uref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(Objects.requireNonNull(ds.child("Name").getValue()).toString().equals(name))
                    {
                        try {
                            Glide.with(getApplicationContext()).load(Objects.requireNonNull(ds.child("ImageUrl").getValue()).toString()).into(profilepic);
                        }catch(NullPointerException ignored)
                        {
                            profilepic.setImageResource(R.drawable.profile_icon);
                        }
                        intrest1.setText(ds.child("interest1").getValue().toString());
                        interest2.setText(ds.child("interest2").getValue().toString());
                        interest3.setText(ds.child("interest3").getValue().toString());

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        uref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    if(Objects.requireNonNull(ds.child("email").getValue()).toString().equals(email))
                    {
                        myname=ds.child("Name").getValue().toString();

                    }
                    for(DataSnapshot ds1: snapshot.getChildren())
                    {
                        if(ds1.child("Name").getValue().toString().equals(name))
                        {
                            try {
                                if (ds1.child("Friends").hasChild(myname)) {
                                    request.setText("Friend");
                                    request.setBackgroundColor(Color.GRAY);
                                    request.setClickable(false);
                                }
                                else
                                {
                                    if(ds1.child("FriendRequest").hasChild(myname))
                                    {
                                        request.setText("Request Sent");
                                        request.setBackgroundColor(Color.GRAY);
                                        request.setClickable(false);
                                    }
                                    else
                                    {
                                        request.setClickable(true);
                                        request.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                ds1.child("FriendRequest").child(myname).getRef().setValue("Pending");
                                                request.setText("Friend Request Sent");
                                                request.setClickable(false);

                                            }
                                        });
                                    }
                                }
                            }catch(NullPointerException ignored){}
                        }
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        username.setText(name);
        userage.setText(age);
        usercity.setText(city);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SearchUserProfile.this,Homepage.class));
        finish();
    }
}