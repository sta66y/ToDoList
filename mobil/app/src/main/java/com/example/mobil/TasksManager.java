package com.example.mobil;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TasksManager {
    private static List<Task> tasks = new ArrayList<>();
    private static final Gson gson = new Gson();

    public static List<Task> getTasks() {
        return tasks;
    }

    public static void addTask(Task task) {
        tasks.add(task);
    }

    public static void updateTask(int index, Task task) {
        tasks.set(index, task);
    }

    public static void deleteTask(int index) {
        tasks.remove(index);
    }

    public static void loadFromJson(Context context, Uri uri) {
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            Type listType = new TypeToken<ArrayList<Task>>() {}.getType();
            tasks = gson.fromJson(reader, listType);
            reader.close();
            Toast.makeText(context, "Tasks loaded", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error loading JSON", Toast.LENGTH_SHORT).show();
        }
    }

    public static void saveToJson(Context context, Uri uri) {
        try {
            OutputStream os = context.getContentResolver().openOutputStream(uri);
            OutputStreamWriter writer = new OutputStreamWriter(os);
            gson.toJson(tasks, writer);
            writer.close();
            Toast.makeText(context, "JSON saved", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error saving JSON", Toast.LENGTH_SHORT).show();
        }
    }
}