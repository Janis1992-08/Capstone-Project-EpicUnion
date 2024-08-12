package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SuppliersDto;
import org.example.backend.model.SuppliersModel;
import org.example.backend.service.SuppliersService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SuppliersController {
    private final SuppliersService suppliersService;

    @GetMapping
    public List<SuppliersModel> getAllSuppliers(Authentication authentication) {
        return suppliersService.getAllSuppliers(authentication.getName());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuppliersModel> getSupplierById(@PathVariable String id, Authentication authentication) {
        String userId = authentication.getName();
        Optional<SuppliersModel> supplier = suppliersService.getSupplierById(id, userId);
        return supplier.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public void addSupplier(@RequestBody SuppliersDto suppliersDto, Authentication authentication) {
        String userId =  authentication.getName();
        suppliersService.addSupplier(suppliersDto, userId);
    }



    @PutMapping("/{id}")
    public void updateSupplier(@PathVariable String id, @RequestBody SuppliersDto suppliersDto, Authentication authentication) {
        String userId = authentication.getName();
        suppliersService.updateSupplier(id, suppliersDto, userId);
    }


    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable String id, Authentication authentication) {
        String userId = authentication.getName();
        suppliersService.deleteSupplier(id, userId);
    }

    @PutMapping("/{supplierId}/tasks/{taskId}")
    public ResponseEntity<Void> assignTaskToSupplier(@PathVariable String supplierId, @PathVariable String taskId, Authentication authentication) {
        String userId = authentication.getName();
        suppliersService.assignTaskToSupplier(supplierId, taskId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{supplierId}/tasks/remove/{taskId}")
    public ResponseEntity<Void> removeTaskFromSupplier(@PathVariable String supplierId, @PathVariable String taskId, Authentication authentication) {
        String userId = authentication.getName();
        suppliersService.removeTaskFromSupplier(supplierId, taskId, userId);
        return ResponseEntity.ok().build();
    }



}

