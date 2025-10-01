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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TasksManager {
    private static List<Task> tasks = new ArrayList<>();
    private static final Gson gson = new Gson();

    public interface SimpleCallback {
        void onSuccess();
        void onError(String message);
    }

    public static List<Task> getTasks() { return tasks; }
    public static void setTasks(List<Task> newTasks) { tasks = newTasks; }

    // ---- Локальные операции ----
    public static void addLocalTask(Task task) { tasks.add(task); }
    public static void updateLocalTask(int index, Task task) { tasks.set(index, task); }
    public static void deleteLocalTask(int index) { tasks.remove(index); }

    // ---- Сохранение/загрузка JSON локально (файловая система через SAF) ----
    public static void loadFromJson(Context context, Uri uri) {
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            Type listType = new TypeToken<ArrayList<Task>>() {}.getType();
            List<Task> loaded = gson.fromJson(reader, listType);
            reader.close();
            if (loaded != null) tasks = loaded;
            Toast.makeText(context, "Tasks loaded", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(context, "Error loading JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(context, "Error saving JSON: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // ---- Сетевые операции (Retrofit) ----
    public static void fetchTasksFromServer(Context context, SimpleCallback callback) {
        ApiClient.getApiService().getAllTasks().enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TaskResponse> resp = response.body();
                    List<Task> local = new ArrayList<>();
                    for (TaskResponse r : resp) {
                        local.add(new Task(r.getId(), r.getTitle(), r.getDescription(), r.isCompleted()));
                    }
                    tasks = local;
                    callback.onSuccess();
                } else {
                    callback.onError("Server error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public static void createTaskOnServer(Context context, TaskRequest req, SimpleCallback callback) {
        ApiClient.getApiService().createTask(req).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TaskResponse r = response.body();
                    tasks.add(new Task(r.getId(), r.getTitle(), r.getDescription(), r.isCompleted()));
                    callback.onSuccess();
                } else {
                    callback.onError("Create failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public static void updateTaskOnServer(Context context, Long id, TaskRequest req, SimpleCallback callback) {
        ApiClient.getApiService().editTaskById(id, req).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TaskResponse r = response.body();
                    // replace in local list by id
                    for (int i = 0; i < tasks.size(); i++) {
                        Task t = tasks.get(i);
                        if (t.getId() != null && t.getId().equals(r.getId())) {
                            tasks.set(i, new Task(r.getId(), r.getTitle(), r.getDescription(), r.isCompleted()));
                            break;
                        }
                    }
                    callback.onSuccess();
                } else {
                    callback.onError("Update failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public static void deleteTaskOnServer(Context context, Long id, SimpleCallback callback) {
        ApiClient.getApiService().deleteTaskById(id).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // remove locally by id
                    for (int i = 0; i < tasks.size(); i++) {
                        Task t = tasks.get(i);
                        if (t.getId() != null && t.getId().equals(id)) {
                            tasks.remove(i);
                            break;
                        }
                    }
                    callback.onSuccess();
                } else {
                    callback.onError("Delete failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public static void toggleCompletedOnServer(Context context, Long id, SimpleCallback callback) {
        ApiClient.getApiService().changeCompletedStatusById(id).enqueue(new Callback<TaskResponse>() {
            @Override
            public void onResponse(Call<TaskResponse> call, Response<TaskResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TaskResponse r = response.body();
                    for (int i = 0; i < tasks.size(); i++) {
                        Task t = tasks.get(i);
                        if (t.getId() != null && t.getId().equals(r.getId())) {
                            tasks.set(i, new Task(r.getId(), r.getTitle(), r.getDescription(), r.isCompleted()));
                            break;
                        }
                    }
                    callback.onSuccess();
                } else {
                    callback.onError("Toggle failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<TaskResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public static void uploadTasksToServer(Context context, List<TaskRequest> requests, SimpleCallback callback) {
        ApiClient.getApiService().uploadTasksFromJson(requests).enqueue(new Callback<List<TaskResponse>>() {
            @Override
            public void onResponse(Call<List<TaskResponse>> call, Response<List<TaskResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<TaskResponse> resp = response.body();
                    List<Task> local = new ArrayList<>();
                    for (TaskResponse r : resp) {
                        local.add(new Task(r.getId(), r.getTitle(), r.getDescription(), r.isCompleted()));
                    }
                    tasks = local;
                    callback.onSuccess();
                } else {
                    callback.onError("Upload failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<TaskResponse>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }
}
