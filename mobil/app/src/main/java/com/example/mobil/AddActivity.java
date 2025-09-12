package com.example.mobil;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

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
            String title = titleEdit.getText().toString();
            String desc = descEdit.getText().toString();
            boolean completed = completedCheck.isChecked();

            if (!title.isEmpty()) {
                Task task = new Task(title, desc, completed);
                if (isEdit) {
                    TasksManager.updateTask(position, task);
                } else {
                    TasksManager.addTask(task);
                }
                finish();
            }
        });
    }
}