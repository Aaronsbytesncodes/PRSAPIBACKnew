
 
package com.PRS.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.PRS.DB.LineItemRepo;
import com.PRS.model.LineItem;

import lombok.*;



@RestController
@RequestMapping("/api/line-items")
@RequiredArgsConstructor
public class LineItemController {   

    private final LineItemRepo lineItemRepo;

    @GetMapping
    public List<LineItem> getAll() {
        return lineItemRepo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineItem> get(@PathVariable Integer id) {
        return lineItemRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


@PostMapping
public ResponseEntity<?> create(@RequestBody LineItem lineItem) {
    try {
        if (lineItem.getProduct() == null || lineItem.getQuantity() == null) {
            return ResponseEntity.badRequest().body("Product and quantity are required.");
        }
        LineItem savedLineItem = lineItemRepo.save(lineItem);
        recalculateTotal(lineItem.getRequest().getId()); // Recalculate total after adding
        return ResponseEntity.ok(savedLineItem);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
    }
}

@PutMapping("/{id}")
public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody LineItem lineItem) {
    try {
        return lineItemRepo.findById(id)
                .map(existingLineItem -> {
                    if (lineItem.getProduct() == null || lineItem.getQuantity() == null) {
                        return ResponseEntity.badRequest().body("Product and quantity are required.");
                    }
                    existingLineItem.setQuantity(lineItem.getQuantity());
                    existingLineItem.setProduct(lineItem.getProduct());
                    existingLineItem.setRequest(lineItem.getRequest());
                    return ResponseEntity.ok(lineItemRepo.save(existingLineItem));
                })
                .orElse(ResponseEntity.status(404).body("LineItem not found."));
    } catch (Exception e) {
        return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
    }
}

@DeleteMapping("/{id}")
public ResponseEntity<?> delete(@PathVariable Integer id) {
    try {
        if (!lineItemRepo.existsById(id)) {
            return ResponseEntity.status(404).body("LineItem not found.");
        }
        lineItemRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    } catch (Exception e) {
        return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
    }
}
}

