package com.PRS.Services;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.PRS.DB.*;
import com.PRS.model.*;

import lombok.RequiredArgsConstructor;
import com.PRS.DB.LineItemRepo;

@Service
@RequiredArgsConstructor
public class RequestService {
@Autowired
    private  RequestRepo requestRepo;

    public Request create(Request request) {
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

        if (request.getTotal().compareTo(BigDecimal.valueOf(50)) <= 0) {
            request.setStatus("APPROVED");
        } else {
            request.setStatus("REVIEW");
        }

        request.setSubmittedDate(LocalDate.now());
        return requestRepo.save(request);
    }
    public void recalculateTotal(Integer requestId) {
        Request request = requestRepo.findById(requestId)
            .orElseThrow(() -> new RuntimeException("Request not found"));

        List<LineItem> items = null;
		try {
			items = LineItemRepo.findByRequestId(requestId);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        BigDecimal total = items.stream()
            .map(item -> item.getProduct().getPrice().multiply(new BigDecimal(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        request.setTotal(total);
        requestRepo.save(request);
    }
}