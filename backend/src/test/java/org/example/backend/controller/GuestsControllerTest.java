package org.example.backend.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import org.example.backend.dto.GuestDto;
import org.example.backend.model.GuestsModel;
import org.example.backend.model.RsvpStatusModel;
import org.example.backend.service.GuestsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.List;
import java.util.Optional;



@SpringBootTest
@AutoConfigureMockMvc
class GuestsControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private GuestsService mockGuestsService;
//
//    private List<GuestsModel> guestTestData;
//
//    @BeforeEach
//    void setUp() {
//        guestTestData = List.of(
//                new GuestsModel("1", "John Doe", "john.doe@example.com", RsvpStatusModel.CONFIRMED, "Notes", List.of())
//        );
//    }
//
//    @Test
//    void getAllGuests() throws Exception {
//
//        List<GuestsModel> expectedGuests = guestTestData;
//
//        when(mockGuestsService.getAllGuests()).thenReturn(expectedGuests);
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/guests"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json("""
//                    [{
//                        "id": "1",
//                        "name": "John Doe",
//                        "email": "john.doe@example.com",
//                        "rsvpStatus": "CONFIRMED",
//                        "notes": "Notes",
//                        "assignedTasks": []
//                    }]
//                """));
//    }
//
//    @Test
//    void getGuestById() throws Exception {
//
//        GuestsModel expectedGuest = guestTestData.getFirst();
//
//        when(mockGuestsService.getGuestById("1")).thenReturn(Optional.of(expectedGuest));
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/guests/1"))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().json("""
//                    {
//                        "id": "1",
//                        "name": "John Doe",
//                        "email": "john.doe@example.com",
//                        "rsvpStatus": "CONFIRMED",
//                        "notes": "Notes",
//                        "assignedTasks": []
//                    }
//                """));
//    }
//
//    @Test
//    void getGuestById_NotFound() throws Exception {
//
//        when(mockGuestsService.getGuestById("4")).thenReturn(Optional.empty());
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/api/guests/4"))
//                .andExpect(MockMvcResultMatchers.status().isNotFound());
//    }
//
//    @Test
//    void addGuest() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/api/guests")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                    {
//                        "name": "Hans Meier",
//                        "email": "hans.meier@example.com",
//                        "rsvpStatus": "PENDING",
//                        "notes": "Notes",
//                        "assignedTasks": []
//                    }
//                """))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        verify(mockGuestsService).addGuest(any(GuestDto.class));
//    }
//
//    @Test
//    void updateGuest() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/guests/1")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("""
//                    {
//                        "name": "John Doe",
//                        "email": "john.doe@example.com",
//                        "rsvpStatus": "PENDING",
//                        "notes": "Updated notes",
//                        "assignedTasks": []
//                    }
//                """))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        verify(mockGuestsService).updateGuest(eq("1"), any(GuestDto.class));
//    }
//
//    @Test
//    void deleteGuest() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.delete("/api/guests/1"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        verify(mockGuestsService).deleteGuest("1");
//    }
//
//    @Test
//    void assignTaskToGuest() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/guests/1/tasks/task1"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        verify(mockGuestsService).assignTaskToGuest("1", "task1");
//    }
//
//    @Test
//    void removeTaskFromGuest() throws Exception {
//
//        mockMvc.perform(MockMvcRequestBuilders.put("/api/guests/1/tasks/remove/task1"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//
//        verify(mockGuestsService).removeTaskFromGuest("1", "task1");
//    }
}
