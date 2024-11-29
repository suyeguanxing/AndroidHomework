package com.example.first.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Notes.class}, version = 1, exportSchema = false)

public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao getNoteDao();
    // 单例模式返回DB
    private static NoteDatabase Instance;

    public static synchronized NoteDatabase getInstance(Context context) {// 传入环境
        if (Instance == null) {
            Instance = Room.databaseBuilder(context.getApplicationContext(), NoteDatabase.class, "note_database").build();
        }
        return Instance;
    }

}
