package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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

public class RegisterActivity extends AppCompatActivity {

    // declaring variables
    TextView alreadyHaveAccount;
    EditText emailTF, pswdTF, confirmPswdTF;
    Button registerBTN;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+\\.[a-z]+";
    String emailDomain = ".ncirl.ie";
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyHaveAccount=findViewById(R.id.reg_alreadyAcc);

        // initializing all the variables
        emailTF=findViewById(R.id.login_emailTF);
        pswdTF=findViewById(R.id.reg_pswdTF);
        confirmPswdTF=findViewById(R.id.login_PswdTF);
        registerBTN=findViewById(R.id.reg_btn);
        progressDialog = new ProgressDialog(this);

        mAuth=FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();





        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            }
        }); //already have an account?

        registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAuthentication();
            }
        });


    }// Override

    private void PerformAuthentication() {
        String email=emailTF.getText().toString();
        String password=pswdTF.getText().toString();
        String confirmPassward = confirmPswdTF.getText().toString();

        if(!email.matches(emailPattern)){
            emailTF.setError("Enter valid email");
        }else if (!email.endsWith(emailDomain)) {
            emailTF.setError("Email must end with " + emailDomain);
        }else if(password.isEmpty()){
            pswdTF.setError("Please Enter a Password");
        }else if(password.length()<6){
            pswdTF.setError("Password must have at least 6 characters");
        }else if(!password.equals(confirmPassward)){
            confirmPswdTF.setError("Both passwords do not match");
        }else{
            progressDialog.setMessage("Please wait while we register your account");
            progressDialog.setTitle("Registration");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        sendSetupActivity();
                        Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }

                
            }); //authResult

        }


    }

    private void sendSetupActivity() {
        Intent intent=new Intent(RegisterActivity.this,SetupActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    /*
    private void sendHomeActivity() {
        Intent intent=new Intent(RegisterActivity.this,HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    */

}