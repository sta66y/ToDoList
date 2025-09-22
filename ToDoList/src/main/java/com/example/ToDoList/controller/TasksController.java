package com.example.ToDoList.controller;

import com.example.ToDoList.models.TaskRequestDTO;
import com.example.ToDoList.models.TaskResponseDTO;
import com.example.ToDoList.service.TasksService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/todos")
@AllArgsConstructor
public class TasksController {
    private final TasksService tasksService;

    @GetMapping
    public List<TaskResponseDTO> getAllTasks() {
        return tasksService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTaskById(@PathVariable Long id) {
        return tasksService.getTaskById(id);
    }

    @PostMapping
    public TaskResponseDTO createTask(@RequestBody @Valid TaskRequestDTO dto) {
        return tasksService.createTask(dto);
    }

    @DeleteMapping("/{id}")
    public TaskResponseDTO deleteTaskById(@PathVariable Long id) {
        return tasksService.deleteTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskResponseDTO editTaskById(@PathVariable Long id, @RequestBody @Valid TaskRequestDTO dto) {
        return tasksService.editTaskById(id, dto);
    }

    @PatchMapping("/{id}")
    public TaskResponseDTO changeCompletedStatusById(@PathVariable Long id) {
        return tasksService.changeCompletedStatusById(id);
    }

    @PostMapping("/upload")
    public List<TaskResponseDTO> uploadTasksFromJson(@RequestBody @Valid List<TaskRequestDTO> dtos) {
        return tasksService.uploadTasksFromJson(dtos);
    }
}
