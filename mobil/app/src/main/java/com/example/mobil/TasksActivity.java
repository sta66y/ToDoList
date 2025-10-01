package com.example.mobil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TasksAdapter adapter;
    private ActivityResultLauncher<Intent> saveLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        TextView titleText = findViewById(R.id.tasks_title);
        titleText.setText("Your's tasks");

        recyclerView = findViewById(R.id.tasks_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TasksAdapter(this, TasksManager.getTasks(), this::editTask, this::deleteTask);
        recyclerView.setAdapter(adapter);

        Button backButton = findViewById(R.id.back_button);
        Button addButton = findViewById(R.id.add_button);
        Button jsonButton = findViewById(R.id.json_button);

        saveLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                TasksManager.saveToJson(this, uri);
            }
        });

        backButton.setOnClickListener(v -> finish());

        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(TasksActivity.this, AddActivity.class);
            intent.putExtra("isEdit", false);
            startActivity(intent);
        });

        jsonButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("application/json");
            intent.putExtra(Intent.EXTRA_TITLE, "tasks.json");
            saveLauncher.launch(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // загружаем актуальные задачи с сервера
        TasksManager.fetchTasksFromServer(this, new TasksManager.SimpleCallback() {
            @Override
            public void onSuccess() {
                runOnUiThread(() -> {
                    adapter.notifyDataSetChanged();
                });
            }

            @Override
            public void onError(String message) {
                runOnUiThread(() -> {
                    Toast.makeText(TasksActivity.this, "Load failed: " + message, Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged(); // покажем локальные, если есть
                });
            }
        });
    }

    private void editTask(int position) {
        Intent intent = new Intent(TasksActivity.this, AddActivity.class);
        intent.putExtra("isEdit", true);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void deleteTask(int position) {
        // nothing else here — TasksAdapter/TasksManager уже удаляют
    }
}
