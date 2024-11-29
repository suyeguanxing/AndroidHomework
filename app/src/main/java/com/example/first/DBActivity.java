package com.example.first;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DBActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dbactivity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void createDB(View view) {
        SQLiteOpenHelper helper = MySqliteOpenHelper.getmInstance(this);
        SQLiteDatabase database = helper.getWritableDatabase();

    }

    public void queryNotes(View view) {
        SQLiteOpenHelper helper = MySqliteOpenHelper.getmInstance(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        if (db.isOpen()) {
            // 返回游标
            Cursor cursor = db.rawQuery("select * from notes", null);
            // 迭代游标，遍历数据
            while (cursor.moveToNext()) {
                int _id = cursor.getInt(0);
                String title = cursor.getString(1);
                String content = cursor.getString(2);
                String created_time = cursor.getString(3);
                String last_modified = cursor.getString(4);
                String tag = cursor.getString(5);

                Log.d("notes", "_id:" + _id + "  title:" + title + "  " + content + "  " + created_time + "  " + last_modified + "  " + tag);
            }
            // 一定关闭游标，否则耗费性能
            cursor.close();
            // 关闭数据库
            db.close();
        }

    }

    public void insertNote(View view) {
        SQLiteOpenHelper helper = MySqliteOpenHelper.getmInstance(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        // 是否打开成功
        if (db.isOpen()) {
            String sql = "insert into notes(title) values('Frank')";
            String content = "insert into notes(content) values('曼波no more')";

            db.execSQL(sql);
            db.close();
        }

    }

    public void updateNote(View view) {
        SQLiteOpenHelper helper = MySqliteOpenHelper.getmInstance(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        if (db.isOpen()) {
            String sql = "update notes set title = ? where _id = ?";

            db.execSQL(sql, new Object[]{"三角洲行动", 2});
            db.close();
        }


    }

    public void deleteNotes(View view){
        SQLiteOpenHelper helper = MySqliteOpenHelper.getmInstance(this);
        SQLiteDatabase db = helper.getWritableDatabase();

        if(db.isOpen()){
            String sql = "delete from notes where _id = ?";

            db.execSQL(sql, new Object[]{2});
            db.close();
        }

    }

}