package com.example.techsamajh_demo1;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {
    EditText reg_email,reg_password,confirm_password;
    Button signup;
    FirebaseAuth auth;
    TextView linkLogin;
    DatabaseReference dref= FirebaseDatabase.getInstance().getReference();
    DatabaseReference countref=dref.child("count");
    public String count;
    ImageButton p1,p2;
    int f1=0,f2=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        auth=FirebaseAuth.getInstance();
        reg_email=findViewById(R.id.editTextText3);
        reg_password=findViewById(R.id.editTextText4);
        confirm_password=findViewById(R.id.editTextText5);
        signup=findViewById(R.id.button2);
        linkLogin=findViewById(R.id.textView5);
        p1=findViewById(R.id.pass1);
        p2=findViewById(R.id.pass2);
        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f1==0)
                {
                    reg_password.setInputType(128);
                    reg_password.setSelection(reg_password.getText().length());
                    p1.setBackgroundColor(Color.RED);
                    f1=1;

                }
                else
                {
                    reg_password.setInputType(129);
                    reg_password.setSelection(reg_password.getText().length());
                    p1.setBackgroundColor(Color.WHITE);
                    f1=0;
                }
            }
        });
        p2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f2==0)
                {
                    confirm_password.setInputType(128);
                    confirm_password.setSelection(confirm_password.getText().length());
                    p2.setBackgroundColor(Color.RED);
                    f2=1;
                }
                else
                {
                    confirm_password.setInputType(129);
                    confirm_password.setSelection(confirm_password.getText().length());
                    p2.setBackgroundColor(Color.WHITE);
                    f2=0;
                }
            }
        });
        countref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count= Objects.requireNonNull(snapshot.getValue()).toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        linkLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkLogin.setTextColor(Color.RED);
                startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
                finish();
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registerUser();
                }
        });



    }
    private void registerUser()
    {
        String email=reg_email.getText().toString();
        String password=reg_password.getText().toString();
        String confirm=confirm_password.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            reg_email.setError("Enter Email");
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            reg_password.setError("Enter Password");
            return;
        }
        if(TextUtils.isEmpty(confirm))
        {
            confirm_password.setError("Enter Password");
            return;
        }
        if(!password.equals(confirm))
        {
            confirm_password.setError("Passwords do not match");
            return;
        }
        if(!email.contains("@") && !email.contains(".") && !email.contains("com"))
        {
            Toast.makeText(SignUpActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(password.length()<6 || password.length()>16)
        {
            reg_password.setError("Password length should be greater than 6 and less than 16");
            return;
        }
        else {
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Integer count1=Integer.parseInt(count)+1;
                    countref.setValue(count1.toString());
                    dref.child("Users").child("User"+count1.toString()).child("email").setValue(email);
                    Toast.makeText(SignUpActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUpActivity.this, "Registration Unsuccessful.Please recheck the email and password.", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(SignUpActivity.this,LoginActivity.class));
        finish();
    }

}