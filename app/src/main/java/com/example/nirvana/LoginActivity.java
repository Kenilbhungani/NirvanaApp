package com.example.nirvana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity {

    TextView textReg,forgot;
    EditText LoginEmail, LoginPassword;
    ProgressBar progressBar;
    Button btnLogin;
    String txtEmail, txtPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textReg = findViewById(R.id.textReg);
        LoginEmail = findViewById(R.id.loginEmail);
        LoginPassword = findViewById(R.id.loginPassword);
        progressBar = findViewById(R.id.loginProgressBar);
        btnLogin = findViewById(R.id.btnLogin);
        forgot = findViewById(R.id.forget);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();

        textReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtEmail = LoginEmail.getText().toString().trim();
                txtPassword = LoginPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(txtEmail)) {
                    if (txtEmail.matches(emailPattern)) {
                        if (!TextUtils.isEmpty(txtPassword)) {
                            LogInUser();
                        } else {
                            LoginPassword.setError("Password Field Can't Be Empty");
                        }

                    } else {
                        LoginEmail.setError("Enter A Valid Email Address");
                    }

                } else {
                    LoginEmail.setError("Email Field Can't Be Empty");
                }

            }
        });

    }

    private void LogInUser() {
        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.INVISIBLE);

        mAuth.signInWithEmailAndPassword(txtEmail,txtPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(LoginActivity.this, "Login Successful !", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, "Error - " + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
            }
        });
    }
}