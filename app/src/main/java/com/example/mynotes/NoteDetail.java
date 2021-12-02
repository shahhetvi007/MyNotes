package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteDetail extends AppCompatActivity {

    private TextView mTitleNote, mContentNote;
    FloatingActionButton mGoToEditNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        mTitleNote = findViewById(R.id.titleNote);
        mContentNote = findViewById(R.id.contentNote);
        mGoToEditNote = findViewById(R.id.goToEditNote);
        Toolbar toolbar = findViewById(R.id.toolbarDetailNote);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent data = getIntent();

        mGoToEditNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EditNote.class);
                intent.putExtra("title", data.getStringExtra("title"));
                intent.putExtra("content", data.getStringExtra("content"));
                intent.putExtra("noteId", data.getStringExtra("noteId"));
                v.getContext().startActivity(intent);
            }
        });
        mTitleNote.setText(data.getStringExtra("title"));
        mContentNote.setText(data.getStringExtra("content"));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}