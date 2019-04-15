package com.teste.devjr.repository;

import com.teste.devjr.model.Order;
import com.teste.devjr.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
