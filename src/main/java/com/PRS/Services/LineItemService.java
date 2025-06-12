package com.PRS.Services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.PRS.DB.LineItemRepo;
import com.PRS.DB.RequestRepo;
import com.PRS.model.LineItem;

@Service
public class LineItemService {

    private final LineItemRepo lineItemRepo;
    private final RequestRepo requestRepo;

    public LineItemService(LineItemRepo lineItemRepo, RequestRepo requestRepo) {
        this.lineItemRepo = lineItemRepo;
        this.requestRepo = requestRepo;
    }

    public List<LineItem> getLineItemsForRequest(Integer requestId) {
        return lineItemRepo.findByRequestId(requestId);
    }

    public Optional<LineItem> getLineItemById(Integer id) {
        return lineItemRepo.findById(id);
    }

    public LineItem addLineItem(LineItem lineItem) {
        LineItem saved = lineItemRepo.save(lineItem);
        recalcRequestTotal(lineItem.getRequest().getId());
        return saved;
    }

    public void updateLineItem(LineItem lineItem) {
        lineItemRepo.save(lineItem);
        recalcRequestTotal(lineItem.getRequest().getId());
    }

    public void deleteLineItem(Integer id) {
        Optional<LineItem> lineItemOpt = lineItemRepo.findById(id);
        if (lineItemOpt.isPresent()) {
            Integer requestId = lineItemOpt.get().getRequest().getId();
            lineItemRepo.deleteById(id);
            recalcRequestTotal(requestId);
        }
    }

    public void recalcRequestTotal(Integer requestId) {
        requestRepo.findById(requestId).ifPresent(request -> {
            BigDecimal total = lineItemRepo.findByRequestId(requestId).stream()
                    .map(li -> li.getProduct().getPrice().multiply(BigDecimal.valueOf(li.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            request.setTotal(total);
            requestRepo.save(request);
        });
    }
}

