package com.example.seminar.database;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.seminar.model.Note;
import com.example.seminar.supportConvert.ImageBitmapString;

@Database(entities = {Note.class}, version = 1, exportSchema = true)
@TypeConverters({ImageBitmapString.class})
public abstract class AppDataBase extends RoomDatabase {

    public abstract NoteDAO noteDAO();
    private static volatile AppDataBase appDataBase;
    protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration config) {
        return null;
    }

    @NonNull
    @Override
    protected InvalidationTracker createInvalidationTracker() {
        return null;
    }

    @Override
    public void clearAllTables() {

    }
}
