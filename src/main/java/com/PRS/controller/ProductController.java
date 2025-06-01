package com.PRS.controller;

import com.PRS.DB.ProductRepo;
import com.PRS.model.Product;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.management.relation.RoleInfoNotFoundException;

@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepo productRepo;

    @GetMapping
    public List<Product> getAll() {
        return productRepo.findAll();
    }

    @GetMapping("/{id}")
    public Product get(@PathVariable Integer id) throws RoleInfoNotFoundException{
        return productRepo.findById(id)
              .orElseThrow(() -> new RoleInfoNotFoundException("Product not found with id " + id));
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productRepo.save(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Integer id, @RequestBody Product product) {
        if (!productRepo.existsById(id)) {
            throw new RuntimeException("Product not found with id " + id);
        }
        return productRepo.save(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        productRepo.deleteById(id);
    }
    }
