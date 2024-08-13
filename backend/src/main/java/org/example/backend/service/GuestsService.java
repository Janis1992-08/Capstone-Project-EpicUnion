package org.example.backend.service;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.GuestDto;
import org.example.backend.model.GuestsModel;
import org.example.backend.model.TasksModel;
import org.example.backend.repository.GuestsRepo;
import org.example.backend.repository.TasksRepo;
import org.springframework.stereotype.Service;

import java.util.*;


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


    public GuestsModel addGuest(GuestDto guestDto, String userId) {
        GuestsModel guest = new GuestsModel(
                idService.generateUUID(),
                guestDto.firstName(),
                guestDto.lastName(),
                guestDto.email(),
                guestDto.phoneNumber(),
                guestDto.rsvpStatus(),
                guestDto.notes(),
                guestDto.assignedTasks(),
                userId
        );

        guestsRepo.save(guest);

        for (String taskId : guestDto.assignedTasks()) {
            TasksModel task = tasksRepo.findById(taskId).orElseThrow();
            List<String> updatedGuest = new ArrayList<>(task.assignedToGuests());
            updatedGuest.add(guest.id());
            GuestsModel updatedTask = guest.withAssignedTasks(updatedGuest);
            guestsRepo.save(updatedTask);
        }

        return guest;
    }


    public GuestsModel updateGuest(String guestId, GuestDto guestDto, String userId) {
        GuestsModel updateGuest = guestsRepo.findByIdAndOwnerId(guestId, userId).orElseThrow();
        List<String> oldAssignedTo = updateGuest.assignedTasks();

        updateGuest = updateGuest
                .withFirstName(guestDto.firstName())
                .withLastName(guestDto.lastName())
                .withEmail(guestDto.email())
                .withPhoneNumber(guestDto.phoneNumber())
                .withRsvpStatus(guestDto.rsvpStatus())
                .withNotes(guestDto.notes())
                .withAssignedTasks(guestDto.assignedTasks());
        guestsRepo.save(updateGuest);

        Set<String> newAssignedTo = new HashSet<>(guestDto.assignedTasks());
        Set<String> oldAssignedToSet = new HashSet<>(oldAssignedTo);

        for (String taskId : oldAssignedToSet) {
            if (!newAssignedTo.contains(taskId)) {
                TasksModel task = tasksRepo.findById(taskId).orElseThrow();
                List<String> updatedGuests = new ArrayList<>(task.assignedToGuests());
                updatedGuests.remove(updateGuest.id());
                TasksModel updatedTask = task.withAssignedToGuests(updatedGuests);
                tasksRepo.save(updatedTask);
            }
        }

        for (String taskId : newAssignedTo) {
            if (!oldAssignedTo.contains(taskId)) {
                TasksModel task = tasksRepo.findById(taskId).orElseThrow();
                List<String> updatedGuests = new ArrayList<>(task.assignedToGuests());
                updatedGuests.add(updateGuest.id());
                TasksModel updatedTask = task.withAssignedToGuests(updatedGuests);
                tasksRepo.save(updatedTask);
            }
        }

        return updateGuest;
    }


    public void deleteGuest(String guestId, String userId) {
        GuestsModel guestToDelete = guestsRepo.findByIdAndOwnerId(guestId, userId).orElseThrow();

        for (String taskId : guestToDelete.assignedTasks()) {
            TasksModel task = tasksRepo.findById(taskId).orElseThrow();
            List<String> updatedGuest = new ArrayList<>(task.assignedToGuests());
            updatedGuest.remove(guestToDelete.id());
            TasksModel updatedTask = task.withAssignedToGuests(updatedGuest);
            tasksRepo.save(updatedTask);
        }

        guestsRepo.deleteById(guestId);
    }


}