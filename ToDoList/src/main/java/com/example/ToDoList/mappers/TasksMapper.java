package com.example.ToDoList.mappers;

import com.example.ToDoList.models.Task;
import com.example.ToDoList.models.TaskRequestDTO;
import com.example.ToDoList.models.TaskResponseDTO;

public class TasksMapper {
    public static TaskResponseDTO toTaskResponseDTO(Task task) {
        return TaskResponseDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .completed(task.getCompleted())
                .build();
    }

    public static Task toTask(TaskRequestDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.getCompleted());

        return task;
    }
}
