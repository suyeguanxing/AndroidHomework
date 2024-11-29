package com.example.first;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.first.room.Notes;
import com.example.first.room.manager.DBEngine;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNoteActivity extends AppCompatActivity {
    private EditText editTitle, editContent;
    private Spinner spinnerTag;
    private TextView editDate;
    private int noteId;
    private HomePageActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        spinnerTag = findViewById(R.id.spinnerTag);
        editDate = findViewById(R.id.editDate);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("NOTE_ID", -1);
        String title = intent.getStringExtra("NOTE_TITLE");
        String content = intent.getStringExtra("NOTE_CONTENT");
        String tag = intent.getStringExtra("NOTE_TAG");
        String createdTime = intent.getStringExtra("NOTE_CREATEDTIME");

        // 设置编辑内容
        editTitle.setText(title);
        editContent.setText(content);
        editDate.setText("创建于：" + createdTime);

        // 创建适配器并设置下拉框字体颜色为黑色
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.tag_options)) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView text = (TextView) view;
                text.setTextColor(getResources().getColor(android.R.color.black));  // 设置下拉框项的字体颜色为黑色
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view;
                text.setTextColor(getResources().getColor(android.R.color.black));  // 设置当前选项的字体颜色为黑色
                return view;
            }
        };

        // 设置适配器
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTag.setAdapter(adapter);

        // 设置默认选择的标签
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(tag)) {
                spinnerTag.setSelection(i);
                break;
            }
        }

    }

    // 保存按钮点击事件
    public void onSaveClicked(View view) {
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        String tag = spinnerTag.getSelectedItem().toString();
        String lastModified = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String createdTime = getIntent().getStringExtra("NOTE_CREATEDTIME");

        if (title.isEmpty()) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return;
        }

        // 更新笔记
        Notes note = new Notes(noteId, title, content, createdTime,lastModified, tag);
        new DBEngine(this).updateNotes(note);

        //activity.loadNotes();

        Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
        finish(); // 返回
    }

    public void onBackClicked(View view){
        finish();
    }

}