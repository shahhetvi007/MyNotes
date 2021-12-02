package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText mForgotPassword;
    private Button mPasswordRecover;
    private TextView mGoBackToLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        getSupportActionBar().hide();

        mForgotPassword = findViewById(R.id.forgotPassword);
        mPasswordRecover = findViewById(R.id.passwordRecover);
        mGoBackToLogin = findViewById(R.id.goBackToLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        mGoBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPassword.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mPasswordRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mForgotPassword.getText().toString().trim();
                if (mail.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Enter your mail first", Toast.LENGTH_SHORT).show();
                } else {
                    //send password recover email
                    firebaseAuth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Mail sent, You can recover your password using mail",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(ForgotPassword.this, MainActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), "Mail is wrong or account does not exist",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}