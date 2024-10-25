package com.example.techsamajh_demo1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Homepage extends AppCompatActivity {
    ImageView menu;
    DrawerLayout drawerLayout;
    NavigationView navi;
    View header;
    TextView alert1,alert2,alert3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        alert1=findViewById(R.id.alert1);
        alert1.setSelected(true);
        alert2=findViewById(R.id.alert2);
        alert2.setSelected(true);
        alert3=findViewById(R.id.alert3);
        alert3.setSelected(true);
        navi=findViewById(R.id.navigation_View);
        menu=findViewById(R.id.menu_button);
        drawerLayout=findViewById(R.id.drawer_layout);
        menu.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
               drawerLayout.open();
            }
        });
        header=navi.getHeaderView(0);
        navi.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.profile) {
                   Intent intent = new Intent(Homepage.this, ProfilePage.class);
                   startActivity(intent);
                }
                if(item.getItemId()==R.id.logout)
                {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(Homepage.this, LoginActivity.class));
                    finish();
                }
                if(item.getItemId()==R.id.search)
                {
                    Intent intent=new Intent(Homepage.this,Searchpage.class);
                    startActivity(intent);
                    finish();
                }
                if(item.getItemId()==R.id.friendreq)
                {
                    Intent intent=new Intent(Homepage.this, FriendsRequest.class);
                    startActivity(intent);
                    finish();
                }
                if(item.getItemId()==R.id.friends)
                {
                    Intent intent=new Intent(Homepage.this,Friends.class);
                    startActivity(intent);
                    finish();
                }
                if(item.getItemId()==R.id.events)
                {
                    Intent intent=new Intent(Homepage.this,Events.class);
                    startActivity(intent);
                    finish();
                }
                if(item.getItemId()==R.id.knowledge)
                {
                    Intent intent=new Intent(Homepage.this,Knowledge.class);
                    startActivity(intent);
                    finish();
                }
                return false;
            }
        });
        ImageView profilepic=header.findViewById(R.id.profilepic);
        TextView username=header.findViewById(R.id.username);
        TextView age=header.findViewById(R.id.age);
        String email= Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        DatabaseReference uref= FirebaseDatabase.getInstance().getReference("Users");
        DatabaseReference dref=FirebaseDatabase.getInstance().getReference();
        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren())
                {
                    if (Objects.equals(ds.getKey(), "alert1"))
                    {
                        alert1.setText(ds.getValue().toString());

                    }
                    if (Objects.equals(ds.getKey(), "alert2"))
                    {
                        alert2.setText(ds.getValue().toString());

                    }
                    if (Objects.equals(ds.getKey(), "alert3"))
                    {
                        alert3.setText(ds.getValue().toString());

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
                for(DataSnapshot ds:snapshot.getChildren()){
                    if(Objects.requireNonNull(ds.child("email").getValue()).toString().equals(email))
                        {
                            try {
                                username.setText(ds.child("Name").getValue().toString());


                            }catch(NullPointerException ignored){username.setText("Edit profile to add Username");}
                            try
                            {
                                age.setText(ds.child("Age").getValue().toString());
                            }catch(NullPointerException ignored){age.setText("Edit profile to add Age");}
                            try
                            {
                                Glide.with(getApplicationContext()).load(ds.child("ImageUrl").getValue().toString()).into(profilepic);
                            }catch(NullPointerException ignored){profilepic.setImageResource(R.drawable.profile_icon);}
                        }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Button learn=findViewById(R.id.learn);
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this,Knowledge.class));
                finish();

            }
        });
        Button find=findViewById(R.id.find);
        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this,Searchpage.class));
                finish();

            }
        });
        Button happening=findViewById(R.id.happening);
        happening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Homepage.this,Events.class));
                finish();
            }
        });





       }

    @Override
    public void onBackPressed() {
        if(!drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.open();
        }
        else {
            super.onBackPressed();
        }

    }
}