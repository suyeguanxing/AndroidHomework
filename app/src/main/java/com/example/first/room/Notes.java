package com.example.first.room;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

// note表
@Entity
public class Notes {
    @PrimaryKey(autoGenerate = true) // 设置自增
    private int id;
    private String title;
    private String content;
    private String createdTime;
    private String lastModified;
    private String tag;

    public Notes(){}

    public Notes(String title, String content, String createdTime, String lastModified, String tag) {
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
        this.lastModified = lastModified;
        this.tag = tag;
    }

    // 用于编辑已有笔记的构造函数，传入 ID
    @Ignore
    public Notes(int id, String title, String content, String createdTime,String lastModified, String tag) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
        this.lastModified = lastModified;
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", createdTime='" + createdTime + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
