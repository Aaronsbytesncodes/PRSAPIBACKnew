package com.PRS.model;

import jakarta.persistence.*;
import lombok.*;

@Entity

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vendor {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    private String address;
    private String city;
    private String state;
    private String zip;
    private String phoneNumber;
    private String email;

   // @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true)
   // @Singular
   // private List<Product> products;
}
