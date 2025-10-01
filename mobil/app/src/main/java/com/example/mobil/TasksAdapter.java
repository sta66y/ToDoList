package com.example.mobil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.function.Consumer;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {
    private List<Task> tasks;
    private Consumer<Integer> onEdit;
    private Consumer<Integer> onDelete;
    private Context context;

    public TasksAdapter(Context ctx, List<Task> tasks, Consumer<Integer> onEdit, Consumer<Integer> onDelete) {
        this.context = ctx;
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

        holder.completedCheck.setOnCheckedChangeListener(null);
        holder.completedCheck.setChecked(task.isCompleted());

        holder.completedCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Long id = task.getId();
            if (id == null) {
                // локальная без id, просто обновим модель
                task.setCompleted(isChecked);
                TasksManager.updateLocalTask(position, task);
                Toast.makeText(context, "Local change", Toast.LENGTH_SHORT).show();
            } else {
                // отправляем PATCH на сервер, он вернёт актуальную сущность
                TasksManager.toggleCompletedOnServer(context, id, new TasksManager.SimpleCallback() {
                    @Override
                    public void onSuccess() {
                        notifyItemChanged(position);
                    }

                    @Override
                    public void onError(String message) {
                        // откатываем чекбокс к старому состоянию
                        holder.completedCheck.setOnCheckedChangeListener(null);
                        holder.completedCheck.setChecked(!isChecked);
                        holder.completedCheck.setOnCheckedChangeListener((b, v) -> { /* no-op */ });
                        Toast.makeText(context, "Toggle failed: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        holder.editButton.setOnClickListener(v -> onEdit.accept(position));
        holder.deleteButton.setOnClickListener(v -> {
            Task t = tasks.get(position);
            Long id = t.getId();
            if (id == null) {
                TasksManager.deleteLocalTask(position);
                notifyItemRemoved(position);
            } else {
                TasksManager.deleteTaskOnServer(context, id, new TasksManager.SimpleCallback() {
                    @Override
                    public void onSuccess() {
                        notifyItemRemoved(position);
                    }

                    @Override
                    public void onError(String message) {
                        Toast.makeText(context, "Delete failed: " + message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            onDelete.accept(position);
        });
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
