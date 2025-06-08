package com.PRS.controller;

import com.PRS.DB.RequestRepo;
import com.PRS.Services.LineItemService;
import com.PRS.model.LineItem;
import com.PRS.model.Request;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

    private final RequestRepo requestRepo;
    private final LineItemService lineItemService;

    public RequestController(RequestRepo requestRepo, LineItemService lineItemService) {
        this.requestRepo = requestRepo;
        this.lineItemService = lineItemService;
    }

    // 17. GET /api/Requests/ - Get all requests
    @GetMapping
    public List<Request> getAll() {
        return requestRepo.findAll();
    }

    // 18. GET /api/Requests/{id} - Get request by ID
    @GetMapping("/{id}")
    public ResponseEntity<Request> getById(@PathVariable Integer id) {
        return requestRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Request create(@RequestBody Request request) {
    
        request.setId(null); 
        request.setStatus("NEW");
        request.setSubmittedDate(null);
        request.setTotal(BigDecimal.ZERO);

        // Persist the request first (must exist before adding line items)
        Request savedRequest = requestRepo.save(request);

        // If lineItems were included in the body, associate them
        if (savedRequest.getLineItems() != null) {
            for (LineItem li : savedRequest.getLineItems()) {
                li.setRequest(savedRequest); // ensure bi-directional link
            }
        }

        // Recalculate total based on the request’s line items
        savedRequest.recalculateTotal();
        return requestRepo.save(savedRequest); // save updated total
    }


    // 20. PUT /api/Requests/{id} - Update a request
    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Integer id, @RequestBody Request request) {
        if (!requestRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        request.setId(id);
        requestRepo.save(request);
        recalcTotal(id);
        return ResponseEntity.noContent().build();
    }

    // 21. DELETE /api/Requests/{id} - Delete a request
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!requestRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        requestRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // 22. PUT /api/Requests/submit-review/{id} - Submit request for review
    @PutMapping("/submit-review/{id}")
    public ResponseEntity<Request> submitForReview(@PathVariable Integer id) {
        return requestRepo.findById(id).map(request -> {
            recalcTotal(id);
            BigDecimal total = request.getTotal();

            if (total.compareTo(BigDecimal.valueOf(50)) <= 0) {
                request.setStatus("APPROVED");
            } else {
                request.setStatus("REVIEW");
            }
            request.setSubmittedDate(LocalDate.now());

            Request saved = requestRepo.save(request);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

    // 23. GET /api/Requests/list-review/{userId} - Get requests for review (exclude user's own)
    @GetMapping("/list-review/{userId}")
    public List<Request> listForReview(@PathVariable Integer userId) {
        return requestRepo.findRequestsForReview(userId);
    }

    
    @PutMapping("/approve/{id}")
    public ResponseEntity<Request> approve(@PathVariable Integer id) {
        return requestRepo.findById(id).map(request -> {
            request.setStatus("APPROVED");
            Request saved = requestRepo.save(request);
            return ResponseEntity.ok(saved);
        }).orElse(ResponseEntity.notFound().build());
    }

  
    @PutMapping("/reject/{id}")
    public ResponseEntity<Object> reject(@PathVariable Integer id, @RequestBody String reason) {
        return requestRepo.findById(id).map(request -> {
            request.setStatus("REJECTED");
            request.setJustification(reason);  // Or create a new field reasonForRejection
            requestRepo.save(request);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }

    private void recalcTotal(Integer requestId) {
        lineItemService.recalcRequestTotal(requestId);
    }
}
