package com.example.first;

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

public class CreateNoteActivity extends AppCompatActivity {
    private EditText editTitle, editContent;
    private Spinner spinnerTag;
    private TextView editDate;
    private DBEngine dbEngine;
    private HomePageActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        // 初始化控件
        editTitle = findViewById(R.id.editTitle);
        editContent = findViewById(R.id.editContent);
        editDate = findViewById(R.id.editDate);
        spinnerTag = findViewById(R.id.spinnerTag);

        dbEngine = new DBEngine(this);

        // 设置当前时间
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        editDate.setText("创建于：" + currentDate);

        // 设置 Spinner 默认选项
//        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
//                R.array.tag_options, android.R.layout.simple_spinner_item);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerTag.setAdapter(adapter);
        // 设置默认选择标签为 "未分类"
        //spinnerTag.setSelection(0); // 这里选择了第一个标签项，即未分类


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

    }

    // 返回按钮点击事件
    public void onBackClicked(View view) {
        finish();
    }

    // 保存按钮点击事件
    public void onSaveClicked(View view) {
        String title = editTitle.getText().toString();
        String content = editContent.getText().toString();
        String tag = spinnerTag.getSelectedItem().toString();
        String createdTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String lastModified = createdTime;

        if (title.isEmpty()) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return;
        }

        // 保存笔记逻辑
        Notes note = new Notes(title, content, createdTime, lastModified, tag);
        dbEngine.insertNotes(note);
        activity.loadNotes();

        Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();

        // 返回首页
        finish();
    }
}