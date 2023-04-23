package com.example.mynciapp;

import static androidx.constraintlayout.widget.StateSet.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextView createnewAccount;

    EditText emailTF, pswdTF;
    Button loginBTN;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createnewAccount=findViewById(R.id.login_newAcc);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        emailTF=findViewById(R.id.login_emailTF);
        pswdTF=findViewById(R.id.login_PswdTF);
        loginBTN=findViewById(R.id.login_btn);
        progressDialog = new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();




        createnewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });

        loginBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });



    } //OnCreate override

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser !=null){
            sendHomeActivity();
        }
    }


    private void performLogin() {
        String email=emailTF.getText().toString();
        String password=pswdTF.getText().toString();


        if(!email.matches(emailPattern)){
            emailTF.setError("Enter valid email");
        }else if(password.isEmpty()){
            pswdTF.setError("Please Enter a Password");
        }else if(password.length()<6){
            pswdTF.setError("Password must have at least 6 characters");
        }else {
            progressDialog.setMessage("Please wait while we log you in");
            progressDialog.setTitle("Login");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());

                        // Read the isAdmin field of the current user
                        userRef.child("isAdmin").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Boolean isAdmin = dataSnapshot.getValue(Boolean.class);
                                if (isAdmin != null && isAdmin) {
                                    sendAdminActivity(); // If the user is an admin, send them to the AdminActivity
                                } else {
                                    sendHomeActivity(); // If the user is not an admin, send them to the HomeActivity
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Log any errors or handle them as needed
                                Log.w(TAG, "Failed to read isAdmin:", databaseError.toException());
                            }
                        });
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this, ""+task.getException(),Toast.LENGTH_SHORT).show();
                    }

                }
            });


        }// else


    }

    private void sendAdminActivity() {
        startActivity(new Intent(MainActivity.this, AdminActivity.class));
    }

    private void sendHomeActivity() {
        Intent intent=new Intent(MainActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}