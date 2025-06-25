package com.PRS.Services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.PRS.DB.*;
import com.PRS.model.*;

@Service

public class RequestService {
@Autowired
    private  RequestRepo requestRepo;


public Request create(Request request) {
    if (request.getUser() == null) {
        throw new IllegalArgumentException("User must not be null");
    }
    request.setRequestNumber(generateRequestNumber());
    return requestRepo.save(request);
}


    private String generateRequestNumber() {
        LocalDate today = LocalDate.now();
        String datePart = today.format(DateTimeFormatter.ofPattern("yyMMdd")); // e.g., 250502
        String prefix = "R" + datePart;

        long count = requestRepo.countByRequestNumberStartingWith(prefix);
        long sequence = count + 1;

        return prefix + String.format("%04d", sequence); // R2505020001
    }

    public Request submit(Integer requestId) {
        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        // Only auto-approve if the status is being set to REVIEW
        if ("NEW".equals(request.getStatus())) {
            // The user is trying to submit for review
            if (request.getTotal().compareTo(BigDecimal.valueOf(50)) <= 0) {
                // Instead of setting to REVIEW, auto-approve
                request.setStatus("APPROVED");
            } else {
                request.setStatus("REVIEW");
            }
            request.setSubmittedDate(LocalDate.now());
            return requestRepo.save(request);
        } else if ("REVIEW".equals(request.getStatus())) {
            // Already in review, do nothing or handle as needed
            return request;
        } else {
            // For other statuses, do nothing or handle as needed
            return request;
        }
    }
   
}