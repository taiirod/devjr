package com.teste.devjr.repository;

import com.teste.devjr.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {

    Product findBySku(String sku);


}
