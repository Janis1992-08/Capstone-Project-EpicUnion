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
        GuestsModel guests = new GuestsModel(idService.generateUUID(),
                guestDto.name(),
                guestDto.email(),
                guestDto.rsvpStatus(),
                guestDto.notes(),
                List.of());
        guestsRepo.save(guests);
    }

    public void updateGuest(String id, GuestDto guestDto) {
        GuestsModel updateGuest = guestsRepo.findById(id).orElseThrow();
        updateGuest = updateGuest.withName(guestDto.name())
                .withEmail(guestDto.email())
                .withRsvpStatus(guestDto.rsvpStatus())
                .withNotes(guestDto.notes());
        guestsRepo.save(updateGuest);
    }


    public void deleteGuest(String id) {
        guestsRepo.deleteById(id);
    }


    public void addTaskToGuest(String guestId, String taskId) {
        GuestsModel guest = guestsRepo.findById(guestId).orElseThrow();
        List<String> updatedTaskIds = new ArrayList<>(guest.taskIds());
        updatedTaskIds.add(taskId);
        guest = guest.withTaskIds(updatedTaskIds);
        guestsRepo.save(guest);
    }


}
