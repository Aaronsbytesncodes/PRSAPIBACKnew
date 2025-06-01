package com.PRS.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Request {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String requestNumber;
    

    private LocalDate submittedDate;

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

    @OneToMany(mappedBy = "request", cascade = CascadeType.ALL, orphanRemoval = true)
    @Singular
    private List<LineItem> lineItems;
}