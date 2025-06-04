package com.PRS.model;

import java.math.BigDecimal;

import jakarta.persistence.*;
import lombok.*;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LineItem {
    public Integer getID() {
		return ID;
	}
	public void setID(Integer iD) {
		ID = iD;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public Request getRequest() {
		return request;
	}
	public void setRequest(Request request) {
		this.request = request;
	}
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

	private BigDecimal total;
    
    public BigDecimal getTotal() {
        return total ;}
        
    

    


public void recalculateTotal() {
   if (product == null || quantity == null) {
        throw new IllegalArgumentException("Product or quantity is missing");
    }
    this.total = product.getPrice().multiply(BigDecimal.valueOf(quantity));
}
}