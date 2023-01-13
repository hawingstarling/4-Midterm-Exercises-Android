package com.example.seminar.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.seminar.model.Note;

import java.util.List;

@Dao
public interface NoteDAO {
    @Query("SELECT * FROM Note")
    List<Note> getAllNotesList();

    @Query("DELETE FROM Note")
    void truncateTheList();

    @Insert
    void insertDataInfoNoteList(Note note);

    @Query("DELETE FROM Note WHERE noteId = :noteId")
    void deleteNoteFromId(int noteId);

    @Query("SELECT * FROM Note WHERE noteId = :noteId")
    Note selectNoteFromAnId(int noteId);

    @Query("UPDATE Note SET noteTitle = :noteTitle, noteDescription = :noteDescription, date = :date, image = :noteImage WHERE noteId = :noteId")
    void updateAnExistingRow(int noteId, String noteTitle, String noteDescription, String date, byte[] noteImage);

    @Query("UPDATE Note SET isComplete = :isComplete WHERE noteId = :noteId")
    void updateIsComplete(boolean isComplete, int noteId);
}
