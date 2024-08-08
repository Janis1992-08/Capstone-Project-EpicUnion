package org.example.backend.controller;


import lombok.RequiredArgsConstructor;
import org.example.backend.dto.TasksDto;
import org.example.backend.model.TasksModel;
import org.example.backend.service.TasksService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
public class TasksController {
    private final TasksService tasksService;

    @GetMapping
    public List<TasksModel> getAllTasks(Authentication authentication) {
        String userId = authentication.getName();
        return tasksService.getAllTasks(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TasksModel> getTaskById(@PathVariable String id, Authentication authentication) {
        String userId = authentication.getName();
        return tasksService.getTaskById(id, userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TasksModel> addTask(@RequestBody TasksDto tasksDto, Authentication authentication) {
        String userId = authentication.getName();
        TasksModel newTask = tasksService.addTask(tasksDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newTask);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TasksModel> updateTask(@PathVariable String id, @RequestBody TasksDto taskDto, Authentication authentication) {
        String userId = authentication.getName();
        TasksModel updatedTask = tasksService.updateTask(id, taskDto, userId);
        return ResponseEntity.ok(updatedTask);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable String id, Authentication authentication) {
        String userId = authentication.getName();
        tasksService.deleteTask(id, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{taskId}/guests/{guestId}")
    public ResponseEntity<Void> addGuestToTask(@PathVariable String taskId, @PathVariable String guestId, Authentication authentication) {
        String userId = authentication.getName();
        tasksService.addGuestToTask(taskId, guestId, userId);
        return ResponseEntity.ok().build();
    }

}
