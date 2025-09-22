package com.example.ToDoList.repository;

import com.example.ToDoList.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TasksRepository extends JpaRepository<Task, Long> {
}
