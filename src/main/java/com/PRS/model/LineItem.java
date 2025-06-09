package com.PRS.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity

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
 // In LineItem.java
    @ManyToOne
    @JoinColumn(name = "RequestId", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private Request request;

 


    
    @JoinColumn(name = "ProductID", nullable = false)
    @JsonBackReference
    private Product product;
}
 
