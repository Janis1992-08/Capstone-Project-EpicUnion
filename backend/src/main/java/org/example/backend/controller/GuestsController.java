package org.example.backend.controller;


import lombok.RequiredArgsConstructor;
import org.example.backend.dto.GuestDto;
import org.example.backend.model.GuestsModel;
import org.example.backend.service.GuestsService;
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
    public void addGuest(@RequestBody GuestDto guestDto, Authentication authentication) {
        String userId = authentication.getName();
        guestsService.addGuest(guestDto, userId);
    }

    @PutMapping("/{id}")
    public void updateGuest(@PathVariable String id, @RequestBody GuestDto guestDto, Authentication authentication) {
        String userId = authentication.getName();
        guestsService.updateGuest(id, guestDto, userId);
    }

    @DeleteMapping("/{id}")
    public void deleteGuest(@PathVariable String id, Authentication authentication) {
        String userId = authentication.getName();
        guestsService.deleteGuest(id, userId);
    }

    @PutMapping("/{guestId}/tasks/{taskId}")
    public ResponseEntity<Void> assignTaskToGuest(@PathVariable String guestId, @PathVariable String taskId, Authentication authentication) {
        String userId = authentication.getName();
        guestsService.assignTaskToGuest(guestId, taskId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{guestId}/tasks/remove/{taskId}")
    public ResponseEntity<Void> removeTaskFromGuest(@PathVariable String guestId, @PathVariable String taskId, Authentication authentication) {
        String userId = authentication.getName();
        guestsService.removeTaskFromGuest(guestId, taskId, userId);
        return ResponseEntity.ok().build();
    }


}
