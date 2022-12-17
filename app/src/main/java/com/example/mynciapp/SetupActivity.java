package com.example.mynciapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private EditText username, firstname, lastname, coursecode;
    private Button saveinfo_btn;
    private CircleImageView profileImage;

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
    }
}