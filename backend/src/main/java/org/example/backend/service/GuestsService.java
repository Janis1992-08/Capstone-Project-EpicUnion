package org.example.backend.service;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.GuestDto;
import org.example.backend.model.GuestsModel;
import org.example.backend.repository.GuestsRepo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class GuestsService {
    private final GuestsRepo guestsRepo;
    private final IdService idService;


    public List<GuestsModel> getAllGuests() {
        return guestsRepo.findAll();
    }


    public Optional<GuestsModel> getGuestById(String id) {
        return guestsRepo.findById(id);
    }

    public void addGuest(GuestDto guestDto) {
        GuestsModel guests = new GuestsModel(
                idService.generateUUID(),
                guestDto.name(),
                guestDto.email(),
                guestDto.rsvpStatus(),
                guestDto.notes(),
                guestDto.assignedTasks() != null ? guestDto.assignedTasks() : new ArrayList<>()
        );
        guestsRepo.save(guests);
    }

    public void updateGuest(String id, GuestDto guestDto) {
        GuestsModel updateGuest = guestsRepo.findById(id).orElseThrow();
        updateGuest = updateGuest.withName(guestDto.name())
                .withEmail(guestDto.email())
                .withRsvpStatus(guestDto.rsvpStatus())
                .withNotes(guestDto.notes())
                .withAssignedTasks(guestDto.assignedTasks() != null ? guestDto.assignedTasks() : new ArrayList<>());
        guestsRepo.save(updateGuest);
    }


    public void deleteGuest(String id) {
        guestsRepo.deleteById(id);
    }


    public void assignTaskToGuest(String guestId, String taskId) {
        GuestsModel guest = guestsRepo.findById(guestId).orElseThrow();
        List<String> updatedTasks = new ArrayList<>(guest.assignedTasks());
        if (!updatedTasks.contains(taskId)) {
            updatedTasks.add(taskId);
            GuestsModel updatedGuest = guest.withAssignedTasks(updatedTasks);
            guestsRepo.save(updatedGuest);
            System.out.println("Task assigned to guest: " + guestId + ", Task ID: " + taskId);
        }
    }



}