package org.example.backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.SuppliersDto;
import org.example.backend.model.SuppliersModel;
import org.example.backend.service.SuppliersService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<SuppliersModel> addSupplier(@RequestBody SuppliersDto suppliersDto, Authentication authentication) {
        String userId = authentication.getName();
        SuppliersModel newSupplier = suppliersService.addSupplier(suppliersDto, userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSupplier);
    }



    @PutMapping("/{id}")
    public ResponseEntity<SuppliersModel> updateSupplier(@PathVariable String id, @RequestBody SuppliersDto suppliersDto, Authentication authentication) {
        String userId = authentication.getName();
        SuppliersModel updatedSupplier = suppliersService.updateSupplier(id, suppliersDto, userId);
        return ResponseEntity.ok(updatedSupplier);
    }


    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable String id, Authentication authentication) {
        String userId = authentication.getName();
        suppliersService.deleteSupplier(id, userId);
    }
}

