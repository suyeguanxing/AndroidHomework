package com.example.first;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.first.room.Notes;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.MyViewHolder> {

    private List<Notes> data;
    private Context context;
    private OnItemClickListener onItemClickListener;

    // 新增字段来管理是否进入选择模式以及每个项是否被选中
    private boolean isSelectionMode = false;
    private List<Boolean> selectionState = new ArrayList<>();
    //private OnSelectionModeChangeListener selectionModeChangeListener;

    public NotesAdapter(List<Notes> data, Context context, OnItemClickListener onItemClickListener) {
        this.data = data;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        //this.selectionModeChangeListener = listener;

        for (int i = 0; i < data.size(); i++) {
            selectionState.add(false);  // 默认没有选中
        }
    }

    // 更新数据
    public void updateData(List<Notes> newNotesList) {
        this.data = newNotesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 这里是布局的加载部分
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // 获取当前的笔记项
        Notes note = data.get(position);

        // 设置笔记的标题
        holder.noteTitle.setText(note.getTitle());

        // 设置部分内容（最多两行）
        String contentPreview = note.getContent().length() > 50 ? note.getContent().substring(0, 50) + "..." : note.getContent();
        holder.noteContent.setText(contentPreview);

        // 设置最后修改时间
        holder.noteModifiedTime.setText("最后修改时间: " + note.getLastModified());

        // 设置标签
        holder.noteTag.setText("标签: " + note.getTag());

        // 设置创建时间
        // holder.noteCreatedTime.setText(note.getCreatedTime());

        // 设置复选框的可见性和选中状态
        holder.checkBox.setVisibility(isSelectionMode ? View.VISIBLE : View.GONE);  // 在选择模式时显示复选框
        holder.checkBox.setChecked(selectionState.get(position));  // 设置复选框的选中状态

        // 长按时进入选择模式
        holder.itemView.setOnLongClickListener(v -> {
            if (!isSelectionMode) {
                isSelectionMode = true;
                notifyDataSetChanged();
            }
            return true;
        });

        // 点击复选框时改变选中状态
        holder.checkBox.setOnClickListener(v -> {
            selectionState.set(position, !selectionState.get(position));  // 切换选中状态
            notifyItemChanged(position);
        });

        // 设置点击事件
        holder.itemView.setOnClickListener(v -> {
            if (isSelectionMode) {
                isSelectionMode = false;  // 退出选择模式
                selectionState.clear();   // 清除选中状态
                for (int i = 0; i < data.size(); i++) {
                    selectionState.add(false);
                }
                notifyDataSetChanged();
            } else {
                // 正常点击，进入编辑界面
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(note);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void toggleSelectionMode() {
        isSelectionMode = !isSelectionMode;
        if (!isSelectionMode) {
            // 如果退出选择模式，清空所有复选框的选中状态
            selectionState.clear();
            for (int i = 0; i < data.size(); i++) {
                selectionState.add(false);  // 默认没有选中
            }
        }

        // 通知回调
//        if (selectionModeChangeListener != null) {
//            selectionModeChangeListener.onSelectionModeChanged(isSelectionMode);
//        }

        notifyDataSetChanged();
    }

    public List<Notes> getSelectedNotes() {
        List<Notes> selectedNotes = new ArrayList<>();
        for (int i = 0; i < selectionState.size(); i++) {
            if (selectionState.get(i)) {
                selectedNotes.add(data.get(i));
            }
        }
        return selectedNotes;
    }

    public void deleteSelectedNotes() {
        List<Notes> selectedNotes = getSelectedNotes();
        data.removeAll(selectedNotes);
        selectionState.clear();
        for (int i = 0; i < data.size(); i++) {
            selectionState.add(false);
        }
        notifyDataSetChanged();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView noteTitle;
        public TextView noteContent;
        public TextView noteModifiedTime;
        public TextView noteTag;
        public CheckBox checkBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            noteTitle = itemView.findViewById(R.id.noteTitle);
            noteContent = itemView.findViewById(R.id.noteContent);
            noteModifiedTime = itemView.findViewById(R.id.noteModifiedTime);
            noteTag = itemView.findViewById(R.id.noteTag);
            checkBox = itemView.findViewById(R.id.checkBox);  // 复选框
        }
    }

    // 回调接口，用于通知Activity是否进入选中模式
//    public interface OnSelectionModeChangeListener {
//        void onSelectionModeChanged(boolean isSelectionMode);
//    }

    public interface OnItemClickListener{
        void onItemClick(Notes notes);
        void onItemLongClick(Notes notes);
    }
}
