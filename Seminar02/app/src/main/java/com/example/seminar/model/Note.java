package com.example.seminar.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    int noteId;
    @ColumnInfo(name = "noteTitle")
    String noteTitle;
    @ColumnInfo(name = "noteDescription")
    String noteDescription;
    @ColumnInfo(name = "date")
    String date;
    @ColumnInfo(name = "dateCurrent")
    String dateCurrent;
    @ColumnInfo(name = "isComplete")
    boolean isComplete;
    @ColumnInfo(name = "alertTimer")
    String alertTimer;
    @ColumnInfo(name = "image", typeAffinity = ColumnInfo.BLOB)
    byte[] noteImage;

    public Note() {

    }

    public Note(int noteId, String noteTitle, String noteDescription, String date, boolean isComplete, String alertTimer, byte[] noteImage) {
        this.noteId = noteId;
        this.noteTitle = noteTitle;
        this.noteDescription = noteDescription;
        this.date = date;
        this.isComplete = isComplete;
        this.alertTimer = alertTimer;
        this.noteImage = noteImage;
    }

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDescription() {
        return noteDescription;
    }

    public void setNoteDescription(String noteDescription) {
        this.noteDescription = noteDescription;
    }

    public String getDateCurrent() {
        return dateCurrent;
    }

    public void setDateCurrent(String dateCurrent) {
        this.dateCurrent = dateCurrent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    public String getAlertTimer() {
        return alertTimer;
    }

    public void setAlertTimer(String alertTimer) {
        this.alertTimer = alertTimer;
    }

    public byte[] getNoteImage() {
        return noteImage;
    }

    public void setNoteImage(byte[] noteImage) {
        this.noteImage = noteImage;
    }
}
