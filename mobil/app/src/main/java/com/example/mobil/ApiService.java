package com.example.mobil;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("api/todos")
    Call<List<TaskResponse>> getAllTasks();

    @GET("api/todos/{id}")
    Call<TaskResponse> getTaskById(@Path("id") Long id);

    @POST("api/todos")
    Call<TaskResponse> createTask(@Body TaskRequest dto);

    @DELETE("api/todos/{id}")
    Call<TaskResponse> deleteTaskById(@Path("id") Long id);

    @PUT("api/todos/{id}")
    Call<TaskResponse> editTaskById(@Path("id") Long id, @Body TaskRequest dto);

    @PATCH("api/todos/{id}")
    Call<TaskResponse> changeCompletedStatusById(@Path("id") Long id);

    @POST("api/todos/upload")
    Call<List<TaskResponse>> uploadTasksFromJson(@Body List<TaskRequest> dtos);
}
