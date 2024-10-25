package com.example.techsamajh_demo1;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Objects;

public class Editprofile extends AppCompatActivity {
    FirebaseAuth auth=FirebaseAuth.getInstance();
    DatabaseReference userref= FirebaseDatabase.getInstance().getReference("Users");
    DatabaseReference dref=FirebaseDatabase.getInstance().getReference();
    DatabaseReference intref=dref.child("Interests");
    StorageReference sref= FirebaseStorage.getInstance().getReference();
    String email;
    String interest1,interest2,interest3;
    EditText name,age,city;
    Button upload;
    Integer index;
    Spinner int1,int2,int3;
    ImageView profilepic;
    Uri imageuri;
    String imageurl;
    TextView cheat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editprofile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        email= Objects.requireNonNull(auth.getCurrentUser()).getEmail();
        name=findViewById(R.id.editTextText6);
        age=findViewById(R.id.editTextText7);
        city=findViewById(R.id.editTextText8);
        upload=findViewById(R.id.button3);
        int1=findViewById(R.id.spinner);
        int2=findViewById(R.id.spinner2);
        int3=findViewById(R.id.spinner3);
        profilepic=findViewById(R.id.imageView2);
        cheat=findViewById(R.id.textView25);
        Search();
        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, 2);
            }
        });
        intref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> interests = new ArrayList<String>();
                for(DataSnapshot snpshot:snapshot.getChildren())
                {
                    interests.add(String.valueOf(snpshot.getValue()));
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Editprofile.this, android.R.layout.simple_spinner_dropdown_item, interests);
                int1.setAdapter(adapter);
                int2.setAdapter(adapter);
                int3.setAdapter(adapter);
                int1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Editprofile.this.interest1=int1.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                int2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Editprofile.this.interest2=int2.getSelectedItem().toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                int3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Editprofile.this.interest3=int3.getSelectedItem().toString();
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
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid=cheat.getText().toString();
                Upload(userid);
            }
        });


    }
    private void Upload(String id)
    {
        DatabaseReference uref=userref.child(id);
        ContentResolver cr =getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        try {
            String type = mime.getExtensionFromMimeType(cr.getType(imageuri));
            sref.child(System.currentTimeMillis()+"."+type).putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    if(taskSnapshot.getMetadata()!=null)
                    {
                        if(taskSnapshot.getMetadata().getReference()!=null)
                        {
                            Task<Uri> result=taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    imageurl=uri.toString();
                                    uref.child("ImageUrl").setValue(Editprofile.this.imageurl);
                                }
                            });
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Editprofile.this, "Image Upload Failed", Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch(NullPointerException exception)
        {

        }

        if(!name.getText().toString().isEmpty())
        {
            uref.child("Name").setValue(name.getText().toString());
        }
        if(!age.getText().toString().isEmpty())
        {
            uref.child("Age").setValue(age.getText().toString());
        }
        if(!city.getText().toString().isEmpty())
        {
            uref.child("City").setValue(city.getText().toString());
        }
        uref.child("interest1").setValue(Editprofile.this.interest1);
        uref.child("interest2").setValue(Editprofile.this.interest2);
        uref.child("interest3").setValue(Editprofile.this.interest3);
        Toast.makeText(Editprofile.this, "Profile Updated", Toast.LENGTH_SHORT).show();

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
                        cheat.setText(ds.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2 && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageuri=data.getData();
            profilepic.setImageURI(imageuri);
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Editprofile.this, ProfilePage.class));
        finish();
    }


}