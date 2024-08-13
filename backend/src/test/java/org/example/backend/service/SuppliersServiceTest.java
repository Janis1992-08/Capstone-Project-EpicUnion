package org.example.backend.service;

import org.example.backend.dto.SuppliersDto;
import org.example.backend.model.SuppliersModel;
import org.example.backend.model.TasksModel;
import org.example.backend.model.TasksStatusModel;
import org.example.backend.repository.SuppliersRepo;
import org.example.backend.repository.TasksRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuppliersServiceTest {

    @Mock
    private SuppliersRepo suppliersRepo;

    @Mock
    private TasksRepo tasksRepo;

    @Mock
    private IdService idService;

    @InjectMocks
    private SuppliersService suppliersService;

    private SuppliersModel supplier;

    private final String userId = "ownerId";

    @BeforeEach
    void setUp() {
        supplier = new SuppliersModel("1", "Supplier Name", "Supplier Description", "https://example.com", 1200.00, "2024-12-31", new ArrayList<>(), "email@example.com", "1234567890", "Address", userId);
    }

    @Test
    void getAllSuppliers_ShouldReturnSuppliersForUser() {
        List<SuppliersModel> expectedSuppliers = List.of(supplier);
        when(suppliersRepo.findByOwnerId(userId)).thenReturn(expectedSuppliers);

        List<SuppliersModel> actualSuppliers = suppliersService.getAllSuppliers(userId);

        assertEquals(expectedSuppliers, actualSuppliers);
        verify(suppliersRepo).findByOwnerId(userId);
    }

    @Test
    void getSupplierById_ShouldReturnSupplierWhenExists() {
        when(suppliersRepo.findByIdAndOwnerId("1", userId)).thenReturn(Optional.of(supplier));

        Optional<SuppliersModel> actual = suppliersService.getSupplierById("1", userId);

        assertTrue(actual.isPresent());
        assertEquals(supplier, actual.get());
        verify(suppliersRepo).findByIdAndOwnerId("1", userId);
    }

    @Test
    void getSupplierById_ShouldReturnEmptyWhenSupplierNotFound() {
        when(suppliersRepo.findByIdAndOwnerId("3", userId)).thenReturn(Optional.empty());

        Optional<SuppliersModel> actual = suppliersService.getSupplierById("3", userId);

        assertTrue(actual.isEmpty());
        verify(suppliersRepo).findByIdAndOwnerId("3", userId);
    }

    @Test
    void addSupplier_ShouldSaveNewSupplier() {
        SuppliersDto newSupplierDto = new SuppliersDto("New Supplier", "New Description", "https://newexample.com", 1500.00, "2025-01-01", List.of(), "newemail@example.com", "9876543210", "New Address", userId);
        when(idService.generateUUID()).thenReturn("2");

        suppliersService.addSupplier(newSupplierDto, userId);

        ArgumentCaptor<SuppliersModel> captor = ArgumentCaptor.forClass(SuppliersModel.class);
        verify(suppliersRepo).save(captor.capture());
        SuppliersModel savedSupplier = captor.getValue();

        assertEquals("New Supplier", savedSupplier.name());
        assertEquals("New Description", savedSupplier.description());
        assertEquals("https://newexample.com", savedSupplier.websiteUrl());
        assertEquals(1500.00, savedSupplier.costs());
        assertEquals("2025-01-01", savedSupplier.deliveryDate());
        assertEquals("newemail@example.com", savedSupplier.contactEmail());
        assertEquals("9876543210", savedSupplier.contactPhone());
        assertEquals("New Address", savedSupplier.contactAddress());
        assertEquals("2", savedSupplier.id());
    }


    @Test
    void updateSupplier_ShouldUpdateSupplierDetailsAndHandleTasksCorrectly() {
        List<String> initialTasks = List.of("task1");
        supplier = new SuppliersModel("1", "Supplier Name", "Supplier Description", "https://example.com", 1200.00, "2024-12-31", initialTasks, "email@example.com", "1234567890", "Address", userId);
        SuppliersDto updateDto = new SuppliersDto("Updated Supplier", "Updated Description", "https://updatedexample.com", 1600.00, "2025-02-01", List.of("task2"), "updatedemail@example.com", "9876543210", "Updated Address", userId);

        TasksModel oldTask = new TasksModel("task1", "Old Task", "Old Description", TasksStatusModel.IN_PROGRESS, "2025-01-01", new ArrayList<>(), List.of("1"), userId);
        TasksModel newTask = new TasksModel("task2", "New Task", "New Description", TasksStatusModel.OPEN, "2025-02-02", new ArrayList<>(), List.of(), userId);

        when(suppliersRepo.findByIdAndOwnerId("1", userId)).thenReturn(Optional.of(supplier));
        when(tasksRepo.findById("task1")).thenReturn(Optional.of(oldTask));
        when(tasksRepo.findById("task2")).thenReturn(Optional.of(newTask));
        when(suppliersRepo.save(ArgumentMatchers.any(SuppliersModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tasksRepo.save(ArgumentMatchers.any(TasksModel.class))).thenAnswer(invocation -> invocation.getArgument(0));


        suppliersService.updateSupplier("1", updateDto, userId);

        ArgumentCaptor<TasksModel> taskCaptor = ArgumentCaptor.forClass(TasksModel.class);
        verify(tasksRepo, times(2)).save(taskCaptor.capture());
        List<TasksModel> savedTasks = taskCaptor.getAllValues();

        assertTrue(savedTasks.stream().anyMatch(task -> task.id().equals("task1") && !task.assignedToSuppliers().contains("1")));
        assertTrue(savedTasks.stream().anyMatch(task -> task.id().equals("task2") && task.assignedToSuppliers().contains("1")));
    }


    @Test
    void deleteSupplier_ShouldDeleteSupplierAndCleanupTasks() {
        String supplierId = "1";
        SuppliersModel supplierToDelete = new SuppliersModel(supplierId, "Supplier Name", "Supplier Description", "https://example.com", 1200.00, "2024-12-31", List.of("task1"), "email@example.com", "1234567890", "Address", userId);
        TasksModel associatedTask = new TasksModel("task1", "Task 1", "Description 1", TasksStatusModel.OPEN, "2024-12-31", new ArrayList<>(), List.of(supplierId), userId);

        when(suppliersRepo.findByIdAndOwnerId(supplierId, userId)).thenReturn(Optional.of(supplierToDelete));
        when(tasksRepo.findById("task1")).thenReturn(Optional.of(associatedTask));

        suppliersService.deleteSupplier(supplierId, userId);

        verify(suppliersRepo).deleteById(supplierId);

        ArgumentCaptor<TasksModel> taskCaptor = ArgumentCaptor.forClass(TasksModel.class);
        verify(tasksRepo).save(taskCaptor.capture());
        TasksModel updatedTask = taskCaptor.getValue();
        assertFalse(updatedTask.assignedToSuppliers().contains(supplierId), "Assigned suppliers list should be empty after deleting the supplier.");
    }

}
