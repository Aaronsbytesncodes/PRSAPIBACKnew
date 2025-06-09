package com.PRS.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.PRS.DB.RequestRepo;
import com.PRS.Services.RequestService;
import com.PRS.model.LineItem;
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

    // 17. GET all requests
    @GetMapping
    public List<Request> getAll() {
        return requestRepo.findAll();
    }

    // 18. GET request by ID
    @GetMapping("/{id}")
    public ResponseEntity<Request> getById(@PathVariable Integer id) {
        return requestRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 19. POST create request
    @PostMapping
    public Request create(@RequestBody Request request) {
        request.setId(null);
        request.setStatus("NEW");
        request.setSubmittedDate(null);
        request.setTotal(BigDecimal.ZERO);

        // Link line items if provided
        if (request.getLineItems() != null) {
            for (LineItem li : request.getLineItems()) {
                li.setRequest(request);
            }
        }

        // Save and assign generated request number
        return requestService.create(request);
    }

    // 20. PUT update request
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody Request request) {
        if (!requestRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        request.setId(id);
        requestRepo.save(request);
        return ResponseEntity.noContent().build();
    }

    // 21. DELETE request
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!requestRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        requestRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 22. PUT submit request for review
    @PutMapping("/submit-review/{id}")
    public ResponseEntity<Request> submitForReview(@PathVariable Integer id) {
        try {
            Request submitted = requestService.submit(id);
            return ResponseEntity.ok(submitted);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 23. GET requests for review (excluding user's own)
    @GetMapping("/list-review/{userId}")
    public List<Request> listForReview(@PathVariable Integer userId) {
        return requestRepo.findRequestsForReview(userId);
    }

    // 24. PUT approve request
    @PutMapping("/approve/{id}")
    public ResponseEntity<Request> approve(@PathVariable Integer id) {
        return requestRepo.findById(id).map(request -> {
            request.setStatus("APPROVED");
            Request saved = requestRepo.save(request);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 25. PUT reject request with justification
    @PutMapping("/reject/{id}")
    public ResponseEntity<Object> reject(@PathVariable Integer id, @RequestBody String reason) {
        return requestRepo.findById(id).map(request -> {
            request.setStatus("REJECTED");
            request.setJustification(reason);
            requestRepo.save(request);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
