package com.dennis_brink.android.takenotev2;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.AdapterView;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NoteRepository {

    private NoteDAO noteDAO;
    private LiveData<List<Note>> notes;


    ExecutorService executors = Executors.newSingleThreadExecutor();

    public NoteRepository(Application application){
        NoteDatabase db = NoteDatabase.getInstance(application);
        noteDAO = db.noteDAO(); // Room added this method so we can call it
        Log.d("DENNIS_B", "NoteRepository:NoteRepository, query on notes in repository class executed");
        notes = noteDAO.getAllNotesById(); // we did define this (the SQL query) so the List object will be filled
    }

    // database operations are not allowed on the main thread for the operation may lock the UI for a long time
    // callback procedure is not needed (as in async task) since the note List object will be monitored by LiveData. If something
    // changes (note is saved, updated or deleted) the UI will be notified (and) modified through LiveData. The executor method needs
    // significantly less code then the async task

    public void insert(Note note){
        //new InsertNoteAsyncTask(noteDAO).execute(note);
        executors.execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.insert(note);
            }
        });
    }

    public void update (Note note){
        //new UpdateNoteAsyncTask(noteDAO).execute(note);
        executors.execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.update(note);
            }
        });
    }

    public void delete (Note note){
        //new DeleteNoteAsyncTask(noteDAO).execute(note);
        executors.execute(new Runnable() {
            @Override
            public void run() {
                noteDAO.delete(note);
            }
        });
    }

    public LiveData<List<Note>> getAllNotesById(){
        Log.d("DENNIS_B", "NoteRepository:getAllNotesById, returned list generated in constructor");
        return notes;
        //return noteDAO.getAllNotesById();
    }

/*
    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDAO noteDAO;

        private InsertNoteAsyncTask(NoteDAO noteDAO){
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.insert(notes[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDAO noteDAO;

        private UpdateNoteAsyncTask(NoteDAO noteDAO){
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void>{

        private NoteDAO noteDAO;

        private DeleteNoteAsyncTask(NoteDAO noteDAO){
            this.noteDAO = noteDAO;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDAO.delete(notes[0]);
            return null;
        }
    }
*/

}
