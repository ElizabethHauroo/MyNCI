package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText username, firstname, lastname, coursecode;
    private Button saveinfo_btn;
    private CircleImageView profilePic;
    private ProgressDialog loadingBar;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private StorageReference UserProfileImageRef;
    final static int Gallery_Pick = 1;
    String currentUser_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        // -------------- INITIALISATION / CASTING ----------------------

        username = (EditText) findViewById(R.id.usernameTF_setup);
        firstname = (EditText) findViewById(R.id.firstnameTF_setup);
        lastname = (EditText) findViewById(R.id.lastnameTF_setup);
        coursecode = (EditText) findViewById(R.id.coursenameTF_setup);
        saveinfo_btn = (Button) findViewById(R.id.setup_btn);
        profilePic= (CircleImageView) findViewById(R.id.photo_setup);
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();//establish a connection for this page
        currentUser_ID = mAuth.getCurrentUser().getUid();  //getting the unique user ID of the current user
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser_ID);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        UserProfileImageRef = storage.getReference().child("Profile Images").child(currentUser_ID + ".jpg");;


        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild("profileimage")){
                    String image = snapshot.child("profileimage").getValue().toString();
                    Picasso.get().load(image).placeholder(R.drawable.defaultprofile).into(profilePic);
                }else {
                    Toast.makeText(SetupActivity.this, "Please select profile image first.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SetupActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });

        // -------------- SAVE INFO BUTTON CLICK ----------------------
        saveinfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(SetupActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
                SaveAccountSetupInfo();
            }
        });

    }

    private void SaveAccountSetupInfo() {
        String user_name = username.getText().toString();
        String first_name = firstname.getText().toString();
        String last_name = lastname.getText().toString();
        String course_code_raw = coursecode.getText().toString();
        String course_code = course_code_raw.replace(" ",""); //no spaces

        if(TextUtils.isEmpty(user_name)){
            Toast.makeText(this, "Please add a Username", Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(first_name)){
            Toast.makeText(this, "Please add your first name", Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(last_name)){
            Toast.makeText(this, "Please add your last name", Toast.LENGTH_SHORT).show();

        }

        if(TextUtils.isEmpty(course_code)){
            Toast.makeText(this, "Please add your course code", Toast.LENGTH_SHORT).show();

        }
        else{

            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are creating your new Account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();//storing information within the Firebase Database
            userMap.put("username", user_name);
            userMap.put("firstname", first_name);
            userMap.put("lastname", last_name);
            userMap.put("course", course_code);
            userMap.put("gender", "Rather not say"); // default value

            //now we have to store it
            usersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(SetupActivity.this, "Your information was saved sucessfully", Toast.LENGTH_LONG).show();
                        //if statement for admin or not?
                        SendUsertoHome();
                        loadingBar.dismiss();
                    }else {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error has occured: "+ message, Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                }
            });

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null) {
            Uri ImageUri = data.getData();

            // Show a loading bar while the image is being uploaded
            loadingBar.setTitle("Uploading Profile Image");
            loadingBar.setMessage("Please wait, while we are uploading your profile image...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);

            final StorageReference filepath = UserProfileImageRef;

            filepath.putFile(ImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(SetupActivity.this, "Profile Image uploaded successfully", Toast.LENGTH_SHORT).show();

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();
                                usersRef.child("profileimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SetupActivity.this, "Profile image stored to Firebase Database successfully.", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        } else {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(SetupActivity.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }



    private void SendUsertoHome() {
        Intent intent=new Intent(SetupActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish(); //means the user can't come back to this page after
    }
}