package com.example.productservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @GetMapping("/{id}")
    public ResponseEntity<String> getProductById(@PathVariable String id) {
        if ("fail".equals(id)) {
            throw new RuntimeException("Simulated failure");
        }
        return ResponseEntity.ok("Product details for ID: " + id);
    }
}