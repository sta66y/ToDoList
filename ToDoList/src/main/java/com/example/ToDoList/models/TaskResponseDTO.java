package com.example.ToDoList.models;

import lombok.Builder;

@Builder
public class TaskResponseDTO {
    private Long id;

    private String title;
    private String description;
    private Boolean completed;
}
