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
import android.widget.ImageView;

public class UpdateNoteActivity extends AppCompatActivity implements FunctionConstants {

    EditText editTextTitleUpdate, editTextNoteUpdate;
    ImageView imgSave, imgDelete;
    int id;
    String function;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_update_note);

        // make room for the keyboard (make type area smaller)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        editTextTitleUpdate = findViewById(R.id.editTextTitleUpdate);
        editTextNoteUpdate = findViewById(R.id.editTextNoteUpdate);
        imgSave = findViewById(R.id.imgSave);
        imgDelete = findViewById(R.id.imgDelete);

        getData();
        configureScreen(function);

        imgSave.setOnClickListener(view -> {
            saveNote();
        });

        imgDelete.setOnClickListener(view -> {
            deleteNote();
        });

    }

    public void getData(){
        Intent i = getIntent();
        id = i.getIntExtra("id", -1);
        function = i.getStringExtra("function");
        // in case of function ADD these are not sent and thus empty. That is correct.
        editTextTitleUpdate.setText(i.getStringExtra("title"));
        editTextNoteUpdate.setText(i.getStringExtra("description"));
    }

    private void configureScreen(String function){
        if(function.equals(ADD)){
            imgDelete.setVisibility(View.INVISIBLE);
        }
    }

    private void saveNote(){

        String currentTitle = editTextTitleUpdate.getText().toString();
        String currentDescription = editTextNoteUpdate.getText().toString();

        Log.d("DENNIS_B", "updated title: " + currentTitle);
        Log.d("DENNIS_B", "id : " + id);
        Log.d("DENNIS_B", "function : " + function);

        Intent i = new Intent();
        i.putExtra("title", currentTitle);
        i.putExtra("description", currentDescription);
        i.putExtra("function", function);
        if(function.equals(UPD)) {
            if (id != -1) {
                i.putExtra("id", id);
            }
            // do something, something went wrong
        }
        setResult(RESULT_OK, i);
        finish();
    }

    private void deleteNote(){

        Log.d("DENNIS_B", "id : " + id);
        Log.d("DENNIS_B", "function : " + function);

        Intent i = new Intent();
        i.putExtra("function", DEL);
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