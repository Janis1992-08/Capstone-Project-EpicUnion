package org.example.backend.service;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.GuestDto;
import org.example.backend.model.GuestsModel;
import org.example.backend.model.TasksModel;
import org.example.backend.repository.GuestsRepo;
import org.example.backend.repository.TasksRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class GuestsService {
    private final GuestsRepo guestsRepo;
    private final IdService idService;
    private final TasksRepo tasksRepo;


    public List<GuestsModel> getAllGuests(String userId) {
        return guestsRepo.findByOwnerId(userId);
    }


    public Optional<GuestsModel> getGuestById(String id, String userId) {
        return guestsRepo.findByIdAndOwnerId(id, userId);
    }


    public void addGuest(GuestDto guestDto, String userId) {
        GuestsModel guests = new GuestsModel(
                idService.generateUUID(),
                guestDto.name(),
                guestDto.email(),
                guestDto.rsvpStatus(),
                guestDto.notes(),
                guestDto.assignedTasks() != null ? guestDto.assignedTasks() : new ArrayList<>(),
                userId
        );
        guestsRepo.save(guests);
    }

    public void updateGuest(String id, GuestDto guestDto, String userId) {
        GuestsModel updateGuest = guestsRepo.findByIdAndOwnerId(id, userId).orElseThrow();
        updateGuest = updateGuest.withName(guestDto.name())
                .withEmail(guestDto.email())
                .withRsvpStatus(guestDto.rsvpStatus())
                .withNotes(guestDto.notes())
                .withAssignedTasks(guestDto.assignedTasks() != null ? guestDto.assignedTasks() : new ArrayList<>());
        guestsRepo.save(updateGuest);
    }



    public void deleteGuest(String id, String userId) {
        GuestsModel guest = guestsRepo.findByIdAndOwnerId(id, userId).orElseThrow();
        guestsRepo.delete(guest);
    }



    public void assignTaskToGuest(String guestId, String taskId, String userId) {
        GuestsModel guest = guestsRepo.findByIdAndOwnerId(guestId, userId).orElseThrow(() ->
                new IllegalArgumentException("Guest not found or does not belong to the user"));

        List<String> updatedTasks = new ArrayList<>(guest.assignedTasks());
        if (!updatedTasks.contains(taskId)) {
            updatedTasks.add(taskId);
            GuestsModel updatedGuest = guest.withAssignedTasks(updatedTasks);
            guestsRepo.save(updatedGuest);
        }

        TasksModel task = tasksRepo.findById(taskId).orElseThrow();
        List<String> updatedGuests = new ArrayList<>(task.assignedToGuests());
        if (!updatedGuests.contains(guestId)) {
            updatedGuests.add(guestId);
            TasksModel updatedTask = task.withAssignedToGuests(updatedGuests);
            tasksRepo.save(updatedTask);
        }
    }

    public void removeTaskFromGuest(String guestId, String taskId, String userId) {
        GuestsModel guest = guestsRepo.findByIdAndOwnerId(guestId, userId).orElseThrow(() ->
                new IllegalArgumentException("Guest not found or does not belong to the user"));

        List<String> updatedTasks = new ArrayList<>(guest.assignedTasks());
        if (updatedTasks.contains(taskId)) {
            updatedTasks.remove(taskId);
            GuestsModel updatedGuest = guest.withAssignedTasks(updatedTasks);
            guestsRepo.save(updatedGuest);
        }

        TasksModel task = tasksRepo.findById(taskId).orElseThrow();
        List<String> updatedGuests = new ArrayList<>(task.assignedToGuests());
        if (updatedGuests.contains(guestId)) {
            updatedGuests.remove(guestId);
            TasksModel updatedTask = task.withAssignedToGuests(updatedGuests);
            tasksRepo.save(updatedTask);
        }
    }

}