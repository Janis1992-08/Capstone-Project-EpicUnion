package org.example.backend.service;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.GuestDto;
import org.example.backend.model.Guests;
import org.example.backend.repository.GuestsRepo;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class GuestsService {
    private final GuestsRepo guestsRepo;
    private final IdService idService;


    public List<Guests> getAllGuests() {
        return guestsRepo.findAll();
    }


    public Optional<Guests> getGuestById(String id) {
        return guestsRepo.findById(id);
    }

    public void addGuest(GuestDto guestDto) {
        Guests guests = new Guests(idService.generateUUID(),
                guestDto.name(),
                guestDto.email(),
                guestDto.rsvpStatus(),
                guestDto.notes());
        guestsRepo.save(guests);
    }

    public void updateGuest(String id, GuestDto guestDto) {
        Guests updateGuest = guestsRepo.findById(id).orElseThrow();
        updateGuest = updateGuest.withName(guestDto.name())
                .withEmail(guestDto.email())
                .withRsvpStatus(guestDto.rsvpStatus())
                .withNotes(guestDto.notes());
        guestsRepo.save(updateGuest);
    }

    public void deleteGuest(String id) {
        guestsRepo.deleteById(id);
    }



}
