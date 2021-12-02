package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private EditText mLoginEmail, mLoginPassword;
    private RelativeLayout mLogin, mGoToSignUp;
    private TextView mGoToForgotPassword;
    private ProgressBar mMainPB;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        mLoginEmail = findViewById(R.id.loginEmail);
        mLoginPassword = findViewById(R.id.loginPassword);
        mLogin = findViewById(R.id.login);
        mGoToForgotPassword = findViewById(R.id.goToForgotPassword);
        mGoToSignUp = findViewById(R.id.goToSignUp);
        mMainPB = findViewById(R.id.mainPB);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null){
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        }

        mGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        mGoToForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(intent);
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = mLoginEmail.getText().toString().trim();
                String password = mLoginPassword.getText().toString().trim();

                if (mail.isEmpty() || password.isEmpty()){
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    //login the user
                    mMainPB.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                checkMailVerification();
                            } else {
                                Toast.makeText(getApplicationContext(), "Account does not exist", Toast.LENGTH_SHORT).show();
                                mMainPB.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkMailVerification(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser.isEmailVerified()==true){
            Toast.makeText(getApplicationContext(), "Logged In", Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this, NotesActivity.class));
        } else {
            mMainPB.setVisibility(View.INVISIBLE);
            Toast.makeText(getApplicationContext(), "Verify your email first", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}