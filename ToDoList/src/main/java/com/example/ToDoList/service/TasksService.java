package com.example.ToDoList.service;

import com.example.ToDoList.exceptions.TaskNotFound;
import com.example.ToDoList.mappers.TasksMapper;
import com.example.ToDoList.models.Task;
import com.example.ToDoList.models.TaskRequestDTO;
import com.example.ToDoList.models.TaskResponseDTO;
import com.example.ToDoList.repository.TasksRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TasksService {
    private final TasksRepository tasksRepository;

    public List<TaskResponseDTO> getAllTasks() {
        return tasksRepository.findAll().stream()
                .map(TasksMapper::toTaskResponseDTO)
                .toList();
    }


    public TaskResponseDTO getTaskById(Long id) {
        Task task = findTaskOrThrow(id);
        return TasksMapper.toTaskResponseDTO(task);
    }


    public TaskResponseDTO createTask(TaskRequestDTO dto) {
        Task task = tasksRepository.save(TasksMapper.toTask(dto));
        return TasksMapper.toTaskResponseDTO(task);
    }

    public TaskResponseDTO deleteTaskById(Long id) {
        Task taskToDelete = findTaskOrThrow(id);

        tasksRepository.delete(taskToDelete);

        return TasksMapper.toTaskResponseDTO(taskToDelete);
    }


    public TaskResponseDTO editTaskById(Long id, TaskRequestDTO dto) {
        Task taskToEdit = findTaskOrThrow(id);

        taskToEdit.setCompleted(dto.getCompleted());
        taskToEdit.setDescription(dto.getDescription());
        taskToEdit.setTitle(dto.getTitle());

        tasksRepository.save(taskToEdit);

        return TasksMapper.toTaskResponseDTO(taskToEdit);
    }

    public TaskResponseDTO changeCompletedStatusById(Long id) {
        Task taskToUpdate = findTaskOrThrow(id);

        taskToUpdate.setCompleted(!taskToUpdate.getCompleted());

        tasksRepository.save(taskToUpdate);

        return TasksMapper.toTaskResponseDTO(taskToUpdate);
    }


    public List<TaskResponseDTO> uploadTasksFromJson(List<TaskRequestDTO> dtos) {
        List<Task> tasks = dtos.stream()
                .map(TasksMapper::toTask)
                .toList();

        tasksRepository.saveAll(tasks);

        return tasks.stream().map(TasksMapper::toTaskResponseDTO).toList();
    }

    private Task findTaskOrThrow(Long id) {
        return tasksRepository.findById(id)
                .orElseThrow(() -> new TaskNotFound("Задачи с таким id не найдено"));
    }

}
