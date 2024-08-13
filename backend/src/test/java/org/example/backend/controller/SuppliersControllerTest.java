package org.example.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.backend.dto.SuppliersDto;
import org.example.backend.model.SuppliersModel;
import org.example.backend.service.SuppliersService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SuppliersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SuppliersService suppliersService;

    private final String ownerId = "ownerId";

    @Test
    @WithMockUser(username = ownerId)
    void getAllSuppliers() throws Exception {
        List<SuppliersModel> suppliers = List.of(new SuppliersModel("1", "Supplier One", "Description for Supplier One", "https://supplierone.com", 150.0, "2025-01-01", List.of(), "email@supplierone.com", "1234567890", "Address One", ownerId));
        when(suppliersService.getAllSuppliers(ownerId)).thenReturn(suppliers);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/suppliers").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    [{
                        "id": "1",
                        "name": "Supplier One",
                        "description": "Description for Supplier One",
                        "websiteUrl": "https://supplierone.com",
                        "costs": 150.0,
                        "deliveryDate": "2025-01-01",
                        "assignedTasks": [],
                        "contactEmail": "email@supplierone.com",
                        "contactPhone": "1234567890",
                        "contactAddress": "Address One",
                        "ownerId": "ownerId"
                    }]
                """));

        verify(suppliersService).getAllSuppliers(ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void getSupplierById() throws Exception {
        Optional<SuppliersModel> supplier = Optional.of(new SuppliersModel("1", "Supplier One", "Description One", "https://supplierone.com", 150.0, "2025-01-01", List.of(), "email@supplierone.com", "1234567890", "Address One", ownerId));
        when(suppliersService.getSupplierById("1", ownerId)).thenReturn(supplier);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/suppliers/1").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                    {
                        "id": "1",
                        "name": "Supplier One",
                        "description": "Description One",
                        "websiteUrl": "https://supplierone.com",
                        "costs": 150.0,
                        "deliveryDate": "2025-01-01",
                        "assignedTasks": [],
                        "contactEmail": "email@supplierone.com",
                        "contactPhone": "1234567890",
                        "contactAddress": "Address One",
                        "ownerId": "ownerId"
                    }
                """));

        verify(suppliersService).getSupplierById("1", ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void addSupplier() throws Exception {
        SuppliersDto newSupplierDto = new SuppliersDto("Supplier Two", "Description Two", "https://suppliertwo.com", 200.0, "2025-12-25", List.of(), "email@suppliertwo.com", "9876543210", "Address Two", ownerId);
        SuppliersModel newSupplier = new SuppliersModel("2", "Supplier Two", "Description Two", "https://suppliertwo.com", 200.0, "2025-12-25", List.of(), "email@suppliertwo.com", "9876543210", "Address Two", ownerId);

        when(suppliersService.addSupplier(any(SuppliersDto.class), eq(ownerId))).thenReturn(newSupplier);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(newSupplierDto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(newSupplier)));

        verify(suppliersService).addSupplier(any(SuppliersDto.class), eq(ownerId));
    }

    @Test
    @WithMockUser(username = ownerId)
    void updateSupplier() throws Exception {
        SuppliersDto updatedSupplierDto = new SuppliersDto("Updated Supplier", "Updated Description", "https://updated.com", 250.0, "2026-01-01", List.of(), "update@email.com", "0987654321", "Updated Address", ownerId);
        SuppliersModel updatedSupplier = new SuppliersModel("1", "Updated Supplier", "Updated Description", "https://updated.com", 250.0, "2026-01-01", List.of(), "update@email.com", "0987654321", "Updated Address", ownerId);

        when(suppliersService.updateSupplier("1", updatedSupplierDto, ownerId)).thenReturn(updatedSupplier);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/suppliers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatedSupplierDto))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(updatedSupplier)));

        verify(suppliersService).updateSupplier("1", updatedSupplierDto, ownerId);
    }

    @Test
    @WithMockUser(username = ownerId)
    void deleteSupplier() throws Exception {
        doNothing().when(suppliersService).deleteSupplier("1", ownerId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/suppliers/1").with(csrf()))
                .andExpect(status().isOk());

        verify(suppliersService).deleteSupplier("1", ownerId);
    }
}
