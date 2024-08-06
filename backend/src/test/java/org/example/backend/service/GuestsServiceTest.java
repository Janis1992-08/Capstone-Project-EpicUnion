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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

class GuestsServiceTest {

    @Mock
    private GuestsRepo mockGuestsRepo;

    @Mock
    private TasksRepo mockTasksRepo;

    @Mock
    private IdService mockIdService;

    @InjectMocks
    private GuestsService guestsService;

    private List<GuestsModel> guestTestData;
    private List<TasksModel> taskTestData;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        guestTestData = List.of(
                new GuestsModel("1", "John Doe", "john.doe@example.com", RsvpStatusModel.CONFIRMED, "Notes", new ArrayList<>()),
                new GuestsModel("2", "Jane Miller", "jane.miller@example.com", RsvpStatusModel.PENDING, "Notes", new ArrayList<>())
        );

        taskTestData = List.of(
                new TasksModel("task1", "Task 1", "Description 1", TasksStatusModel.OPEN, "2024-12-31", new ArrayList<>()),
                new TasksModel("task2", "Task 2", "Description 2", TasksStatusModel.OPEN, "2024-12-31", new ArrayList<>())
        );
    }

    @Test
    void getAllGuests_shouldReturnListOfAllGuests_whenCalled() {
        // GIVEN
        when(mockGuestsRepo.findAll()).thenReturn(guestTestData);

        // WHEN
        List<GuestsModel> actual = guestsService.getAllGuests();

        // THEN
        assertEquals(guestTestData, actual);
        verify(mockGuestsRepo).findAll();
    }

    @Test
    void getGuestById_shouldReturnGuest_whenCalledWithValidId() {
        // GIVEN
        Optional<GuestsModel> expected = Optional.of(guestTestData.getFirst());
        when(mockGuestsRepo.findById("1")).thenReturn(expected);

        // WHEN
        Optional<GuestsModel> actual = guestsService.getGuestById("1");

        // THEN
        assertEquals(expected, actual);
        verify(mockGuestsRepo).findById("1");
    }

    @Test
    void getGuestById_shouldReturnEmptyOptional_whenCalledWithInvalidId() {
        // GIVEN
        when(mockGuestsRepo.findById("3")).thenReturn(Optional.empty());

        // WHEN
        Optional<GuestsModel> actual = guestsService.getGuestById("3");

        // THEN
        assertTrue(actual.isEmpty());
        verify(mockGuestsRepo).findById("3");
    }

    @Test
    void addGuest_shouldSaveNewGuest_whenCalledWithGuestDto() {
        // GIVEN
        GuestDto newGuestDto = new GuestDto("Hans Meier", "hans.meier@example.com", RsvpStatusModel.PENDING, "Notes", List.of());
        GuestsModel newGuest = new GuestsModel("generated-id", "Hans Meier", "hans.meier@example.com", RsvpStatusModel.PENDING, "Notes", List.of());

        when(mockIdService.generateUUID()).thenReturn("generated-id");
        when(mockGuestsRepo.save(any(GuestsModel.class))).thenReturn(newGuest);

        // WHEN
        guestsService.addGuest(newGuestDto);

        // THEN
        ArgumentCaptor<GuestsModel> guestCaptor = ArgumentCaptor.forClass(GuestsModel.class);
        verify(mockGuestsRepo).save(guestCaptor.capture());
        GuestsModel savedGuest = guestCaptor.getValue();
        assertEquals("Hans Meier", savedGuest.name());
        assertEquals("hans.meier@example.com", savedGuest.email());
        assertEquals(RsvpStatusModel.PENDING, savedGuest.rsvpStatus());
        assertEquals("Notes", savedGuest.notes());
        assertEquals(List.of(), savedGuest.assignedTasks());
    }

    @Test
    void updateGuest_shouldUpdateAndSaveGuest_whenCalledWithValidIdAndGuestDto() {
        // GIVEN
        GuestDto updateDto = new GuestDto("John Doe", "john.doe@example.com", RsvpStatusModel.PENDING, "Updated notes", List.of("task1"));
        GuestsModel existingGuest = guestTestData.getFirst();
        GuestsModel updatedGuest = new GuestsModel("1", "John Doe", "john.doe@example.com", RsvpStatusModel.PENDING, "Updated notes", List.of("task1"));

        when(mockGuestsRepo.findById("1")).thenReturn(Optional.of(existingGuest));
        when(mockGuestsRepo.save(any(GuestsModel.class))).thenReturn(updatedGuest);

        // WHEN
        guestsService.updateGuest("1", updateDto);

        // THEN
        ArgumentCaptor<GuestsModel> guestCaptor = ArgumentCaptor.forClass(GuestsModel.class);
        verify(mockGuestsRepo).save(guestCaptor.capture());
        GuestsModel savedGuest = guestCaptor.getValue();
        assertEquals(updatedGuest, savedGuest);
    }

    @Test
    void updateGuest_shouldThrowNoSuchElementException_whenCalledWithInvalidId() {
        // GIVEN
        GuestDto updateDto = new GuestDto("John Doe", "john.doe@example.com", RsvpStatusModel.PENDING, "Updated notes", List.of());

        when(mockGuestsRepo.findById("3")).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(NoSuchElementException.class, () -> guestsService.updateGuest("3", updateDto));
        verify(mockGuestsRepo).findById("3");
        verify(mockGuestsRepo, never()).save(any(GuestsModel.class));
    }

    @Test
    void deleteGuest_shouldDeleteGuest_whenCalledWithValidId() {
        // WHEN
        guestsService.deleteGuest("1");

        // THEN
        verify(mockGuestsRepo).deleteById("1");
    }

    @Test
    void assignTaskToGuest_shouldAddTaskToGuestAndTask_whenCalledWithValidIds() {
        // GIVEN
        GuestsModel guest = guestTestData.getFirst();
        TasksModel task = taskTestData.getFirst();

        when(mockGuestsRepo.findById("1")).thenReturn(Optional.of(guest));
        when(mockTasksRepo.findById("task1")).thenReturn(Optional.of(task));
        when(mockGuestsRepo.save(any(GuestsModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mockTasksRepo.save(any(TasksModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        guestsService.assignTaskToGuest("1", "task1");

        // THEN
        ArgumentCaptor<GuestsModel> guestCaptor = ArgumentCaptor.forClass(GuestsModel.class);
        verify(mockGuestsRepo).save(guestCaptor.capture());
        GuestsModel updatedGuest = guestCaptor.getValue();
        assertTrue(updatedGuest.assignedTasks().contains("task1"));

        ArgumentCaptor<TasksModel> taskCaptor = ArgumentCaptor.forClass(TasksModel.class);
        verify(mockTasksRepo).save(taskCaptor.capture());
        TasksModel updatedTask = taskCaptor.getValue();
        assertTrue(updatedTask.assignedTo().contains("1"));
    }

    @Test
    void removeTaskFromGuest_shouldRemoveTaskFromGuestAndTask_whenCalledWithValidIds() {
        // GIVEN
        GuestsModel guest = guestTestData.getFirst();
        guest = new GuestsModel(guest.id(), guest.name(), guest.email(), guest.rsvpStatus(), guest.notes(), List.of("task1"));
        TasksModel task = taskTestData.getFirst();
        task = new TasksModel(task.id(), task.title(), task.description(), task.taskStatus(), task.dueDate(), List.of("1"));

        when(mockGuestsRepo.findById("1")).thenReturn(Optional.of(guest));
        when(mockTasksRepo.findById("task1")).thenReturn(Optional.of(task));
        when(mockGuestsRepo.save(any(GuestsModel.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(mockTasksRepo.save(any(TasksModel.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // WHEN
        guestsService.removeTaskFromGuest("1", "task1");

        // THEN
        ArgumentCaptor<GuestsModel> guestCaptor = ArgumentCaptor.forClass(GuestsModel.class);
        verify(mockGuestsRepo).save(guestCaptor.capture());
        GuestsModel updatedGuest = guestCaptor.getValue();
        assertFalse(updatedGuest.assignedTasks().contains("task1"));

        ArgumentCaptor<TasksModel> taskCaptor = ArgumentCaptor.forClass(TasksModel.class);
        verify(mockTasksRepo).save(taskCaptor.capture());
        TasksModel updatedTask = taskCaptor.getValue();
        assertFalse(updatedTask.assignedTo().contains("1"));
    }
}
