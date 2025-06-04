package com.PRS.controller;

import com.PRS.DB.VendorRepo;
import com.PRS.model.Vendor;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor // injects final vendorRepo
public class VendorController {
@Autowired
    private VendorRepo vendorRepo;

    @GetMapping
    public List<Vendor> getAll() {
        return vendorRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Vendor> get(@PathVariable Integer id) {
        return vendorRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Vendor create(@RequestBody Vendor vendor) {
        return vendorRepo.save(vendor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Vendor> update(@PathVariable Integer id, @RequestBody Vendor vendor) {
        if (!vendorRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vendor.setId(id);
        return ResponseEntity.ok(vendorRepo.save(vendor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!vendorRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        vendorRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}