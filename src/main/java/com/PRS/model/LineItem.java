package com.PRS.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;
import com.PRS.model.Request;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @Column(nullable = false)
    private Integer quantity;

    @ManyToOne
    @JoinColumn(name = "ProductID", nullable = false)
    private Product product;

    @ManyToOne
   @JoinColumn(name = "RequestID", nullable = false)
    private Request request;
    
    public BigDecimal getTotal() {
        return total;

    


public void recalculateTotal() {
   if (product == null || quantity == null) {
        throw new IllegalArgumentException("Product or quantity is missing");
    }
    this.total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
}
}