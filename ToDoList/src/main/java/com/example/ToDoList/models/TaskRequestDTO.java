package com.example.ToDoList.models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TaskRequestDTO {
    private String title;
    private String description;
    private Boolean completed;
}
