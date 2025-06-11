package com.PRS.controller;

import com.PRS.Services.LineItemService;
import com.PRS.model.LineItem;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lineitems")
public class LineItemController {

    private final LineItemService lineItemService;

    public LineItemController(LineItemService lineItemService) {
        this.lineItemService = lineItemService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<LineItem> getLineItemById(@PathVariable Integer id) {
        return lineItemService.getLineItemById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/lines-for-req/{reqId}")
    public ResponseEntity<List<LineItem>> getLineItemsForRequest(@PathVariable Integer reqId) {
        return ResponseEntity.ok(lineItemService.getLineItemsForRequest(reqId));
    }

    @PostMapping
    public ResponseEntity<LineItem> addLineItem(@RequestBody LineItem lineItem) {
        return ResponseEntity.ok(lineItemService.addLineItem(lineItem));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateLineItem(@PathVariable Integer id, @RequestBody LineItem lineItem) {
        if (!id.equals(lineItem.getID())) {
            return ResponseEntity.badRequest().build();
        }
        lineItemService.updateLineItem(lineItem);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLineItem(@PathVariable Integer id) {
        lineItemService.deleteLineItem(id);
        return ResponseEntity.noContent().build();
    }
}

