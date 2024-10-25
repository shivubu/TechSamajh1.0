package com.example.techsamajh_demo1;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.common.UserRecoverableException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.UserWriteRecord;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    public String email,password,resetemail;
    FirebaseAuth auth;
    EditText emailText,passwordText,r_email;
    Button loginButton,reset;
    TextView linkSignUp;
    ImageButton pass;
    int flag=0,f1=0;
    static int f=0;
    TextView forgot_link,forgot_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(LoginActivity.this,Homepage.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
        emailText = findViewById(R.id.editTextText);
        passwordText = findViewById(R.id.editTextText2);
        loginButton = findViewById(R.id.button);
        linkSignUp = findViewById(R.id.textView2);
        pass=findViewById(R.id.lookpass);
        forgot_link=findViewById(R.id.forgot_link);
        forgot_message=findViewById(R.id.forgot_message);
        r_email=findViewById(R.id.reset_email);
        reset=findViewById(R.id.reset_button);
        forgot_message.setVisibility(View.INVISIBLE);
        r_email.setVisibility(View.INVISIBLE);
        reset.setVisibility(View.INVISIBLE);
        forgot_link.setTextColor(Color.CYAN);
        ImageView icon=findViewById(R.id.icon);
        icon.setVisibility(View.INVISIBLE);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();
            }
        });
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linkSignUp.setTextColor(Color.RED);
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            }
        });
        pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0)
                {
                    passwordText.setInputType(InputType.TYPE_CLASS_TEXT);
                    passwordText.setSelection(passwordText.getText().length());
                    pass.setBackgroundColor(Color.RED);
                    flag=1;
                }
                else {
                    passwordText.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordText.setSelection(passwordText.getText().length());
                    pass.setBackgroundColor(Color.WHITE);
                    flag=0;
                }

            }
        });
        forgot_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(f1==0)
                {
                    forgot_link.setTextColor(Color.RED);
                    forgot_message.setVisibility(View.VISIBLE);
                    r_email.setVisibility(View.VISIBLE);
                    reset.setVisibility(View.VISIBLE);
                    icon.setVisibility(View.VISIBLE);
                    f1=1;
                }
                else
                {
                    forgot_link.setTextColor(Color.CYAN);
                    forgot_message.setVisibility(View.INVISIBLE);
                    r_email.setVisibility(View.INVISIBLE);
                    reset.setVisibility(View.INVISIBLE);
                    icon.setVisibility(View.INVISIBLE);
                    f1=0;
                }

            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetemail=r_email.getText().toString();
                DatabaseReference uref= FirebaseDatabase.getInstance().getReference().child("Users");
                uref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        f=0;
                        for(DataSnapshot ds: snapshot.getChildren())
                        {
                            if(Objects.requireNonNull(ds.child("email").getValue()).toString().equals(resetemail))
                            {
                                f=1;
                                FirebaseAuth.getInstance().sendPasswordResetEmail(resetemail).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(LoginActivity.this, "Password reset Link sent Successfully.", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        }
                        if(f==0)
                        {
                            Toast.makeText(LoginActivity.this, "No such email exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if(TextUtils.isEmpty(resetemail))
                {
                    r_email.setError("Enter Email");
                    return;
                }
                if(!resetemail.contains("@") && !resetemail.contains("."))
                {
                    Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }
    public void loginUser(){
        email=emailText.getText().toString();
        password=passwordText.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            emailText.setError("Enter Email");
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            passwordText.setError("Enter Password");
            return;
        }
        if(!email.contains("@") && !email.contains("."))
        {
            Toast.makeText(LoginActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,Homepage.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Login Unsuccessful.Please recheck the email and password.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}