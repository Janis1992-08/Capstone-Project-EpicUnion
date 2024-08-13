package org.example.backend.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.dto.GuestDto;
import org.example.backend.model.GuestsModel;
import org.example.backend.model.RsvpStatusModel;
import org.example.backend.service.GuestsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import java.util.Optional;



@SpringBootTest
@AutoConfigureMockMvc
class GuestsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GuestsService mockGuestsService;

    private final String ownerId = "ownerId";

    @Test
    @WithMockUser(username = ownerId)
    void getAllGuests() throws Exception {
        List<GuestsModel> guests = List.of(new GuestsModel("1", "John", "Doe", "john.doe@example.com", "0123456789", RsvpStatusModel.CONFIRMED, "Notes here", List.of(), ownerId));
        when(mockGuestsService.getAllGuests(ownerId)).thenReturn(guests);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/guests").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [{
                        "id": "1",
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "john.doe@example.com",
                        "phoneNumber": "0123456789",
                        "rsvpStatus": "CONFIRMED",
                        "notes": "Notes here",
                        "assignedTasks": [],
                        "ownerId": "ownerId"
                    }]
                """));

        verify(mockGuestsService).getAllGuests(ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void getGuestById() throws Exception {
        Optional<GuestsModel> guest = Optional.of(new GuestsModel("1", "John", "Doe", "john.doe@example.com", "0123456789", RsvpStatusModel.CONFIRMED, "Notes", List.of(), ownerId));
        when(mockGuestsService.getGuestById("1", ownerId)).thenReturn(guest);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/guests/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {
                        "id": "1",
                        "firstName": "John",
                        "lastName": "Doe",
                        "email": "john.doe@example.com",
                        "phoneNumber": "0123456789",
                        "rsvpStatus": "CONFIRMED",
                        "notes": "Notes",
                        "assignedTasks": [],
                        "ownerId": "ownerId"
                    }
                """));
        verify(mockGuestsService).getGuestById("1", ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void addGuest() throws Exception {
        GuestDto newGuestDto = new GuestDto("Alice", "Smith", "alice@example.com", "0123456789", RsvpStatusModel.PENDING, "Some notes", List.of(), ownerId);
        GuestsModel newGuest = new GuestsModel("2", "Alice", "Smith", "alice@example.com", "0123456789", RsvpStatusModel.PENDING, "Some notes", List.of(), ownerId);

        when(mockGuestsService.addGuest(any(GuestDto.class), eq(ownerId))).thenReturn(newGuest);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newGuestDto))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().json(new ObjectMapper().writeValueAsString(newGuest)));

        verify(mockGuestsService).addGuest(any(GuestDto.class), eq(ownerId));
    }



    @Test
    @WithMockUser(username = ownerId)
    void updateGuest() throws Exception {
        GuestDto guestUpdate = new GuestDto("John", "Updated", "john.updated@example.com", "9876543210", RsvpStatusModel.CONFIRMED, "Updated notes", List.of(), ownerId);

        mockMvc.perform(put("/api/guests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(guestUpdate))
                        .with(csrf()))
                .andExpect(status().isOk());

        verify(mockGuestsService).updateGuest("1", guestUpdate, ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void deleteGuest() throws Exception {
        doNothing().when(mockGuestsService).deleteGuest("1", ownerId);

        mockMvc.perform(delete("/api/guests/1").with(csrf()))
                .andExpect(status().isOk());

        verify(mockGuestsService).deleteGuest("1", ownerId);
    }
}