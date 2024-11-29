package com.example.first.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao  // 进行增删改查
public interface NoteDao {
    @Insert
    void insertNotes(Notes ... note); // 传入可变参数

    @Update
    void updateNotes(Notes ... note);

    // 根据 ID 删除笔记
    @Query("DELETE FROM notes WHERE id = :noteId") // 根据 ID 删除笔记
    void deleteNoteById(int noteId);

    // 查询所有笔记
    @Query("SELECT * FROM Notes ORDER BY ID DESC")
    List<Notes> getAllNotes();

    // 根据标题查询笔记
    @Query("SELECT * FROM notes WHERE title LIKE :title") // 根据标题模糊查询笔记
    List<Notes> getNotesByTitle(String title);

}
