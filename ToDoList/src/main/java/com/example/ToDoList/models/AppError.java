package com.example.ToDoList.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppError {
    private String message;
    private int code;
}
