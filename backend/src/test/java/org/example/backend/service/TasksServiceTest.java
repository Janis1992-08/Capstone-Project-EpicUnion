package org.example.backend.service;

import org.example.backend.dto.TasksDto;
import org.example.backend.model.*;
import org.example.backend.repository.GuestsRepo;
import org.example.backend.repository.SuppliersRepo;
import org.example.backend.repository.TasksRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TasksServiceTest {

    @Mock
    private TasksRepo tasksRepo;

    @Mock
    private GuestsRepo guestsRepo;

    @Mock
    private SuppliersRepo suppliersRepo;

    @Mock
    private IdService idService;

    @InjectMocks
    private TasksService tasksService;

    private TasksModel task;
    private final String userId = "ownerId";

    @BeforeEach
    void setUp() {
        task = new TasksModel("task1", "Task One", "Description One", TasksStatusModel.OPEN, "2025-01-01", List.of("guest1"), List.of("supplier1"), userId);
    }

    @Test
    void getAllTasks_ShouldReturnTasksForUser() {
        List<TasksModel> expectedTasks = List.of(task);
        when(tasksRepo.findAllByOwnerId(userId)).thenReturn(expectedTasks);

        List<TasksModel> actualTasks = tasksService.getAllTasks(userId);

        assertEquals(expectedTasks, actualTasks);
        verify(tasksRepo).findAllByOwnerId(userId);
    }

    @Test
    void getTaskById_ShouldReturnTaskWhenExists() {
        when(tasksRepo.findByIdAndOwnerId("task1", userId)).thenReturn(Optional.of(task));

        Optional<TasksModel> actual = tasksService.getTaskById("task1", userId);

        assertTrue(actual.isPresent());
        assertEquals(task, actual.get());
        verify(tasksRepo).findByIdAndOwnerId("task1", userId);
    }

    @Test
    void addTask_ShouldSaveNewTaskWithAssignments() {
        TasksDto newTaskDto = new TasksDto("Task Two", "Description Two", TasksStatusModel.IN_PROGRESS, "2025-12-31", List.of("guest2"), List.of("supplier2"), userId);
        when(idService.generateUUID()).thenReturn("task2");
        when(guestsRepo.findById("guest2")).thenReturn(Optional.of(new GuestsModel("guest2", "Guest", "Two", "email@guesttwo.com", "0123456789", RsvpStatusModel.PENDING, "Notes here", new ArrayList<>(), userId)));
        when(suppliersRepo.findById("supplier2")).thenReturn(Optional.of(new SuppliersModel("supplier2", "Supplier Two", "Description Two", "http://suppliertwo.com", 250.0, "2025-01-01", new ArrayList<>(), "email@suppliertwo.com", "9876543210", "Address Two", userId)));

        tasksService.addTask(newTaskDto, userId);

        ArgumentCaptor<TasksModel> taskCaptor = ArgumentCaptor.forClass(TasksModel.class);
        verify(tasksRepo).save(taskCaptor.capture());
        TasksModel savedTask = taskCaptor.getValue();

        assertNotNull(savedTask);
        assertEquals("Task Two", savedTask.title());
        assertTrue(savedTask.assignedToGuests().contains("guest2"));
        assertTrue(savedTask.assignedToSuppliers().contains("supplier2"));
    }

    @Test
    void updateTask_ShouldUpdateDetailsAndAssignments() {
        // Arrange
        TasksDto updateDto = new TasksDto("Updated Task", "Updated Description", TasksStatusModel.DONE, "2026-01-01", List.of("guest2"), List.of("supplier2"), userId);
        List<String> initialGuests = List.of("guest1");
        List<String> initialSuppliers = List.of("supplier1");
        task = new TasksModel("task1", "Task One", "Description One", TasksStatusModel.OPEN, "2025-01-01", initialGuests, initialSuppliers, userId);

        GuestsModel oldGuest = new GuestsModel("guest1", "Guest", "One", "email@example.com", "0123456789", RsvpStatusModel.PENDING, "Notes here", new ArrayList<>(), userId);
        SuppliersModel oldSupplier = new SuppliersModel("supplier1", "Supplier One", "Description One", "https://supplierone.com", 1200.00, "2024-12-31", new ArrayList<>(), "email@example.com", "1234567890", "Address", userId);

        GuestsModel newGuest = new GuestsModel("guest2", "Guest", "Two", "email@guesttwo.com", "0123456789", RsvpStatusModel.PENDING, "More notes", new ArrayList<>(), userId);
        SuppliersModel newSupplier = new SuppliersModel("supplier2", "Supplier Two", "Description Two", "http://suppliertwo.com", 250.0, "2025-01-01", new ArrayList<>(), "email@suppliertwo.com", "9876543210", "Address Two", userId);

        when(tasksRepo.findByIdAndOwnerId("task1", userId)).thenReturn(Optional.of(task));
        when(guestsRepo.findById("guest1")).thenReturn(Optional.of(oldGuest));
        when(guestsRepo.findById("guest2")).thenReturn(Optional.of(newGuest));
        when(suppliersRepo.findById("supplier1")).thenReturn(Optional.of(oldSupplier));
        when(suppliersRepo.findById("supplier2")).thenReturn(Optional.of(newSupplier));

        when(guestsRepo.save(any(GuestsModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(suppliersRepo.save(any(SuppliersModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tasksRepo.save(any(TasksModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        tasksService.updateTask("task1", updateDto, userId);

        ArgumentCaptor<TasksModel> taskCaptor = ArgumentCaptor.forClass(TasksModel.class);
        verify(tasksRepo).save(taskCaptor.capture());
        TasksModel updatedTask = taskCaptor.getValue();

        assertNotNull(updatedTask);
        assertTrue(updatedTask.assignedToGuests().contains("guest2"));
        assertTrue(updatedTask.assignedToSuppliers().contains("supplier2"));
        assertFalse(updatedTask.assignedToGuests().contains("guest1"));
        assertFalse(updatedTask.assignedToSuppliers().contains("supplier1"));

        ArgumentCaptor<GuestsModel> guestCaptor = ArgumentCaptor.forClass(GuestsModel.class);
        verify(guestsRepo, times(2)).save(guestCaptor.capture());
        List<GuestsModel> updatedGuests = guestCaptor.getAllValues();

        ArgumentCaptor<SuppliersModel> supplierCaptor = ArgumentCaptor.forClass(SuppliersModel.class);
        verify(suppliersRepo, times(2)).save(supplierCaptor.capture());
        List<SuppliersModel> updatedSuppliers = supplierCaptor.getAllValues();

        assertTrue(updatedGuests.stream().anyMatch(g -> g.id().equals("guest2") && g.assignedTasks().contains("task1")));
        assertTrue(updatedSuppliers.stream().anyMatch(s -> s.id().equals("supplier2") && s.assignedTasks().contains("task1")));
    }


    @Test
    void deleteTask_ShouldRemoveTaskAndCleanupAssignments() {
        String taskId = "task1";
        TasksModel taskToDelete = new TasksModel(taskId, "Task One", "Description One", TasksStatusModel.OPEN, "2025-01-01", List.of("guest1"), List.of("supplier1"), userId);
        SuppliersModel associatedSupplier = new SuppliersModel("supplier1", "Supplier One", "Description One", "https://supplierone.com", 1200.00, "2024-12-31", List.of(taskId), "email@example.com", "1234567890", "Address", userId);
        GuestsModel associatedGuest = new GuestsModel("guest1", "Guest", "One", "email@example.com", "0123456789", RsvpStatusModel.PENDING, "Notes here", List.of(taskId), userId);

        when(tasksRepo.findByIdAndOwnerId(taskId, userId)).thenReturn(Optional.of(taskToDelete));
        when(suppliersRepo.findById("supplier1")).thenReturn(Optional.of(associatedSupplier));
        when(guestsRepo.findById("guest1")).thenReturn(Optional.of(associatedGuest));


        tasksService.deleteTask(taskId, userId);

        verify(tasksRepo).deleteById(taskId);
        verify(guestsRepo, atLeastOnce()).save(any(GuestsModel.class));
        verify(suppliersRepo, atLeastOnce()).save(any(SuppliersModel.class));

        ArgumentCaptor<GuestsModel> guestCaptor = ArgumentCaptor.forClass(GuestsModel.class);
        verify(guestsRepo).save(guestCaptor.capture());
        GuestsModel updatedGuest = guestCaptor.getValue();
        assertFalse(updatedGuest.assignedTasks().contains(taskId), "Assigned tasks list should be empty after deleting the task.");

        ArgumentCaptor<SuppliersModel> supplierCaptor = ArgumentCaptor.forClass(SuppliersModel.class);
        verify(suppliersRepo).save(supplierCaptor.capture());
        SuppliersModel updatedSupplier = supplierCaptor.getValue();
        assertFalse(updatedSupplier.assignedTasks().contains(taskId), "Assigned tasks list should be empty after deleting the task.");
    }


    @Test
    void addGuestToTask_ShouldAddGuestIfNotAlreadyAssigned() {
        String guestId = "guest2";
        GuestsModel newGuest = new GuestsModel(guestId, "Guest", "Two", "email@guesttwo.com", "0123456789", RsvpStatusModel.PENDING, "Notes here", new ArrayList<>(), userId);

        when(tasksRepo.findByIdAndOwnerId("task1", userId)).thenReturn(Optional.of(task));
        lenient().when(guestsRepo.findById(guestId)).thenReturn(Optional.of(newGuest));

        tasksService.addGuestToTask("task1", guestId, userId);

        ArgumentCaptor<TasksModel> taskCaptor = ArgumentCaptor.forClass(TasksModel.class);
        verify(tasksRepo).save(taskCaptor.capture());
        TasksModel updatedTask = taskCaptor.getValue();
        assertTrue(updatedTask.assignedToGuests().contains(guestId), "Guest should be added to the assigned guests list");
    }


    @Test
    void addSupplierToTask_ShouldAddSupplierIfNotAlreadyAssigned() {
        String supplierId = "supplier2";
        SuppliersModel newSupplier = new SuppliersModel(supplierId, "Supplier Two", "Description Two", "http://suppliertwo.com", 250.0, "2025-01-01", new ArrayList<>(), "email@suppliertwo.com", "9876543210", "Address Two", userId);
        when(tasksRepo.findByIdAndOwnerId("task1", userId)).thenReturn(Optional.of(task));
        lenient().when(suppliersRepo.findById(supplierId)).thenReturn(Optional.of(newSupplier));

        tasksService.addSupplierToTask("task1", supplierId, userId);

        ArgumentCaptor<TasksModel> taskCaptor = ArgumentCaptor.forClass(TasksModel.class);
        verify(tasksRepo).save(taskCaptor.capture());
        TasksModel updatedTask = taskCaptor.getValue();
        assertTrue(updatedTask.assignedToSuppliers().contains(supplierId), "Supplier should be added to the assigned suppliers list");
    }

}
