package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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

public class EditNote extends AppCompatActivity {

    private EditText mEditTitleNote, mContentEditNote;
    Intent data;
    FloatingActionButton saveEditNote;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        mEditTitleNote = findViewById(R.id.editTitleNote);
        mContentEditNote = findViewById(R.id.editContentNote);
        saveEditNote = findViewById(R.id.saveEditNote);
        Toolbar toolbar = findViewById(R.id.toolbarEditNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        data = getIntent();

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        String noteTitle = data.getStringExtra("title");
        String noteContent = data.getStringExtra("content");
        mEditTitleNote.setText(noteTitle);
        mContentEditNote.setText(noteContent);

        saveEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newTitle = mEditTitleNote.getText().toString();
                String newContent = mContentEditNote.getText().toString();
                if (newTitle.isEmpty() || newContent.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Something is empty", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    DocumentReference documentReference = firebaseFirestore.collection("notes")
                            .document(firebaseUser.getUid()).collection("myNotes")
                            .document(data.getStringExtra("noteId"));
                    Map<String, Object> note = new HashMap<>();
                    note.put("title", newTitle);
                    note.put("content", newContent);
                    documentReference.set(note).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Note Updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(EditNote.this, NotesActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed to Update", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}