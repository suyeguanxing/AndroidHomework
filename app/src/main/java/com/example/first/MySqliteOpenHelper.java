package com.example.first;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MySqliteOpenHelper extends SQLiteOpenHelper {

    private  static  SQLiteOpenHelper mInstance;
    public static synchronized SQLiteOpenHelper getmInstance(Context context){
        if(mInstance == null){
            mInstance = new MySqliteOpenHelper(context, "noteDB.db", null, 1);
        }

        return mInstance;
    }

    public MySqliteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // 创建表，第一次创建时调用，第二次不会重复创建
    //数据库初始化
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表

        String sql = "CREATE TABLE notes (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "content TEXT, " +
                "created_time TEXT, " +
                "last_modified TEXT, " +
                "tag TEXT)";

        db.execSQL(sql);
    }

    // 升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
