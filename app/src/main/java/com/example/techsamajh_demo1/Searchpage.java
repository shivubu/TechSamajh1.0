package com.example.techsamajh_demo1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
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

public class Searchpage extends AppCompatActivity implements RecyclerView_interface {
    DatabaseReference userref= FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference intref=FirebaseDatabase.getInstance().getReference("Interests");
    Button go;
    RecyclerView recyclerView;
    Spinner searchInt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_searchpage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        go=findViewById(R.id.button6);
        recyclerView=findViewById(R.id.recycler1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchInt=findViewById(R.id.spinner4);
        intref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> interests = new ArrayList<String>();
                for(DataSnapshot snpshot:snapshot.getChildren())
                {
                    if(!Objects.requireNonNull(snpshot.getValue()).toString().equals("None")) {
                        interests.add(String.valueOf(snpshot.getValue()));
                    }

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Searchpage.this, android.R.layout.simple_spinner_dropdown_item, interests);
                searchInt.setAdapter(adapter);
                searchInt.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ((TextView) searchInt.getSelectedView()).setTextColor(Color.BLACK);
                    }
                });
                searchInt.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        go.setOnClickListener(new View.OnClickListener() {
                            RVAdapter adapter;
                            @Override
                            public void onClick(View v) {
                                if(searchInt.getSelectedItem().toString().equals("None"))
                                {
                                    Toast.makeText(Searchpage.this, "Select an Interest", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    String interest=searchInt.getSelectedItem().toString();
                                    ArrayList<UserModel> users=new ArrayList<>();
                                    userref.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot){
                                            for(DataSnapshot ds:snapshot.getChildren())
                                            {

                                                String interest1=ds.child("interest1").getValue(String.class);
                                                String interest2=ds.child("interest2").getValue(String.class);
                                                String interest3=ds.child("interest3").getValue(String.class);
                                                if(interest1.equals(interest)||interest2.equals(interest)||interest3.equals(interest))
                                                {
                                                    if(!ds.child("email").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                                                    {
                                                        users.add(new UserModel(ds.child("Name").getValue(String.class), ds.child("Age").getValue(String.class), ds.child("City").getValue(String.class), ds.child("ImageUrl").getValue(String.class)));
                                                    }
                                                }
                                            }
                                            adapter=new RVAdapter(Searchpage.this,users,Searchpage.this);
                                            recyclerView.setAdapter(adapter);


                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }

                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Searchpage.this, Homepage.class));
        finish();
    }

    @Override
    public void onItemClick(String name, String age, String city) {
        Intent intent=new Intent(Searchpage.this,SearchUserProfile.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("name",name);
        intent.putExtra("age",age);
        intent.putExtra("city",city);
        startActivity(intent);
        finish();
    }
}