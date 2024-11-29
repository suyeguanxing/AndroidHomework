package com.example.first.room.manager;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.first.room.NoteDao;
import com.example.first.room.NoteDatabase;
import com.example.first.room.Notes;

import java.util.List;

public class DBEngine {
    private NoteDao noteDao;

    public DBEngine(Context context){
        NoteDatabase noteDatabase = NoteDatabase.getInstance(context);
        noteDao = noteDatabase.getNoteDao();
    }

    // 新增
    public void insertNotes(Notes... notes){
        new InsertAsyncTask(noteDao).execute(notes);
    }

    // 修改
    public void updateNotes(Notes... notes){
        new UpdateAsyncTask(noteDao).execute(notes);
    }

    // 删除
    public void deleteNoteById(int noteId){
        new DeleteAsyncTask(noteDao).execute(noteId);
    }

    // 查询
    public void listAllNotes(final NotesQueryCallback callback){
        new ListAllAsyncTask(noteDao, callback).execute();
    }



    // 异步操作

    private static class InsertAsyncTask extends AsyncTask<Notes, Void, Void>{

        private NoteDao dao;

        public InsertAsyncTask(NoteDao noteDao) {
            dao = noteDao;
        }

        @Override
        protected Void doInBackground(Notes... notes){
            dao.insertNotes(notes);
            return null;
        }
    }

    private static class UpdateAsyncTask extends AsyncTask<Notes, Void, Void>{

        private NoteDao dao;

        public UpdateAsyncTask(NoteDao noteDao) {
            dao = noteDao;
        }

        @Override
        protected Void doInBackground(Notes... notes){
            dao.updateNotes(notes);
            return null;
        }
    }

    private static class DeleteAsyncTask extends AsyncTask<Integer, Void, Void>{

        private NoteDao dao;

        public DeleteAsyncTask(NoteDao noteDao) {
            dao = noteDao;
        }

        @Override
        protected Void doInBackground(Integer... noteIds) {
            for (Integer noteId : noteIds) {
                dao.deleteNoteById(noteId);  // 根据 ID 删除笔记
            }
            return null;
        }
    }

    private static class ListAllAsyncTask extends AsyncTask<Void, Void, List<Notes>>{

        private NoteDao dao;
        private NotesQueryCallback callback;

        public ListAllAsyncTask(NoteDao noteDao, NotesQueryCallback callback) {
            dao = noteDao;
            this.callback = callback;
        }

        @Override
        protected List<Notes> doInBackground(Void... voids) {
            return dao.getAllNotes();  // 查询所有笔记
        }

        @Override
        protected void onPostExecute(List<Notes> notes) {
            super.onPostExecute(notes);
            if (callback != null) {
                callback.onNotesFetched(notes);  // 查询完毕后通过回调返回数据
            }
        }
    }

    // 回调接口，用于处理查询结果
    public interface NotesQueryCallback {
        void onNotesFetched(List<Notes> notes);  // 查询到笔记后回调
    }

}
