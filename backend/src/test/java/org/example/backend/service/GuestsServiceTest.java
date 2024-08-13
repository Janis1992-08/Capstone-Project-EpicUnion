package org.example.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.example.backend.dto.GuestDto;
import org.example.backend.model.GuestsModel;
import org.example.backend.model.RsvpStatusModel;
import org.example.backend.model.TasksStatusModel;
import org.example.backend.model.TasksModel;
import org.example.backend.repository.GuestsRepo;
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

@ExtendWith(MockitoExtension.class)
class GuestsServiceTest {

    @Mock
    private GuestsRepo guestsRepo;

    @Mock
    private TasksRepo tasksRepo;

    @Mock
    private IdService idService;

    @InjectMocks
    private GuestsService guestsService;

    private GuestsModel guest;

    private final String userId = "ownerId";

    @BeforeEach
    void setUp() {
        guest = new GuestsModel("1", "John", "Doe", "john@example.com", "0123456789", RsvpStatusModel.CONFIRMED, "Notes here", new ArrayList<>(), userId);
    }

    @Test
    void getAllGuests_ShouldReturnGuestsForUser() {
        List<GuestsModel> expectedGuests = List.of(guest);
        when(guestsRepo.findByOwnerId(userId)).thenReturn(expectedGuests);

        List<GuestsModel> actualGuests = guestsService.getAllGuests(userId);

        assertEquals(expectedGuests, actualGuests);
        verify(guestsRepo).findByOwnerId(userId);
    }

    @Test
    void getGuestById_ShouldReturnGuestWhenExists() {
        when(guestsRepo.findByIdAndOwnerId("1", userId)).thenReturn(Optional.of(guest));

        Optional<GuestsModel> actual = guestsService.getGuestById("1", userId);

        assertTrue(actual.isPresent());
        assertEquals(guest, actual.get());
        verify(guestsRepo).findByIdAndOwnerId("1", userId);
    }

    @Test
    void getGuestById_ShouldReturnEmptyWhenGuestNotFound() {
        when(guestsRepo.findByIdAndOwnerId("3", userId)).thenReturn(Optional.empty());

        Optional<GuestsModel> actual = guestsService.getGuestById("3", userId);

        assertTrue(actual.isEmpty());
        verify(guestsRepo).findByIdAndOwnerId("3", userId);
    }

    @Test
    void addGuest_ShouldSaveNewGuest() {
        GuestDto newGuestDto = new GuestDto("Jane", "Doe", "jane@example.com", "0123456789", RsvpStatusModel.PENDING, "Some notes", List.of(), userId);
        when(idService.generateUUID()).thenReturn("2");

        guestsService.addGuest(newGuestDto, userId);

        ArgumentCaptor<GuestsModel> captor = ArgumentCaptor.forClass(GuestsModel.class);
        verify(guestsRepo).save(captor.capture());
        GuestsModel savedGuest = captor.getValue();

        assertEquals("Jane", savedGuest.firstName());
        assertEquals("Doe", savedGuest.lastName());
        assertEquals("jane@example.com", savedGuest.email());
        assertEquals("0123456789", savedGuest.phoneNumber());
        assertEquals(RsvpStatusModel.PENDING, savedGuest.rsvpStatus());
        assertEquals("Some notes", savedGuest.notes());
        assertEquals("2", savedGuest.id());
    }


    @Test
    void updateGuest_ShouldUpdateGuestDetails() {
        List<String> initialTasks = List.of("task1");
        guest = new GuestsModel("1", "John", "Doe", "john@example.com", "0123456789", RsvpStatusModel.CONFIRMED, "Notes here", initialTasks, userId);
        GuestDto updateDto = new GuestDto("John", "Updated", "john.updated@example.com", "9876543210", RsvpStatusModel.PENDING, "Updated notes", List.of("task2"), userId);

        TasksModel oldTask = new TasksModel("task1", "Old Task", "Old Description", TasksStatusModel.IN_PROGRESS, "2025-01-01", new ArrayList<>(), new ArrayList<>(), userId);
        TasksModel newTask = new TasksModel("task2", "New Task", "New Description", TasksStatusModel.OPEN, "2025-02-01", new ArrayList<>(), new ArrayList<>(), userId);

        when(guestsRepo.findByIdAndOwnerId("1", userId)).thenReturn(Optional.of(guest));
        when(tasksRepo.findById("task1")).thenReturn(Optional.of(oldTask));
        when(tasksRepo.findById("task2")).thenReturn(Optional.of(newTask));
        when(guestsRepo.save(any(GuestsModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(tasksRepo.save(any(TasksModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        guestsService.updateGuest("1", updateDto, userId);

        ArgumentCaptor<TasksModel> taskCaptor = ArgumentCaptor.forClass(TasksModel.class);
        verify(tasksRepo, times(2)).save(taskCaptor.capture());
        List<TasksModel> savedTasks = taskCaptor.getAllValues();

        assertTrue(savedTasks.stream().anyMatch(task -> task.id().equals("task1") && task.assignedToGuests().isEmpty()));
        assertTrue(savedTasks.stream().anyMatch(task -> task.id().equals("task2") && task.assignedToGuests().contains("1")));
    }




    @Test
    void deleteGuest_ShouldDeleteGuestAndCleanupTasks() {

        String guestId = "1";
        GuestsModel guestToDelete = new GuestsModel(guestId, "John", "Doe", "john@example.com", "0123456789", RsvpStatusModel.CONFIRMED, "Notes here", List.of("task1"), userId);
        TasksModel associatedTask = new TasksModel("task1", "Task 1", "Description 1", TasksStatusModel.OPEN, "2024-12-31", List.of(guestId), new ArrayList<>(), userId);

        when(guestsRepo.findByIdAndOwnerId(guestId, userId)).thenReturn(Optional.of(guestToDelete));
        when(tasksRepo.findById("task1")).thenReturn(Optional.of(associatedTask));

        guestsService.deleteGuest(guestId, userId);

        verify(guestsRepo).deleteById(guestId);

        ArgumentCaptor<TasksModel> taskCaptor = ArgumentCaptor.forClass(TasksModel.class);
        verify(tasksRepo).save(taskCaptor.capture());
        TasksModel updatedTask = taskCaptor.getValue();
        assertFalse(updatedTask.assignedToGuests().contains(guestId), "Assigned guests list should be empty after deleting the guest.");
    }


}