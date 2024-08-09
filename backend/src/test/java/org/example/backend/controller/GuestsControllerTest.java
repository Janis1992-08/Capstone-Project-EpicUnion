package org.example.backend.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

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
    void getAllGuests_ShouldReturnGuests() throws Exception {
        List<GuestsModel> guests = List.of(new GuestsModel("1", "John Doe", "john.doe@example.com", RsvpStatusModel.CONFIRMED, "Notes", List.of(), ownerId));
        when(mockGuestsService.getAllGuests(ownerId)).thenReturn(guests);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/guests").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("[{'id':'1','name':'John Doe','email':'john.doe@example.com','rsvpStatus':'CONFIRMED','notes':'Notes','assignedTasks':[],'ownerId':'ownerId'}]"));

        verify(mockGuestsService).getAllGuests(ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void getGuestById_ShouldReturnGuest_WhenGuestExists() throws Exception {
        Optional<GuestsModel> guest = Optional.of(new GuestsModel("1", "John Doe", "john.doe@example.com", RsvpStatusModel.CONFIRMED, "Notes", List.of(), ownerId));
        when(mockGuestsService.getGuestById("1", ownerId)).thenReturn(guest);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/guests/1").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json("{'id':'1','name':'John Doe','email':'john.doe@example.com','rsvpStatus':'CONFIRMED','notes':'Notes','assignedTasks':[],'ownerId':'ownerId'}"));

        verify(mockGuestsService).getGuestById("1", ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void getGuestById_ShouldReturnNotFound_WhenGuestDoesNotExist() throws Exception {
        when(mockGuestsService.getGuestById("1", ownerId)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/guests/1").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());

        verify(mockGuestsService).getGuestById("1", ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void addGuest_ShouldCreateGuest() throws Exception {
        GuestDto newGuest = new GuestDto("Alice Smith", "alice@example.com", RsvpStatusModel.PENDING, "Some notes", List.of(), ownerId);

        doNothing().when(mockGuestsService).addGuest(any(GuestDto.class), eq(ownerId));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/guests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newGuest))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(mockGuestsService).addGuest(any(GuestDto.class), eq(ownerId));
    }


    @Test
    @WithMockUser(username = ownerId)
    void updateGuest_ShouldUpdateExistingGuest() throws Exception {
        GuestDto guestUpdate = new GuestDto("John Updated", "john.updated@example.com", RsvpStatusModel.CONFIRMED, "Updated notes", List.of(), ownerId);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/guests/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(guestUpdate))
                        .with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(mockGuestsService).updateGuest("1", guestUpdate, ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void deleteGuest_ShouldDeleteGuest() throws Exception {
        doNothing().when(mockGuestsService).deleteGuest("1", ownerId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/guests/1").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(mockGuestsService).deleteGuest("1", ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void assignTaskToGuest_ShouldAssignTask() throws Exception {
        doNothing().when(mockGuestsService).assignTaskToGuest("1", "task1", ownerId);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/guests/1/tasks/task1").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(mockGuestsService).assignTaskToGuest("1", "task1", ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void removeTaskFromGuest_ShouldRemoveTask() throws Exception {
        doNothing().when(mockGuestsService).removeTaskFromGuest("1", "task1", ownerId);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/guests/1/tasks/remove/task1").with(csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(mockGuestsService).removeTaskFromGuest("1", "task1", ownerId);
    }
}
