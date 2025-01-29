package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SignupActivity extends AppCompatActivity {

    TextView textLogin;
    EditText signupfullname, signupEmail, signupMobile, signupPassword, signupConfirmPassword;
    ProgressBar progressBar;
    Button btnSignup;
    String txtFullName, txtEmail, txtMobile, txtPassword, txtConfirmPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        textLogin = findViewById(R.id.textLogin);
        signupfullname = findViewById(R.id.signupfullname);
        signupEmail = findViewById(R.id.signupEmail);
        signupMobile = findViewById(R.id.signupMobile);
        signupPassword = findViewById(R.id.signupPassword);
        signupConfirmPassword = findViewById(R.id.signupConfirmPassword);
        progressBar = findViewById(R.id.signupProgressBar);
        btnSignup = findViewById(R.id.btnSignup);

        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtFullName = signupfullname.getText().toString();
                txtEmail = signupEmail.getText().toString().trim();
                txtMobile = signupMobile.getText().toString().trim();
                txtPassword = signupPassword.getText().toString().trim();
                txtConfirmPassword = signupConfirmPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(txtFullName)) {
                    if (!TextUtils.isEmpty(txtEmail)) {
                        if (txtEmail.matches(emailPattern)) {
                            if (!TextUtils.isEmpty(txtMobile)) {
                                if (txtMobile.length() == 10) {
                                    if (!TextUtils.isEmpty(txtPassword)) {
                                        if (!TextUtils.isEmpty(txtConfirmPassword)) {
                                            if (txtConfirmPassword.equals(txtPassword)) {
                                                SignUpUser();
                                            } else {
                                                signupConfirmPassword.setError("Confirm Password and Password Should Be Same");
                                            }

                                        } else {
                                            signupConfirmPassword.setError("Confirm Password Field Can't Be Empty");
                                        }

                                    } else {
                                        signupPassword.setError("Password Field Can't Be Empty");
                                    }

                                } else {
                                    signupMobile.setError("Enter A Valid Mobile Number");
                                }

                            } else {
                                signupMobile.setError("Mobile Number Field Can't Be empty ");
                            }

                        } else {
                            signupEmail.setError("Enter A Valid Email Address");
                        }
                    } else {
                        signupEmail.setError("Email Field Can't Be Empty");
                    }
                } else {
                    signupfullname.setError("Full Name Field Can't Be Empty");
                }
            }
        });

    }

    private void SignUpUser() {
        progressBar.setVisibility(View.VISIBLE);
        btnSignup.setVisibility(View.INVISIBLE);

        mAuth.createUserWithEmailAndPassword(txtEmail,txtPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Map<String, Object> user = new HashMap<>();
                user.put("FullName", txtFullName);
                user.put("Email", txtEmail);
                user.put("MobileNumber", txtMobile);
                user.put("Password", txtPassword);

                db.collection("users")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(SignupActivity.this, "Data Stored Successfully !", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(SignupActivity.this, "Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignupActivity.this,"Error" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                btnSignup.setVisibility(View.VISIBLE);
            }
        });

    }
}