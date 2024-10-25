package com.example.techsamajh_demo1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

public class ProfilePage extends AppCompatActivity {
    FirebaseAuth auth=FirebaseAuth.getInstance();
    DatabaseReference userref= FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference dref=FirebaseDatabase.getInstance().getReference();
    DatabaseReference countref=dref.child("count");
    String email;
    TextView name,age,city,interest1,interest2,interest3;
    ImageView profile;
    Button gotoedit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        name=findViewById(R.id.textView11);
        age=findViewById(R.id.textView12);
        city=findViewById(R.id.textView13);
        email= Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        profile=findViewById(R.id.imageView3);
        interest1=findViewById(R.id.textView18);
        interest2=findViewById(R.id.textView19);
        interest3=findViewById(R.id.textView20);
        gotoedit=findViewById(R.id.button4);
        profile.setImageResource(R.drawable.profile_icon);
        Search();
        gotoedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePage.this, Editprofile.class));
                finish();
            }
        });
    }
    private void Search()
    {
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    if(Objects.equals(ds.child("email").getValue(String.class), email))
                    {
                        Load(ds.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void Load(String uname)
    {
            DatabaseReference uref=userref.child(uname);
            uref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot ds:snapshot.getChildren()) {
                        if (Objects.equals(ds.getKey(), "Name"))
                            try {
                                name.setText(ds.getValue(String.class));
                            } catch (NullPointerException exception) {
                                name.setText("Please enter name");
                            }

                        if (Objects.equals(ds.getKey(), "Age"))
                            try {
                                age.setText(ds.getValue(String.class));
                            } catch (NullPointerException exception) {
                                age.setText("Please enter age");
                            }
                        if (Objects.equals(ds.getKey(), "City"))
                            try {
                                city.setText(ds.getValue(String.class));
                            } catch (NullPointerException exception) {
                                city.setText("Please enter city");
                            }
                        if (Objects.equals(ds.getKey(), "interest1"))
                            try {
                                interest1.setText(ds.getValue(String.class));
                            } catch (NullPointerException exception) {
                                interest1.setText("Please enter interest");
                            }
                        if (Objects.equals(ds.getKey(), "interest2"))
                            try {
                                interest2.setText(ds.getValue(String.class));
                            } catch (NullPointerException exception) {
                                interest2.setText("Please enter interest");
                            }
                        if (Objects.equals(ds.getKey(), "interest3"))
                            try {
                                interest3.setText(ds.getValue(String.class));
                            } catch (NullPointerException exception) {
                                interest3.setText("Please enter interest");
                            }
                        if(Objects.equals(ds.getKey(),"ImageUrl"))
                        {
                            String url=ds.getValue(String.class);
                            try {
                                Glide.with(getApplicationContext()).load(url).into(profile);
                            }catch (NullPointerException exception) {
                                profile.setImageResource(R.drawable.profile_icon);
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
        startActivity(new Intent(ProfilePage.this, Homepage.class));
        finish();
    }

}
