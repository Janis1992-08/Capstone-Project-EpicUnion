package org.example.backend.service;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.TasksDto;
import org.example.backend.model.TasksModel;
import org.example.backend.repository.TasksRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TasksService {
    private final TasksRepo tasksRepo;
    private final IdService idService;


    public List<TasksModel> getAllTasks() {
        return tasksRepo.findAll();
    }

    public Optional<TasksModel> getTaskById(String id) {
        return tasksRepo.findById(id);
    }

    public TasksModel addTask(TasksDto tasksDto) {
        TasksModel tasks = new TasksModel(idService.generateUUID(),
                tasksDto.title(),
                tasksDto.description(),
                tasksDto.taskStatus(),
                tasksDto.dueDate(),
                tasksDto.assignedTo());
        return tasksRepo.save(tasks);
    }

    public TasksModel updateTask(String id, TasksDto tasksDto) {
        TasksModel updateTask = tasksRepo.findById(id).orElseThrow();
        updateTask = updateTask.withTitle(tasksDto.title())
                .withDescription(tasksDto.description())
                .withTaskStatus(tasksDto.taskStatus())
                .withDueDate(tasksDto.dueDate())
                .withAssignedTo(tasksDto.assignedTo());
        return tasksRepo.save(updateTask);
    }

    public void deleteTask(String id) {
        tasksRepo.deleteById(id);
    }


}
