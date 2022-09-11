package com.dennis_brink.android.takenotev2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Locale;

public class UpsertNoteActivity extends AppCompatActivity implements FunctionConstants {

    EditText editTextTitleUpdate, editTextNoteUpdate;
    ImageView imgSave, imgDelete, imgRecord, imgClear;
    int id;
    String function;
    ActivityResultLauncher<Intent> activityResultLauncherSpeechToText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("Note");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(R.layout.activity_upsert_note);

        // make room for the keyboard (make type area smaller)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        editTextTitleUpdate = findViewById(R.id.editTextTitleUpdate);
        editTextNoteUpdate = findViewById(R.id.editTextNoteUpdate);
        imgSave = findViewById(R.id.imgSave);
        imgDelete = findViewById(R.id.imgDelete);
        imgRecord = findViewById(R.id.imgRecord);
        imgClear = findViewById(R.id.imgClear);

        registerActivityResultLauncherSpeechToText();

        getData();
        configureScreen(function);

        imgSave.setOnClickListener(view -> {
            saveNote();
        });

        imgDelete.setOnClickListener(view -> {
            deleteNote();
        });

        imgRecord.setOnClickListener(view -> {
            recordNote();
        });

        imgClear.setOnClickListener(view -> {
            clearNote();
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
            imgRecord.setVisibility(View.VISIBLE);
        }
    }

    private void saveNote(){

        String currentTitle = editTextTitleUpdate.getText().toString();
        String currentDescription = editTextNoteUpdate.getText().toString();

        Log.d("DENNIS_B", "updated title: " + currentTitle);
        Log.d("DENNIS_B", "id : " + id);
        Log.d("DENNIS_B", "function : " + function);

        if(currentTitle.equals("") || currentDescription.equals("")){
            // empty note? do not create or update this
            Log.d("DENNIS_B", "empty title or empty description. note will not be created");
        }

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

        Log.d("DENNIS_B", "UpsertNoteActivity.deleteNote: note id = " + id);
        Log.d("DENNIS_B", "UpsertNoteActivity.deleteNote: function = " + function);

        Intent i = new Intent();
        i.putExtra("function", DEL);
        if(id != -1) {
            i.putExtra("id", id);
            setResult(RESULT_OK, i);
            finish();
        }
    }

    private void recordNote() {

        Log.d("DENNIS_B", "UpsertNoteActivity.recordNote: start");

        // first version will start with device language to listen to.
        // the first three words of the spoken text will be used as a title,
        // like Word gets the initial proposed file name based on the first
        // few words of the document

        Log.d("DENNIS_B", "UpsertNoteActivity.recordNote: prepare to listen for " + Locale.getDefault());

        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault()); // device language

        activityResultLauncherSpeechToText.launch(i);

    }

    public void registerActivityResultLauncherSpeechToText(){
        activityResultLauncherSpeechToText = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    ArrayList<String> speakResults = result.getData().getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Log.d("DENNIS_B", "UpsertNoteActivity.registerActivityResultLauncherSpeechToText: converted speech to text");

                    if (editTextNoteUpdate.getText().toString().equals("")){
                        editTextNoteUpdate.setText(speakResults.get(0));
                    } else { // append
                        editTextNoteUpdate.setText(editTextNoteUpdate.getText().toString().trim() + " " + speakResults.get(0));
                    }
                    if(editTextTitleUpdate.getText().toString().equals("") && !editTextNoteUpdate.getText().toString().equals("")) {
                        // get the first three words (if possible) and use it for a title
                        String words[] = speakResults.get(0).split("\\W+"); //regular expression !!

                        Log.d("DENNIS_B", "UpsertNoteActivity.registerActivityResultLauncherSpeechToText: found " + words.length + " words in this text");

                        for(int i = 0; i<=words.length; i++){
                            Log.d("DENNIS_B", "UpsertNoteActivity.registerActivityResultLauncherSpeechToText: i = " + i);
                            if (i == 2){
                                break;
                            }
                            if(i == 0){
                                editTextTitleUpdate.setText(words[0]);
                            }else{
                                editTextTitleUpdate.setText(editTextTitleUpdate.getText().toString().trim() + " " + words[i]);
                            }
                        }
                    }
                }
            }
        );
    }

    private void clearNote() {
        editTextNoteUpdate.setText("");
        editTextTitleUpdate.setText("");
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.d("DENNIS_B", "UpsertNoteActivity.onOptionsItemSelected: menu item " + item.getItemId());

        switch(item.getItemId()){
            case android.R.id.home:
                Log.d("DENNIS_B", "UpsertNoteActivity.onOptionsItemSelected: back arrow in menu toolbar clicked");
                finish(); // it's enough just to close this activity only
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}