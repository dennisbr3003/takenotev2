package com.dennis_brink.android.takenotev2;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    private static NoteDatabase instance; // So it will be available in every corner of the app

    public abstract NoteDAO noteDAO(); // Room database will handle implementation so no body needed here for this method

    public static synchronized NoteDatabase getInstance(Context context){
        if(instance==null){
            // This is standard
            instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_db")
                           .fallbackToDestructiveMigration()
                        // .allowMainThreadQueries() <-- don't do this
                           .addCallback(roomCallback)
                           .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){

        // This method is executed once after installation of the app when the database is being created. Five notes are initially inserted
        // so that there will be some data to show (test purposes, this will be removed later)
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            //new PopulateDatabaseAsyncTask(instance).execute();

            NoteDAO noteDAO = instance.noteDAO(); // instance represents the database object (this class actually)
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    noteDAO.insert(new Note("Note 0", "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Error neque recusandae est iure, assumenda eum officia labore officiis deserunt commodi soluta, praesentium, eligendi inventore corporis eaque dolorum et! Ut, veritatis!"));
                    noteDAO.insert(new Note("Note 1", "Lorem ipsum dolor sit amet consectetur adipisicing elit. Perferendis, saepe."));
                    noteDAO.insert(new Note("Note 2", "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Consequatur commodi nobis aperiam nesciunt necessitatibus."));
                    noteDAO.insert(new Note("Note 3", "Lorem ipsum dolor, sit amet consectetur adipisicing elit. Tempore provident perspiciatis labore"));
                    noteDAO.insert(new Note("Note 4", "Lorem ipsum dolor sit amet consectetur adipisicing elit. Ipsam tempora cum maiores nemo vitae voluptatum sequi fugit libero beatae quod."));
                    noteDAO.insert(new Note("Note 5", "Deze wil ik niet zien, dan klopt het wat ik denk"));
                }
            });

        }
    };

    /*
    private static class PopulateDatabaseAsyncTask extends AsyncTask<Void, Void, Void>{

        private NoteDAO noteDAO;

        private PopulateDatabaseAsyncTask(NoteDatabase db){
            noteDAO = db.noteDAO();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            noteDAO.insert(new Note("Note 0", "Lorem ipsum, dolor sit amet consectetur adipisicing elit. Error neque recusandae est iure, assumenda eum officia labore officiis deserunt commodi soluta, praesentium, eligendi inventore corporis eaque dolorum et! Ut, veritatis!"));
            noteDAO.insert(new Note("Note 1", "Lorem ipsum dolor sit amet consectetur adipisicing elit. Perferendis, saepe."));
            noteDAO.insert(new Note("Note 2", "Lorem ipsum dolor sit amet consectetur, adipisicing elit. Consequatur commodi nobis aperiam nesciunt necessitatibus."));
            noteDAO.insert(new Note("Note 3", "Lorem ipsum dolor, sit amet consectetur adipisicing elit. Tempore provident perspiciatis labore"));
            noteDAO.insert(new Note("Note 4", "Lorem ipsum dolor sit amet consectetur adipisicing elit. Ipsam tempora cum maiores nemo vitae voluptatum sequi fugit libero beatae quod."));
            return null;
        }
    }
    */
}
