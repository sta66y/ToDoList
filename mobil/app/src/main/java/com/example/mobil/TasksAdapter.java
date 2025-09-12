package com.example.mobil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    private List<Task> tasks;
    private Consumer<Integer> onEdit;
    private Consumer<Integer> onDelete;

    public TasksAdapter(List<Task> tasks, Consumer<Integer> onEdit, Consumer<Integer> onDelete) {
        this.tasks = tasks;
        this.onEdit = onEdit;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.titleText.setText(task.getTitle());

        // сбрасываем старый слушатель, чтобы не было лишних вызовов при переиспользовании View
        holder.completedCheck.setOnCheckedChangeListener(null);
        holder.completedCheck.setChecked(task.isCompleted());

        holder.completedCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            TasksManager.updateTask(position, task); // чтобы точно сохранить изменение
        });

        holder.editButton.setOnClickListener(v -> onEdit.accept(position));
        holder.deleteButton.setOnClickListener(v -> onDelete.accept(position));
    }


    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleText;
        CheckBox completedCheck;
        Button editButton;
        Button deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.task_title);
            completedCheck = itemView.findViewById(R.id.task_completed);
            editButton = itemView.findViewById(R.id.edit_button);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }
}