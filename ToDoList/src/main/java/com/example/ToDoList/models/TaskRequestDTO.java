package com.example.ToDoList.models;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequestDTO {
    @NotNull(message = "Заголовок задачи обязателен")
    private String title;
    private String description;
    private Boolean completed;
}
