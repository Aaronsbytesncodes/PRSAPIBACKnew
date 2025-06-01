package com.PRS.model;

import jakarta.persistence.*;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import com.PRS.model.Product;



@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
   
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<LineItem> lineItems;

    @Column(nullable = false, unique = true, length = 30)
    private String partNumber;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(length = 20)
    private String unit;

    private String photoPath;

    @ManyToOne
    @JoinColumn(name = "VendorId", nullable = false)
    private Vendor vendor;
}
