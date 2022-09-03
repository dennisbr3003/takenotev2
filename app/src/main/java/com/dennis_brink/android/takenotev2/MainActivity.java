package com.dennis_brink.android.takenotev2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

// TODO
// -- save last opened note id an reopen it on launch (if none was opened launch lister)
// -- lister should not show complete note but just the first few lines
// -- color picker for background
// -- Decent error message template (layout that can show any error message)
// -- Edit text full screen for update (see also add)
// -- Maybe save a date created and a date updated
// -- security (password/pin/photo points)
// -- add note should be a fab (with image mounted like in math thingy)
// -- icons and design

public class MainActivity extends AppCompatActivity implements FunctionConstants{

    private NoteViewModel noteViewModel;
    ActivityResultLauncher<Intent> activityResultLauncherAddNote;
    ActivityResultLauncher<Intent> activityResultLauncherUpdateNote;

    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupLogo();

        // important
        registerActivityAddNote();
        registerActivityUpdateNote();

        RecyclerView recyclerView = findViewById(R.id.rcvNotes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabAdd = findViewById(R.id.fabAdd);

        NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication())
                                              .create(NoteViewModel.class);

        /* create an observer that will report changes */
        noteViewModel.getAllNotesById().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                // update recycler view
                adapter.setNotes(notes);
            }
        });

        adapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent i = new Intent(MainActivity.this, UpdateNoteActivity.class);
                i.putExtra("title", note.getTitle());
                i.putExtra("description", note.getDescription());
                i.putExtra("id", note.getId());
                i.putExtra("function", UPD);

                ///activityResultLauncher
                activityResultLauncherUpdateNote.launch(i);
            }
        });

        fabAdd.setOnClickListener(view -> {

            Intent i = new Intent(MainActivity.this, UpdateNoteActivity.class);
            i.putExtra("function", ADD);

            ///activityResultLauncher
            activityResultLauncherUpdateNote.launch(i);

        });

    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String title = data.getStringExtra("noteTitle");
            String text = data.getStringExtra("noteText");
            Note note = new Note(title, text);
            noteViewModel.insert(note);
        }

    }
*/
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Log.d("DENNIS_B", "menu item " + item.getItemId());

        switch(item.getItemId()){
            case R.id.top_menu:
               Intent i = new Intent(MainActivity.this, AddNoteActivity.class);
               //startActivityForResult(i, 1);
               //activityResultLauncher
                activityResultLauncherAddNote.launch(i);
               return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        //return super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.new_menu, menu);
        return true;
    }

    public void registerActivityUpdateNote(){
        activityResultLauncherUpdateNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                int resultcode = result.getResultCode();
                Intent data = result.getData();

                if(resultcode == RESULT_OK && data != null){

                    String title = data.getStringExtra("title");
                    String text = data.getStringExtra("description");
                    String function = data.getStringExtra("function");
                    int id = data.getIntExtra("id", -1);

                    Log.d("DENNIS_B", "Id " + id);
                    Log.d("DENNIS_B", "Function " + function);

                    Note note = new Note(title, text);

                    switch (function){
                        case ADD:
                            noteViewModel.insert(note);
                            break;
                        case UPD:
                            note.setId(id);
                            noteViewModel.update(note);
                            break;
                        case DEL:
                            note.setId(id);
                            noteViewModel.delete(note);
                            break;
                    }

                }

            }
        });
    }

    public void registerActivityAddNote(){
        activityResultLauncherAddNote = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                int resultcode = result.getResultCode();
                Intent data = result.getData();
                // data is the intent
                if(resultcode == RESULT_OK && data != null){
                    String title = data.getStringExtra("noteTitle");
                    String text = data.getStringExtra("noteText");
                    Note note = new Note(title, text);
                    noteViewModel.insert(note);
                }

            }
        });
    }

    private void setupLogo(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.tnv2_logo_padding);
        getSupportActionBar().setTitle("Take Note");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

}