package com.example.mynciapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingActivity extends AppCompatActivity {

    BottomNavigationView nav;
    Button update_courseBTN, changeImageButton, change_passwordBTN;
    TextView settings_fullname_txt, settings_studentNumber_txt, settings_course_txt;
    private ProgressDialog loadingBar;

    private CircleImageView profilePic;
    private DatabaseReference usersRef;
    private StorageReference UserProfileImageRef;
    String currentUser_ID;
    final static int Gallery_Pick = 1;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        //Bottom Navigation
        nav=findViewById(R.id.bottom_navigation_setting);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_bottomnav:
                        startActivity(new Intent(SettingActivity.this, HomeActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.add_bottomnav:
                        startActivity(new Intent(SettingActivity.this, AddActivity.class));
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.profile_bottomnav:
                        startActivity(new Intent(SettingActivity.this, ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        break;

                    default:
                }



                return true;
            }
        });

        loadingBar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        currentUser_ID = mAuth.getCurrentUser().getUid();
        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUser_ID);
        profilePic = findViewById(R.id.settings_my_profile_picture);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        UserProfileImageRef = storage.getReference().child("Profile Image").child(currentUser_ID + ".jpg");

        settings_fullname_txt=findViewById(R.id.settings_fullname_txt);
        settings_studentNumber_txt=findViewById(R.id.settings_studentNumber_txt);
        settings_course_txt=findViewById(R.id.settings_course_txt);

        update_courseBTN = findViewById(R.id.settings_update_courseBTN);
        changeImageButton = findViewById(R.id.settings_update_pictureBTN);
        change_passwordBTN = findViewById(R.id.settings_change_passwordBTN);

        getCurrentUserInfo();

        changeImageButton.setOnClickListener(new View.OnClickListener() {
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SettingActivity.this, "Error: "+error, Toast.LENGTH_SHORT).show();
            }
        });





        update_courseBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentCourse = settings_course_txt.getText().toString();
                showUpdateCourseDialog(currentCourse);
            }
        });

        change_passwordBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });


    } // ---------- OnCreate()

    private void getCurrentUserInfo() {
        mAuth = FirebaseAuth.getInstance();
        String currentUserID = mAuth.getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserID);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String firstName = snapshot.child("firstname").getValue(String.class);
                final String lastName = snapshot.child("lastname").getValue(String.class);
                String fullName = firstName + " "+lastName;
                settings_fullname_txt.setText(fullName);
                final String course = snapshot.child("course").getValue(String.class);
                settings_course_txt.setText(course);
                final String username = snapshot.child("username").getValue(String.class);
                settings_studentNumber_txt.setText(username);

                if (snapshot.hasChild("profileimage")) {
                    String profileImageUrl = snapshot.child("profileimage").getValue().toString();
                    Picasso.get().load(profileImageUrl).placeholder(R.drawable.defaultprofile).into(profilePic);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();
                                usersRef.child("profileimage").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(SettingActivity.this, "Profile Image updated successfully", Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        } else {
                                            String message = task.getException().getMessage();
                                            Toast.makeText(SettingActivity.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                                            loadingBar.dismiss();
                                        }
                                    }
                                });
                            }
                        });
                    } else {
                        String message = task.getException().getMessage();
                        Toast.makeText(SettingActivity.this, "Error Occurred: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }


    /*
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.settings_dialog_change_password, null);

        EditText currentPasswordEditText = view.findViewById(R.id.current_password_edit_text);
        EditText newPasswordEditText = view.findViewById(R.id.new_password_edit_text);
        EditText confirmNewPasswordEditText = view.findViewById(R.id.confirm_new_password_edit_text);

        builder.setView(view)
                .setTitle("Change Password")
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        validatePasswordChange(currentPasswordEditText, newPasswordEditText, confirmNewPasswordEditText);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }
    */

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.settings_dialog_change_password, null);

        EditText currentPasswordEditText = view.findViewById(R.id.current_password_edit_text);
        EditText newPasswordEditText = view.findViewById(R.id.new_password_edit_text);
        EditText confirmNewPasswordEditText = view.findViewById(R.id.confirm_new_password_edit_text);

        builder.setView(view)
                .setTitle("Change Password")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (validatePasswordChange(currentPasswordEditText, newPasswordEditText, confirmNewPasswordEditText)) {
                            String newPassword = newPasswordEditText.getText().toString().trim();
                            updatePasswordInFirebase(newPassword);
                            alertDialog.dismiss(); // Only close the dialog if the password has been updated successfully
                        }
                    }
                });
            }
        });

        alertDialog.show();
    }

    private boolean validatePasswordChange(EditText currentPasswordEditText, EditText newPasswordEditText, EditText confirmNewPasswordEditText) {
        String currentPassword = currentPasswordEditText.getText().toString();
        String newPassword = newPasswordEditText.getText().toString();
        String confirmNewPassword = confirmNewPasswordEditText.getText().toString();

        if (TextUtils.isEmpty(currentPassword)) {
            currentPasswordEditText.setError("Please enter your current password");
            return false;
        } else {
            currentPasswordEditText.setError(null);
        }

        if (TextUtils.isEmpty(newPassword) || newPassword.length() < 6) {
            newPasswordEditText.setError("Password must have at least 6 characters");
            return false;
        } else {
            newPasswordEditText.setError(null);
        }

        if (TextUtils.isEmpty(confirmNewPassword) || !newPassword.equals(confirmNewPassword)) {
            confirmNewPasswordEditText.setError("Both passwords do not match");
            return false;
        } else {
            confirmNewPasswordEditText.setError(null);
        }

        reauthenticateUser(currentPassword, newPassword, currentPasswordEditText);
        return true;
    }

    private void reauthenticateUser(String currentPassword, String newPassword, EditText currentPasswordEditText) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("PasswordUpdate", "Reauthentication successful");
                        updatePasswordInFirebase(newPassword);
                    } else {
                        Log.d("PasswordUpdate", "Reauthentication failed");
                        // The current password is incorrect, show an error message
                        currentPasswordEditText.setError("Incorrect current password");
                    }
                }
            });
        }
    }

    private void updatePasswordInFirebase(String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        // Update was successful, show a success message
                        Toast.makeText(SettingActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                        Log.d("PasswordUpdate", "Password update successful");
                    } else {
                        // Update failed, show an error message
                        Toast.makeText(SettingActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                        Log.d("PasswordUpdate", "Password update failed");
                    }
                }
            });
        }
    }

    private void showUpdateCourseDialog(String currentCourse) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.settings_dialog_update_course, null);

        EditText courseCodeEditText = view.findViewById(R.id.course_code_edit_text);

        getCurrentUserInfo();
        courseCodeEditText.setText(currentCourse);

        builder.setView(view)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String newCourseCode = courseCodeEditText.getText().toString().trim();
                        if (!newCourseCode.isEmpty()) {
                            // Save the new course code to Firebase
                            updateCourseCodeInFirebase(newCourseCode);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    private void updateCourseCodeInFirebase(String newCourseCode) {

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());
        userRef.child("course").setValue(newCourseCode).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Update was successful, show a success message
                } else {
                    // Update failed, show an error message
                }
            }
        });
        getCurrentUserInfo();
    }

}