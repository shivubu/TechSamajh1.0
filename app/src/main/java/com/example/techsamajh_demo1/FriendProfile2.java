package com.example.techsamajh_demo1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class FriendProfile2 extends AppCompatActivity {
    TextView frnd_name,frnd_age,frnd_city,frnd_int1,frnd_int2,frnd_int3;
    ImageView frnd_pic;
    DatabaseReference uref= FirebaseDatabase.getInstance().getReference().child("Users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_friend_profile2);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        String name=getIntent().getStringExtra("Name");
        frnd_name=findViewById(R.id.frnd_name);
        frnd_age=findViewById(R.id.frnd_age);
        frnd_city=findViewById(R.id.frnd_city);
        frnd_int1=findViewById(R.id.frnd_int1);
        frnd_int2=findViewById(R.id.frnd_int2);
        frnd_int3=findViewById(R.id.frnd_int3);
        frnd_pic=findViewById(R.id.frnd_pic);
        uref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren())
                {
                    if(Objects.requireNonNull(ds.child("Name").getValue()).toString().equals(name))
                    {
                        frnd_name.setText(ds.child("Name").getValue().toString());
                        frnd_age.setText(ds.child("Age").getValue().toString());
                        frnd_city.setText(ds.child("City").getValue().toString());
                        frnd_int1.setText(ds.child("interest1").getValue().toString());
                        frnd_int2.setText(ds.child("interest2").getValue().toString());
                        frnd_int3.setText(ds.child("interest3").getValue().toString());
                        try {
                            Glide.with(getApplicationContext()).load(ds.child("ImageUrl").getValue().toString()).into(frnd_pic);
                        }catch(NullPointerException e)
                        {
                            frnd_pic.setImageResource(R.drawable.profile_icon);
                        }

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
        startActivity(new Intent(FriendProfile2.this,Friends.class));
        finish();
    }
}