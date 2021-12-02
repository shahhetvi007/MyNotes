package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateNote extends AppCompatActivity {

    EditText mCreateTitleNote, mCreateContentNote;
    FloatingActionButton mSaveNote;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    FirebaseFirestore firebaseFirestore;
    ProgressBar mCreateNotePB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        mSaveNote = findViewById(R.id.saveNote);
        mCreateTitleNote = findViewById(R.id.createTitleNote);
        mCreateContentNote = findViewById(R.id.createContentNote);
        mCreateNotePB = findViewById(R.id.createNotePB);

        Toolbar toolbar = findViewById(R.id.toolbarCreateNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        mSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mCreateTitleNote.getText().toString();
                String content = mCreateContentNote.getText().toString();
                if (title.isEmpty() || content.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Both fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    mCreateNotePB.setVisibility(View.VISIBLE);
                    DocumentReference documentReference = firebaseFirestore.collection("notes")
                            .document(firebaseUser.getUid()).collection("myNotes").document();
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", title);
                    note.put("content", content);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Note created successfully", Toast.LENGTH_SHORT).show();
                            mCreateNotePB.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(CreateNote.this, NotesActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to create note", Toast.LENGTH_SHORT).show();
                            mCreateNotePB.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}