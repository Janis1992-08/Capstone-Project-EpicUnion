package org.example.backend.controller;


import lombok.RequiredArgsConstructor;
import org.example.backend.dto.TasksDto;
import org.example.backend.model.TasksModel;
import org.example.backend.service.TasksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TasksController {
    private final TasksService tasksService;

    @GetMapping
    public List<TasksModel> getAllTasks() {
        return tasksService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TasksModel> getTaskById(@PathVariable String id) {
        Optional<TasksModel> task = tasksService.getTaskById(id);
        return task.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TasksModel> addTask(@RequestBody TasksDto tasksDto) {
        TasksModel newTask = tasksService.addTask(tasksDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TasksModel> updateTask(@PathVariable String id, @RequestBody TasksDto taskDto) {
        TasksModel updatedTask = tasksService.updateTask(id, taskDto);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id) {
        tasksService.deleteTask(id);
        return ResponseEntity.ok().build();
    }





}
