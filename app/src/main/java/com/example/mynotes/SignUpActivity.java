package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private EditText mSignUpEmail, mSignUpPassword;
    private RelativeLayout mSignUp;
    private TextView mGoToLogin;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();

        mSignUpEmail = findViewById(R.id.signUpEmail);
        mSignUpPassword = findViewById(R.id.signUpPassword);
        mSignUp = findViewById(R.id.signUp);
        mGoToLogin = findViewById(R.id.goToLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        mGoToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mSignUpEmail.getText().toString().trim();
                String password = mSignUpPassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else if (password.length()<7){
                    Toast.makeText(getApplicationContext(), "Password length should be more than 7", Toast.LENGTH_SHORT).show();
                } else {
                    //register the user to firebase
                    firebaseAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplicationContext(), "Registration successful", Toast.LENGTH_SHORT).show();
                                sendEmailVerification();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
    //send email verification
    private void sendEmailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //if user exists
        if (firebaseUser != null){
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Verification email is sent, Verify and Login again", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Failed to send verification email", Toast.LENGTH_SHORT).show();
        }
    }
}