package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.dto.TasksDto;
import org.example.backend.model.TasksModel;
import org.example.backend.model.TasksStatusModel;
import org.example.backend.service.TasksService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TasksControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TasksService tasksService;

    private final String ownerId = "ownerId";

    @Test
    @WithMockUser(username = ownerId)
    void getAllTasks() throws Exception {
        List<TasksModel> tasks = List.of(new TasksModel("task1", "Task Title", "Task Description", TasksStatusModel.OPEN, "2025-01-01", new ArrayList<>(), new ArrayList<>(), ownerId));
        when(tasksService.getAllTasks(ownerId)).thenReturn(tasks);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [{
                        "id": "task1",
                        "title": "Task Title",
                        "description": "Task Description",
                        "taskStatus": "OPEN",
                        "dueDate": "2025-01-01",
                        "assignedToGuests": [],
                        "assignedToSuppliers": [],
                        "ownerId": "ownerId"
                    }]
                """));

        verify(tasksService).getAllTasks(ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void getTaskById() throws Exception {
        TasksModel task = new TasksModel("task1", "Task Title", "Task Description", TasksStatusModel.OPEN, "2025-01-01", new ArrayList<>(), new ArrayList<>(), ownerId);
        when(tasksService.getTaskById("task1", ownerId)).thenReturn(Optional.of(task));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/task1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {
                        "id": "task1",
                        "title": "Task Title",
                        "description": "Task Description",
                        "taskStatus": "OPEN",
                        "dueDate": "2025-01-01",
                        "assignedToGuests": [],
                        "assignedToSuppliers": [],
                        "ownerId": "ownerId"
                    }
                """));

        verify(tasksService).getTaskById("task1", ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void getTaskById_NotFound() throws Exception {
        when(tasksService.getTaskById("task1", ownerId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/tasks/task1").with(csrf()))
                .andExpect(status().isNotFound());

        verify(tasksService).getTaskById("task1", ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void addTask() throws Exception {
        TasksDto newTaskDto = new TasksDto("New Task", "New Description", TasksStatusModel.OPEN, "2026-01-01", new ArrayList<>(), new ArrayList<>(), ownerId);
        TasksModel newTask = new TasksModel("task2", "New Task", "New Description", TasksStatusModel.OPEN, "2026-01-01", new ArrayList<>(), new ArrayList<>(), ownerId);

        when(tasksService.addTask(newTaskDto, ownerId)).thenReturn(newTask);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newTaskDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(newTask)));

        verify(tasksService).addTask(newTaskDto, ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void updateTask() throws Exception {
        TasksDto taskDto = new TasksDto("Updated Task", "Updated Description", TasksStatusModel.IN_PROGRESS, "2027-01-01", new ArrayList<>(), new ArrayList<>(), ownerId);
        TasksModel updatedTask = new TasksModel("task1", "Updated Task", "Updated Description", TasksStatusModel.IN_PROGRESS, "2027-01-01", new ArrayList<>(), new ArrayList<>(), ownerId);

        when(tasksService.updateTask("task1", taskDto, ownerId)).thenReturn(updatedTask);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/task1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(taskDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(updatedTask)));

        verify(tasksService).updateTask("task1", taskDto, ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void deleteTask() throws Exception {
        doNothing().when(tasksService).deleteTask("task1", ownerId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/task1").with(csrf()))
                .andExpect(status().isOk());

        verify(tasksService).deleteTask("task1", ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void addGuestToTask() throws Exception {
        doNothing().when(tasksService).addGuestToTask("task1", "guest1", ownerId);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/task1/guests/guest1").with(csrf()))
                .andExpect(status().isOk());

        verify(tasksService).addGuestToTask("task1", "guest1", ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void addSupplierToTask() throws Exception {
        doNothing().when(tasksService).addSupplierToTask("task1", "supplier1", ownerId);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/tasks/task1/suppliers/supplier1").with(csrf()))
                .andExpect(status().isOk());

        verify(tasksService).addSupplierToTask("task1", "supplier1", ownerId);
    }
}
