package com.dennis_brink.android.takenotev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class AddNoteActivity extends AppCompatActivity {

    EditText editTextTitle, editTextNote;
    Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Add Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_add_note);
        // make room for the keyboard (make type area smaller)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextNote = findViewById(R.id.editTextNote);
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNote();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.d("DENNIS_B", "menu item " + item.getItemId());

        switch(item.getItemId()){
            case android.R.id.home:
                Log.d("DENNIS_B", "Back arrow in menu toolbar clicked");
                finish(); // it's enough just to close this activity only
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveNote(){
        String noteTitle = editTextTitle.getText().toString();
        String noteText = editTextNote.getText().toString();

        // later send it to an receiver
        Intent i = new Intent();
        i.putExtra("noteTitle", noteTitle);
        i.putExtra("noteText", noteText);
        setResult(RESULT_OK, i);
        finish();

    }

}