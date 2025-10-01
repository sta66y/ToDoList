package com.example.mobil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddActivity extends AppCompatActivity {

    private EditText titleEdit;
    private EditText descEdit;
    private CheckBox completedCheck;
    private boolean isEdit;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        TextView titleText = findViewById(R.id.add_title);
        titleText.setText("Add/Edit Task");

        titleEdit = findViewById(R.id.title_edit);
        descEdit = findViewById(R.id.desc_edit);
        completedCheck = findViewById(R.id.completed_check);

        Button cancelButton = findViewById(R.id.cancel_button);
        Button saveButton = findViewById(R.id.save_button);

        isEdit = getIntent().getBooleanExtra("isEdit", false);
        if (isEdit) {
            position = getIntent().getIntExtra("position", -1);
            Task task = TasksManager.getTasks().get(position);
            titleEdit.setText(task.getTitle());
            descEdit.setText(task.getDescription());
            completedCheck.setChecked(task.isCompleted());
        }

        cancelButton.setOnClickListener(v -> finish());

        saveButton.setOnClickListener(v -> {
            String title = titleEdit.getText().toString().trim();
            String desc = descEdit.getText().toString().trim();
            boolean completed = completedCheck.isChecked();

            if (title.isEmpty()) {
                Toast.makeText(this, "Title required", Toast.LENGTH_SHORT).show();
                return;
            }

            TaskRequest req = new TaskRequest(title, desc, completed);

            if (isEdit) {
                Task old = TasksManager.getTasks().get(position);
                if (old.getId() == null) {
                    // локальное, просто обновляем
                    old.setTitle(title);
                    old.setDescription(desc);
                    old.setCompleted(completed);
                    TasksManager.updateLocalTask(position, old);
                    finish();
                } else {
                    TasksManager.updateTaskOnServer(this, old.getId(), req, new TasksManager.SimpleCallback() {
                        @Override
                        public void onSuccess() {
                            runOnUiThread(() -> {
                                Toast.makeText(AddActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                finish();
                            });
                        }

                        @Override
                        public void onError(String message) {
                            runOnUiThread(() -> Toast.makeText(AddActivity.this, "Update failed: " + message, Toast.LENGTH_SHORT).show());
                        }
                    });
                }
            } else {
                TasksManager.createTaskOnServer(this, req, new TasksManager.SimpleCallback() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(() -> {
                            Toast.makeText(AddActivity.this, "Created", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> Toast.makeText(AddActivity.this, "Create failed: " + message, Toast.LENGTH_SHORT).show());
                    }
                });
            }
        });
    }
}
