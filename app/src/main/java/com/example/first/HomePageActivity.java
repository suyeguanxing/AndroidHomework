package com.example.first;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.first.room.Notes;
import com.example.first.room.manager.DBEngine;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class HomePageActivity extends AppCompatActivity implements DBEngine.NotesQueryCallback, NotesAdapter.OnItemClickListener {

    private List<Notes> data = new ArrayList<>();
    private List<Notes> filteredData = new ArrayList<>();  // 用于存储过滤后的数据
    private DBEngine dbEngine;
    private RecyclerView recyclerView;
    private NotesAdapter notesAdapter;
    private Button deleteButton;  // 删除按钮
    private SearchView searchView;  // 搜索框

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_page);

        dbEngine = new DBEngine(this);

        // 获取 MaterialToolbar
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);  // 将其设置为 ActionBar

        // 设置标题
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("MineNotes");
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.notesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 删除按钮初始化
        deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setVisibility(View.VISIBLE);

        // 搜索框初始化
        searchView = findViewById(R.id.searchView);
        // 设置搜索框的监听器
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 在提交时过滤数据
                filterNotes(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 在文本变化时过滤数据
                filterNotes(newText);
                return false;
            }
        });


        // 设置删除按钮的点击事件
        deleteButton.setOnClickListener(v -> onDeleteClicked(v));

        // 加载笔记
        loadNotes();
    }

    public void loadNotes() {
        // 获取所有笔记数据
        dbEngine.listAllNotes(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 每次跳转回来时，加载最新的数据
        loadNotes();
    }

    public void onNotesFetched(List<Notes> notes) {
        // 查询到笔记后更新 UI
        if (notes != null) {
            data.clear();
            data.addAll(notes);
            // 更新 RecyclerView
            notesAdapter = new NotesAdapter(data, this, this);
            recyclerView.setAdapter(notesAdapter);
            notesAdapter.notifyDataSetChanged();
        }
    }

    public void addNoteClicked(View view) {
        // 启动新建笔记页面
        Intent intent = new Intent(HomePageActivity.this, CreateNoteActivity.class);
        startActivity(intent);
    }

    //    public void insertAction(View view){
//        Notes note = new Notes("兰亭序", "无关风月...", "1月1日", "11月27日", "歌曲");
//        dbEngine.insertNotes(note);
//    }
// 过滤笔记列表
    private void filterNotes(String query) {
        List<Notes> filteredList = new ArrayList<>();
        for (Notes note : data) {
            if (note.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(note);
            }
        }
        filteredData.clear();
        filteredData.addAll(filteredList);
        // 如果适配器已经初始化，更新其数据源
        if (notesAdapter != null) {
            notesAdapter.updateData(filteredData);
        }
        //notesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(Notes note) {
        // 点击事件处理，跳转到编辑界面
        Intent intent = new Intent(HomePageActivity.this, EditNoteActivity.class);
        intent.putExtra("NOTE_ID", note.getId());
        intent.putExtra("NOTE_TITLE", note.getTitle());
        intent.putExtra("NOTE_CONTENT", note.getContent());
        intent.putExtra("NOTE_CREATEDTIME", note.getCreatedTime());
        intent.putExtra("NOTE_TAG", note.getTag());
        startActivity(intent);
    }

    // 删除选中的笔记
    public void onDeleteClicked(View view) {
        List<Notes> selectedNotes = notesAdapter.getSelectedNotes();  // 获取选中的笔记
        if (selectedNotes.isEmpty()) {
            Toast.makeText(this, "请选择要删除的笔记", Toast.LENGTH_SHORT).show();
            return;
        }

        // 执行删除操作
        for (Notes note : selectedNotes) {
            dbEngine.deleteNoteById(note.getId());
        }

        // 更新 UI
        loadNotes();
        notesAdapter.deleteSelectedNotes();  // 清除选中状态
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
    }

    // 启动选择模式
    @Override
    public void onItemLongClick(Notes note) {
        notesAdapter.toggleSelectionMode();  // 切换选择模式
    }

    public void queryAction(View view) {

    }

}