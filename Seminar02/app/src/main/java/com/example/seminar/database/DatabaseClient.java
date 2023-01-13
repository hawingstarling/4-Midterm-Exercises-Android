package com.example.seminar.database;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {
    private Context mCtx;
    private static DatabaseClient mInstance;

    private AppDataBase appDataBase;

    private DatabaseClient(Context mCtx) {
        this.mCtx = mCtx;
        appDataBase = Room.databaseBuilder(mCtx, AppDataBase.class, "Note.db")
                .fallbackToDestructiveMigration()
                .build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDataBase getAppDataBase() {
        return appDataBase;
    }
}
