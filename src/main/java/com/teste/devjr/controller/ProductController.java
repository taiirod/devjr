package com.teste.devjr.controller;

import com.teste.devjr.model.Product;
import com.teste.devjr.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;


    @GetMapping
    public List<Product> all() {
        return productRepository.findAll();
    }

    @GetMapping("/{sku}")
    public Optional<Product> byId(@PathVariable String sku) {
        return productRepository.findById(sku);
    }

}
