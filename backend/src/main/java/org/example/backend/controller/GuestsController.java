package org.example.backend.controller;
import lombok.RequiredArgsConstructor;
import org.example.backend.dto.GuestDto;
import org.example.backend.model.GuestsModel;
import org.example.backend.service.GuestsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guests")
public class GuestsController {
    private final GuestsService guestsService;

    @GetMapping
    public List<GuestsModel> getAllGuests(Authentication authentication) {
        String userId = authentication.getName();
        return guestsService.getAllGuests(userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuestsModel> getGuestById(@PathVariable String id, Authentication authentication) {
        String userId = authentication.getName();
        Optional<GuestsModel> guest = guestsService.getGuestById(id, userId);
        return guest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<GuestsModel> addGuest(@RequestBody GuestDto guestDto, Authentication authentication) {
        String userId = authentication.getName();
        GuestsModel newGuest = guestsService.addGuest(guestDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newGuest);
    }


    @PutMapping("/{id}")
    public ResponseEntity<GuestsModel> updateGuest(@PathVariable String id, @RequestBody GuestDto guestDto, Authentication authentication) {
        String userId = authentication.getName();
        GuestsModel updatedGuest = guestsService.updateGuest(id, guestDto, userId);
        return ResponseEntity.ok(updatedGuest);
    }

    @DeleteMapping("/{id}")
    public void deleteGuest(@PathVariable String id, Authentication authentication) {
        String userId = authentication.getName();
        guestsService.deleteGuest(id, userId);
    }



}
