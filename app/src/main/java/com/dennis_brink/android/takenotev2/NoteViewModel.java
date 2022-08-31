package com.dennis_brink.android.takenotev2;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<Note>> notes;

    public NoteViewModel(@NonNull Application application) {
        super(application);

        repository = new NoteRepository(application);
        Log.d("DENNIS_B", "NoteViewModel:NoteViewModel, query on notes in repository class executed");
        notes = repository.getAllNotesById();

    }

    public void insert(Note note){
        repository.insert(note);
    }

    public void update(Note note){
        repository.update(note);
    }

    public void delete(Note note){
        repository.delete(note);
    }

    public LiveData<List<Note>> getAllNotesById(){
        Log.d("DENNIS_B", "NoteViewModel:getAllNotesById, returned list generated in constructor");
        return notes;
    }

}
