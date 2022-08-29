package com.dennis_brink.android.takenotev2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateNoteActivity extends AppCompatActivity {

    EditText editTextTitleUpdate, editTextNoteUpdate;
    Button btnSaveUpdate, btnCancelUpdate;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Update Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_update_note);

        editTextTitleUpdate = findViewById(R.id.editTextTitleUpdate);
        editTextNoteUpdate = findViewById(R.id.editTextNoteUpdate);
        btnCancelUpdate = findViewById(R.id.btnCancelUpdate);
        btnSaveUpdate = findViewById(R.id.btnSaveUpdate);

        getData();

        btnCancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSaveUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateNote();
            }
        });

    }

    public void getData(){
        Intent i = getIntent();
        id = i.getIntExtra("id", -1);
        editTextTitleUpdate.setText(i.getStringExtra("title"));
        editTextNoteUpdate.setText(i.getStringExtra("description"));
    }

    private void updateNote(){

        String currentTitle = editTextTitleUpdate.getText().toString();
        String currentDescription = editTextNoteUpdate.getText().toString();

        Log.d("DENNIS_B", "updated title: " + currentTitle);
        Log.d("DENNIS_B", "id : " + id);

        Intent i = new Intent();
        i.putExtra("title", currentTitle);
        i.putExtra("description", currentDescription);
        if(id != -1) {
            i.putExtra("id", id);
            setResult(RESULT_OK, i);
            finish();
        }
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
}