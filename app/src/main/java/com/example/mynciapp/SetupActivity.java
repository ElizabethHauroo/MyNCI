package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText username, firstname, lastname, coursecode;
    private Button saveinfo_btn;
    private CircleImageView profileImage;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    String currentUser_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        username = (EditText) findViewById(R.id.usernameTF_setup);
        firstname = (EditText) findViewById(R.id.firstnameTF_setup);
        lastname = (EditText) findViewById(R.id.lastnameTF_setup);
        coursecode = (EditText) findViewById(R.id.coursenameTF_setup);
        saveinfo_btn = (Button) findViewById(R.id.setup_btn);
        profileImage= (CircleImageView) findViewById(R.id.photo_setup);

        mAuth = FirebaseAuth.getInstance();
        currentUser_ID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser_ID);



        saveinfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveAccountSetupInfo();
            }
        });

    }

    private void SaveAccountSetupInfo() {
        String user_name = username.getText().toString();
        String first_name = firstname.getText().toString();
        String last_name = lastname.getText().toString();
        String course_code = coursecode.getText().toString();

        if(TextUtils.isEmpty(user_name)){
            Toast.makeText(this, "Please add a Username", Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(first_name)){
            Toast.makeText(this, "Please add your first name", Toast.LENGTH_SHORT).show();

        }
        if(TextUtils.isEmpty(last_name)){
            Toast.makeText(this, "Please add your last name", Toast.LENGTH_SHORT).show();

        }
        /*
        if(TextUtils.isEmpty(course_code)){
            Toast.makeText(this, "Please add your course code", Toast.LENGTH_SHORT).show();

        }
        */else{
            HashMap userMap = new HashMap();
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
                    }else {
                        String message = task.getException().getMessage();
                        Toast.makeText(SetupActivity.this, "Error has occured: "+ message, Toast.LENGTH_LONG).show();
                    }
                }
            });

        }


    }
}