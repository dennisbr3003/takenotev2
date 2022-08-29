package com.dennis_brink.android.takenotev2;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDAO {

    // Room will create the insert methods for SQLite
    @Insert
    void insert(Note note);

    // Room will create the update methods for SQLite
    @Update
    void update(Note note);

    // Room will create the delete methods for SQLite
    @Delete
    void delete(Note note);

    // LiveData<> will ensure that any change will be directly forwarded to the UI (=listener)
    // Room will detect any SQL errors at compile time (as opposed to runtime)
    @Query("SELECT * FROM note ORDER BY id ASC")
    LiveData<List<Note>> getAllNotesById();

}
