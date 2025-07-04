package com.PRS.model;

import jakarta.persistence.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity


public class Request {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String requestNumber;

	
   

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getRequestNumber() {
		return requestNumber;
	}

	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}

	public LocalDate getSubmittedDate() {
		return submittedDate;
	}

	public void setSubmittedDate(LocalDate submittedDate) {
		this.submittedDate = submittedDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getJustification() {
		return justification;
	}

	public void setJustification(String justification) {
		this.justification = justification;
	}

	public LocalDate getDateNeeded() {
		return dateNeeded;
	}

	public void setDateNeeded(LocalDate dateNeeded) {
		this.dateNeeded = dateNeeded;
	}
	
	

	public String getDeliveryMode() {
	    return deliveryMode;
	}

	public void setDeliveryMode(String deliveryMode) {
	    this.deliveryMode = deliveryMode;
	}


	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BigDecimal getTotal() {
		return total;
	}

	public void setTotal(BigDecimal total) {
		this.total = total;
	}

	public List<LineItem> getLineItems() {
		return lineItems;
	}

	public void setLineItems(List<LineItem> lineItems) {
		this.lineItems = lineItems;
	}

	private LocalDate submittedDate;
	
	private String reasonForRejection;

	
	public String getReasonForRejection() {
	    return reasonForRejection;
	}

	public void setReasonForRejection(String reasonForRejection) {
	    this.reasonForRejection = reasonForRejection;
	}


    @Column(nullable = false, length = 255)
    private String description;

    @Column(length = 255)
    private String justification;

    @Column(name = "DateNeeded")
    private LocalDate dateNeeded;

    @Column(length = 20)
    private String deliveryMode;

    @Column(length = 20)
    private String status;

    @Column(nullable = false)
    private BigDecimal total;
    @JsonIgnore
    @OneToMany (mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
   
    private List<LineItem> lineItems;

   
   
    
    @ManyToOne
    @JoinColumn(name = "UserID", referencedColumnName = "Id", nullable = false)

	private User user;
    public User getUser() {
    return this.user;
}

public void setUser(User user) {
    this.user = user;
}
}


