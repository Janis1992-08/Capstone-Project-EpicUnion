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
    private TasksModel task;
    private final String userId = "ownerId";

    @BeforeEach
    void setUp() {
        guest = new GuestsModel("1", "John Doe", "john@example.com", RsvpStatusModel.CONFIRMED, "Notes here", new ArrayList<>(), userId);
        task = new TasksModel("task1", "Task 1", "Description 1", TasksStatusModel.OPEN, "2024-12-31", new ArrayList<>(), userId);
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
        GuestDto newGuestDto = new GuestDto("Jane Doe", "jane@example.com", RsvpStatusModel.PENDING, "Some notes", List.of(), userId);
        when(idService.generateUUID()).thenReturn("2");

        guestsService.addGuest(newGuestDto, userId);

        ArgumentCaptor<GuestsModel> captor = ArgumentCaptor.forClass(GuestsModel.class);
        verify(guestsRepo).save(captor.capture());
        GuestsModel savedGuest = captor.getValue();

        assertEquals("Jane Doe", savedGuest.name());
        assertEquals("jane@example.com", savedGuest.email());
        assertEquals(RsvpStatusModel.PENDING, savedGuest.rsvpStatus());
        assertEquals("Some notes", savedGuest.notes());
        assertEquals("2", savedGuest.id());
    }

    @Test
    void updateGuest_ShouldUpdateGuestDetails() {
        GuestDto updateDto = new GuestDto("John Doe", "john.doe@example.com", RsvpStatusModel.PENDING, "Updated notes", List.of("task1"), userId);
        when(guestsRepo.findByIdAndOwnerId("1", userId)).thenReturn(Optional.of(guest));

        guestsService.updateGuest("1", updateDto, userId);

        verify(guestsRepo).save(any(GuestsModel.class));
    }

    @Test
    void deleteGuest_ShouldDeleteGuest() {
        when(guestsRepo.findByIdAndOwnerId("1", userId)).thenReturn(Optional.of(guest));

        guestsService.deleteGuest("1", userId);

        verify(guestsRepo).delete(guest);
    }

    @Test
    void assignTaskToGuest_ShouldAddTaskToGuestAndGuestToTask() {
        when(guestsRepo.findByIdAndOwnerId("1", userId)).thenReturn(Optional.of(guest));
        when(tasksRepo.findById("task1")).thenReturn(Optional.of(task));

        guestsService.assignTaskToGuest("1", "task1", userId);

        verify(guestsRepo).save(any(GuestsModel.class));
        verify(tasksRepo).save(any(TasksModel.class));
    }

    @Test
    void removeTaskFromGuest_ShouldRemoveTaskFromGuestAndGuestFromTask() {
        guest = new GuestsModel(guest.id(), guest.name(), guest.email(), guest.rsvpStatus(), guest.notes(), List.of("task1"), userId);
        task = new TasksModel(task.id(), task.title(), task.description(), task.taskStatus(), task.dueDate(), List.of("1"), userId);

        when(guestsRepo.findByIdAndOwnerId("1", userId)).thenReturn(Optional.of(guest));
        when(tasksRepo.findById("task1")).thenReturn(Optional.of(task));

        guestsService.removeTaskFromGuest("1", "task1", userId);

        verify(guestsRepo).save(any(GuestsModel.class));
        verify(tasksRepo).save(any(TasksModel.class));
    }
}