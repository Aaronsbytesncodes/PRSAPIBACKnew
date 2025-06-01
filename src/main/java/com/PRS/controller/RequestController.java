
package com.PRS.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.PRS.DB.RequestRepo;
import com.PRS.Services.RequestService;
import com.PRS.model.Request;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestRepo requestRepo;
    private final RequestService requestService;

    public RequestController(RequestRepo requestRepo, RequestService requestService) {
        this.requestRepo = requestRepo;
        this.requestService = requestService;
    }

    @GetMapping
    public List<Request> getAll() {
        return requestRepo.findAll();
    }

    
    @GetMapping("/{id}")
    public ResponseEntity<Request> get(@PathVariable Integer id) {
        return requestRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

@GetMapping("/list-review/{userId}")
public ResponseEntity<List<Request>> getRequestsForReview(@PathVariable Integer userId) {
    List<Request> requests = requestRepo.findRequestsForReview(userId);
    return ResponseEntity.ok(requests);
}

    @PostMapping
    public ResponseEntity<Request> create(@RequestBody Request request) {
    	request.setStatus("NEW");
        return ResponseEntity.ok(requestService.create(request));}
    

    @PutMapping("/{id}/submit")
    public ResponseEntity<Request> submitRequest(@PathVariable Integer id) {
        try {
            Request updated = requestService.submit(id);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Request> update(@PathVariable Integer id, @RequestBody Request request) {
        if (!requestRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        request.setId(id);
        return ResponseEntity.ok(requestRepo.save(request));
    }
    @PutMapping("/submit-review/{id}")
    public ResponseEntity<Request> submitForReview(@PathVariable Integer id) {
        return requestRepo.findById(id).map(request -> {
            // Update status based on total
            if (request.getTotal().compareTo(BigDecimal.valueOf(50)) <= 0) {
                request.setStatus("APPROVED");
            } else {
                request.setStatus("REVIEW");
            }
            // Set submittedDate to current date
            request.setSubmittedDate(LocalDate.now());
            // Save the updated request
            return ResponseEntity.ok(requestRepo.save(request));
        }).orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/approve/{id}")
    public ResponseEntity<Request> approveRequest(@PathVariable Integer id) {
        return requestRepo.findById(id).map(request -> {
            // Set status to APPROVED
            request.setStatus("APPROVED");
            // Save the updated request
            return ResponseEntity.ok(requestRepo.save(request));
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!requestRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        requestRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

