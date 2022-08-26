package com.dennis_brink.android.takenotev2;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// This 'Entity' annotation will ensure that the room database wil create the required SQLite
// tables (named: note) for this entity. In TakeNoteV1 this was done directly and manually

@Entity(tableName = "note")
public class Note {

    @PrimaryKey(autoGenerate = true)  // create a pkey auto increment
    public int id;

    public String title;

    public String description;

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
