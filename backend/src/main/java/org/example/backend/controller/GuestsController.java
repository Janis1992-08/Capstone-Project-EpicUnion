package org.example.backend.controller;


import lombok.RequiredArgsConstructor;
import org.example.backend.dto.GuestDto;
import org.example.backend.model.Guests;
import org.example.backend.service.GuestsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/guests")
public class GuestsController {
    private final GuestsService guestsService;

    @GetMapping
    public List<Guests> getAllGuests() {
        return guestsService.getAllGuests();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Guests> getGuestById(@PathVariable String id) {
        Optional<Guests> guest = guestsService.getGuestById(id);
        return guest.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public void addGuest(@RequestBody GuestDto guestDto) {
        guestsService.addGuest(guestDto);
    }

    @PutMapping("/{id}")
    public void updateGuest(@PathVariable String id, @RequestBody GuestDto guestDto) {
        guestsService.updateGuest(id, guestDto);
    }

    @DeleteMapping("/{id}")
    public void deleteGuest(@PathVariable String id) {
        guestsService.deleteGuest(id);
    }



}
