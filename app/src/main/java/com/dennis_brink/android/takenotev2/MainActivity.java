package com.dennis_brink.android.takenotev2;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.github.clans.fab.FloatingActionButton;

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
    ActivityResultLauncher<Intent> activityResultLauncherUpdateNote;

    FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupLogo();

        // important
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

    private void setupLogo(){
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.takenote_logo_white_padding);
        getSupportActionBar().setTitle("Take Note");
        getSupportActionBar().setDisplayUseLogoEnabled(true);
    }

}